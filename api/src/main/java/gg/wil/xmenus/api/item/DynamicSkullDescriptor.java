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
import java.util.function.Function;

/**
 * A {@link DynamicItemDescriptor} that represents a skull
 */
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
}
