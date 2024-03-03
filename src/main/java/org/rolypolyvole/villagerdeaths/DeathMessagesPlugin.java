package org.rolypolyvole.villagerdeaths;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rolypolyvole.villagerdeaths.event.EntityDeathListener;

public final class DeathMessagesPlugin extends JavaPlugin {

    private JDA jda;

    public JDA getJda() {
        return jda;
    }

    @Override
    public void onEnable() {
        YamlConfiguration configuration = (YamlConfiguration) getConfig();

        configuration.options().copyDefaults();
        saveDefaultConfig();

        ConfigurationSection discordMessageSettings = configuration.getConfigurationSection("messages.discord-messages");
        assert discordMessageSettings != null;

        boolean discordEnabled = discordMessageSettings.getBoolean("enabled");

        if (discordEnabled) {
            String token = discordMessageSettings.getString("bot-token");

            JDABuilder jdaBuilder = JDABuilder.createDefault(token);

            try {
                jda = jdaBuilder.build().awaitReady();
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }

        Bukkit.getPluginManager().registerEvents(new EntityDeathListener(this), this);
    }
}