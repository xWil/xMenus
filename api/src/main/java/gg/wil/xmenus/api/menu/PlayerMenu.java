package gg.wil.xmenus.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerMenu {

    private final Menu menu;
    private final Player viewer;

    private Inventory inventory;
    private List<ActiveMenuItem> items;
    private ActiveMenuItem[] contents;
    private ActiveMenuItem backgroundItem;

    private AtomicBoolean closing = new AtomicBoolean(false);

    protected PlayerMenu(final Menu menu, final Player viewer) {
        this.menu = menu;
        this.viewer = viewer;
        this.buildInventory();
    }

    private void buildInventory() {
        this.inventory = Bukkit.createInventory(null, this.menu.getSize(), this.menu.getTitle());
        this.items = new ArrayList<>(this.menu.getContents().size());
        this.contents = new ActiveMenuItem[this.menu.getSize()];

        for (MenuItem item : this.menu.getContents()) {
            ActiveMenuItem activeItem = item.getActive(this);
            this.items.add(activeItem);
            this.contents[item.getPos()] = activeItem;
        }

        ItemStack[] items = new ItemStack[this.menu.getSize()];
        MenuItem backgroundMenuItem = this.menu.getBackground();
        this.backgroundItem = backgroundMenuItem == null ? null : backgroundMenuItem.getActive(this);

        for (int i = 0; i < this.menu.getSize(); i++) {
            ActiveMenuItem activeItem = this.contents[i];
            items[i] = activeItem != null ? activeItem.getItemStack() : this.backgroundItem == null ? null : this.backgroundItem.getItemStack();
        }

        this.inventory.setContents(items);
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Player getViewer() {
        return this.viewer;
    }

    protected void open() {
        this.viewer.closeInventory();
        this.viewer.openInventory(this.inventory);
    }

    public void close() {
        if (!this.closing.compareAndSet(false, true)) return;
        if (this.menu.getCloseHandler() != null) this.menu.getCloseHandler().accept(this);
        this.viewer.closeInventory();
    }

    public void tick() {
        this.items.forEach(ActiveMenuItem::tick);
    }

    protected void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;

        int slot = event.getSlot();
        if (slot < 0 || slot >= this.menu.getSize()) return;

        ActiveMenuItem item = this.contents[slot];
        if (item != null) item.onClick(event);
        else if (this.backgroundItem != null) this.backgroundItem.onClick(event);
    }

    protected boolean onClose(InventoryCloseEvent event) {
        if (event.getInventory() != this.inventory) return false;
        if (!this.closing.compareAndSet(false, true)) return false;
        if (this.menu.getCloseHandler() != null) this.menu.getCloseHandler().accept(this);
        return true;
    }
}
