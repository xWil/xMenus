package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Represents a {@link MenuItem} that has multiple {@link ItemDescriptor}s
 */
public class AnimatedMenuItem extends MenuItem {

    private final int animateRate;
    private final List<ItemDescriptor> descriptors;

    private AnimatedMenuItem(String identifier, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler, int animateRate, List<ItemDescriptor> descriptors, boolean swapList) {
        super(identifier, clickHandler);
        this.animateRate = animateRate;
        this.descriptors = swapList ? Collections.unmodifiableList(descriptors) : descriptors;
    }

    /**
     * Gets the first {@link ItemDescriptor} in the list
     *
     * @return The first {@link ItemDescriptor}
     */
    @Override
    public ItemDescriptor getDescriptor() {
        return this.descriptors.getFirst();
    }

    /**
     * Gets the list of {@link ItemDescriptor}s
     *
     * @return The list of {@link ItemDescriptor}s
     */
    public List<ItemDescriptor> getDescriptors() {
        return this.descriptors;
    }

    /**
     * Gets the rate (in ticks) at which the menu should animate
     * @return The rate (in ticks)
     */
    public int getAnimateRate() {
        return this.animateRate;
    }

    /**
     * Creates a new {@link AnimatedMenuItem} with the same properties as this one
     * @return A new {@link AnimatedMenuItem} with the same properties as this one
     */
    @Override
    public MenuItem clone() {
        return new AnimatedMenuItem(this.identifier, this.clickHandler, this.animateRate, this.descriptors, false);
    }

    /**
     * Creates a new {@link AnimatedMenuItem.Builder}
     * @return A new {@link AnimatedMenuItem.Builder}
     */
    public static AnimatedMenuItem.Builder builder() {
        return new AnimatedMenuItem.Builder();
    }

    /**
     * Builder for creating {@link AnimatedMenuItem}s.
     */
    public static class Builder {

        private String identifier = null;
        private BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler = null;
        private int animateRate = 1;
        private List<ItemDescriptor> descriptors = new ArrayList<>();

        private Builder() {}

        /**
         * Sets the identifier of the {@link AnimatedMenuItem}
         * The identifier is used to identify the {@link AnimatedMenuItem} in the {@link PlayerMenu}
         *
         * @param identifier The identifier of the {@link AnimatedMenuItem}
         * @return This builder
         */
        public AnimatedMenuItem.Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Sets the click handler of the {@link AnimatedMenuItem}
         * The click handler is called when the {@link AnimatedMenuItem} is clicked
         *
         * @param clickHandler The click handler of the {@link AnimatedMenuItem}
         * @return This builder
         */
        public AnimatedMenuItem.Builder onClick(BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        /**
         * Sets the rate (in ticks) at which the menu should animate.
         * The rate is the time between each frame ({@link ItemDescriptor}) of the animation.
         * 20 ticks = 1 second.
         *
         * @param animateRate The rate (in ticks)
         * @return This builder
         */
        public AnimatedMenuItem.Builder animateRate(int animateRate) {
            this.animateRate = animateRate;
            return this;
        }

        /**
         * Sets the frames of the animation.
         * The frames are the {@link ItemDescriptor}s that make up the animation.
         *
         * @param descriptors The frames of the animation
         * @return This builder
         */
        public AnimatedMenuItem.Builder frames(List<ItemDescriptor> descriptors) {
            this.descriptors = new ArrayList<>(descriptors);
            return this;
        }

        /**
         * Sets the frames of the animation.
         * The frames are the {@link ItemDescriptor}s that make up the animation.
         *
         * @param descriptors The frames of the animation
         * @return This builder
         */
        public AnimatedMenuItem.Builder frames(ItemDescriptor... descriptors) {
            this.descriptors = new ArrayList<>(List.of(descriptors));
            return this;
        }

        /**
         * Adds a frame to the animation.
         *
         * @param descriptor The frame to add
         * @return This builder
         */
        public AnimatedMenuItem.Builder addFrame(ItemDescriptor descriptor) {
            this.descriptors.add(descriptor);
            return this;
        }

        /**
         * Adds frames to the animation.
         *
         * @param descriptors The frames to add
         * @return This builder
         */
        public AnimatedMenuItem.Builder addFrames(ItemDescriptor... descriptors) {
            this.descriptors.addAll(List.of(descriptors));
            return this;
        }

        /**
         * Adds frames to the animation.
         *
         * @param descriptors The frames to add
         * @return This builder
         */
        public AnimatedMenuItem.Builder addFrames(List<ItemDescriptor> descriptors) {
            this.descriptors.addAll(descriptors);
            return this;
        }

        /**
         * Builds the {@link AnimatedMenuItem}
         *
         * @return The built {@link AnimatedMenuItem}
         */
        public AnimatedMenuItem build() {
            return new AnimatedMenuItem(this.identifier, this.clickHandler, this.animateRate, this.descriptors, true);
        }
    }
}
