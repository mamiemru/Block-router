package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.world.item.ItemStack;

public class PatternRow {
    public int slot;
    public ItemStack is;
    public int axe;

    public PatternRow(int slot, ItemStack is, int axe) {
        this.slot = slot;
        this.is = is;
        this.axe = axe;
    }
}
