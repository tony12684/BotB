package io.github.tony12684.BotB;


import java.util.Map;
import java.util.Collection;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import com.craftmend.openaudiomc.api.ClientApi;
import com.craftmend.openaudiomc.api.channels.VoiceChannel;
import com.craftmend.openaudiomc.api.clients.Client;
import com.craftmend.openaudiomc.api.VoiceApi;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;

//TODO update spigot api to latest version when possible

//TODO front end: find an easy way to mock up
//TODO middle ware: build object and logic
//TODO back end: build database and basic crud operations

public class Main extends JavaPlugin implements Listener {
    final boolean debugMode = true;
    //these are private so that they only load from our files once
    private ChannelManager channelManager;
    private double voiceBlockDistance;
    @Override
    public void onEnable() {
        //triggered when the plugin is enabled
        //TODO prevent players from creating their own voice channels
        getLogger().info("BotB plugin enabled.");
        if (debugMode) {
            getLogger().info("onEnable is called!");
            try {
            initMySQLDataSource();
            } catch (SQLException e) {
                getLogger().severe("Failed to initialize MySQL DataSource: " + e.getMessage());
            }



        }
        //server initialization
        ServerSettingsInit serverSettings = new ServerSettingsInit();
        serverSettings.initializeServerSettings();

        //TODO check spawn protection settings
        //load world name
        String worldName = loadFromSettings("worldName").toString();
        //init gamerules for world
        serverSettings.initializeWorldSettings(worldName);
        //load voice block distance
        voiceBlockDistance = (double) loadFromSettings("voiceBlockDistance");

        //initialize channel manager so channel changers are only loaded once
        channelManager = new ChannelManager();

        //registers event handlers so they can fire
        //uses this for listener because this implements listener
        //uses this as plugin because this is a plugin
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        //triggered when the plugin is disabled
        if (debugMode) { getLogger().info("onDisable is called!"); }
        getLogger().info("BotB plugin disabled.");
    }

    @EventHandler
    public void asyncPlayerChat(AsyncPlayerChatEvent event) {
        //triggered when a player sends a chat message
        // TODO: make it so that players who are in a voice chat only message those people
        // TODO: wiretap all player messages to storyteller?
        // TODO move this start game trigger to player command preprocess event and build game start command

        if (debugMode) {getLogger().info("Async player chat event for : " + event.getPlayer().getDisplayName());}
        if (event.getMessage() != null && event.getMessage().startsWith("BOTBSTART")) {
            nightTime();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //triggered when a player joins the server
        if (debugMode) {getLogger().info("Player join event for : " + event.getPlayer().getDisplayName());}
        event.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "Welcome to the Blocktower... ");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //triggers every time a players position changes?
        //TODO adjust this code to change voice channels'
        double offset = (double) voiceBlockDistance;
        Material from = event.getFrom().clone().subtract(0, offset, 0).getBlock().getType();
        Material to = event.getTo().clone().subtract(0, offset, 0).getBlock().getType();
        if (from != to ) { //the block material we need to check has changed
            Map<String, String> channelChangers = channelManager.getAllChannels();
            for (Map.Entry<String, String> entry : channelChangers.entrySet()) {
                String channelBlockType = entry.getKey();
                Material channelMaterial = Material.getMaterial(channelBlockType);
                String channelTarget = entry.getValue();
                //only legacy materials are being used and found.
                if (channelMaterial == null) {
                    getLogger().warning("Invalid material in channelChangers.yaml for key: " + channelBlockType);
                    getLogger().warning("Check channelChangers.yaml to ensure correct material names.");
                
                } else if (channelTarget.equals("disconnect") && to == channelMaterial) {
                    // Player has moved into a block that disconnects them from voice
                    Player player = event.getPlayer();
                    channelTarget = channelChangers.get(from.toString());
                    if (channelTarget != null) { // only disconnect if we know what channel they are in
                        changeChannel(channelTarget, player, true);
                    }
                } else if (to == channelMaterial) {
                    // This is guranteed to fire if the disconnect check fires and fails
                    // Player has moved into a block that changes their voice channel
                    Player player = event.getPlayer();
                    changeChannel(channelTarget, player,false);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        //triggers when an entity takes damage
        //TODO fix so players health is full after damage
        if (event.getEntityType().toString().equals("PLAYER")) {
            Bukkit.getPlayer(event.getEntity().getUniqueId()).setHealth(20);
            Bukkit.getPlayer(event.getEntity().getUniqueId()).setFoodLevel(20);
            nightTime();
        }
    }
    
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        //triggers when a player enters a command
        //TODO enable whispers sent specifically to storyteller
        if (event.getMessage().toLowerCase().startsWith("/w ") || event.getMessage().toLowerCase().startsWith("/tell")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "The whisper/tell command is disabled on this server.");
        }
    }

    private Object loadFromSettings(String key) {
        //load yaml configuration for settings
        //ensure that any settings not found are handled with defaults
        getLogger().info("Loading '" + key + "' from settings.yaml");
        String defaultWorldName = "world";
        double defaultVoiceBlockDistance = -1.0;
        try (InputStream in = Main.class.getResourceAsStream("/settings.yaml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                Object data = yaml.load(in);
                if (data instanceof java.util.Map) {
                    return ((java.util.Map<?,?>)data).get(key);
                }
            }
        } catch (Exception e) {
            getLogger().warning("Could not load '" + key + "' from settings.yaml: " + e.getMessage());
        }
        //handle defaults
        if (key.equals("worldName")) {
            getLogger().info("Using default world name: " + defaultWorldName);
            return defaultWorldName;
        } else if (key.equals("voiceBlockDistance")) {
            getLogger().info("Using default voice block distance: " + defaultVoiceBlockDistance);
            return defaultVoiceBlockDistance;
        }
        return null;
    }

    private void testDataSource(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
            else {
                getLogger().info("Database connection test successful.");
            }
        }
    }

    private MysqlDataSource initMySQLDataSource() throws SQLException {

        // this will only successfully create the yaml if you build the constructor with LoaderOptions
        // i do not know why

        // load yaml configuration for DB connection
        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(new Constructor(Config.class, options));
        try (InputStream in = Main.class.getResourceAsStream("/connectionData.yaml")) {
            Config config = yaml.load(in);
            getLogger().info("Database URL: " + config.getUrl());
            getLogger().info("Database Port: " + config.getPort());
            getLogger().info("Database Username: " + config.getUsername());
            // It's not a good idea to log passwords, so we won't do that here
            // we set our credentials

            //pull database info from config
            //TODO streamline config database object interaction
            Database database = config.getDatabase();
            MysqlDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setServerName(database.getHost());
            dataSource.setPortNumber(database.getPort());
            dataSource.setDatabaseName(database.getDatabase());
            dataSource.setUser(database.getUser());
            dataSource.setPassword(database.getPassword());
            //dataSource.set
            //test it and send it back out
            testDataSource(dataSource);
            getLogger().info("MySQL DataSource initialized successfully.");
            return dataSource;
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration: " + e.getMessage());
        }
        return null;
    }

    private void changeChannel(String channelTarget, Player player, boolean leaveCurrent) {
        //channelTarget should match a valid voice channel name
        //if disconnecting channel target is the current voice channel
        Client client = ClientApi.getInstance().getClient(player.getUniqueId());
        VoiceChannel channel = VoiceApi.getInstance().getChannel(channelTarget);
        //when walking over a channel changer block invalid material is being logged
        //  for every single block in channelChangers.yaml
        if (debugMode && !leaveCurrent) {getLogger().info("Attempting to connect player " + player.getName() + " to channel " + channelTarget);}
        if (debugMode && leaveCurrent) {getLogger().info("Attempting to disconnect player " + player.getName() + " from channel " + channelTarget);}
        if (client == null) {
            getLogger().warning("Client not found for player: " + player.getName());
            return;
        } else if (client.isConnected() == false) {
            getLogger().warning("Client is not connected for player: " + player.getName());
            return;
        } else if (leaveCurrent) {
            if (debugMode) {getLogger().info("Disconnecting " + player.getName() + " from voice.");}
            channel.removeMember(client);
        } else if (channel == null){
            getLogger().warning("Voice channel not found: " + channelTarget);
            return;
        } else {
            if (debugMode) {getLogger().info("Moved player " + player.getName() + " to channel " + channelTarget);}
            channel.addMember(client);
        }
    }

    public void nightTime() {
        //set time to night in the main world
        String worldName = loadFromSettings("worldName").toString();
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            world.setTime(16500); // Set time to night
            Collection<?> playerList = Bukkit.getOnlinePlayers();
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
    //when giving roles out do not log it as an action
    //  EXCEPT when giving a fake role to the drunk
    //add grimoire state to the action notes for spy SOMEHOW
    //GAME END can be used for execution actions and slayer actions and demon actions that end the game
    //avoid uuids as action notes
    //for slayer and gossip and savant and artist actions that happen through voice, give the storyteller the option to log them for the player
    //if a kill action is protected (monk or tea lady or pacifist or fool) notes are the role they were protected by
    //need permanent action buttons for actions that occurr outside of nighttime (slayer, gossip, moonchild)
    //juggler do up to 5 targets, the notes are up to 5 roles guessed delimited by ","
    //if game over is prevented by evil twin, maybe add a note or create an action for it?

}