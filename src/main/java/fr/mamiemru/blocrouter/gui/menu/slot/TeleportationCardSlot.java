package fr.mamiemru.blocrouter.gui.menu.slot;

import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class TeleportationCardSlot extends SlotItemHandler {

    public TeleportationCardSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getCount() == 1 && stack.getItem() instanceof ItemTeleportationSlot;
    }
}
