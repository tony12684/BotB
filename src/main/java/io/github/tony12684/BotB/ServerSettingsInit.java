package io.github.tony12684.BotB;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.GameRule;

public class ServerSettingsInit {

    //TODO add functionality to revert server settings to previous state
    //    this may require different initialization method for our plugin
    //      example: play survival and BOTB on the same world

	public void initializeServerSettings() {
		Bukkit.setDefaultGameMode(GameMode.ADVENTURE);
		Bukkit.setIdleTimeout(30);
		Bukkit.setMaxPlayers(21);
		Bukkit.setPauseWhenEmptyTime(5);
		Bukkit.setWhitelist(true);
		Bukkit.setWhitelistEnforced(true);
	}

	public void initializeWorldSettings(String worldName, boolean debugMode) {
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
            Bukkit.getLogger().severe("World " + worldName + " not found! Game rules not set!");
            Bukkit.getLogger().severe("Please check your settings.yaml for the correct world name.");
            return;
        }
		//TODO load game rules from settings.yaml
		//TODO reasses these settings to not mess with other peoples worlds too much
		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, Boolean.FALSE);
		world.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, Boolean.FALSE);
		world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, Boolean.FALSE);
		world.setGameRule(GameRule.DISABLE_PLAYER_MOVEMENT_CHECK, Boolean.TRUE);
		world.setGameRule(GameRule.DISABLE_RAIDS, Boolean.TRUE);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
		world.setGameRule(GameRule.DO_ENTITY_DROPS, Boolean.FALSE);
		world.setGameRule(GameRule.DO_FIRE_TICK, Boolean.FALSE);
		world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, Boolean.FALSE);
		world.setGameRule(GameRule.DO_INSOMNIA, Boolean.FALSE);
		world.setGameRule(GameRule.DO_MOB_LOOT, Boolean.FALSE);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, Boolean.FALSE);
		world.setGameRule(GameRule.DO_PATROL_SPAWNING, Boolean.FALSE);
		world.setGameRule(GameRule.DO_TILE_DROPS, Boolean.FALSE);
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, Boolean.FALSE);
		world.setGameRule(GameRule.DO_VINES_SPREAD, Boolean.FALSE);
		world.setGameRule(GameRule.DO_WARDEN_SPAWNING, Boolean.FALSE);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, Boolean.TRUE);
		world.setGameRule(GameRule.DROWNING_DAMAGE, Boolean.FALSE);
		world.setGameRule(GameRule.FALL_DAMAGE, Boolean.FALSE);
		world.setGameRule(GameRule.FIRE_DAMAGE, Boolean.FALSE);
		world.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, Boolean.TRUE);
		world.setGameRule(GameRule.FREEZE_DAMAGE, Boolean.FALSE);
		world.setGameRule(GameRule.GLOBAL_SOUND_EVENTS, Boolean.TRUE);
		world.setGameRule(GameRule.KEEP_INVENTORY, Boolean.TRUE);
		world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 48);
		world.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, Boolean.FALSE);
		world.setGameRule(GameRule.MOB_GRIEFING, Boolean.FALSE);
		world.setGameRule(GameRule.NATURAL_REGENERATION, Boolean.FALSE);
		world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 100);
		world.setGameRule(GameRule.PROJECTILES_CAN_BREAK_BLOCKS, Boolean.FALSE);
		world.setGameRule(GameRule.REDUCED_DEBUG_INFO, Boolean.TRUE);
		if (debugMode) {
			world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.TRUE);
		} else {
			world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.FALSE);
		}
		world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, Boolean.TRUE);
		world.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, 1);
		world.setGameRule(GameRule.SPAWN_RADIUS, 0);
		world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, Boolean.FALSE);
		world.setGameRule(GameRule.TNT_EXPLODES, Boolean.FALSE);
		world.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, Boolean.FALSE);
		world.setGameRule(GameRule.WATER_SOURCE_CONVERSION, Boolean.FALSE);
	}
}
