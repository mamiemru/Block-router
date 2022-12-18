package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.DispatcherPatternEncoderEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.DispatcherPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.CycleIconButton;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreen;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import fr.mamiemru.blocrouter.items.custom.ItemDispatcherRoutingPattern;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.DispatcherPatternEncoderChangerVectorTypeC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderEncodeC2SPacket;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.List;
import java.util.Optional;

public class DispatcherPatternEncoderScreen extends BaseContainerScreenPatternEncoder<DispatcherPatternEncoderMenu> {

    protected int imageHeight = 275;
    private CycleIconButton vectorType;

    public DispatcherPatternEncoderScreen(DispatcherPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();

        List<CycleIconButton.Icon> vector_space_icons = List.of(
                new CycleIconButton.Icon(getTextureGui(), 180, 3), new CycleIconButton.Icon(getTextureGui(), 198, 3),
                new CycleIconButton.Icon(getTextureGui(), 216, 3)
        );

        this.vectorType = addRenderableWidget(
            new CycleIconButton(leftPos+25, topPos+115, 20, 20, vector_space_icons,
                (btn) -> {
                    int vectorType = getMenu().incrementVectorType();
                    ModNetworking.sendToServer(new DispatcherPatternEncoderChangerVectorTypeC2SPacket(vectorType));
                    getMenu().setVectorType(vectorType);
                },
                () -> getMenu().getVectorType()
            )
        );
    }

    @Override
    protected void displayEncodeButton() {
        addRenderableWidget(
            new ExtendedButton(leftPos+90, topPos+117, 50, 18, Component.literal("Encode"),
                (btn) -> ModNetworking.sendToServer(new PatternEncoderEncodeC2SPacket())
            )
        );
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderButtons(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        // this.vectorType.renderButton(pPoseStack, mouseX, mouseY, delta);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {

    }


    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int offsetX = 19;
        int offsetY = 7;
        if (getMenu().getEntity().hasTeleportationCardSlot() && isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, 161, 161)) {
            Vec3i slotVector = getSlotVectorFromMouseCursorCoordinates(pMouseX, pMouseY, x, y, offsetX, offsetY, 18);
            BlockPos blockPosInWorld = ItemDispatcherRoutingPattern.getBlockPosCoordinatesFromReferenceAndVectorType(getMenu().getEntity(), getMenu().getVectorType(), slotVector);
            BlockState blockStateInWorld = getMenu().getEntity().getBlockStateFromCoordinates(blockPosInWorld);
            List<Component> pText = List.of(
                    Component.literal("Target: " + blockStateInWorld.getBlock().getDescriptionId()),
                    Component.literal("Coordinates: " + blockPosInWorld.toShortString())
            );
            renderTooltip(pPoseStack, pText, Optional.empty(), x - 12, y - 12);
        }

        else if (isMouseAboveArea(pMouseX, pMouseY, x, y,37,153,20,36)) {
            List<Component> pText = List.of(
                    Component.literal(DispatcherPatternEncoderEntity.VectorType.fromIndex(getMenu().getVectorType()) + " perspective")
            );
            renderTooltip(pPoseStack, pText, Optional.empty(), pMouseX, pMouseY);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTextureGui());
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight, 256, 300);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_dispatcher_pattern_encoder_gui.png");
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
