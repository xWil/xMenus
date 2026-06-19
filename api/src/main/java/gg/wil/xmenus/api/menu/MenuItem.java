package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class MenuItem implements Cloneable {

    private final ItemDescriptor descriptor;
    protected final BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler;

    protected MenuItem(BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        this.descriptor = null;
        this.clickHandler = clickHandler;
    }

    private MenuItem(ItemDescriptor descriptor, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        this.descriptor = descriptor;
        this.clickHandler = clickHandler;
    }

    public ItemDescriptor getDescriptor() {
        return descriptor;
    }

    public BiConsumer<ActiveMenuItem, InventoryClickEvent> getClickHandler() {
        return this.clickHandler;
    }

    public static MenuItem of(ItemDescriptor descriptor, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return new MenuItem(descriptor, clickHandler);
    }

    @Override
    public MenuItem clone() {
        return new MenuItem(this.descriptor, this.clickHandler);
    }
}
