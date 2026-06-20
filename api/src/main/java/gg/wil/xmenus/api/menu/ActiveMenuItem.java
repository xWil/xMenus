package gg.wil.xmenus.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item that is currently being displayed in a menu.
 * @see MenuItem
 */
public class ActiveMenuItem {

    private final PlayerMenu menu;
    private final MenuItem item;
    private final String identifier;

    private int position;
    private ItemStack itemStack;

    private final AnimatedMenuItem animatedItem;
    private int ticks = 0;
    private int frame = 0;

    protected ActiveMenuItem(final PlayerMenu menu, final MenuItem item, String identifier, int position) {
        this.menu = menu;
        this.item = item;
        this.identifier = identifier;
        this.position = position;

        if (item instanceof AnimatedMenuItem animated) {
            this.animatedItem = animated;
        } else {
            this.animatedItem = null;
        }

        this.itemStack = item.getDescriptor().getItemStack(this);
    }

    /**
     * Gets the {@link PlayerMenu} that this item is in.
     *
     * @return The {@link PlayerMenu}
     */
    public PlayerMenu getMenu() {
        return this.menu;
    }

    /**
     * Gets the {@link MenuItem} related to this item.
     *
     * @return The {@link MenuItem}
     */
    public MenuItem getItem() {
        return this.item;
    }

    /**
     * Gets the identifier of the {@link MenuItem}.
     *
     * @return The identifier of the {@link MenuItem}, or null if there is no identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Gets the position of the item in the menu.
     *
     * @return The position of the item in the menu
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Gets the {@link ItemStack} that currently represents the
     * {@link gg.wil.xmenus.api.item.ItemDescriptor} currently being displayed.
     *
     * @return The {@link ItemStack}
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Checks if this item is the background item.
     *
     * @return True if this item is the background item, false otherwise
     */
    public boolean isBackground() {
        return this.position == -1;
    }

    protected void tick() {
        if (this.animatedItem == null) return;
        this.ticks++;
        if (this.ticks >= this.animatedItem.getAnimateRate()) {
            this.ticks = 0;
            this.frame++;
            if (this.frame >= this.animatedItem.getDescriptors().size()) this.frame = 0;
            this.itemStack = this.animatedItem.getDescriptors().get(this.frame).getItemStack(this);
            this.menu.update(this);
        }
    }

    /**
     * Moves this item to a new position in the menu.
     * If the item is the background item, it will not move.
     *
     * @param newPos The new position of the item
     */
    public void move(int newPos) {
        if (this.position == newPos) return;
        if (this.isBackground()) return;
        if (newPos < 0) return;

        int oldPos = this.position;
        this.position = newPos;
        this.menu.move(this, oldPos);
    }

    /**
     * Refreshes the item's {@link ItemStack} based on the current {@link gg.wil.xmenus.api.item.ItemDescriptor}.
     */
    public void refresh() {
        if (this.animatedItem == null) {
            this.itemStack = this.item.getDescriptor().getItemStack(this);
        } else {
            this.itemStack = this.animatedItem.getDescriptors().get(this.frame).getItemStack(this);
        }
        this.menu.update(this);
    }

    protected void onClick(InventoryClickEvent event) {
        if (this.item.getClickHandler() == null) return;
        this.item.getClickHandler().accept(this, event);
    }
}
