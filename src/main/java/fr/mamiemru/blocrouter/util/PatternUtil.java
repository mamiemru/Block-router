package fr.mamiemru.blocrouter.util;

import fr.mamiemru.blocrouter.entities.custom.PatternEncoderEntity;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class PatternUtil {

    public static final String[] LIST_OF_AXES = {" ", "N", "S", "E", "W", "U", "D"};

    public static final String axesToString(String axe) {
        return switch (axe) {
            case "N" -> "North";
            case "S" -> "South";
            case "E" -> "East";
            case "W" -> "West";
            case "U" -> "Up";
            case "D" -> "Down";
            default -> "<not set>";
        };
    }

    public static final String axeIdToString(int side) {
        return axesToString(LIST_OF_AXES[side]);
    }

    public static CompoundTag encodePatternIngredient(int index, ItemStack stack, int side) {
        boolean empty = stack.isEmpty();
        if(empty) {
            stack = new ItemStack((Item)null);
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("index", index);
        nbt.putInt("side", side);
        stack.save(nbt);
        return nbt;
    }

    public static Pattern decodePatternIngredient(CompoundTag tag) {
        Tag uuid = tag.get(ItemRoutingPattern.getNbtUuid());
        List<PatternRow> list = new ArrayList<>();
        int index = 0;
        for(int i = 0; i < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
            CompoundTag nbt = tag.getCompound(String.valueOf(i));
            if (!nbt.isEmpty()) {
                int slot = nbt.getInt("Index");
                int side = nbt.getInt("side");
                ItemStack stack = ItemStack.of(nbt);
                stack = stack.isEmpty() ? ItemStack.EMPTY : stack;
                if (!stack.isEmpty()) {
                    list.add(index++, new PatternRow(slot, stack, side));
                }
            }
        }
        return new Pattern(
            list, uuid.getAsString()
        );
    }
}
