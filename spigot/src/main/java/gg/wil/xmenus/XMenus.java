package gg.wil.xmenus;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class XMenus extends JavaPlugin {

    public static XMenus PLUGIN;
    public static Logger LOGGER;

    @Override
    public void onEnable() {
        PLUGIN = this;
        LOGGER = getLogger();

        LOGGER.info("Successfully enabled!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Successfully disabled!");
    }
}
