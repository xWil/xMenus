package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * An {@link ItemDescriptor} that represents an item that is
 * always the same.
 */
public class StaticItemDescriptor implements ItemDescriptor {

    private final Material material;
    protected final String name;
    protected final List<String> lore;
    protected final int amount;
    protected final boolean glowing;
    protected final List<ItemFlag> flags;

    private ItemStack cache;

    protected StaticItemDescriptor(Material material, String name, List<String> lore, int amount, boolean glowing, List<ItemFlag> flags) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.amount = amount;
        this.glowing = glowing;
        this.flags = flags;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem item) {
        if (cache == null) this.cache = ItemDescriptor.createItemStack(this.material, this.name, this.lore, this.amount, this.glowing, this.flags.toArray(new ItemFlag[0]));
        return this.cache;
    }
}
