package fr.mamiemru.blocrouter.gui.menu.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Supplier;

public class CycleIconButton extends Button {

    private List<Icon> icons;
    private Supplier<Integer> supplier;


    public CycleIconButton(int xPos, int yPos, int width, int height, List<Icon> icons, OnPress handler, Supplier<Integer> supplier) {
        super(xPos, yPos, width, height, Component.empty(), handler);
        this.icons = icons;
        this.supplier = supplier;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
        Icon icon = icons.get(supplier.get());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, icon.resourceLocation);
        blit(poseStack, x+2, y+2, icon.offsetX, icon.offsetY, 18, 18);
    }

    public static class Icon {
        int offsetX;
        int offsetY;
        ResourceLocation resourceLocation;

        public Icon(ResourceLocation resourceLocation, int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.resourceLocation = resourceLocation;
        }

        @Override
        public String toString() {
            return "Icon{" +
                    "offsetX=" + offsetX +
                    ", offsetY=" + offsetY +
                    ", resourceLocation=" + resourceLocation +
                    '}';
        }
    }
}
