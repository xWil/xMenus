package gg.wil.xmenus.api.item;

import gg.wil.xmenus.api.menu.ActiveMenuItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

/**
 * An {@link ItemDescriptor} that represents an item that is dynamic
 * and changes based on the {@link Function}s provided.
 */
public class DynamicItemDescriptor implements ItemDescriptor {

    protected final Function<ActiveMenuItem, Material> material;
    protected final Function<ActiveMenuItem, String> name;
    protected final Function<ActiveMenuItem, List<String>> lore;
    protected final Function<ActiveMenuItem, Integer> amount;
    protected final Function<ActiveMenuItem, Boolean> glowing;
    protected final Function<ActiveMenuItem, List<ItemFlag>> flags;

    protected DynamicItemDescriptor(Function<ActiveMenuItem, Material> material, Function<ActiveMenuItem, String> name, Function<ActiveMenuItem, List<String>> lore,
                                    Function<ActiveMenuItem, Integer> amount, Function<ActiveMenuItem, Boolean> glowing, Function<ActiveMenuItem, List<ItemFlag>> flags) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.amount = amount;
        this.glowing = glowing;
        this.flags = flags;
    }

    @Override
    public ItemStack getItemStack(ActiveMenuItem item) {
        Material material = this.material == null ? Material.STONE : this.material.apply(item);
        String name = this.name == null ? "Item" : this.name.apply(item);
        List<String> lore = this.lore == null ? List.of() : this.lore.apply(item);
        int amount = this.amount == null ? 1 : this.amount.apply(item);
        boolean glowing = this.glowing != null && this.glowing.apply(item);
        List<ItemFlag> flags = this.flags == null ? List.of() : this.flags.apply(item);

        return ItemDescriptor.createItemStack(material, name, lore, amount, glowing, flags.toArray(new ItemFlag[0]));
    }
}
