package gg.wil.xmenus.api.item;

import com.google.common.base.Preconditions;
import gg.wil.xmenus.api.MojangAPIHelper;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DynamicSkullDescriptor extends DynamicItemDescriptor {

    private final Function<ActiveMenuItem, String> owner;

    protected DynamicSkullDescriptor(Function<ActiveMenuItem, String> owner, Function<ActiveMenuItem, String> name, Function<ActiveMenuItem, List<String>> lore,
                                     Function<ActiveMenuItem, Integer> amount, Function<ActiveMenuItem, Boolean> glowing, Function<ActiveMenuItem, List<ItemFlag>> flags) {
        super(null, name, lore, amount, glowing, flags);
        this.owner = owner;
    }

    @Override
    public ItemStack getItemStack(final ActiveMenuItem item) {
        String owner = this.owner.apply(item);
        String name = this.name.apply(item);
        List<String> lore = this.lore.apply(item);
        int amount = this.amount.apply(item);
        boolean glowing = this.glowing.apply(item);
        List<ItemFlag> flags = this.flags.apply(item);

        PlayerProfile profile = null;
        Player player = Bukkit.getPlayerExact(owner);
        if (player != null) {
            profile = player.getPlayerProfile();
        }
        else {
            if (!MojangAPIHelper.isPlayerCached(owner)) {
                CompletableFuture.supplyAsync(() -> MojangAPIHelper.getPlayerProfile(owner))
                        .thenAccept(_ -> item.refresh());
            } else profile = MojangAPIHelper.getPlayerProfile(owner);
        }

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setEnchantmentGlintOverride(glowing);
        if (profile != null) meta.setOwnerProfile(profile);
        meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static class Builder extends SkullDescriptor.Builder {

        private Function<ActiveMenuItem, String> ownerSupplier = null;
        private Function<ActiveMenuItem, String> nameSupplier = null;
        private Function<ActiveMenuItem, List<String>> loreSupplier = null;
        private Function<ActiveMenuItem, Integer> amountSupplier = null;
        private Function<ActiveMenuItem, Boolean> glowingSupplier = null;
        private Function <ActiveMenuItem, List<ItemFlag>> flagsSupplier = null;

        protected Builder() {}

        @Override
        public Builder owner(String owner) {
            if (owner != null) this.owner = owner;
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

        @Override
        public Builder flags(ItemFlag... flags) {
            if (flags != null) this.flags.addAll(Arrays.asList(flags));
            return this;
        }

        @Override
        public Builder flag(ItemFlag flag) {
            if (flag != null) this.flags.add(flag);
            return this;
        }

        public final Builder owner(Function<ActiveMenuItem, String> ownerSupplier) {
            this.ownerSupplier = ownerSupplier;
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

        public final Builder flags(Function<ActiveMenuItem, List<ItemFlag>> flagsSupplier) {
            this.flagsSupplier = flagsSupplier;
            return this;
        }

        @Override
        public ItemDescriptor build() {
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
