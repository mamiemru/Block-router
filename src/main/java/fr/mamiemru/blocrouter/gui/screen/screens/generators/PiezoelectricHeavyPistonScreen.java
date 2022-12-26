package fr.mamiemru.blocrouter.gui.screen.screens.generators;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.generators.PiezoelectricGeneratorBaseMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.generators.PiezoelectricHeavyPistonMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PiezoelectricHeavyPistonScreen extends BaseContainerScreenEnergy<PiezoelectricHeavyPistonMenu> {

    public PiezoelectricHeavyPistonScreen(PiezoelectricHeavyPistonMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_piezoelectric_heavy_piston_gui.png");
    }
}
