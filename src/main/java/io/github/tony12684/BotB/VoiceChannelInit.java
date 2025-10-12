package io.github.tony12684.BotB;

import java.util.Map;

import org.bukkit.Bukkit;

import com.craftmend.openaudiomc.api.VoiceApi;
import com.craftmend.openaudiomc.api.clients.Client;

public class VoiceChannelInit {
    public VoiceChannelInit(ChannelManager channelManager, Client client) {
        initializeVoiceChatSettings(channelManager, client);
    }
    
    private void initializeVoiceChatSettings(ChannelManager channelManager, Client client) {
        Map<String, String> channels = channelManager.getAllChannels();
        VoiceApi voiceApi = VoiceApi.getInstance();
        
        if (voiceApi == null) {
            Bukkit.getLogger().severe("VoiceApi instance is null! Voice channels not initialized!");
            return;
        }
        /* TODO build to modify the OAMc file
            need to init channels and disable channel command
        for (Map.Entry<String, String> entry : channels.entrySet()) {
            String channel = entry.getValue();
            // Here you can add logic to initialize or register the voice channel
            // For example, logging the loaded channels
            if (channel != null && voiceApi.getChannel(channel) == null && voiceApi.isChannelNameValid(channel)) {
                Bukkit.getLogger().info("Creating voice channel: " + channel);
                voiceApi.createChannel(channel, client, false, null);
            } else {
                System.out.println("Invalid voice channel for: " + entry.getValue());
            }
        }
        */
    }
}
