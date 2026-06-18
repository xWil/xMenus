package gg.wil.xmenus.api.item;

import com.google.common.base.Preconditions;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SimpleItemDescriptor implements ItemDescriptor {

    private final Material material;
    private final String name;
    private final List<String> lore;
    private final int amount;
    private final boolean glowing;

    private ItemStack cache;

    private SimpleItemDescriptor(Material material, String name, List<String> lore, int amount, boolean glowing) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.amount = amount;
        this.glowing = glowing;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem item) {
        if (cache == null) this.cache = ItemDescriptor.createItemStack(this.material, this.name, this.lore, this.amount, this.glowing);
        return this.cache;
    }

    public static class Builder {

        protected Material material = Material.STONE;
        protected String name = "Item";
        protected List<String> lore = List.of();
        protected int amount = 1;
        protected boolean glowing = false;

        protected Builder() {}

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lore(List<String> lore) {
            this.lore = lore;
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

        public ItemDescriptor build() {
            return new SimpleItemDescriptor(this.material, this.name, this.lore, this.amount, this.glowing);
        }
    }
}
