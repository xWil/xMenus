package gg.wil.xmenus.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Menu {

    private final int size;
    private final Function<PlayerMenu, String> title;
    private final Map<MenuItem, int[]> contents;
    private final MenuItem background;
    private final Consumer<PlayerMenu> closeHandler;

    private Menu(int size, Function<PlayerMenu, String> title, Map<MenuItem, int[]> contents, MenuItem background, Consumer<PlayerMenu> closeHandler) {
        this.size = size;
        this.title = title;
        this.contents = contents;
        this.background = background;
        this.closeHandler = closeHandler;
    }

    public int getSize() {
        return this.size;
    }

    public String getTitle(PlayerMenu menu) {
        return this.title.apply(menu);
    }

    public Map<MenuItem, int[]> getContents() {
        return this.contents;
    }

    public MenuItem getBackground() {
        return this.background;
    }

    public Consumer<PlayerMenu> getCloseHandler() {
        return this.closeHandler;
    }

    public void open(final JavaPlugin plugin, final Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerMenu menu = new PlayerMenu(this, player);

            MenuManager manager = MenuManager.get(plugin);
            if (manager == null) return;
            manager.registerMenu(player, menu);

            Bukkit.getScheduler().runTask(plugin, menu::open);
        });
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {

        private int size = 9;
        private Function<PlayerMenu, String> title = (_) -> "§xMenus Menu";
        private Map<MenuItem, int[]> contents = new HashMap<>();
        private MenuItem background = null;
        private Consumer<PlayerMenu> closeHandler = null;

        private MenuBuilder() {}

        public MenuBuilder size(int size) {
            this.size = size;
            return this;
        }

        public MenuBuilder title(String title) {
            this.title = (_) -> title;
            return this;
        }

        public MenuBuilder title(Function<PlayerMenu, String> title) {
            this.title = title;
            return this;
        }

        public MenuBuilder background(MenuItem background) {
            this.background = background;
            return this;
        }

        public MenuBuilder contents(Map<MenuItem, int[]> contents) {
            this.contents = contents;
            return this;
        }

        public MenuBuilder addItem(MenuItem item, int slot, int... slots) {
            if (slots.length == 0) slots = new int[] { slot };
            else {
                int[] newSlots = new int[slots.length + 1];
                newSlots[0] = slot;
                System.arraycopy(slots, 0, newSlots, 1, slots.length);
                slots = newSlots;
            }
            this.contents.put(item, slots);
            return this;
        }

        public MenuBuilder onClose(Consumer<PlayerMenu> closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        public Menu build() {
            return new Menu(this.size, this.title, this.contents, this.background, this.closeHandler);
        }
    }
}
