package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.SortMode;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergySortMode;
import fr.mamiemru.blocrouter.gui.menu.widgets.CycleIconButton;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.SortableModeMachineStateC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public abstract class BaseContainerScreenEnergySortMode<T extends BaseContainerMenuEnergySortMode> extends BaseContainerScreenEnergy<T> {

    private CycleIconButton sortMethod;

    public BaseContainerScreenEnergySortMode(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private MutableComponent getSortMethodStateTooltip() {
        return Component.literal(getMenu().getSortMethodState() + " mode");
    }

    @Override
    protected void init() {
        super.init();

        List<CycleIconButton.Icon> state = List.of(
                new CycleIconButton.Icon(getTextureGui(), 204, 23), new CycleIconButton.Icon(getTextureGui(), 222, 23)
        );
        this.sortMethod = addRenderableWidget(
                new CycleIconButton(leftPos-22, topPos+25, 20, 20, state, pButton -> {
                    Comparable newState = getMenu().switchSortMethodState();
                    ModNetworking.sendToServer(new SortableModeMachineStateC2SPacket(newState));
                },
                        () -> SortMode.toIndex(getMenu().getSortMethodState())
                )
        );
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, -22, 25, 20 , 20)) {
            renderTooltip(pPoseStack, List.of(getSortMethodStateTooltip()), Optional.empty(), pMouseX, pMouseY);
        }
    }

    @Override
    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        super.renderButtons(pPoseStack, mouseX, mouseY, delta);
        this.sortMethod.renderButton(pPoseStack, mouseX, mouseY, delta);
    }
}
