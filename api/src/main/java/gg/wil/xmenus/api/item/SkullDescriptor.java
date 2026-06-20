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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class SkullDescriptor extends SimpleItemDescriptor {

    private volatile PlayerProfile profile = null;
    private List<ActiveMenuItem> itemsToRefresh = null;
    private ItemStack cache = null;

    public SkullDescriptor(String owner, String name, List<String> lore, int amount, boolean glowing, List<ItemFlag> flags) {
        super(Material.PLAYER_HEAD, name, lore, amount, glowing, flags);

        Player player = Bukkit.getPlayerExact(owner);
        if (player != null) this.profile = player.getPlayerProfile();
        else {
            this.itemsToRefresh = new CopyOnWriteArrayList<>();
            CompletableFuture.runAsync(() -> this.profile = MojangAPIHelper.getPlayerProfile(owner))
                    .thenRun(() -> this.itemsToRefresh.forEach(ActiveMenuItem::refresh))
                    .thenRun(() -> this.itemsToRefresh = null);
        }
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem menu) {
        if (this.cache != null) return this.cache;

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, this.amount);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

        meta.setDisplayName(this.name);
        meta.setLore(this.lore);
        meta.setEnchantmentGlintOverride(this.glowing);
        boolean hasProfile = false;
        if (this.profile != null) {
            hasProfile = true;
            meta.setOwnerProfile(this.profile);
        }
        meta.addItemFlags(this.flags.toArray(new ItemFlag[0]));
        itemStack.setItemMeta(meta);

        if (hasProfile) {
            this.cache = itemStack;
        } else {
            this.itemsToRefresh.add(menu);
        }
        return itemStack;
    }

    public static class Builder {

        protected String owner = "xWil";
        protected String name = "Item";
        protected List<String> lore = List.of();
        protected int amount = 1;
        protected boolean glowing = false;
        List<ItemFlag> flags = new ArrayList<>();

        protected Builder() {}

        public DynamicSkullDescriptor.Builder dynamic() {
            return new DynamicSkullDescriptor.Builder();
        }

        public SkullDescriptor.Builder owner(String owner) {
            if (owner != null) this.owner = owner;
            return this;
        }

        public SkullDescriptor.Builder name(String name) {
            if (name != null) this.name = name;
            return this;
        }

        public SkullDescriptor.Builder lore(List<String> lore) {
            if (lore != null) this.lore = lore;
            return this;
        }

        public SkullDescriptor.Builder amount(int amount) {
            Preconditions.checkArgument(amount >= 1, "Amount must be greater than or equal to 1");
            this.amount = amount;
            return this;
        }

        public SkullDescriptor.Builder glowing(boolean glowing) {
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
            return new SkullDescriptor(this.owner, this.name, this.lore, this.amount, this.glowing, this.flags);
        }
    }
}
