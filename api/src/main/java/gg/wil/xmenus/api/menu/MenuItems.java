package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * A utility class for creating {@link MenuItem}s.
 * @see MenuItem
 */
public class MenuItems {

    /**
     * Creates a {@link MenuItem} that has no tooltip or lore.
     *
     * @param material The material of the item
     * @return A new {@link MenuItem} with the given material
     */
    public static MenuItem blank(Material material) {
        return blank(material, null);
    }

    /**
     * Creates a {@link MenuItem} that has no tooltip or lore with a click handler.
     * The click handler is called when the item is clicked in the menu.
     *
     * @param material The material of the item
     * @param clickHandler The click handler of the item
     * @return A new {@link MenuItem} with the given material and click handler
     */
    public static MenuItem blank(Material material, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setHideTooltip(true);
        itemStack.setItemMeta(itemMeta);

        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), clickHandler);
    }

    /**
     * Creates a {@link MenuItem} with the given properties.
     *
     * @param material The material of the item
     * @param amount The item count
     * @param name The display name of the item
     * @param lore The lore of the item
     * @param glowing Whether the item should glow
     * @return A new {@link MenuItem} with the given properties
     */
    public static MenuItem simple(Material material, int amount, String name, List<String> lore, boolean glowing) {
        return simple(material, amount, name, lore, glowing, null);
    }

    /**
     * Creates a {@link MenuItem} with the given properties and click handler.
     * The click handler is called when the item is clicked in the menu.
     *
     * @param material The material of the item
     * @param amount The item count
     * @param name The display name of the item
     * @param lore The lore of the item
     * @param glowing Whether the item should glow
     * @param clickHandler The click handler of the item
     * @return A new {@link MenuItem} with the given properties and click handler
     */
    public static MenuItem simple(Material material, int amount, String name, List<String> lore, boolean glowing, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        ItemDescriptor descriptor = ItemDescriptor.builder()
                .material(material)
                .amount(amount)
                .name(name)
                .lore(lore)
                .glowing(glowing)
                .build();
        return MenuItem.of(descriptor, clickHandler);
    }

    /**
     * Creates a {@link MenuItem} from an {@link ItemStack}.
     *
     * @param itemStack The {@link ItemStack} to create the {@link MenuItem} from
     * @return A new {@link MenuItem}.
     */
    public static MenuItem fromItemStack(ItemStack itemStack) {
        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), null);
    }

    /**
     * Creates a {@link MenuItem} from an {@link ItemStack} with a click handler.
     * The click handler is called when the item is clicked in the menu.
     *
     * @param itemStack The {@link ItemStack} to create the {@link MenuItem} from
     * @param clickHandler The click handler of the item
     * @return A new {@link MenuItem} with the given click handler.
     */
    public static MenuItem fromItemStack(ItemStack itemStack, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), clickHandler);
    }
}
