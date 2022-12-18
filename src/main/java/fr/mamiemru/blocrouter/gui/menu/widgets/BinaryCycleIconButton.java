package fr.mamiemru.blocrouter.gui.menu.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class BinaryCycleIconButton extends Button {

    private List<CycleIconButton.Icon> icons;
    private Supplier<Boolean> supplier;

    public BinaryCycleIconButton(int xPos, int yPos, int width, int height, List<CycleIconButton.Icon> icons, OnPress handler, Supplier<Boolean> supplier) {
        super(xPos, yPos, width, height, Component.empty(), handler);
        this.icons = icons;
        this.supplier = supplier;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
        CycleIconButton.Icon icon = this.icons.get(supplier.get()? 0 : 1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, icon.resourceLocation);
        this.blit(poseStack, x+1, y, icon.offsetX, icon.offsetY, 18, 18);
    }

}
