package gg.wil.xmenus.api.item;

import com.google.common.base.Preconditions;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A builder for {@link ItemDescriptor}s
 * @param <T> The type of the builder itself
 */
public abstract class ItemDescriptorBuilder<T> {

    protected String name = "Item";
    protected List<String> lore = List.of();
    protected int amount = 1;
    protected boolean glowing = false;
    protected final List<ItemFlag> flags = new ArrayList<>();

    protected Function<ActiveMenuItem, String> nameSupplier = null;
    protected Function<ActiveMenuItem, List<String>> loreSupplier = null;
    protected Function<ActiveMenuItem, Integer> amountSupplier = null;
    protected Function<ActiveMenuItem, Boolean> glowingSupplier = null;
    protected Function<ActiveMenuItem, List<ItemFlag>> flagsSupplier = null;

    private ItemDescriptorBuilder() {}

    protected abstract T getInstance();

    public T name(String name) {
        if (name != null) this.name = name;
        return this.getInstance();
    }

    public final T name(Function<ActiveMenuItem, String> nameSupplier) {
        this.nameSupplier = nameSupplier;
        return this.getInstance();
    }

    public T lore(List<String> lore) {
        if (lore != null) this.lore = lore;
        return this.getInstance();
    }

    public final T lore(Function<ActiveMenuItem, List<String>> loreSupplier) {
        this.loreSupplier = loreSupplier;
        return this.getInstance();
    }

    public T amount(int amount) {
        Preconditions.checkArgument(amount >= 1, "Amount must be greater than or equal to 1");
        this.amount = amount;
        return this.getInstance();
    }

    public final T amount(Function<ActiveMenuItem, Integer> amountSupplier) {
        this.amountSupplier = amountSupplier;
        return this.getInstance();
    }

    public T glowing(boolean glowing) {
        this.glowing = glowing;
        return this.getInstance();
    }

    public final T glowing(Function<ActiveMenuItem, Boolean> glowingSupplier) {
        this.glowingSupplier = glowingSupplier;
        return this.getInstance();
    }

    public T flag(ItemFlag flag) {
        if (flag != null) this.flags.add(flag);
        return this.getInstance();
    }

    public T flags(ItemFlag... flags) {
        if (flags != null) this.flags.addAll(Arrays.asList(flags));
        return this.getInstance();
    }

    public final T flags(Function<ActiveMenuItem, List<ItemFlag>> flagsSupplier) {
        this.flagsSupplier = flagsSupplier;
        return this.getInstance();
    }

    public abstract ItemDescriptor build();

    public static class InitialBuilder extends ItemDescriptorBuilder<ItemBuilder> {

        protected InitialBuilder() {}

        public ItemDescriptor itemStack(ItemStack itemStack) {
            return new ItemStackDescriptor(itemStack);
        }

        public SkullBuilder skull() {
            return new SkullBuilder();
        }

        public SkullBuilder skull(String owner) {
            return new SkullBuilder().owner(owner);
        }

        public SkullBuilder skull(Function<ActiveMenuItem, String> ownerSupplier) {
            return new SkullBuilder().owner(ownerSupplier);
        }

        public ItemBuilder material(Material material) {
            return new ItemBuilder().material(material);
        }

        public final ItemBuilder material(Function<ActiveMenuItem, Material> materialSupplier) {
            return new ItemBuilder().material(materialSupplier);
        }

        @Override
        protected ItemBuilder getInstance() {
            return new ItemBuilder();
        }

        @Override
        public ItemDescriptor build() {
            return new StaticItemDescriptor(Material.STONE, this.name, this.lore, this.amount, this.glowing, this.flags);
        }
    }

    public static class ItemBuilder extends ItemDescriptorBuilder<ItemBuilder> {

        private Material material = Material.STONE;
        private Function<ActiveMenuItem, Material> materialSupplier = null;

        private ItemBuilder() {}

        @Override
        protected ItemBuilder getInstance() {
            return this;
        }

        public ItemBuilder material(Material material) {
            if (material != null) this.material = material;
            return this;
        }

        public final ItemBuilder material(Function<ActiveMenuItem, Material> materialSupplier) {
            this.materialSupplier = materialSupplier;
            return this;
        }

        public ItemDescriptor build() {
            if (isDynamic()) return buildDynamic();
            return new StaticItemDescriptor(this.material, this.name, this.lore, this.amount, this.glowing, this.flags);
        }

        private boolean isDynamic() {
            return this.materialSupplier != null || this.nameSupplier != null ||
                    this.loreSupplier != null || this.amountSupplier != null ||
                    this.glowingSupplier != null || this.flagsSupplier != null;
        }

        private ItemDescriptor buildDynamic() {
            if (this.materialSupplier == null) this.materialSupplier = _ -> this.material;
            if (this.nameSupplier == null) this.nameSupplier = _ -> this.name;
            if (this.loreSupplier == null) this.loreSupplier = _ -> this.lore;
            if (this.amountSupplier == null) this.amountSupplier = _ -> this.amount;
            if (this.glowingSupplier == null) this.glowingSupplier = _ -> this.glowing;
            if (this.flagsSupplier == null) this.flagsSupplier = _ -> this.flags;

            return new DynamicItemDescriptor(this.materialSupplier, this.nameSupplier, this.loreSupplier, this.amountSupplier, this.glowingSupplier, this.flagsSupplier);
        }
    }

    public static class SkullBuilder extends ItemDescriptorBuilder<SkullBuilder> {

        private String owner = "xWil";
        private Function<ActiveMenuItem, String> ownerSupplier = null;

        private SkullBuilder() {}

        @Override
        protected SkullBuilder getInstance() {
            return this;
        }

        public SkullBuilder owner(String owner) {
            if (owner != null) this.owner = owner;
            return this;
        }

        public final SkullBuilder owner(Function<ActiveMenuItem, String> ownerSupplier) {
            this.ownerSupplier = ownerSupplier;
            return this;
        }

        @Override
        public ItemDescriptor build() {
            if (isDynamic()) return buildDynamic();
            return new StaticSkullDescriptor(this.owner, this.name, this.lore, this.amount, this.glowing, this.flags);
        }

        private boolean isDynamic() {
            return this.ownerSupplier != null || this.nameSupplier != null ||
                    this.loreSupplier != null || this.amountSupplier != null ||
                    this.glowingSupplier != null || this.flagsSupplier != null;
        }

        private ItemDescriptor buildDynamic() {
            if (this.ownerSupplier == null) this.ownerSupplier = (_) -> this.owner;
            if (this.nameSupplier == null) this.nameSupplier = (_) -> this.name;
            if (this.loreSupplier == null) this.loreSupplier = (_) -> this.lore;
            if (this.amountSupplier == null) this.amountSupplier = (_) -> this.amount;
            if (this.glowingSupplier == null) this.glowingSupplier = (_) -> this.glowing;
            if (this.flagsSupplier == null) this.flagsSupplier = (_) -> this.flags;

            return new DynamicSkullDescriptor(this.ownerSupplier, this.nameSupplier, this.loreSupplier, this.amountSupplier, this.glowingSupplier, this.flagsSupplier);
        }
    }
}
