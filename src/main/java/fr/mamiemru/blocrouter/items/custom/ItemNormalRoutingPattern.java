package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.Sides;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.util.patterns.NormalRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.PatternRow;
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

public class ItemNormalRoutingPattern extends ItemRoutingPattern {

    public static void encodePatternTag(ItemStack is, ListTag ingredients) {
        CompoundTag nbtData = new CompoundTag();
        int ingredientsIndex = 0;
        for (Tag tag : ingredients) {
            nbtData.put(String.valueOf(ingredientsIndex++), tag);
        }
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    public static NormalRoutingPattern decodePatternTag(@NotNull ItemStack is) {
        if (is.hasTag()) {
            CompoundTag tag = is.getTag();
            Tag uuid = tag.get(ItemRoutingPattern.getNbtUuid());
            List<PatternRow> list = new ArrayList<>();
            int index = 0;
            for(int i = 0; i < PatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
                CompoundTag nbt = tag.getCompound(String.valueOf(i));
                if (!nbt.isEmpty()) {
                    int slot = nbt.getInt("index");
                    int side = nbt.getInt("side");
                    ItemStack stack = ItemStack.of(nbt);
                    stack = stack.isEmpty() ? ItemStack.EMPTY : stack;
                    if (!stack.isEmpty()) {
                        list.add(index++, new PatternRow(slot, stack, side));
                    }
                }
            }
            return new NormalRoutingPattern(
                    list, uuid.getAsString()
            );
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            pTooltipComponents.add(Component.literal("Ingredients"));
            NormalRoutingPattern pattern = (NormalRoutingPattern) decodePatternTag(pStack);
            for (PatternRow row : pattern.getRows()) {
                MutableComponent str = Component.empty();
                str.append("Side ");
                str.append(Sides.fromIndex(row.axe).toString());
                str.append(" ");
                str.append(row.is.getItem()+" x"+row.is.getCount());
                pTooltipComponents.add(str);
            }
        }

        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }
}
