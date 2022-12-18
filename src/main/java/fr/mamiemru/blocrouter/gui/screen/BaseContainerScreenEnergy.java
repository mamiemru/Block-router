package fr.mamiemru.blocrouter.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.OnOffState;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.widgets.BinaryCycleIconButton;
import fr.mamiemru.blocrouter.gui.menu.widgets.CycleIconButton;
import fr.mamiemru.blocrouter.gui.screen.renderer.EnergyInfoArea;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.SwitchableMachineStateC2SPacket;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;
import java.util.Optional;

public abstract class BaseContainerScreenEnergy<T extends BaseContainerMenuEnergy> extends BaseContainerScreen<T> {

    protected int imageWidth = 200;
    private BinaryCycleIconButton enabledBtn;
    private EnergyInfoArea energyInfoArea;

    public BaseContainerScreenEnergy(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private MutableComponent getRedstoneStateTooltip() {
        return Component.literal(OnOffState.fromIndex(getMenu().getRedstoneState()).toString());
    }

    protected void assignEnergyInfoArea() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        energyInfoArea = new EnergyInfoArea(x + 171, y + 9,  menu.getEntity().getEnergyStorage());
    }

    @Override
    protected void init() {
        super.init();
        assignEnergyInfoArea();

        List<CycleIconButton.Icon> state = List.of(
            new CycleIconButton.Icon(getTextureGui(), 204, 3), new CycleIconButton.Icon(getTextureGui(), 222, 3)
        );
        this.enabledBtn = addRenderableWidget(
                new BinaryCycleIconButton(leftPos-22, topPos+3, 20, 20, state, pButton -> {
                    boolean newState = getMenu().switchRedstoneState();
                    ModNetworking.sendToServer(new SwitchableMachineStateC2SPacket(newState));
                },
                        () -> getMenu().getRedstoneState()
                )
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
        energyInfoArea.draw(pPoseStack);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);
        int x = (width - imageWidth + 24) / 2;
        int y = (height - imageHeight) / 2;

        if (isMouseAboveArea(pMouseX, pMouseY, x, y, -22, 3, 19 , 19)) {
            renderTooltip(pPoseStack, List.of(getRedstoneStateTooltip()), Optional.empty(), pMouseX, pMouseY);
        }
    }

    protected void renderEnergyArea(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 170, 9, 10, 72)) {
            List<Component> tooltips = List.of(
                energyInfoArea.getTooltips(),
                Component.literal("Usage: "+getMenu().getEntity().getEnergyOperationCost()+"FE/t"),
                Component.literal("Max I/O Transfer: "+getMenu().getEntity().getEnergyMaxTransfer()+"FE/t")
            );
            renderTooltip(pPoseStack, tooltips, Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderButtons(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderLabels(pPoseStack, pMouseX, pMouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        renderEnergyArea(pPoseStack, pMouseX, pMouseY, x, y);
    }

    public void renderButtons(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        this.enabledBtn.renderButton(pPoseStack, mouseX, mouseY, delta);
    }
}
