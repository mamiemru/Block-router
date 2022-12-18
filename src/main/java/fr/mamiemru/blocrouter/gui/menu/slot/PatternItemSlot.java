package fr.mamiemru.blocrouter.gui.menu.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PatternItemSlot extends SlotItemHandler {

    private final int index;

    public PatternItemSlot(IItemHandler container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        this.index = index;
    }
}
