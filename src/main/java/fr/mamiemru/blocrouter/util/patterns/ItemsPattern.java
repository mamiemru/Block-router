package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class ItemsPattern extends Pattern {

    private List<ItemStack> rows;

    public ItemsPattern(List<ItemStack> rows, String uuid) {
        super(uuid);
        this.rows = rows;
    }

    public List<ItemStack> getRows() {
        return rows;
    }
}
