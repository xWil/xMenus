package gg.wil.xmenus.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class Menu {

    private final int size;
    private final String title;
    private final List<MenuItem> contents;
    private final MenuItem background;
    private final Consumer<PlayerMenu> closeHandler;

    private Menu(int size, String title, List<MenuItem> contents, MenuItem background, Consumer<PlayerMenu> closeHandler) {
        this.size = size;
        this.title = title;
        this.contents = contents;
        this.background = background;
        this.closeHandler = closeHandler;
    }

    public int getSize() {
        return this.size;
    }

    public String getTitle() {
        return this.title;
    }

    public List<MenuItem> getContents() {
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
        private String title = "§cxMenus menu";
        private List<MenuItem> contents = new ArrayList<>();
        private MenuItem background = null;
        private Consumer<PlayerMenu> closeHandler = null;

        private MenuBuilder() {}

        public MenuBuilder size(int size) {
            this.size = size;
            return this;
        }

        public MenuBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MenuBuilder background(MenuItem background) {
            this.background = background;
            return this;
        }

        public MenuBuilder contents(List<MenuItem> contents) {
            this.contents = contents;
            return this;
        }

        public MenuBuilder contents(MenuItem... contents) {
            this.contents = new ArrayList<>(List.of(contents));
            return this;
        }

        public MenuBuilder addItem(MenuItem item) {
            this.contents.add(item);
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
