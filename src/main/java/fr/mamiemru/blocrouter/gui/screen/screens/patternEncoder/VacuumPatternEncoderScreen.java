package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.VacuumPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class VacuumPatternEncoderScreen extends BaseContainerScreenPatternEncoder<VacuumPatternEncoderMenu> {

    public VacuumPatternEncoderScreen(VacuumPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_vacuum_pattern_encoder_gui.png");
    }
}
