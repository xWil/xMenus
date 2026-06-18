package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class MenuItem {

    protected final int pos;
    private final ItemDescriptor descriptor;
    protected final BiConsumer<PlayerMenu, InventoryClickEvent> clickHandler;

    protected MenuItem(int pos, BiConsumer<PlayerMenu, InventoryClickEvent> clickHandler) {
        this.pos = pos;
        this.descriptor = null;
        this.clickHandler = clickHandler;
    }

    private MenuItem(int pos, ItemDescriptor descriptor, BiConsumer<PlayerMenu, InventoryClickEvent> clickHandler) {
        this.pos = pos;
        this.descriptor = descriptor;
        this.clickHandler = clickHandler;
    }

    public int getPos() {
        return pos;
    }

    public ItemDescriptor getDescriptor() {
        return descriptor;
    }

    public BiConsumer<PlayerMenu, InventoryClickEvent> getClickHandler() {
        return this.clickHandler;
    }

    protected ActiveMenuItem getActive(PlayerMenu menu) {
        return new ActiveMenuItem(menu, this);
    }

    public static MenuItem of(int pos, ItemDescriptor descriptor, BiConsumer<PlayerMenu, InventoryClickEvent> clickHandler) {
        return new MenuItem(pos, descriptor, clickHandler);
    }
}
