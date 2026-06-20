package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class MenuItem implements Cloneable {

    private final ItemDescriptor descriptor;
    protected final String identifier;
    protected final BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler;

    protected MenuItem(String identifier, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        this.descriptor = null;
        this.identifier = identifier;
        this.clickHandler = clickHandler;
    }

    private MenuItem(ItemDescriptor descriptor, String idenfifier, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        this.descriptor = descriptor;
        this.identifier = idenfifier;
        this.clickHandler = clickHandler;
    }

    public ItemDescriptor getDescriptor() {
        return this.descriptor;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public BiConsumer<ActiveMenuItem, InventoryClickEvent> getClickHandler() {
        return this.clickHandler;
    }

    public static MenuItem of(ItemDescriptor descriptor) {
        return new MenuItem(descriptor, null, null);
    }

    public static MenuItem of(ItemDescriptor descriptor, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return new MenuItem(descriptor, null, clickHandler);
    }

    public static MenuItem of(ItemDescriptor descriptor, String identifier, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return new MenuItem(descriptor, identifier, clickHandler);
    }

    @Override
    public MenuItem clone() {
        return new MenuItem(this.descriptor, this.identifier, this.clickHandler);
    }
}
