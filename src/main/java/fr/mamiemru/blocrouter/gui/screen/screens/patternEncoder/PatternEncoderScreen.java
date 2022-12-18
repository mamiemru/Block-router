package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.Sides;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.PatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class PatternEncoderScreen extends BaseContainerScreenPatternEncoder<PatternEncoderMenu> {

    protected int imageWidth = 200;
    private XButton[] sideButtons = new XButton[PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS];

    public PatternEncoderScreen(PatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    private MutableComponent getSideTextButton(int index) {
        return Component.literal(Sides.fromIndex(getMenu().getSideData(index)).toString());
    }

    private MutableComponent getTextTooltip(int index) {
        return Component.literal("Insert the ItemStack above on " +
                Sides.fromIndex(getMenu().getSideData(index)) +
                " side"
        );
    }

    @Override
    protected void init() {
        super.init();

        for (int sideSlotId = 0; sideSlotId < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            int finalSideSlotId = sideSlotId;

            sideButtons[sideSlotId] = addRenderableWidget(
                    new XButton(leftPos+8+(18*sideSlotId), topPos+35, 18, 18, getSideTextButton(finalSideSlotId),
                        (btn) -> {
                            // ModNetworking.sendToServer(new PatternEncoderChangerSideC2SPacket(finalSideSlotId, true));
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
        for (int sideSlotId = 0; sideSlotId < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++sideSlotId) {
            sideButtons[sideSlotId].renderButton(pPoseStack, mouseX, mouseY, delta, getSideTextButton(sideSlotId));
        }

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
