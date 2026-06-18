package gg.wil.xmenus.api.item;

public class ItemDescriptorBuilder {

    protected ItemDescriptorBuilder() {
    }

    public DynamicItemDescriptor.Builder dynamic() {
        return new DynamicItemDescriptor.Builder();
    }

    public SimpleItemDescriptor.Builder simple() {
        return new SimpleItemDescriptor.Builder();
    }
}
