package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.entities.custom.patternEncoder.VacuumPatternEncoderEntity;
import fr.mamiemru.blocrouter.util.patterns.VacuumPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemVacuumRoutingPattern extends ItemRoutingPattern {


    public static void encodePatternTag(ItemStack is, ListTag ingredients, ListTag result) {
        CompoundTag nbtData = new CompoundTag();
        int ingredientsIndex = 0;
        for (Tag tag : ingredients) {
            nbtData.put(String.valueOf(ingredientsIndex++), tag);
        }
        for (Tag tag : result) {
            nbtData.put("r"+ingredientsIndex++, tag);
        }
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            VacuumPattern pattern = decodePatternTag(pStack);
            pTooltipComponents.add(Component.literal("To Drop"));
            for (ItemStack is : pattern.getRows()) {
                MutableComponent str = Component.literal(is.getItem()+" x"+is.getCount());
                pTooltipComponents.add(str);
            }
            pTooltipComponents.add(Component.literal("To grab"));
            for (ItemStack is : pattern.getResult()) {
                MutableComponent str = Component.literal(is.getItem()+" x"+is.getCount());
                pTooltipComponents.add(str);
            }
        }
    }

    public static VacuumPattern decodePatternTag(@NotNull ItemStack is) {
        if (is.hasTag()) {
            CompoundTag tag = is.getTag();
            Tag uuid = tag.get(ItemRoutingPattern.getNbtUuid());
            List<ItemStack> list = new ArrayList<>();
            List<ItemStack> result = new ArrayList<>();
            int index = 0;
            for(int i = 0; i < VacuumPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
                CompoundTag nbt = tag.getCompound(String.valueOf(i));
                if (!nbt.isEmpty()) {
                    ItemStack stack = ItemStack.of(nbt);
                    stack = stack.isEmpty() ? ItemStack.EMPTY : stack;
                    if (!stack.isEmpty()) {
                        list.add(index++, stack);
                    }
                }
            }
            index = 0;
            for(int i = 0; i < VacuumPatternEncoderEntity.NUMBER_OF_INGREDIENTS_OUTPUT_SLOTS; ++i) {
                CompoundTag nbt = tag.getCompound("r"+i);
                if (!nbt.isEmpty()) {
                    ItemStack stack = ItemStack.of(nbt);
                    stack = stack.isEmpty() ? ItemStack.EMPTY : stack;
                    if (!stack.isEmpty()) {
                        result.add(index++, stack);
                    }
                }
            }
            return new VacuumPattern(list, result, uuid.getAsString());
        }
        return null;
    }
}
