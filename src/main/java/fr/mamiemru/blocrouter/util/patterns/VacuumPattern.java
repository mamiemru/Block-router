package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class VacuumPattern extends ItemsPattern {

    private List<ItemStack> result;

    public VacuumPattern(List<ItemStack> rows, List<ItemStack> result, String uuid) {
        super(rows, uuid);
        this.result = result;
    }

    public List<ItemStack> getResult() {
        return result;
    }
}
