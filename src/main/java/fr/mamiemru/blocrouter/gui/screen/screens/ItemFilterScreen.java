package fr.mamiemru.blocrouter.gui.screen.screens;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ItemFilterScreen extends BaseContainerScreen<ItemFilterMenu> {

    public ItemFilterScreen(ItemFilterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_item_filter_gui.png");
    }
}
