package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.BufferEntity;
import fr.mamiemru.blocrouter.gui.menu.BufferMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.NumberEditBox;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.BufferSetItemThresholdC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;

public class BufferScreen extends AbstractAbstractContainerScreen<BufferMenu> {

    @Override
    protected void init() {
        super.init();

        MutableComponent text = Component.literal("0");
        BufferEntity entity = ((BufferEntity)menu.getEntity());

        NumberEditBox numberEditBox = new NumberEditBox(this.font, leftPos+40, topPos+52, 50, 14, text);

        addRenderableWidget(numberEditBox);
        addRenderableWidget(
                new ExtendedButton(leftPos+95, topPos+52, 50, 15, Component.literal("Set Limit"),
                    (btn) -> {
                        if (numberEditBox.valueExist()) {
                            ModNetworking.sendToServer(new BufferSetItemThresholdC2SPacket(entity.getBlockPos(), numberEditBox.getValue()));
                        }
                    }
                )
        );
    }

    public BufferScreen(BufferMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);

        MutableComponent text = Component.literal(menu.getItemCount() + " / " + menu.getItemMaxCount());
        this.font.draw(pPoseStack, text, 40, 18, 4210752);
        MutableComponent filtered = Component.literal(menu.getBookedItem());
        this.font.draw(pPoseStack, filtered, 40, 32, 4210752);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_item_buffer_gui.png");
    }
}
