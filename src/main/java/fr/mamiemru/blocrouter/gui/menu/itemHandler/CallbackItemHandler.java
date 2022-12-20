package fr.mamiemru.blocrouter.gui.menu.itemHandler;

import fr.mamiemru.blocrouter.items.custom.ItemFilter;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CallbackItemHandler extends ItemStackHandler {

    private final ItemStack inventoryItemStackHolder;

    public CallbackItemHandler(ItemStack inventoryItemStackHolder) {
        this(1, inventoryItemStackHolder);
    }

    public CallbackItemHandler(int size, ItemStack inventoryItemStackHolder) {
        this(NonNullList.withSize(size, ItemStack.EMPTY), inventoryItemStackHolder);
    }

    public CallbackItemHandler(NonNullList<ItemStack> stacks, ItemStack inventoryItemStackHolder) {
        super(stacks);
        this.inventoryItemStackHolder = inventoryItemStackHolder;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        ItemFilter.saveNbt(this.inventoryItemStackHolder, this);
    }
}
