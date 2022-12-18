package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class BaseContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public BaseContainerScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    public abstract ResourceLocation getTextureGui();

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTextureGui());
        int x = (width - imageWidth) / 2;
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
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    protected boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        int minZoneX = (x + offsetX);
        int minZoneY = (y + offsetY);
        return (pMouseX >= minZoneX && pMouseX <= minZoneX + width) && (pMouseY >= minZoneY && pMouseY <= minZoneY + height);
    }

    protected Vec3i getSlotVectorFromMouseCursorCoordinates(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int sizePerSlot) {
        int minZoneX = (x + offsetX);
        int minZoneY = (y + offsetY);
        pMouseX = pMouseX - minZoneX;
        pMouseY = pMouseY - minZoneY;

        return new Vec3i(Math.floorDiv(pMouseX, sizePerSlot), 0, Math.floorDiv(pMouseY, sizePerSlot));
    }
}
