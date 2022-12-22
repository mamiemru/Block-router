package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.EnderPatternEncoderEntity;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.SlotPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.SlotPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.FakeItemInsertionC2SPacket;
import fr.mamiemru.blocrouter.network.packet.FakeItemRemoveC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderChangerSlotC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderEncodeC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;


public class SlotPatternEncoderScreen extends BaseContainerScreenPatternEncoder<SlotPatternEncoderMenu> {

    private XButton[] slotButtons = new XButton[SlotPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS];

    private MutableComponent getSlotTextButton(int index) {
        return Component.literal(String.valueOf(getMenu().getSlotData(index)));
    }

    public SlotPatternEncoderScreen(SlotPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_slot_pattern_encoder_gui.png");
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(
            new ExtendedButton(leftPos+90, topPos+63, 50, 18, Component.literal("Encode"),
                (btn) -> ModNetworking.sendToServer(new PatternEncoderEncodeC2SPacket())
            )
        );

        for (int sideSlotId = 0; sideSlotId < SlotPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            int finalSideSlotId = sideSlotId;

            slotButtons[sideSlotId] = addRenderableWidget(
                new XButton(leftPos+8+(18*sideSlotId), topPos+35, 18, 18, getSlotTextButton(finalSideSlotId),
                    (btn) -> {
                        int slot = getMenu().incrementSlotData(finalSideSlotId);
                        ModNetworking.sendToServer(new PatternEncoderChangerSlotC2SPacket(finalSideSlotId, slot));
                    }
                )
            );
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderButtons(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        for (int sideSlotId = 0; sideSlotId < SlotPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            slotButtons[sideSlotId].renderButton(pPoseStack, mouseX, mouseY, delta, getSlotTextButton(sideSlotId));
        }
    }


    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int x = getX();
        int y = getY();
        for (int slotIndex = 0; slotIndex < SlotPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++slotIndex) {
            if (changeFakeItemStackFromCoordinates(pMouseX, pMouseY, x, y, 8 + 18 * slotIndex, 18, slotIndex)) {
                break;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
