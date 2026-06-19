package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import gg.wil.xmenus.api.item.ItemStackDescriptor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;

public class MenuItems {

    public static MenuItem blank(Material material) {
        return blank(material, null);
    }

    public static MenuItem blank(Material material, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler){
        ItemDescriptor descriptor = ItemDescriptor.builder().simple()
                .material(material)
                .name("§0")
                .build();
        return MenuItem.of(descriptor, clickHandler);
    }

    public static MenuItem simple(Material material, int amount, String name, List<String> lore, boolean glowing) {
        return simple(material, amount, name, lore, glowing, null);
    }

    public static MenuItem simple(Material material, int amount, String name, List<String> lore, boolean glowing, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        ItemDescriptor descriptor = ItemDescriptor.builder().simple()
                .material(material)
                .amount(amount)
                .name(name)
                .lore(lore)
                .glowing(glowing)
                .build();
        return MenuItem.of(descriptor, clickHandler);
    }

    public static MenuItem fromItemStack(ItemStack itemStack) {
        return MenuItem.of(ItemStackDescriptor.of(itemStack), null);
    }

    public static MenuItem fromItemStack(ItemStack itemStack, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
        return MenuItem.of(ItemStackDescriptor.of(itemStack), clickHandler);
    }
}
