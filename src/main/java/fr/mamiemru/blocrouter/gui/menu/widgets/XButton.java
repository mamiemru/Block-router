package fr.mamiemru.blocrouter.gui.menu.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class XButton extends ExtendedButton {

    public XButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick, MutableComponent text) {
        setMessage(text);
        super.renderButton(poseStack, mouseX, mouseY, partialTick);
    }
}
