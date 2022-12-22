package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuPatternEncoder;
import fr.mamiemru.blocrouter.gui.menu.widgets.CycleIconButton;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.FakeItemInsertionC2SPacket;
import fr.mamiemru.blocrouter.network.packet.FakeItemRemoveC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderChangeC2SPacket;
import fr.mamiemru.blocrouter.network.packet.PatternEncoderEncodeC2SPacket;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public abstract class BaseContainerScreenPatternEncoder<T extends BaseContainerMenuPatternEncoder> extends BaseContainerScreen<T> {

    protected int imageWidth = 200;

    @Override
    protected void init() {
        super.init();

        displayEncodeButton();

        for (int patternEncoderIndex = 0; patternEncoderIndex < PatternEncoderChangeC2SPacket.patternEncoderList.length; ++patternEncoderIndex) {
            int finalPatternEncoderIndex = patternEncoderIndex;
            addRenderableWidget(
                    new XButton(leftPos - 62, topPos + 3 + (finalPatternEncoderIndex * 23), 60, 20,
                            Component.literal(PatternEncoderChangeC2SPacket.patternEncoderList[patternEncoderIndex]),
                            (btn) -> ModNetworking.sendToServer(new PatternEncoderChangeC2SPacket(finalPatternEncoderIndex))
                    )
            );
        }
    }

    protected void displayEncodeButton() {
        addRenderableWidget(
            new ExtendedButton(leftPos+90, topPos+63, 50, 18, Component.literal("Encode"),
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

    private void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    }


    public BaseContainerScreenPatternEncoder(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTextureGui());
        this.blit(pPoseStack, getX(), getY(), 0, 0, imageWidth, imageHeight);
    }

    protected boolean changeFakeItemStackFromCoordinates(double pMouseX, double pMouseY, int x, int y, int offsetX, int offsetY, int slotIndex) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, 18, 18)) {
            final ItemStack carriedItem = menu.getCarried();
            if (carriedItem.isEmpty()) {
                ModNetworking.sendToServer(new FakeItemRemoveC2SPacket(getMenu().containerId, slotIndex));
            } else {
                ModNetworking.sendToServer(new FakeItemInsertionC2SPacket(getMenu().containerId, slotIndex, carriedItem));
            }
            return true;
        }
        return false;
    }

    protected int getX() {
        return (width - imageWidth + 24) / 2;
    }

    protected int getY() {
        return (height - imageHeight) / 2;
    }
}
