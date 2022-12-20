package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.VectorTypeMode;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.DispatcherPatternEncoderEntity;
import fr.mamiemru.blocrouter.util.patterns.DispatcherPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemDispatcherRoutingPattern extends ItemRoutingPattern {

    private static int uglyAlgo(int i) {
        return switch (i) {
            case 0 -> 8;
            case 1 -> 7;
            case 2 -> 6;
            case 3 -> 5;
            case 5 -> 3;
            case 6 -> 2;
            case 7 -> 1;
            case 8 -> 0;
            default -> i;
        };
    }

    public static BlockPos getBlockPosCoordinatesFromReferenceAndVectorType(DispatcherPatternEncoderEntity entity, int vectorType, Vec3i slotVector) {
        BlockPos blockPosInWorld = null;
        BlockPos referenceBlockPos = entity.getTeleportationCardBlockPos();
        if (referenceBlockPos != null) {
            switch (VectorTypeMode.fromIndex(vectorType)) {
                case HORIZONTAL -> {
                    blockPosInWorld = referenceBlockPos.offset(
                            uglyAlgo(slotVector.getZ()), 0, slotVector.getX()
                    ).atY(referenceBlockPos.getY());
                }
                case VERTICAL -> {
                    blockPosInWorld = referenceBlockPos.offset(
                            0, slotVector.getZ(), slotVector.getX()
                    ).atY(referenceBlockPos.getY() + uglyAlgo(slotVector.getZ()));
                }
                case DEPTH -> {
                    blockPosInWorld = referenceBlockPos.offset(
                            uglyAlgo(slotVector.getZ()), slotVector.getX(), 0
                    ).atY(referenceBlockPos.getY() + slotVector.getX());
                }
            }
            ;

            return blockPosInWorld;
        }

        return null;
    }

    public static CompoundTag encodeSlotAndCoordinates(DispatcherPatternEncoderEntity entity, int slotIndexX, int slotIndexY, ItemStack is) {
        BlockPos targetedBlockPos = getBlockPosCoordinatesFromReferenceAndVectorType(entity, entity.getVectorType(), new Vec3i(slotIndexX, 0, slotIndexY));
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("i", slotIndexX);
        compoundTag.putInt("j", slotIndexY);
        compoundTag.putInt("x", targetedBlockPos.getX());
        compoundTag.putInt("y", targetedBlockPos.getY());
        compoundTag.putInt("z", targetedBlockPos.getZ());
        is.save(compoundTag);
        return compoundTag;
    }

    public static void encodePatternTag(ItemStack is, ListTag ingredients, int vectorType, BlockPos referenceBlockPos) {
        CompoundTag nbtData = new CompoundTag();
        nbtData.put("ingredients", ingredients);
        nbtData.putInt("ingredientsSize", ingredients.size());
        nbtData.putInt("vectorType", vectorType);
        nbtData.putInt("x", referenceBlockPos.getX());
        nbtData.putInt("y", referenceBlockPos.getY());
        nbtData.putInt("z", referenceBlockPos.getZ());
        nbtData.putString(ItemRoutingPattern.getNbtUuid(), UUID.randomUUID().toString());
        is.setTag(nbtData);
    }

    public static DispatcherPattern decodePatternTag(ItemStack is) {
        CompoundTag compoundTag = is.getTag();
        List<DispatcherPattern.DispatcherPatternIngredient> dispatcherPatternIngredientList = new ArrayList<>();
        ListTag ingredients = compoundTag.getList("ingredients", Tag.TAG_COMPOUND);
        BlockPos reference = new BlockPos(compoundTag.getInt("x"), compoundTag.getInt("y"), compoundTag.getInt("z"));
        VectorTypeMode vectorType = VectorTypeMode.fromIndex(compoundTag.getInt("vectorType"));

        for (int ingredientId = 0; ingredientId < compoundTag.getInt("ingredientsSize"); ++ingredientId) {
            CompoundTag nbtIngredient = ingredients.getCompound(ingredientId);
            BlockPos blockPos = new BlockPos(nbtIngredient.getInt("x"), nbtIngredient.getInt("y"), nbtIngredient.getInt("z"));
            dispatcherPatternIngredientList.add(new DispatcherPattern.DispatcherPatternIngredient(
                    blockPos, ItemStack.of(nbtIngredient), nbtIngredient.getInt("i"), nbtIngredient.getInt("j")
            ));
        }

        return new DispatcherPattern(reference, vectorType, dispatcherPatternIngredientList);
    }

    private String getBlockStateNameAndCoordinates(Level level, BlockPos blockPos) {
        BlockState blockStateReference = level.getBlockState(blockPos);
        return blockStateReference.getBlock().getDescriptionId()+" "+blockPos;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            DispatcherPattern dispatcherPattern = decodePatternTag(pStack);

            pTooltipComponents.add(Component.literal("Apply vector "+ dispatcherPattern.getVectorType()));
            pTooltipComponents.add(Component.literal("With block reference "+getBlockStateNameAndCoordinates(level, dispatcherPattern.getReference())));

            pTooltipComponents.add(Component.literal("With ingredients: "));
            for (DispatcherPattern.DispatcherPatternIngredient ingredient : dispatcherPattern.getIngredientList()) {
                pTooltipComponents.add(Component.literal(ingredient.getItemStack()+" -> "+getBlockStateNameAndCoordinates(level, ingredient.getBlockPos())));
            }

            super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
        }
    }
}
