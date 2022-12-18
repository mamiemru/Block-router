package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.entities.custom.patternEncoder.EnderPatternEncoderEntity;
import fr.mamiemru.blocrouter.util.patterns.EnderRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.PatternRowCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemEnderRoutingPattern extends ItemRoutingPattern {

    public static void encodePatternTag(ItemStack is, ListTag ingredients, ListTag outputs) {
        CompoundTag nbtData = new CompoundTag();
        int ingredientsIndex = 0;
        for (Tag tag : ingredients) {
            nbtData.put(String.valueOf(ingredientsIndex++), tag);
        }
        nbtData.put("outputs", outputs);
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            pTooltipComponents.add(Component.literal("Ingredients and locations"));
            EnderRoutingPattern pattern =decode(pStack.getTag());
            for (PatternRowCoordinates row : pattern.getRows()) {
                MutableComponent str = Component.empty();
                BlockPos blockPos = row.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);
                str.append(row.is.getItem()+" x"+row.is.getCount()+" -> "+blockState.getBlock().getDescriptionId()+": "+blockPos.toShortString());
                pTooltipComponents.add(str);
            }

            if (!pattern.getOutputs().isEmpty()) {
                pTooltipComponents.add(Component.literal("Retrieve items from"));
                for (BlockPos blockPos : pattern.getOutputs()) {
                    MutableComponent str = Component.empty();
                    BlockState blockState = level.getBlockState(blockPos);
                    str.append(blockState.getBlock().getDescriptionId()+": "+blockPos.toShortString());
                    pTooltipComponents.add(str);
                }
            }
        }

        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public static EnderRoutingPattern decode(CompoundTag tag) {
        Tag uuid = tag.get(ItemRoutingPattern.getNbtUuid());
        List<PatternRowCoordinates> patternRowCoordinatesArrayList = new ArrayList<>();
        List<BlockPos> blockPosArrayList = new ArrayList<>();

        for(int i = 0; i < EnderPatternEncoderEntity.NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++i) {
            CompoundTag nbt = tag.getCompound(String.valueOf(i));
            if (!nbt.isEmpty()) {
                int slot = nbt.getInt("index");
                int side = nbt.getInt("side");
                ItemStack stack = ItemStack.of(nbt);
                stack = stack.isEmpty() ? ItemStack.EMPTY : stack;
                BlockPos blockPos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));

                if (!stack.isEmpty()) {
                    patternRowCoordinatesArrayList.add(new PatternRowCoordinates(slot, stack, side, blockPos));
                }
            }
        }

        ListTag nbtOutput = tag.getList("outputs", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < EnderPatternEncoderEntity.NUMBER_OF_TELEPORTATION_CARD_OUTPUT_SLOTS; ++i) {
            CompoundTag nbt = nbtOutput.getCompound(i);
            if (!nbt.isEmpty()) {
                blockPosArrayList.add(Pattern.decodeCoords(nbt));
            }
        }

        return new EnderRoutingPattern(patternRowCoordinatesArrayList, blockPosArrayList, uuid.getAsString());
    }
}
