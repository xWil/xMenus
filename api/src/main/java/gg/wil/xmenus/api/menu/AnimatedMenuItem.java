package gg.wil.xmenus.api.menu;

import gg.wil.xmenus.api.item.ItemDescriptor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class AnimatedMenuItem extends MenuItem {

    private final int animateRate;
    private final List<ItemDescriptor> descriptors;

    private AnimatedMenuItem(int pos, BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler, int animateRate, List<ItemDescriptor> descriptors, boolean swapList) {
        super(pos, clickHandler);
        this.animateRate = animateRate;
        this.descriptors = swapList ? Collections.unmodifiableList(descriptors) : descriptors;
    }

    @Override
    public ItemDescriptor getDescriptor() {
        return this.descriptors.getFirst();
    }

    public List<ItemDescriptor> getDescriptors() {
        return this.descriptors;
    }

    public int getAnimateRate() {
        return this.animateRate;
    }

    @Override
    public AnimatedMenuItem copy(int newPos) {
        return new AnimatedMenuItem(newPos, this.clickHandler, this.animateRate, this.descriptors, false);
    }

    public static AnimatedMenuItem.Builder builder() {
        return new AnimatedMenuItem.Builder();
    }

    public static class Builder {

        private int pos = 0;
        private BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler;
        private int animateRate;
        private List<ItemDescriptor> descriptors = new ArrayList<>();

        private Builder() {}

        public AnimatedMenuItem.Builder pos(int pos) {
            this.pos = pos;
            return this;
        }

        public AnimatedMenuItem.Builder onClick(BiConsumer<ActiveMenuItem, InventoryClickEvent> clickHandler) {
            this.clickHandler = clickHandler;
            return this;
        }

        public AnimatedMenuItem.Builder animateRate(int animateRate) {
            this.animateRate = animateRate;
            return this;
        }

        public AnimatedMenuItem.Builder frames(List<ItemDescriptor> descriptors) {
            this.descriptors = new ArrayList<>(descriptors);
            return this;
        }

        public AnimatedMenuItem.Builder frames(ItemDescriptor... descriptors) {
            this.descriptors = new ArrayList<>(List.of(descriptors));
            return this;
        }

        public AnimatedMenuItem.Builder addFrame(ItemDescriptor descriptor) {
            this.descriptors.add(descriptor);
            return this;
        }

        public AnimatedMenuItem.Builder addFrames(ItemDescriptor... descriptors) {
            this.descriptors.addAll(List.of(descriptors));
            return this;
        }

        public AnimatedMenuItem.Builder addFrames(List<ItemDescriptor> descriptors) {
            this.descriptors.addAll(descriptors);
            return this;
        }

        public AnimatedMenuItem build() {
            return new AnimatedMenuItem(this.pos, this.clickHandler, this.animateRate, this.descriptors, true);
        }
    }
}
