package gg.wil.xmenus.api.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;

public final class MenuListeners implements Listener {

    private final MenuManager manager;

    MenuListeners(MenuManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (manager.getPlugin() != event.getPlugin()) return;
        MenuManager.remove(event.getPlugin());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        PlayerMenu menu = this.manager.getMenu(player);
        if (menu != null) {
            event.setCancelled(true);
            menu.onClick(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        PlayerMenu menu = this.manager.getMenu(player);
        if (menu != null) {
           if (menu.onClose(event)) this.manager.unregisterMenu(player);
        }
    }
}
