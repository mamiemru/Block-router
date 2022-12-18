package fr.mamiemru.blocrouter.gui.screen.screens.routers;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.routers.VacuumRouterEntity;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.SlotRouterMenu;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.VacuumRouterMenu;
import fr.mamiemru.blocrouter.gui.menu.widgets.XButton;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreenEnergy;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.VacuumRouterChangerDropHeightC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public class VacuumRouterScreen extends BaseContainerScreenEnergy<VacuumRouterMenu> {

    private XButton dropHeight;

    public VacuumRouterScreen(VacuumRouterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    public MutableComponent getDropHeight() {
        return Component.literal(String.valueOf(VacuumRouterEntity.DROP_HEIGHT_CYCLE[getMenu().getDropHeightIndex()]));
    }

    @Override
    protected void init() {
        super.init();

        this.dropHeight = addRenderableWidget(
                new XButton(leftPos-22, topPos+24, 20, 20, getDropHeight(), (btn) -> {
                    int index = getMenu().incrementHeightIndex();
                    ModNetworking.sendToServer(new VacuumRouterChangerDropHeightC2SPacket(index));
                })
        );
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_vacuum_router_gui.png");
    }


    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, -22, 24, 19 , 19)) {
            renderTooltip(pPoseStack, List.of(Component.literal("Drop items at height")), Optional.empty(), pMouseX, pMouseY);
        }
    }

    @Override
    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        super.renderButtons(pPoseStack, mouseX, mouseY, delta);
        this.dropHeight.renderButton(pPoseStack, mouseX, mouseY, delta, getDropHeight());
    }
}
