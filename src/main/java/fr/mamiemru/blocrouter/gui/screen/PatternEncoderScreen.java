package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.PatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderEncodeC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderChangerSideC2SPacket;
import fr.mamiemru.blocrouter.util.MouseUtil;
import fr.mamiemru.blocrouter.util.PatternUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.List;
import java.util.Optional;

public class PatternEncoderScreen extends AbstractAbstractContainerScreen<PatternEncoderMenu> {

    protected int imageWidth = 200;
    private XButton[] sideButtons = new XButton[PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS];
    // private XButton[] slotButtons = new XButton[PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS];

    private MutableComponent getSideTextButton(int index) {
        return Component.literal(PatternUtil.LIST_OF_AXES[getMenu().getSideData(index)]);
    }

    private MutableComponent getSlotTextButton(int index) {
        return Component.literal(String.valueOf(getMenu().getSlotData(index)));
    }

    private MutableComponent getTextTooltip(int index) {
        return Component.literal("Insert the ItemStack above on " +
                PatternUtil.axeIdToString(getMenu().getSideData(index)) +
                " side"
        );
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(
                new ExtendedButton(leftPos+90, topPos+63, 50, 18, Component.literal("Encode"),
                        (btn) -> ModNetworking.sendToServer(new PatternEncoderEncodeC2SPacket())
                )
        );

        for (int sideSlotId = 0; sideSlotId < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            int finalSideSlotId = sideSlotId;

            sideButtons[sideSlotId] = addRenderableWidget(
                    new XButton(leftPos+8+(18*sideSlotId), topPos+35, 18, 18, getSideTextButton(finalSideSlotId),
                        (btn) -> {
                            // ModNetworking.sendToServer(new PatternEncoderChangerSideC2SPacket(finalSideSlotId, true));
                        }
                    )
            );
            /*
            slotButtons[sideSlotId] = addRenderableWidget(
                    new XButton(leftPos+8+(18*sideSlotId), topPos+53, 18, 18, getSlotTextButton(finalSideSlotId),
                            (btn) -> {
                                // ModNetworking.sendToServer(new PatternEncoderChangerSideC2SPacket(finalSideSlotId, false));
                            }
                    )
            );
            */
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
        for (int sideSlotId = 0; sideSlotId < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            sideButtons[sideSlotId].renderButton(pPoseStack, mouseX, mouseY, delta, getSideTextButton(sideSlotId));
            // slotButtons[sideSlotId].renderButton(pPoseStack, mouseX, mouseY, delta, getSlotTextButton(sideSlotId));
        }

    }

    public PatternEncoderScreen(PatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;

        for (int sideSlotId = 0; sideSlotId < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            if (isMouseAboveArea(pMouseX, pMouseY, x, y, 8+(18*sideSlotId), 35, 18, 18)) {
                List<Component> pText = List.of(getTextTooltip(sideSlotId));
                renderTooltip(pPoseStack, pText, Optional.empty(), pMouseX, pMouseY);
            }
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTextureGui());
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }
    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_pattern_encoder_gui.png");
    }
}
