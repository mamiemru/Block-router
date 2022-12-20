package fr.mamiemru.blocrouter.gui.menu.menus;

import fr.mamiemru.blocrouter.gui.menu.BaseItemContainerMenu;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import fr.mamiemru.blocrouter.gui.menu.itemHandler.CallbackItemHandler;
import fr.mamiemru.blocrouter.items.custom.ItemFilter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ItemFilterMenu extends BaseItemContainerMenu {

    public ItemFilterMenu(int windowId, Inventory inventory, Player player){
        super(MenuTypes.ITEM_FILTER_MENU.get(), windowId, inventory, player);

        int px = 0;
        int py = 0;
        for (int slotId = 0; slotId < invWrapper.getSlots(); ++slotId) {
            this.addSlot(new SlotItemHandler(invWrapper, slotId, 9 + 18 * slotId, 18 + 18 * py));
            if (px == 8) {
                ++py;
                px = 0;
            } else {
                ++px;
            }
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return slotLocked.getItem() == containerStack;
    }

    @Override
    protected CallbackItemHandler loadInventory(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemFilter) {
            return ItemFilter.loadNbt(itemStack);
        }
        return ItemFilter.emptyInventory(itemStack);
    }
}
