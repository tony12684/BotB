package io.github.tony12684.BotB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

import com.craftmend.openaudiomc.api.ClientApi;
import com.craftmend.openaudiomc.api.channels.VoiceChannel;
import com.craftmend.openaudiomc.api.clients.Client;
import com.craftmend.openaudiomc.api.VoiceApi;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

//import io.github.projectunified.unidialog.core.opener.DialogOpener;
//import io.github.projectunified.unidialog.spigot.SpigotDialogManager;
//import net.md_5.bungee.api.dialog.ConfirmationDialog;
//import net.md_5.bungee.api.dialog.Dialog;
//import net.md_5.bungee.api.dialog.DialogBase;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

/**
 * The main class for the BotB plugin.
 * This class is responsible for initializing and managing the plugin's core functionality,
 * including event handling, command registration, and database interactions.
 * 
 * @author Tony12684
*/

public class Main extends JavaPlugin implements Listener {
    public final boolean debugMode = true;
    private Game game;
    //these are private so that they only load from our files once
    private ChannelManager channelManager;
    private double voiceBlockDistance;
    private MysqlConnectionPoolDataSource dataSource;
    //private SpigotDialogManager dialogManager;


    /**
     * Runs when the plugin is enabled on the server.
     * Registers commands, initializes the database, initializes server settings, and sets up event listeners.
     */
    @Override
    public void onEnable() {
        //TODO prevent players from creating their own voice channels
        getLogger().info("BotB plugin enabled.");
        if (debugMode) {
            getLogger().info("onEnable is called!");
        }

        //register commands
        this.getCommand("BOTBStartGame").setExecutor(new CommandBOTBStartGame());

        //initialize dialog manager
        //this.dialogManager = new SpigotDialogManager(this);
        //dialogManager.register();

        //database initialization
        try {
            dataSource = initMySQLDataSource();
            initDatabase();
            initRolesInDB();
            initTeamsInDB();
        } catch (Exception e) {
            getLogger().severe("Failed to initialize Database");
        }// TODO: warn storyteller about failed data logging

        //server initialization
        ServerSettingsInit serverSettings = new ServerSettingsInit();
        serverSettings.initializeServerSettings();

        //TODO check spawn protection settings
        //load world name
        String worldName = loadFromSettings("worldName").toString();
        //init gamerules for world
        serverSettings.initializeWorldSettings(worldName, debugMode);
        //load voice block distance
        voiceBlockDistance = (double) loadFromSettings("voiceBlockDistance");

        //initialize channel manager so channel changers are only loaded once
        channelManager = new ChannelManager();

        //registers event handlers so they can fire
        //uses this for listener because this implements listener
        //uses this as plugin because this is a plugin
        // TODO: move event listeners to a separate class for cleaner code.
        getServer().getPluginManager().registerEvents(this, this);
    }


    /**
     * Runs when the plugin is disabled, through server close or otherwise.
     * This method is responsible for cleaning up resources, saving data, and performing any necessary shutdown tasks.
     */
    @Override
    public void onDisable() {
        // TODO: add saving of game information on plugin disable.
        //   to resume the game on server crash
        if (debugMode) { getLogger().info("onDisable is called!"); }
        getLogger().info("BotB plugin disabled.");
    }

    /**
     * Returns the instance of the main plugin class.
     * This method can be used to access the plugin instance from other classes.
     * 
     * @return this instance of the Main class
     */
    public Main getInstance() {
        // tbh I don't remember why this exists. Seems pointless.
        return this;
    }

    /**
     * Handles player chat events asynchronously.
     * This method is triggered when a player sends a chat message.
     * It can be used to implement custom chat behavior, such as restricting messages to certain players or logging messages.
     * 
     * @param event The AsyncPlayerChatEvent that triggered this method.
     */
    @EventHandler
    public void asyncPlayerChat(AsyncPlayerChatEvent event) {
        // TODO: make it so that players who are in a voice chat only message those people
        // TODO: wiretap all player messages to storyteller?

        //if (debugMode) {getLogger().info("Async player chat event for : " + event.getPlayer().getDisplayName());}
    }


    /**
     * Handles player join events.
     * This method is triggered when a player joins the server.
     * It can be used to implement custom join behavior, such as sending welcome messages or initializing player data.
     * 
     * @param event The PlayerJoinEvent that triggered this method.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (debugMode) {getLogger().info("Player join event for : " + event.getPlayer().getDisplayName());}
        // custom join message
        event.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "Welcome to the Blocktower... ");
    }

    /**
     * Handles player drop item events.
     * This method is triggered when a player attempts to drop an item.
     * It can be used to prevent players from dropping items or to implement custom drop behavior.
     * 
     * @param event The PlayerDropItemEvent that triggered this method.
     */
    @EventHandler
    public void onPlayerDropItem(org.bukkit.event.player.PlayerDropItemEvent event) {
        // prevents item dropping
        event.setCancelled(true);
    }

    /**
     * Handles custom player click events.
     * This method is triggered when a player interacts with a custom dialog.
     * It can be used to implement custom behavior based on the player's interaction with the dialog.
     * 
     * @param event The PlayerCustomClickEvent that triggered this method.
     */
    @EventHandler
    public void onCustomEvent(PlayerCustomClickEvent event) {
        if (debugMode) { // get sender info
            event.getPlayer().sendMessage(ChatColor.RED + "Custom click event fired.");
            event.getPlayer().sendMessage(ChatColor.RED + "Data: " + event.getData().toString());
            event.getPlayer().sendMessage(ChatColor.RED + "ID: " + event.getId());
        }
        // parse data for custom number submissions
        if (event.getId().toString().equals("minecraft:num_sub")) {
            //process our number submission from json data
            int number = event.getData().getAsJsonObject().get("num").getAsInt();
            // aquire pending future registered to the player dialog we just recieved
            CompletableFuture<Integer> future = getGame().getGrimoire().getPendingNumResponses().remove(event.getPlayer().getUniqueId());
            if (future != null) {
                // if that future is still active complete it with the number we got
                future.complete(number);
            }
        }
    }


    /**
     * Handles player move events.
     * This method is triggered when a player moves.
     * It can be used to implement custom behavior based on the player's movement, such as updating health or changing voice channels.
     * 
     * @param event The PlayerMoveEvent that triggered this method.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // TODO: move this to a persistent timer to handle afk players
        // keep players healthy and fed
        event.getPlayer().setAbsorptionAmount(100.0);
        event.getPlayer().setHealth(20.0);
        event.getPlayer().setFoodLevel(100);
        event.getPlayer().setSaturation(100);
        //TODO adjust this code to change voice channels'
        /* TEST DIALOG
        DialogOpener opener = dialogManager.createConfirmationDialog()
            .title("Test")
            .canCloseWithEscape(false)
            .input("name", builder -> builder.textInput().label("Enter your name:"))
            .yesAction(builder -> builder.label("Confirm"))
            .noAction(builder -> builder.label("Cancel"))
            .opener();
        opener.open(event.getPlayer().getUniqueId());
        */
        //ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        //Bukkit.dispatchCommand(console, "dialog show " + event.getPlayer().getName() + " {type:\"minecraft:confirmation\",title:{text:\"Test\",type:\"text\",color:\"gray\"},inputs:[],can_close_with_escape:1,pause:0,after_action:\"close\",yes:{label:\"Yes please\",action:{type:\"minecraft:dynamic/custom\",id:\"test\",additions:{Num:2,SetupMode:0b,Team:\"GOOD\"}}},no:{label:\"No thanks\"}}");
        
        // requires a game version in which the material list is consitent and NON-LEGACY
        // I know it's jank but it's like this so that people can make their own maps
        //  and place their own voice blocks to manage channels
        // get the programmed distance required to trigger a voice block
        double offset = (double) voiceBlockDistance;
        // get the block type we came from and went to
        Material from = event.getFrom().clone().subtract(0, offset, 0).getBlock().getType();
        Material to = event.getTo().clone().subtract(0, offset, 0).getBlock().getType();
        if (from != to ) { //the block material we need to check has changed
            // get the list of voice blocks
            Map<String, String> channelChangers = channelManager.getAllChannels();
            // for each possible voice block
            for (Map.Entry<String, String> entry : channelChangers.entrySet()) {
                // get the key and translate it into a Material
                //  IF VOICE BLOCK KEYS ARE NOT CONSISTENT WITH MATERIAL NAMES THIS WILL FAIL
                String channelBlockType = entry.getKey();
                Material channelMaterial = Material.getMaterial(channelBlockType);
                // get the channel that voice block is registered to
                String channelTarget = entry.getValue();
                if (channelMaterial == null) { // if the voice block material was parsed incorrectly
                    // warn the server console
                    getLogger().warning("Invalid material in channelChangers.yaml for key: " + channelBlockType);
                    getLogger().warning("Check channelChangers.yaml to ensure correct material names.");
                } else if (channelTarget.equals("disconnect") && to == channelMaterial) {
                    // Player has moved into a voice block that disconnects them from voice
                    Player player = event.getPlayer(); // get the player that triggered the event
                    // get the channel they were previously in based on the block they were last on
                    channelTarget = channelChangers.get(from.toString());
                    if (channelTarget != null) {
                        // if target is null we were probably walking INTO a house so don't disconnect
                        changeChannel(channelTarget, player, true);
                    }
                } else if (to == channelMaterial) {// Player has moved into a block that changes their voice channel
                    Player player = event.getPlayer(); // get the player that triggered the event
                    changeChannel(channelTarget, player,false); // change the player's voice channel
                }
            }
        }
    }


    /**
     * Handles entity damage events.
     * This method is triggered when an entity takes damage.
     * 
     * @param event The EntityDamageEvent that triggered this method.
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType().toString().equals("PLAYER")) { // if a player took damage
            // heal them back up
            Bukkit.getPlayer(event.getEntity().getUniqueId()).setHealth(20);
            Bukkit.getPlayer(event.getEntity().getUniqueId()).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 255, false, false, false));
            // do not cancel the damage, if you do you cancel the knockback and that's no fun.
        }
    }
    
    /**
     * Handles player command preprocess events.
     * This method is triggered when a player enters a command.
     * 
     * @param event The PlayerCommandPreprocessEvent that triggered this method.
     */
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        //TODO enable whispers sent specifically to storyteller
        // if the player was sending a private message
        if (event.getMessage().toLowerCase().startsWith("/w ") || event.getMessage().toLowerCase().startsWith("/tell")) {
            event.setCancelled(true); // cancel the message
            // and tell them they can't do that
            event.getPlayer().sendMessage(ChatColor.RED + "The whisper/tell command is disabled on this server.");
        }
    }

    /**
     * Returns the current game instance.
     * 
     * @return The current game instance.
     */
    public Game getGame() {
        //TODO handle no current game
        return game;
    }
    /**
     * Sets the current game instance.
     * 
     * @param game The game instance to set.
     */
    public void setGame(Game game) {
        //set current game instance
        //TODO handle existing game
        this.game = game;
    }

    /**
     * Loads a value from the settings.yaml file.
     * 
     * @param key The key to look up in the settings.yaml file.
     * @return The value associated with the key, or a default value if not found.
     */
    private Object loadFromSettings(String key) {
        // TODO: pull data one time and hold it?
        getLogger().info("Loading '" + key + "' from settings.yaml");
        // ensure that any settings not found are handled with defaults
        String defaultWorldName = "world";
        double defaultVoiceBlockDistance = -1.0;
        // get input stream from file
        try (InputStream in = Main.class.getResourceAsStream("/settings.yaml")) {
            if (in != null) {// if the file exists
                // create a yaml object and pull the data from the input stream
                Yaml yaml = new Yaml();
                Object data = yaml.load(in);
                if (data instanceof java.util.Map) { // if all that worked
                    // return the value associated with the key in the map
                    return ((java.util.Map<?,?>)data).get(key);
                }
            }
        } catch (Exception e) {
            getLogger().warning("Could not load '" + key + "' from settings.yaml: " + e.getMessage());
        }
        //handle default returns if key lookup failed
        if (key.equals("worldName")) {
            getLogger().info("Using default world name: " + defaultWorldName);
            return defaultWorldName;
        } else if (key.equals("voiceBlockDistance")) {
            getLogger().info("Using default voice block distance: " + defaultVoiceBlockDistance);
            return defaultVoiceBlockDistance;
        }
        //TODO replace with an thrown exception
        return null;
    }

    /**
     * Initializes the MySQL data source.
     * DataSource
     * 
     * @return The initialized MySQL data source. null if init failed.
     * @throws SQLException If an error occurs while initializing the data source.
     */
    private MysqlConnectionPoolDataSource initMySQLDataSource() throws SQLException {

        // this will only successfully create the Yaml if you build the constructor with LoaderOptions
        //  i do not know why

        // load yaml configuration for DB connection
        LoaderOptions options = new LoaderOptions();
        // TODO: place the config into .gitignore
        Yaml yaml = new Yaml(new Constructor(Config.class, options));
        try (InputStream in = Main.class.getResourceAsStream("/connectionData.yaml")) {
            Config config = yaml.load(in);
            getLogger().info("Database URL: " + config.getUrl());
            getLogger().info("Database Port: " + config.getPort());
            getLogger().info("Database Username: " + config.getUsername());

            // pull database info from config
            // TODO streamline config database object interaction
            Database database = config.getDatabase();
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            // set database parameters
            dataSource.setServerName(database.getHost());
            dataSource.setPortNumber(database.getPort());
            dataSource.setDatabaseName(database.getDatabase());
            dataSource.setUser(database.getUser());
            dataSource.setPassword(database.getPassword());
            // dataSource.set
            // test the datasource connection
            testDataSource(dataSource);
            getLogger().info("MySQL DataSource initialized successfully.");
            // send it out
            return dataSource;
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration: " + e.getMessage());
        }
        return null;
    }

    /**
     * Tests the provided data source by attempting to establish a connection.
     *
     * @param dataSource The data source to test.
     * @throws SQLException If the connection is invalid or cannot be established.
     */
    private void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(2)) { // try for 2 seconds to get connection
                throw new SQLException("Could not establish database connection.");
            }
            else {
                getLogger().info("Database connection test successful.");
            }
        }
    }

    /**
     * Alias of getConnection()
     * 
     * @return A connection from the data source.
     * @throws SQLException If a connection cannot be established.
     */
    private Connection getConn() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Changes the voice channel of a player.
     *
     * @param channelTarget The target voice channel name. Need to match an existing voice channel name in ChannelChangers.yaml
     * @param player The player whose channel is to be changed.
     * @param leaveCurrent Whether the player should leave the current channel. Should be true whenever they are already in a voice channel.
     */
    private void changeChannel(String channelTarget, Player player, boolean leaveCurrent) {
        // get the voice API client of the player
        Client client = ClientApi.getInstance().getClient(player.getUniqueId());
        // get the target voice channel
        VoiceChannel channel = VoiceApi.getInstance().getChannel(channelTarget);
        if (debugMode && !leaveCurrent) {getLogger().info("Attempting to connect player " + player.getName() + " to channel " + channelTarget);}
        if (debugMode && leaveCurrent) {getLogger().info("Attempting to disconnect player " + player.getName() + " from channel " + channelTarget);}
        if (client == null) {// no voice client was found
            getLogger().warning("Client not found for player: " + player.getName());
        } else if (client.isConnected() == false) {// voice client not connected
            getLogger().warning("Client is not connected for player: " + player.getName());
        } else if (leaveCurrent) {// disconnect the players voice
            if (debugMode) {getLogger().info("Disconnecting " + player.getName() + " from voice.");}
            channel.removeMember(client);
        } else if (channel == null){// voice channel not found
            getLogger().warning("Voice channel not found: " + channelTarget);
        } else {// voice channel found and client is connected so join
            if (debugMode) {getLogger().info("Moved player " + player.getName() + " to channel " + channelTarget);}
            channel.addMember(client);
        }
    }

    /**
     * Sets the in server conditions to night conditions required for the game.
     */
    /*
    public void nightTime() {
        // load world from settings
        String worldName = loadFromSettings("worldName").toString();
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            world.setTime(16500); // Set time to night
            Collection<?> playerList = Bukkit.getOnlinePlayers();
            // hide all players from each other
            // TODO: exclude storyteller from this
            for (Object i : playerList) {
                for (Object j : playerList) {
                    if (i instanceof Player && j instanceof Player) {
                        Player playerI = (Player) i;
                        Player playerJ = (Player) j;
                        playerI.hidePlayer(this, playerJ);
                    }
                }
            }
        } else {
            getLogger().warning("World " + worldName + " not found! Cannot set time to night.");
        }
    }
    */

    /**
     * Sets the in server conditions to day conditions required for the game
     */
    /* TODO: merge dayTime and Nighttime to remove redundant code, pass time as parameter
    public void dayTime() {
        //set time to day in the main world
        String worldName = loadFromSettings("worldName").toString();
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            world.setTime(1000); // Set time to day
            Collection<?> playerList = Bukkit.getOnlinePlayers();
            for (Object i : playerList) {
                for (Object j : playerList) {
                    if (i instanceof Player && j instanceof Player) {
                        Player playerI = (Player) i;
                        Player playerJ = (Player) j;
                        playerI.showPlayer(this, playerJ);
                    }
                }
            }
        } else {
            getLogger().warning("World " + worldName + " not found! Cannot set time to day.");
        }
    }
    */

    /**
     * Initializes the database by creating necessary tables if they do not exist.
     * 
     * @throws SQLException if a database access error or sql error occurs
     * @throws IOException if an I/O error occurs while reading the database setup file
     */
    private boolean initDatabase() throws SQLException, IOException{
        String setup;
        // load sql from the file
        try (InputStream in = getClassLoader().getResourceAsStream("BOTB_database_setup.sql")) {
            if (in != null) {
                setup = new String(in.readAllBytes());
            } else {
                getLogger().severe("Database setup SQL file not found!");
                return false;
            }
        } catch (IOException e) {
            getLogger().severe("Error reading database setup SQL file: " + e.getMessage());
            throw e;
        }
        // split up each query
        String[] queries = setup.split(";");
        // execute each query
        for (String query : queries) {
            String trimmedQuery = query.trim();
            if (!trimmedQuery.isEmpty()) {
                try (Connection conn = getConn(); 
                    PreparedStatement stmt = conn.prepareStatement(trimmedQuery)) {
                    stmt.execute();
                } catch (SQLException e) {
                    getLogger().severe("Error executing database setup query: " + trimmedQuery);
                    getLogger().severe("Error details: " + e.getMessage());
                    throw e;
                }
            }
        }
        if (debugMode) { getLogger().info("Database initialized successfully."); }
        return true;
    }

    /**
     * Initializes the roles in the database by loading them from role_ids.yaml file.
     * 
     * @return true if roles were successfully initialized, false otherwise
     */
    public boolean initRolesInDB() {
        // load in the role ids from role_ids.yaml
        //  when updating role_ids ONLY append else your database will become wrong
        Yaml yaml = new Yaml();
        try (InputStream in = Main.class.getResourceAsStream("/role_ids.yaml")) {
            if (in == null) {
                getLogger().severe("role_ids.yaml not found");
                return false;
            }
            // map out the ids to the names
            Map<Integer, String> roles = yaml.loadAs(in, Map.class);
            for (Map.Entry<Integer, String> entry : roles.entrySet()) {
                String roleName = entry.getValue().toString();
                try (Connection conn = getConn();
                    // update such that the resulting table will match role_ids.yaml
                    //  if table has been modified to be inconsistent this will overide
                    PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO roles (role_name) VALUES (?) ON DUPLICATE KEY UPDATE role_name = role_name")) {
                    stmt.setString(1, roleName);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    // should only run if roles doesn't exist or database init failed.
                    getLogger().severe("Database insertion error for role: " + roleName);
                    getLogger().severe("Error details: " + e.getMessage());
                    return false;
                }
            }
        } catch (Exception e) {
            getLogger().severe("Failed to load role IDs from YAML: " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Initializes the teams in the database.
     * 
     * @return true if teams were successfully initialized, false otherwise
     */
    public boolean initTeamsInDB() {
        // IF A NEW TEAM IS ADDED ENSURE IT'S APPENDED to the teams array
        // Ensure team names match Role.Team Enums
        //  Team Enum uses uppercase and database uses lowercase
        String[] teams = {"good", "evil", "storyteller"};
        for (String teamName : teams) {
            try (Connection conn = getConn();
                // insert each name into DATABASE
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO teams (team_name) VALUES (?) ON DUPLICATE KEY UPDATE team_name = team_name")) {
                stmt.setString(1, teamName);
                stmt.executeUpdate();
            } catch (Exception e) {
                getLogger().severe("Failed to initialize teams in database for team '" + teamName + "': " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param playerUUID The UUID of the player.
     * @param playerName The name of the player.
     * @return The number of rows affected.
     */
    public int insertUser(String playerUUID, String playerName) {
        // TODO: update this to utilize nicknames when implemented
        // TODO: add update nickname method for changed nicknames
        // TODO: fire this code on user join
        try (Connection conn = getConn();
            PreparedStatement stmt = conn.prepareStatement(
                //if the user already exists, do not insert again. uuid = uuid means do nothing
                "INSERT INTO users (uuid, username) VALUES (?, ?) ON DUPLICATE KEY UPDATE uuid = uuid")) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, playerName);
            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            throw new RuntimeException("Database insertion error in insertUser(): " + e.getMessage());
        }
    }

    /**
     * Inserts a new game start into the database.
     *
     * @return The ID of the newly inserted game.
     */
    public int insertGameStart() {
        try (Connection conn = getConn();
            // insert the current time as a new game entry
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO games (game_start_date_time) VALUES (NOW())")) {
            // TODO this is returning game id 1 every time
            int gameId = stmt.executeUpdate();
            return gameId;
        } catch (Exception e) {
            throw new RuntimeException("Database insertion error in insertGameStart(): " + e.getMessage());
        }
    }

    /**
     * Inserts users into a game in the database. One entry per user per game.
     *
     * @param gameId The ID of the game.
     * @param playerUUIDs The list of player UUIDs.
     * @return A map of player UUIDs to the number of rows affected.
     */
    public Map<String, Integer> insertGameUsers(int gameId, List<String> playerUUIDs) {
        // TODO: call method for users joining the game late
        // build a map for the return
        Map<String, Integer> rows = new HashMap<String,Integer>();
        for (String playerUUID : playerUUIDs) {
            try (Connection conn = getConn();
                // insert the entry
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO user_games (game_id, uuid) VALUES (?, ?)")) {
                stmt.setInt(1, gameId);
                stmt.setString(2, playerUUID);
                int row = stmt.executeUpdate();
                rows.put(playerUUID, row);
            } catch (Exception e) {
                throw new RuntimeException("Database insertion error in insertGameUsers(): " + e.getMessage());
            }
        }
        return rows;
    }

    /**
     * Inserts user roles into a game in the database. One entry per user per game.
     * 
     * @param gameId The ID of the game from the games table.
     * @param playerPerformers The list of PlayerPerformers containing the users and their assigned roles for the game.
     */
    public Map<String, Integer> insertGameRoles(int gameId, List<PlayerPerformer> playerPerformers, int actionId) {
        // Insert all user roles in a game into the user_game_roles table for that game
        // One entry per user per game
        // use actionId = 0 for no action associated with role assignment
        Map<String, Integer> rows = new HashMap<String,Integer>();
        for (PlayerPerformer performer : playerPerformers) {
            try (Connection conn = getConn();
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO user_game_roles (game_id, uuid, role_id, action_id) VALUES (?, ?, (SELECT role_id FROM roles WHERE role_name = ?), ?)")) {
                stmt.setInt(1, gameId);
                stmt.setString(2, performer.getUUID().toString());
                stmt.setString(3, performer.getRole().getRoleNameActual().toLowerCase());
                if (actionId == 0) {
                    stmt.setNull(4, java.sql.Types.INTEGER);
                } else {
                    stmt.setInt(4, actionId);
                }
                int row = stmt.executeUpdate();
                rows.put(performer.getUUID().toString(), row);

            } catch (Exception e) {
                throw new RuntimeException("Database insertion error in insertGameRoles(): " + e.getMessage());
            }
        }
        return rows;
    }

    public Map<String, Integer> insertGameTeams(int gameId, List<PlayerPerformer> playerPerformers, int actionId) {
        // Insert all user teams in a game into the user_game_teams table for that game
        // One entry per user per game
        Map<String, Integer> rows = new HashMap<String,Integer>();
        for (PlayerPerformer performer : playerPerformers) {
            try (Connection conn = getConn();
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO user_game_teams (game_id, uuid, team_id) VALUES (?, ?, (SELECT team_id FROM teams WHERE team_name = ?))")) {
                stmt.setInt(1, gameId);
                stmt.setString(2, performer.getUUID().toString());
                stmt.setString(3, performer.getRole().toString().toLowerCase());
                int row = stmt.executeUpdate();
                rows.put(performer.getUUID().toString(), row);

            } catch (Exception e) {
                throw new RuntimeException("Database insertion error in insertGameTeams(): " + e.getMessage());
            }
        }
        return rows;
    }

    public int insertAction(int gameId, Performer performer, int actionDay, String actionType, Boolean actionContainsLie, Boolean actionHasTargets, String actionNotes) {
        // Insert a new action into the database
        try (Connection conn = getConn();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO actions (game_id, uuid, team_id, role_id, action_day, action_type, action_contains_lie, action_has_targets, action_date_time, action_notes) VALUES (?, ?, (SELECT team_id FROM teams WHERE team_name = ?), (SELECT role_id FROM roles WHERE role_name = ?), ?, ?, ?, ?, (NOW()), ?)")) {
            stmt.setInt(1, gameId);
            stmt.setString(2, performer.getUUID().toString());
            stmt.setString(3, performer.getRole().getTeamActual().toString().toLowerCase());
            stmt.setString(4, performer.getRole().getRoleNameActual().toLowerCase());
            stmt.setInt(5, actionDay);
            stmt.setString(6, actionType);
            stmt.setBoolean(7, actionContainsLie);
            stmt.setBoolean(8, actionHasTargets);
            if (actionNotes == null) { actionNotes = ""; }
            stmt.setString(9, actionNotes);
            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            throw new RuntimeException("Database insertion error in insertAction(): " + e.getMessage());
        }
    }

    //add grimoire state to the action notes for spy SOMEHOW
    //GAME END can be used for execution actions and slayer actions and demon actions that end the game
    //avoid uuids as action notes
    //for slayer and gossip and savant and artist actions that happen through voice, give the storyteller the option to log them for the player
    //if a kill action is protected (monk or tea lady or pacifist or fool) notes are the role they were protected by
    //need permanent action buttons for actions that occurr outside of nighttime (slayer, gossip, moonchild)
    //juggler do up to 5 targets, the notes are up to 5 roles guessed delimited by ","
    //if game over is prevented by evil twin, maybe add a note or create an action for it?
}