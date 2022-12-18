package fr.mamiemru.blocrouter.tabs;

import fr.mamiemru.blocrouter.items.ItemsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class RouterCreativeTab extends CreativeModeTab {

    public RouterCreativeTab() {
        super("block_router");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemsRegistry.ITEM_NORMAL_ROUTING_PATTERN.get());
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Block Router");
    }

}
