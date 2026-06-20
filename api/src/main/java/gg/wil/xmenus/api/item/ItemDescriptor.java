package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Represents a specific state of an item and
 * defines how an {@link ItemStack} should be created.
 * <p>
 * Used when creating a {@link gg.wil.xmenus.api.menu.MenuItem}
 */
public interface ItemDescriptor {

    /**
     * Get the ItemStack that this descriptor represents.
     *
     * @param item The active item that this descriptor is being used for
     * @return The ItemStack that the descriptor represents
     */
    ItemStack getItemStack(ActiveMenuItem item);

    /**
     * Creates an {@link ItemDescriptor} from an {@link ItemStack}.
     *
     * @param itemStack The item stack to create the descriptor from
     * @return A new {@link ItemStackDescriptor} with the given {@link ItemStack}
     */
    static ItemDescriptor fromItemStack(ItemStack itemStack) {
        return new ItemStackDescriptor(itemStack);
    }

    /**
     * Creates a builder for creating {@link ItemDescriptor}s.
     *
     * @return A new {@link ItemDescriptorBuilder}
     */
    static ItemDescriptorBuilder.InitialBuilder builder() {
        return new ItemDescriptorBuilder.InitialBuilder();
    }

    static ItemStack createItemStack(Material material, String name, List<String> lore, int amount, boolean glowing, ItemFlag... flags) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemMeta.setEnchantmentGlintOverride(glowing);
        itemMeta.addItemFlags(flags);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
