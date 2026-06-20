package gg.wil.xmenus.api.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents the initial state of a menu.
 */
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

    /**
     * Gets the size of the menu
     *
     * @return The size of the menu
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets the title of the menu
     *
     * @param menu The {@link PlayerMenu} to get the title for
     * @return The title of the menu
     */
    public String getTitle(PlayerMenu menu) {
        return this.title.apply(menu);
    }

    /**
     * Gets the contents of the menu
     *
     * @return A map of {@link MenuItem}s to their slots
     */
    public Map<MenuItem, int[]> getContents() {
        return this.contents;
    }

    /**
     * Gets the background of the menu.
     *
     * @return A {@link MenuItem} representing the background of the menu, or null if there is no background
     */
    public MenuItem getBackground() {
        return this.background;
    }

    /**
     * Gets the close handler of the menu.
     *
     * @return The close handler of the menu, or null if there is no close handler
     */
    public Consumer<PlayerMenu> getCloseHandler() {
        return this.closeHandler;
    }

    /**
     * Opens the menu for a player.
     * Runs asynchronously.
     *
     * @param plugin The plugin that is opening the menu
     * @param player The player to open the menu for
     */
    public void open(final JavaPlugin plugin, final Player player) {
        this.open(plugin, player, null);
    }

    /**
     * Opens the menu for a player with an attachment.
     * Runs asynchronously.
     *
     * @param plugin The plugin that is opening the menu
     * @param player The player to open the menu for
     * @param attachment The attachment to attach to the menu
     */
    public void open(final JavaPlugin plugin, final Player player, Object attachment) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerMenu menu = new PlayerMenu(this, player, attachment);

            MenuManager manager = MenuManager.get(plugin);
            if (manager == null) return;
            manager.registerMenu(player, menu);

            Bukkit.getScheduler().runTask(plugin, menu::open);
        });
    }

    /**
     * Creates a new {@link MenuBuilder}
     *
     * @return A new {@link MenuBuilder}
     */
    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    /**
     * Builder for creating {@link Menu}s.
     */
    public static class MenuBuilder {

        private int size = 9;
        private Function<PlayerMenu, String> title = (_) -> "§cxMenus Menu";
        private Map<MenuItem, int[]> contents = new HashMap<>();
        private MenuItem background = null;
        private Consumer<PlayerMenu> closeHandler = null;

        private MenuBuilder() {}

        public MenuBuilder size(int size) {
            this.size = size;
            return this;
        }

        /**
         * Sets a static title of the {@link Menu}.
         *
         * @param title The title of the {@link Menu}.
         * @return This builder
         */
        public MenuBuilder title(String title) {
            this.title = (_) -> title;
            return this;
        }

        /**
         * Sets a dynamic title of the {@link Menu}.
         *
         * @param title A function that returns the title of the {@link Menu}.
         * @return This builder
         */
        public MenuBuilder title(Function<PlayerMenu, String> title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the background of the {@link Menu}.
         *
         * @param background The background of the {@link Menu}.
         * @return This builder
         */
        public MenuBuilder background(MenuItem background) {
            this.background = background;
            return this;
        }

        /**
         * Sets the contents of the {@link Menu}.
         *
         * @param contents The contents of the {@link Menu}.
         * @return This builder
         */
        public MenuBuilder contents(Map<MenuItem, int[]> contents) {
            this.contents = contents;
            return this;
        }

        /**
         * Adds an item to the {@link Menu}.
         *
         * @param item The item to add.
         * @param slot The slot to add the item to.
         * @param slots Any additional slots to add the item to.
         * @return This builder
         */
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

        /**
         * Sets the close handler of the {@link Menu}.
         * The close handler is called when the menu is closed.
         *
         * @param closeHandler The close handler of the {@link Menu}.
         * @return This builder
         */
        public MenuBuilder onClose(Consumer<PlayerMenu> closeHandler) {
            this.closeHandler = closeHandler;
            return this;
        }

        /**
         * Builds the {@link Menu}
         *
         * @return The built {@link Menu}
         */
        public Menu build() {
            return new Menu(this.size, this.title, this.contents, this.background, this.closeHandler);
        }
    }
}
