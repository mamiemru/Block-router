package fr.mamiemru.blocrouter.gui.screen.screens.generators;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.generators.PiezoelectricGeneratorBaseMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class PiezoelectricGeneratorBaseScreen extends BaseContainerScreenEnergy<PiezoelectricGeneratorBaseMenu> {

    public PiezoelectricGeneratorBaseScreen(PiezoelectricGeneratorBaseMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_piezoelectric_generator_base_gui.png");
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);

        List<MutableComponent> texts = menu.getProcessInformation();
        for (int textIndex = 0; textIndex < texts.size(); ++textIndex) {
            this.font.draw(pPoseStack, texts.get(textIndex), 5, 18 + 9 * textIndex, 4210752);
        }
    }
}
