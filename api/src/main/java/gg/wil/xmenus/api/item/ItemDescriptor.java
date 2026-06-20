package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public interface  ItemDescriptor {

    ItemStack getItemStack(ActiveMenuItem menu);

    static ItemDescriptorBuilder builder() {
        return new ItemDescriptorBuilder();
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
