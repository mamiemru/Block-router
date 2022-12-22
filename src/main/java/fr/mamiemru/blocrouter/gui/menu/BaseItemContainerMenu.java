package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.gui.menu.itemHandler.CallbackItemHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import fr.mamiemru.blocrouter.gui.menu.slot.SlotLocked;
import fr.mamiemru.blocrouter.items.custom.ItemFilter;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public abstract class BaseItemContainerMenu extends AbstractContainerMenu {

    protected final Inventory inv;
    protected final Player player;
    protected ItemStack containerStack;
    protected ItemStackHandler invWrapper;
    protected Slot slotLocked;

    protected abstract ItemStackHandler loadInventory(ItemStack itemStack);

    public BaseItemContainerMenu(MenuType<ItemFilterMenu> menu, int windowId, Inventory inventory, Player player) {
        super(menu, windowId);
        this.inv = inventory;
        this.player = player;
        this.containerStack = player.getMainHandItem();

        invWrapper = loadInventory(containerStack);
        initPlayerSLots();
    }

    protected void initPlayerSLots() {
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            if (i == playerInventory.selected) {
                this.slotLocked = new SlotLocked(playerInventory, i, 8 + i * 18, 144);
                this.addSlot(this.slotLocked);
            } else {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
            }
        }
    }
}
