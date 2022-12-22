package fr.mamiemru.blocrouter.gui.screen.screens;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import fr.mamiemru.blocrouter.gui.screen.BaseContainerScreen;
import fr.mamiemru.blocrouter.items.custom.ItemFilter;
import fr.mamiemru.blocrouter.network.ModNetworking;
import fr.mamiemru.blocrouter.network.packet.FakeItemInsertionC2SPacket;
import fr.mamiemru.blocrouter.network.packet.FakeItemRemoveC2SPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

public class ItemFilterScreen extends BaseContainerScreen<ItemFilterMenu> {

    public ItemFilterScreen(ItemFilterMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public ResourceLocation getTextureGui() {
        return new ResourceLocation(BlocRouter.MOD_ID,"textures/gui/block_item_filter_gui.png");
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        for (int slotIndex = 0; slotIndex < ItemFilter.NUMBER_OF_ITEMS; ++slotIndex) {
            if (isMouseAboveArea(pMouseX, pMouseY, x, y, 9+slotIndex*18, 18, 18, 18)) {
                final ItemStack carriedItem = menu.getCarried();
                if (carriedItem.isEmpty()) {
                    ModNetworking.sendToServer(new FakeItemRemoveC2SPacket(getMenu().containerId, slotIndex));
                } else {
                    ModNetworking.sendToServer(new FakeItemInsertionC2SPacket(getMenu().containerId, slotIndex, carriedItem));
                }
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
