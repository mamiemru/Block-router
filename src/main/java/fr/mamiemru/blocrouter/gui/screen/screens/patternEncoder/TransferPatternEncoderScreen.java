package fr.mamiemru.blocrouter.gui.screen.screens.patternEncoder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.IOMode;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.WhiteBlackList;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.TransferPatternEncoderMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenPatternEncoder;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.TransferPatternEncoderEncodeIEC2SPacket;
import fr.mamiemru.blocrouter.network.packet.TransferPatternEncoderEncodeWhiteBlackListC2SPacket;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class TransferPatternEncoderScreen extends BaseContainerScreenPatternEncoder<TransferPatternEncoderMenu> {

    private XButton insertOrExtract;
    private XButton whiteBlackList;

    private MutableComponent getWhiteBlackListTextButton() {
        return Component.literal(WhiteBlackList.fromIndex(getMenu().getWhiteBlackList()).toString());
    }

    private MutableComponent getInsertOrExtractTextButton() {
        return Component.literal(IOMode.fromIndex(getMenu().getInsertOrExtract()).toString());
    }

    private MutableComponent getWhiteBlackListToolTip() {
        String f = WhiteBlackList.fromIndex(getMenu().getWhiteBlackList()).toString();
        if (getMenu().isExtract()) {
            return Component.literal(f + " for insertion");
        } else {
            return Component.literal(f + " for extraction");
        }
    }

    private MutableComponent getInsertOrExtractToolTip() {
        if (getMenu().isExtract()) {
            return Component.literal("Extract everything from Input but insert with filters");
        } else {
            return Component.literal("Extract with filter from Input but insert everything");
        }
    }

    @Override
    protected void init() {
        super.init();

        this.insertOrExtract = addRenderableWidget(
            new XButton(leftPos+6, topPos+35, 18, 18, getInsertOrExtractTextButton(),
                (btn) -> {
                    int ioe = getMenu().getInsertOrExtract();
                    ModNetworking.sendToServer(new TransferPatternEncoderEncodeIEC2SPacket(ioe));
                    getMenu().toggleInsertOrExtract(ioe);
                    getMenu().initSlots();
                }
            )
        );
        this.whiteBlackList = addRenderableWidget(
            new XButton(leftPos+6, topPos+53, 18, 18, getWhiteBlackListTextButton(),
                (btn) -> {
                    int wb = getMenu().getWhiteBlackList();
                    ModNetworking.sendToServer(new TransferPatternEncoderEncodeWhiteBlackListC2SPacket(wb));
                    getMenu().toggleWhiteBlackList(wb);
                }
            )
        );
    }

    public TransferPatternEncoderScreen(TransferPatternEncoderMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,
                (getMenu().isExtract()) ? "textures/gui/block_transfer_pattern_encoder_insertion_gui.png" : "textures/gui/block_transfer_pattern_encoder_extraction_gui.png"
        );
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
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderButtons(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int mouseX, int mouseY) {
        super.renderTooltip(pPoseStack, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(mouseX, mouseY, x, y, 6, 35, 18, 18)) {
            List<Component> pText = List.of(getInsertOrExtractToolTip());
            renderTooltip(pPoseStack, pText, Optional.empty(), mouseX, mouseY);
        }
        if (isMouseAboveArea(mouseX, mouseY, x, y, 6, 53, 18, 18)) {
            List<Component> pText = List.of(getWhiteBlackListToolTip());
            renderTooltip(pPoseStack, pText, Optional.empty(), mouseX, mouseY);
        }
    }

    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        this.whiteBlackList.renderButton(pPoseStack, mouseX, mouseY, delta, getWhiteBlackListTextButton());
        this.insertOrExtract.renderButton(pPoseStack, mouseX, mouseY, delta, getInsertOrExtractTextButton());
    }
}
