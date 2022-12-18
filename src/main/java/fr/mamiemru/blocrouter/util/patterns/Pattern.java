package fr.mamiemru.blocrouter.util.patterns;


import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    public static ItemStack decodeIngredient(CompoundTag compoundTag) {
        return ItemStack.of(compoundTag);
    }

    public static List<ItemStack> decodeIngredients(ListTag listTag) {
        List<ItemStack> l = new ArrayList<>();
        for (int i = 0; i < listTag.size(); ++i) {
            l.add(decodeIngredient(listTag.getCompound(i)));
        }
        return l;
    }

    public static List<BlockPos> decodeCoords(ListTag listTag) {
        List<BlockPos> l = new ArrayList<>();
        for (int i = 0; i < listTag.size(); ++i) {
            l.add(decodeCoords(listTag.getCompound(i)));
        }
        return l;
    }

    public static CompoundTag encodeCoords(ItemStack stack) {
        return ItemTeleportationSlot.encodeCoords(ItemTeleportationSlot.getCoordinates(stack));
    }

    public static BlockPos decodeCoords(CompoundTag compoundTag) {
        return ItemTeleportationSlot.decodeCoords(compoundTag);
    }

    public static TransferPattern decodeTransferPattern(CompoundTag tag) {
        Tag uuid = tag.get(ItemRoutingPattern.getNbtUuid());
        int isExtraction = tag.getInt("isExtraction");
        int isWhitelist = tag.getInt("isWhitelist");

        List<ItemStack> ingredients = decodeIngredients(tag.getList("ingredients", Tag.TAG_COMPOUND));
        List<ItemStack> trash = decodeIngredients(tag.getList("trash", Tag.TAG_COMPOUND));

        List<BlockPos> transferOutput = decodeCoords(tag.getList("transferOutput", Tag.TAG_COMPOUND));
        BlockPos transferInput = decodeCoords(tag.getList("transferOutput", Tag.TAG_COMPOUND).getCompound(0));

        return new TransferPattern(uuid.getAsString(), isExtraction, isWhitelist, ingredients, trash, transferInput, transferOutput);
    }
}
