package io.github.tony12684.BotB;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class ChannelManager {
	private final Map<String, String> channelMap;

	public ChannelManager() {
		channelMap = loadChannelsFromYaml();
	}

	private Map<String, String> loadChannelsFromYaml() {
		try (InputStream in = ChannelManager.class.getResourceAsStream("/channelChangers.yaml")) {
			if (in != null) {
				Yaml yaml = new Yaml();
				Object data = yaml.load(in);
				if (data instanceof Map) {
					Map<?,?> rawMap = (Map<?,?>) data;
					Map<String, String> result = new java.util.HashMap<>();
					for (Map.Entry<?,?> entry : rawMap.entrySet()) {
						Object key = entry.getKey();
						Object value = entry.getValue();
						if (key instanceof String && value instanceof String) {
							result.put((String) key, (String) value);
						} else {
                            // Optionally log warning about invalid entry
                            Bukkit.getLogger().warning("Invalid channel entry in channelChangers.yaml: " + entry);
                            Bukkit.getLogger().warning("Only string key-value pairs are allowed.");
                        }
					}
					return result;
				}
			}
		} catch (Exception e) {
			// Optionally log error
		}
		return java.util.Collections.emptyMap();
	}

	public String getChannelBlock(String channelKey) {
		return channelMap.get(channelKey);
	}

	// Optionally, add a method to get all channels
	public Map<String, String> getAllChannels() {
		return channelMap;
	}
}
