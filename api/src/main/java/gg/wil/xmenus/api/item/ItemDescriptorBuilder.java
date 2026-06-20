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

    protected String name = "§cxMenus Item";
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

    /**
     * Sets the display name of the item
     *
     * @param name The display name of the item
     * @return The builder itself
     */
    public T name(String name) {
        if (name != null) this.name = name;
        return this.getInstance();
    }

    /**
     * Sets the {@link Function} that will be used to get the display name of the item.
     *
     * @param nameSupplier The {@link Function} that will be used to get the display name of the item.
     * @return The builder itself
     */
    public final T name(Function<ActiveMenuItem, String> nameSupplier) {
        this.nameSupplier = nameSupplier;
        return this.getInstance();
    }

    /**
     * Sets the lore of the item
     *
     * @param lore A String list representing the lore of the item
     * @return The builder itself
     */
    public T lore(List<String> lore) {
        if (lore != null) this.lore = lore;
        return this.getInstance();
    }

    /**
     * Sets the {@link Function} that will be used to get the lore of the item.
     *
     * @param loreSupplier The {@link Function} that will be used to get the lore of the item.
     * @return The builder itself
     */
    public final T lore(Function<ActiveMenuItem, List<String>> loreSupplier) {
        this.loreSupplier = loreSupplier;
        return this.getInstance();
    }

    /**
     * Sets the item count for the item
     *
     * @param amount The item count for the item
     * @return The builder itself
     */
    public T amount(int amount) {
        Preconditions.checkArgument(amount >= 1, "Amount must be greater than or equal to 1");
        this.amount = amount;
        return this.getInstance();
    }

    /**
     * Sets the {@link Function} that will be used to get the item count for the item.
     *
     * @param amountSupplier The {@link Function} that will be used to get the item count for the item.
     * @return The builder itself
     */
    public final T amount(Function<ActiveMenuItem, Integer> amountSupplier) {
        this.amountSupplier = amountSupplier;
        return this.getInstance();
    }

    /**
     * Sets whether the item should be glowing
     *
     * @param glowing Whether the item should be glowing
     * @return The builder itself
     */
    public T glowing(boolean glowing) {
        this.glowing = glowing;
        return this.getInstance();
    }

    /**
     * Sets the {@link Function} that will be used to get whether the item should be glowing.
     *
     * @param glowingSupplier The {@link Function} that will be used to get whether the item should be glowing.
     * @return The builder itself
     */
    public final T glowing(Function<ActiveMenuItem, Boolean> glowingSupplier) {
        this.glowingSupplier = glowingSupplier;
        return this.getInstance();
    }

    /**
     * Adds an {@link ItemFlag} to the item
     *
     * @param flag The {@link ItemFlag} to add
     * @return The builder itself
     */
    public T flag(ItemFlag flag) {
        if (flag != null) this.flags.add(flag);
        return this.getInstance();
    }

    /**
     * Adds multiple {@link ItemFlag}s to the item
     *
     * @param flags The {@link ItemFlag}s to add
     * @return The builder itself
     */
    public T flags(ItemFlag... flags) {
        if (flags != null) this.flags.addAll(Arrays.asList(flags));
        return this.getInstance();
    }

    /**
     * Sets the {@link Function} that will be used to get the {@link ItemFlag}s for the item.
     * @param flagsSupplier The {@link Function} that will be used to get the {@link ItemFlag}s for the item.
     * @return The builder itself
     */
    public final T flags(Function<ActiveMenuItem, List<ItemFlag>> flagsSupplier) {
        this.flagsSupplier = flagsSupplier;
        return this.getInstance();
    }

    /**
     * Builds the {@link ItemDescriptor}
     *
     * @return The built {@link ItemDescriptor}
     */
    public abstract ItemDescriptor build();

    public static class InitialBuilder extends ItemDescriptorBuilder<ItemBuilder> {

        protected InitialBuilder() {}

        /**
         * Creates a {@link ItemStackDescriptor} from an {@link ItemStack}
         *
         * @param itemStack The {@link ItemStack} to create the {@link ItemStackDescriptor} from
         * @return A new {@link ItemStackDescriptor} with the given {@link ItemStack}
         */
        public ItemDescriptor itemStack(ItemStack itemStack) {
            return new ItemStackDescriptor(itemStack);
        }

        /**
         * Switches the builder to a {@link SkullBuilder}
         *
         * @return A new {@link SkullBuilder}
         */
        public SkullBuilder skull() {
            return new SkullBuilder();
        }

        /**
         * Switches the builder to a {@link SkullBuilder} and sets the owner of the skull
         *
         * @param owner The name used to get a skin for the skull
         * @return A new {@link SkullBuilder} with the given owner
         */
        public SkullBuilder skull(String owner) {
            return new SkullBuilder().owner(owner);
        }

        /**
         * Switches the builder to a {@link SkullBuilder} and sets the
         * {@link Function} that will be used to get the owner of the skull.
         *
         * @param ownerSupplier A {@link Function} that returns the name used to get a skin for the skull
         * @return A new {@link SkullBuilder} with the given {@link Function}
         */
        public SkullBuilder skull(Function<ActiveMenuItem, String> ownerSupplier) {
            return new SkullBuilder().owner(ownerSupplier);
        }

        /**
         * Switches the builder to a {@link ItemBuilder} and sets the material of the item
         *
         * @param material The material of the item
         * @return A new {@link ItemBuilder} with the given material
         */
        public ItemBuilder material(Material material) {
            return new ItemBuilder().material(material);
        }

        /**
         * Switches the builder to a {@link ItemBuilder} and sets the {@link Function}
         * that will be used to get the material of the item.
         *
         * @param materialSupplier A {@link Function} that returns the material of the item
         * @return A new {@link ItemBuilder} with the given {@link Function}
         */
        public final ItemBuilder material(Function<ActiveMenuItem, Material> materialSupplier) {
            return new ItemBuilder().material(materialSupplier);
        }

        @Override
        protected ItemBuilder getInstance() {
            return new ItemBuilder();
        }

        /**
         * Builds the {@link ItemDescriptor}
         * Can only be a {@link StaticItemDescriptor} with the default values
         *
         * @return The built {@link ItemDescriptor}
         */
        @Override
        public ItemDescriptor build() {
            return new StaticItemDescriptor(Material.STONE, this.name, this.lore, this.amount, this.glowing, this.flags);
        }
    }

    /**
     * A builder for {@link ItemDescriptor}s that can build a {@link StaticItemDescriptor} or a {@link DynamicItemDescriptor}.
     */
    public static class ItemBuilder extends ItemDescriptorBuilder<ItemBuilder> {

        private Material material = Material.STONE;
        private Function<ActiveMenuItem, Material> materialSupplier = null;

        private ItemBuilder() {}

        @Override
        protected ItemBuilder getInstance() {
            return this;
        }

        /**
         * Sets the material of the item
         *
         * @param material The material of the item
         * @return The builder itself
         */
        public ItemBuilder material(Material material) {
            if (material != null) this.material = material;
            return this;
        }

        /**
         * Sets the {@link Function} that will be used to get the material of the item.
         *
         * @param materialSupplier The {@link Function} that will be used to get the material of the item.
         * @return The builder itself
         */
        public final ItemBuilder material(Function<ActiveMenuItem, Material> materialSupplier) {
            this.materialSupplier = materialSupplier;
            return this;
        }

        /**
         * Builds the {@link ItemDescriptor}
         * Can either be a {@link StaticItemDescriptor} or a {@link DynamicItemDescriptor}
         *
         * @return The built {@link ItemDescriptor}
         */
        public ItemDescriptor build() {
            if (isDynamic()) return this.buildDynamic();
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

    /**
     * A builder for {@link ItemDescriptor}s that can build a {@link StaticSkullDescriptor} or a {@link DynamicSkullDescriptor}.
     */
    public static class SkullBuilder extends ItemDescriptorBuilder<SkullBuilder> {

        private String owner = "xWil";
        private Function<ActiveMenuItem, String> ownerSupplier = null;

        private SkullBuilder() {}

        @Override
        protected SkullBuilder getInstance() {
            return this;
        }

        /**
         * Sets the owner of the skull
         *
         * @param owner The name of a player whose skin will be used for the skull
         * @return The builder itself
         */
        public SkullBuilder owner(String owner) {
            if (owner != null) this.owner = owner;
            return this;
        }

        /**
         * Sets the {@link Function} that will be used to get the owner of the skull.
         *
         * @param ownerSupplier A {@link Function} that returns the name of a player whose skin will be used for the skull
         * @return The builder itself
         */
        public final SkullBuilder owner(Function<ActiveMenuItem, String> ownerSupplier) {
            this.ownerSupplier = ownerSupplier;
            return this;
        }

        /**
         * Builds the {@link ItemDescriptor}
         * Can either be a {@link StaticSkullDescriptor} or a {@link DynamicSkullDescriptor}
         *
         * @return The built {@link ItemDescriptor}
         */
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
