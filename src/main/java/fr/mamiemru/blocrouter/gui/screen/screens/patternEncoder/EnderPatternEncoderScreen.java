package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.EnderPatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.EnderPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.FakeItemInsertionC2SPacket;
import fr.mamiemru.blocrouter.network.packet.FakeItemRemoveC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class EnderPatternEncoderScreen extends BaseContainerScreenPatternEncoder<EnderPatternEncoderMenu> {

    public EnderPatternEncoderScreen(EnderPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_ender_pattern_encoder_gui.png");
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int x = getX();
        int y = getY();
        for (int slotIndex = 0; slotIndex < EnderPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++slotIndex) {
            if (changeFakeItemStackFromCoordinates(pMouseX, pMouseY, x, y,9 + 18 * slotIndex, 18, slotIndex)) {
                break;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
