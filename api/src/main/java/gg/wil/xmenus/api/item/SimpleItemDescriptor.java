package gg.wil.xmenus.api.item;

import com.google.common.base.Preconditions;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleItemDescriptor implements ItemDescriptor {

    private final Material material;
    protected final String name;
    protected final List<String> lore;
    protected final int amount;
    protected final boolean glowing;
    protected final List<ItemFlag> flags;

    private ItemStack cache;

    protected SimpleItemDescriptor(Material material, String name, List<String> lore, int amount, boolean glowing, List<ItemFlag> flags) {
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

    public static class Builder {

        protected Material material = Material.STONE;
        protected String name = "Item";
        protected List<String> lore = List.of();
        protected int amount = 1;
        protected boolean glowing = false;
        protected List<ItemFlag> flags = new ArrayList<>();

        protected Builder() {}

        public Builder material(Material material) {
            if (material != null) this.material = material;
            return this;
        }

        public Builder name(String name) {
            if (name != null) this.name = name;
            return this;
        }

        public Builder lore(List<String> lore) {
            if (lore != null) this.lore = lore;
            return this;
        }

        public Builder amount(int amount) {
            Preconditions.checkArgument(amount >= 1, "Amount must be greater than or equal to 1");
            this.amount = amount;
            return this;
        }

        public Builder glowing(boolean glowing) {
            this.glowing = glowing;
            return this;
        }

        public Builder flags(ItemFlag... flags) {
            if (flags != null) this.flags.addAll(Arrays.asList(flags));
            return this;
        }

        public Builder flag(ItemFlag flag) {
            if (flag != null) this.flags.add(flag);
            return this;
        }

        public ItemDescriptor build() {
            return new SimpleItemDescriptor(this.material, this.name, this.lore, this.amount, this.glowing, this.flags);
        }
    }
}
