package fr.mamiemru.blocrouter.util.patterns;

import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class Pattern {

    private String uuid;

    public Pattern(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }


    public static CompoundTag encodePatternIngredient(int index, ItemStack stack, int side) {
        if(stack.isEmpty()) {
            stack = new ItemStack((Item)null);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("index", index);
        nbt.putInt("side", side);
        stack.save(nbt);
        return nbt;
    }

    public static CompoundTag encodeBlockPos(CompoundTag nbt, BlockPos blockPos) {
        nbt.putInt("x", blockPos.getX());
        nbt.putInt("y", blockPos.getY());
        nbt.putInt("z", blockPos.getZ());
        return nbt;
    }

    public static CompoundTag encodePatternIngredient(int index, ItemStack stack, int side, BlockPos blockPos) {
        return encodeBlockPos(encodePatternIngredient(index, stack, side), blockPos);
    }

    public static CompoundTag encodeIngredient(ItemStack stack) {
        if(stack.isEmpty()) {
            stack = new ItemStack((Item)null);
        }
        CompoundTag nbt = new CompoundTag();
        stack.save(nbt);
        return nbt;
    }

    public static CompoundTag encodeCoords(ItemStack stack) {
        return ItemTeleportationSlot.encodeCoords(ItemTeleportationSlot.getCoordinates(stack));
    }

}
