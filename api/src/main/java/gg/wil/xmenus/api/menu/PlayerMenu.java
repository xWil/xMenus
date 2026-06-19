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
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerMenu {

    private final Menu menu;
    private final Player viewer;

    private Inventory inventory;
    private List<ActiveMenuItem> items;
    private List<List<ActiveMenuItem>> contents;
    private ActiveMenuItem backgroundItem;

    private final AtomicBoolean closing = new AtomicBoolean(false);

    protected PlayerMenu(final Menu menu, final Player viewer) {
        this.menu = menu;
        this.viewer = viewer;
        this.buildInventory();
    }

    private void buildInventory() {
        this.inventory = Bukkit.createInventory(null, this.menu.getSize(), this.menu.getTitle(this));
        this.items = new ArrayList<>(this.menu.getContents().size());
        this.contents = new ArrayList<>(this.menu.getSize());
        for (int i = 0; i < this.menu.getSize(); i++) this.contents.add(new ArrayList<>(3));

        for (Map.Entry<MenuItem, int[]> entry : this.menu.getContents().entrySet()) {
            MenuItem item = entry.getKey();
            for (int slot : entry.getValue()) {
                ActiveMenuItem activeItem = new ActiveMenuItem(this, item, slot);
                this.items.add(activeItem);
                this.contents.get(slot).add(activeItem);
            }
        }

        ItemStack[] items = new ItemStack[this.menu.getSize()];
        MenuItem backgroundMenuItem = this.menu.getBackground();
        this.backgroundItem = backgroundMenuItem == null ? null : new ActiveMenuItem(this, backgroundMenuItem, -1);

        for (int i = 0; i < this.menu.getSize(); i++) {
            List<ActiveMenuItem> slotItems = this.contents.get(i);
            ActiveMenuItem activeItem = slotItems.isEmpty() ? null : slotItems.getLast();
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

    public Inventory getInventory() {
        return this.inventory;
    }

    public ActiveMenuItem getBackgroundItem() {
        return this.backgroundItem;
    }

    public ActiveMenuItem getTopItem(int slot) {
        if (slot < 0 || slot >= this.contents.size()) return null;
        List<ActiveMenuItem> slotItems = this.contents.get(slot);
        return slotItems.isEmpty() ? null : slotItems.getLast();
    }

    public List<ActiveMenuItem> getSlotItems(int slot) {
        if (slot < 0 || slot >= this.contents.size()) return null;
        return this.contents.get(slot);
    }

    public List<ActiveMenuItem> getItems() {
        return this.items;
    }

    protected void open() {
        this.viewer.closeInventory();
        this.viewer.openInventory(this.inventory);
    }

    public void close() {
        if (!this.closing.compareAndSet(false, true)) return;
        if (this.menu.getCloseHandler() != null) this.menu.getCloseHandler().accept(this);
        this.viewer.closeInventory();

        this.inventory = null;
        this.items.clear();
        this.contents.clear();
        this.backgroundItem = null;
    }

    public void tick() {
        this.items.forEach(ActiveMenuItem::tick);
    }

    public void update(int... slots) {
        for (int slot : slots) {
            List<ActiveMenuItem> slotItems = this.contents.get(slot);
            ActiveMenuItem item = slotItems.isEmpty() ? null : slotItems.getLast();

            if (item != null) this.inventory.setItem(slot, item.getItemStack());
            else if (this.backgroundItem != null) this.inventory.setItem(slot, this.backgroundItem.getItemStack());
            else this.inventory.setItem(slot, null);
        }
    }

    public void update(ActiveMenuItem item) {
        this.inventory.setItem(item.getPosition(), item.getItemStack());
    }

    protected void move(ActiveMenuItem item, int oldPos) {
        this.contents.get(oldPos).remove(item);
        this.contents.get(item.getPosition()).add(item);
        this.update(oldPos, item.getPosition());
    }

    protected void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null ||event.getClickedInventory().getType() == InventoryType.PLAYER) return;

        int slot = event.getSlot();
        if (slot < 0 || slot >= this.menu.getSize()) return;

        List<ActiveMenuItem> slotItems = this.contents.get(slot);
        ActiveMenuItem item = slotItems.isEmpty() ? null : slotItems.getLast();
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
