package fr.mamiemru.blocrouter.gui.menu.slot;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class PatternItemSlotWithBg extends PatternItemSlot {

    private final int index;

    public PatternItemSlotWithBg(IItemHandler container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
        this.index = index;
    }
}
