package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.MojangAPIHelper;
import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A {@link StaticItemDescriptor} that represents a skull
 */
public class StaticSkullDescriptor extends StaticItemDescriptor {

    private volatile PlayerProfile profile = null;
    private List<ActiveMenuItem> itemsToRefresh = null;
    private ItemStack cache = null;

    protected StaticSkullDescriptor(String owner, String name, List<String> lore, int amount, boolean glowing, List<ItemFlag> flags) {
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
}
