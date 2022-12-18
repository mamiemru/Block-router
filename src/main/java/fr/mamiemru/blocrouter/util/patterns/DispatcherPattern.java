package fr.mamiemru.blocrouter.util.patterns;

import fr.mamiemru.blocrouter.entities.custom.patternEncoder.DispatcherPatternEncoderEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DispatcherPattern extends Pattern {

    public static class DispatcherPatternIngredient {
        private int slotIndexX;
        private int slotIndexY;
        private BlockPos blockPos;
        private ItemStack itemStack;
        public DispatcherPatternIngredient(BlockPos blockPos, ItemStack is, int slotIndexX, int slotIndexY) {
            this.blockPos = blockPos;
            this.itemStack = is;
            this.slotIndexX = slotIndexX;
            this.slotIndexY = slotIndexY;
        }
        public BlockPos getBlockPos() {return blockPos;}
        public ItemStack getItemStack() {return itemStack;}
    }

    private BlockPos reference;
    private DispatcherPatternEncoderEntity.VectorType vectorType;
    private List<DispatcherPatternIngredient> ingredientList;

    public DispatcherPattern(BlockPos reference, DispatcherPatternEncoderEntity.VectorType vectorType, List<DispatcherPatternIngredient> ingredientList) {
        super(null);
        this.reference = reference;
        this.vectorType = vectorType;
        this.ingredientList = ingredientList;
    }

    public BlockPos getReference() {
        return reference;
    }

    public DispatcherPatternEncoderEntity.VectorType getVectorType() {
        return vectorType;
    }

    public List<DispatcherPatternIngredient> getIngredientList() {
        return ingredientList;
    }

}
