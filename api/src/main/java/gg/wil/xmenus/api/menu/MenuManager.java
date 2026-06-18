package gg.wil.xmenus.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public final class MenuManager {

    private static final Map<Plugin, MenuManager> MANAGERS = new HashMap<>();

    public static MenuManager get(Plugin plugin) {
        if (!plugin.isEnabled()) return null;
        return MANAGERS.computeIfAbsent(plugin, MenuManager::new);
    }

    static void remove(Plugin plugin) {
        MenuManager manager = MANAGERS.get(plugin);
        if (manager != null) {
            manager.finish();
            MANAGERS.remove(plugin);
        }
    }

    private final Plugin plugin;
    private final Map<Player, PlayerMenu> menus;
    private final BukkitTask tickTask;
    private final MenuListeners listeners;

    private volatile boolean finishing = false;

    private MenuManager(Plugin plugin) {
        this.plugin = plugin;
        this.menus = new HashMap<>();
        this.tickTask = Bukkit.getScheduler().runTaskTimer(plugin, new MenuTicker(this), 0, 1);
        this.listeners = new MenuListeners(this);
        Bukkit.getPluginManager().registerEvents(this.listeners, plugin);
    }

    Plugin getPlugin() {
        return this.plugin;
    }

    void registerMenu(Player player, PlayerMenu menu) {
        if (!this.plugin.isEnabled()) return;
        this.menus.put(player, menu);
    }

    void unregisterMenu(Player player) {
        if (!this.plugin.isEnabled() || this.finishing) return;
        this.menus.remove(player);
    }

    public PlayerMenu getMenu(Player player) {
        return this.menus.get(player);
    }

    private void finish() {
        this.finishing = true;
        this.menus.values().forEach(PlayerMenu::close);
        this.menus.clear();
        this.tickTask.cancel();
        HandlerList.unregisterAll(this.listeners);
    }

    private record MenuTicker(MenuManager manager) implements Runnable {

        @Override
            public void run() {
                this.manager.menus.values().forEach(PlayerMenu::tick);
            }
        }
}
