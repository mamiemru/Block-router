package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.SlotPatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.VacuumPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.VacuumPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.slot.PatternItemSlot;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import fr.mamiemru.blocrouter.items.custom.ItemVacuumRoutingPattern;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.FakeItemInsertionC2SPacket;
import fr.mamiemru.blocrouter.network.packet.FakeItemRemoveC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class VacuumPatternEncoderScreen extends BaseContainerScreenPatternEncoder<VacuumPatternEncoderMenu> {

    public VacuumPatternEncoderScreen(VacuumPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_vacuum_pattern_encoder_gui.png");
    }
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        int x = getX();
        int y = getY();

        int px = 0;
        int py = 0;
        for (int slotIndex = 0; slotIndex < VacuumPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++slotIndex) {
            if (changeFakeItemStackFromCoordinates(pMouseX, pMouseY, x, y, 26 + 18 * px, 17 + 18 * py, slotIndex + VacuumPatternEncoderEntity.SLOT_INPUT_SLOT_0)) {
                return super.mouseClicked(pMouseX, pMouseY, pButton);
            }
            if (px == 2) {
                ++py;
                px = 0;
            } else {
                ++px;
            }
        }

        px = 0;
        py = 0;
        for (int slotIndex = 0; slotIndex < VacuumPatternEncoderEntity.NUMBER_OF_INGREDIENTS_OUTPUT_SLOTS; ++slotIndex) {
            if (changeFakeItemStackFromCoordinates(pMouseX, pMouseY, x, y, 125 + 18 * px, 17 + 18 * py, VacuumPatternEncoderEntity.SLOT_OUTPUT_SLOT_0 + slotIndex)) {
                return super.mouseClicked(pMouseX, pMouseY, pButton);
            }
            if (px == 1) {
                ++py;
                px = 0;
            } else {
                ++px;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
