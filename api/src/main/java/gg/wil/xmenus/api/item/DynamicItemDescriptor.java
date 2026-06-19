package gg.wil.xmenus.api.item;

import com.google.common.base.Preconditions;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public class DynamicItemDescriptor implements ItemDescriptor {

    private final Function<ActiveMenuItem, Material> material;
    private final Function<ActiveMenuItem, String> name;
    private final Function<ActiveMenuItem, List<String>> lore;
    private final Function<ActiveMenuItem, Integer> amount;
    private final Function<ActiveMenuItem, Boolean> glowing;

    protected DynamicItemDescriptor(Function<ActiveMenuItem, Material> material, Function<ActiveMenuItem, String> name, Function<ActiveMenuItem, List<String>> lore,
                                    Function<ActiveMenuItem, Integer> amount, Function<ActiveMenuItem, Boolean> glowing) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.amount = amount;
        this.glowing = glowing;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem item) {
        Material material = this.material == null ? Material.STONE : this.material.apply(item);
        String name = this.name == null ? "Item" : this.name.apply(item);
        List<String> lore = this.lore == null ? List.of() : this.lore.apply(item);
        int amount = this.amount == null ? 1 : this.amount.apply(item);
        boolean glowing = this.glowing != null && this.glowing.apply(item);

        return ItemDescriptor.createItemStack(material, name, lore, amount, glowing);
    }

    public static class Builder extends SimpleItemDescriptor.Builder {

        private Function<ActiveMenuItem, Material> materialSupplier = null;
        private Function<ActiveMenuItem, String> nameSupplier = null;
        private Function<ActiveMenuItem, List<String>> loreSupplier = null;
        private Function<ActiveMenuItem, Integer> amountSupplier = null;
        private Function<ActiveMenuItem, Boolean> glowingSupplier = null;

        protected Builder() {}

        @Override
        public Builder material(Material material) {
            if (material != null) this.material = material;
            return this;
        }

        @Override
        public Builder name(String name) {
            if (name != null) this.name = name;
            return this;
        }

        @Override
        public Builder lore(List<String> lore) {
            if (lore != null) this.lore = lore;
            return this;
        }

        @Override
        public Builder amount(int amount) {
            Preconditions.checkArgument(amount >= 1, "Amount must be greater than or equal to 1");
            this.amount = amount;
            return this;
        }

        @Override
        public Builder glowing(boolean glowing) {
            this.glowing = glowing;
            return this;
        }

        public final Builder material(Function<ActiveMenuItem, Material> materialSupplier) {
            this.materialSupplier = materialSupplier;
            return this;
        }

        public final Builder name(Function<ActiveMenuItem, String> nameSupplier) {
            this.nameSupplier = nameSupplier;
            return this;
        }

        public final Builder lore(Function<ActiveMenuItem, List<String>> loreSupplier) {
            this.loreSupplier = loreSupplier;
            return this;
        }

        public final Builder amount(Function<ActiveMenuItem, Integer> amountSupplier) {
            this.amountSupplier = amountSupplier;
            return this;
        }

        public final Builder glowing(Function<ActiveMenuItem, Boolean> glowingSupplier) {
            this.glowingSupplier = glowingSupplier;
            return this;
        }

        @Override
        public ItemDescriptor build() {
            if (materialSupplier == null) materialSupplier = (_) -> this.material;
            if (nameSupplier == null) nameSupplier = (_) -> this.name;
            if (loreSupplier == null) loreSupplier = (_) -> this.lore;
            if (amountSupplier == null) amountSupplier = (_) -> this.amount;
            if (glowingSupplier == null) glowingSupplier = (_) -> this.glowing;
            return new DynamicItemDescriptor(this.materialSupplier, this.nameSupplier, this.loreSupplier, this.amountSupplier, this.glowingSupplier);
        }
    }
}
