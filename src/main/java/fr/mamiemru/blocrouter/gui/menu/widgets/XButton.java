package fr.mamiemru.blocrouter.gui.menu.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class XButton extends ExtendedButton {

    public XButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    public XButton(int xPos, int yPos, int width, int height, String displayString, OnPress handler) {
        this(xPos, yPos, width, height, Component.literal(displayString), handler);
    }

    public XButton(int xPos, int yPos, int width, int height, int displayString, OnPress handler) {
        this(xPos, yPos, width, height, String.valueOf(displayString), handler);
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick, MutableComponent text) {
        setMessage(text);
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
    }
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick, String text) {
        renderButton(poseStack, mouseX, mouseY, partialTick, Component.literal(text));
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick, int text) {
        renderButton(poseStack, mouseX, mouseY, partialTick, String.valueOf(text));
    }
}
