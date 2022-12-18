package fr.mamiemru.blocrouter.gui.screen.screens.scatter;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.scatter.ScatterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ScatterScreen extends BaseContainerScreenEnergy<ScatterMenu> {

    public ScatterScreen(ScatterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_scatter_gui.png");
    }

}
