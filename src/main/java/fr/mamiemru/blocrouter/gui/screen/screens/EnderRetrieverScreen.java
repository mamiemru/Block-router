package fr.mamiemru.blocrouter.gui.screen.screens;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.EnderRetrieverMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnderRetrieverScreen extends BaseContainerScreenEnergy<EnderRetrieverMenu> {

    public EnderRetrieverScreen(EnderRetrieverMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_ender_retriever_gui.png");
    }
}
