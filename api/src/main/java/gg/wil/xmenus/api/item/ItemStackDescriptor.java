package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.inventory.ItemStack;

public class ItemStackDescriptor implements ItemDescriptor {

    private final ItemStack itemStack;

    private ItemStackDescriptor(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem menu) {
        return this.itemStack;
    }

    public static ItemStackDescriptor of(ItemStack itemStack) {
        return new ItemStackDescriptor(itemStack);
    }
}
