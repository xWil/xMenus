package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;

public class MenuItems {

    public static MenuItem blank(Material material) {
        return blank(material, null);
    }

    public static MenuItem blank(Material material, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setHideTooltip(true);
        itemStack.setItemMeta(itemMeta);

        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), clickHandler);
    }

    public static MenuItem simple(Material material, int amount, String name, List<String> lore, boolean glowing) {
        return simple(material, amount, name, lore, glowing, null);
    }

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

    public static MenuItem fromItemStack(ItemStack itemStack) {
        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), null);
    }

    public static MenuItem fromItemStack(ItemStack itemStack, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return MenuItem.of(ItemDescriptor.fromItemStack(itemStack), clickHandler);
    }
}
