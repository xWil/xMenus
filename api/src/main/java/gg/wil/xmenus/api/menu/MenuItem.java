package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

/**
 * Represents an item that can be displayed and clicked on.
 */
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

    /**
     * Gets the {@link ItemDescriptor}
     *
     * @return The {@link ItemDescriptor}
     */
    public ItemDescriptor getDescriptor() {
        return this.descriptor;
    }

    /**
     * Gets the identifier
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Gets the click handler
     *
     * @return The click handler
     */
    public BiConsumer<ActiveMenuItem, InventoryClickEvent> getClickHandler() {
        return this.clickHandler;
    }

    /**
     * Creates a new {@link MenuItem} from the given {@link ItemDescriptor}.
     *
     * @param descriptor The item descriptor to create the {@link MenuItem} from
     * @return A new {@link MenuItem} with the given {@link ItemDescriptor}
     */
    public static MenuItem of(ItemDescriptor descriptor) {
        return new MenuItem(descriptor, null, null);
    }

    /**
     * Creates a new {@link MenuItem} from the given {@link ItemDescriptor}
     * with a click handler.
     * The click handler is called when the item is clicked in the menu.
     *
     * @param descriptor The {@link ItemDescriptor} to create the {@link MenuItem} from
     * @param clickHandler The click handler to use for the {@link MenuItem}
     * @return A new {@link MenuItem} with the given {@link ItemDescriptor} and click handler
     */
    public static MenuItem of(ItemDescriptor descriptor, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return new MenuItem(descriptor, null, clickHandler);
    }

    /**
     * Creates a new {@link MenuItem} from the given {@link ItemDescriptor}
     * with an identifier, and click handler.
     * The click handler is called when the item is clicked in the menu.
     * The identifier is used if you need to retrieve an {@link ActiveMenuItem} from the {@link PlayerMenu}
     *
     * @param descriptor The {@link ItemDescriptor} to create the {@link MenuItem} from
     * @param identifier The identifier to use for the {@link MenuItem}
     * @param clickHandler The click handler to use for the {@link MenuItem}
     * @return A new {@link MenuItem} with the given {@link ItemDescriptor}, identifier, and click handler
     */
    public static MenuItem of(ItemDescriptor descriptor, String identifier, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return new MenuItem(descriptor, identifier, clickHandler);
    }

    /**
     * Clones the {@link MenuItem}
     *
     * @return A new {@link MenuItem} with the same properties as the original
     */
    @Override
    public MenuItem clone() {
        return new MenuItem(this.descriptor, this.identifier, this.clickHandler);
    }
}
