package gg.wil.xmenus.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ActiveMenuItem {

    private final PlayerMenu menu;
    private final MenuItem item;

    private int position;
    private ItemStack itemStack;

    private final AnimatedMenuItem animatedItem;
    private int ticks = 0;
    private int frame = 0;

    protected ActiveMenuItem(final PlayerMenu menu, final MenuItem item) {
        this.menu = menu;
        this.item = item;
        this.position = item.getPos();

        if (item instanceof AnimatedMenuItem animated) {
            this.animatedItem = animated;
        } else {
            this.animatedItem = null;
        }

        this.itemStack = item.getDescriptor().getItemStack(this);
    }

    public PlayerMenu getMenu() {
        return this.menu;
    }

    public MenuItem getItem() {
        return this.item;
    }

    public int getPosition() {
        return this.position;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    protected void markAsBackground() {
        this.position = -1;
    }

    public boolean isBackground() {
        return this.position == -1;
    }

    public void tick() {
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

    public void move(int newPos) {
        if (this.position == newPos) return;
        if (this.isBackground()) return;
        if (newPos < 0) return;

        int oldPos = this.position;
        this.position = newPos;
        this.menu.move(this, oldPos);
    }

    public void onClick(InventoryClickEvent event) {
        if (this.item.getClickHandler() == null) return;
        this.item.getClickHandler().accept(this, event);
    }
}
