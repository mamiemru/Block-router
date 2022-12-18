package fr.mamiemru.blocrouter.gui.screen.screens.scatter;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.EnderScatterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnderScatterScreen extends BaseContainerScreenEnergy<EnderScatterMenu> {

    public EnderScatterScreen(EnderScatterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_ender_scatter_gui.png");
    }
}
