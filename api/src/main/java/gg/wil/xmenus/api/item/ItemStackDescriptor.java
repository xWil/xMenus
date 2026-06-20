package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.inventory.ItemStack;

public class ItemStackDescriptor implements ItemDescriptor {

    private final ItemStack itemStack;

    protected ItemStackDescriptor(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem menu) {
        return this.itemStack;
    }
}
