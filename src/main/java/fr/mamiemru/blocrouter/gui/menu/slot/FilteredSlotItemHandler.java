package fr.mamiemru.blocrouter.gui.menu.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FilteredSlotItemHandler extends SlotItemHandler {

    public FilteredSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
}
