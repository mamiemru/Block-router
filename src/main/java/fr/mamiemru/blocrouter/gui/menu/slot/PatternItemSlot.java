package fr.mamiemru.blocrouter.gui.menu.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PatternItemSlot extends SlotItemHandler {

    private final int index;

    public PatternItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }
}
