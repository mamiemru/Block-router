package fr.mamiemru.blocrouter.gui.screen.screens.routers;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.DispatcherRouterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DispatcherRouterScreen extends BaseContainerScreenEnergy<DispatcherRouterMenu> {

    public DispatcherRouterScreen(DispatcherRouterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_dispatcher_router_gui.png");
    }
}
