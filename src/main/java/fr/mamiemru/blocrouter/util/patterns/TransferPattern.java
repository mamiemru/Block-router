package fr.mamiemru.blocrouter.util.patterns;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TransferPattern extends Pattern {

    private List<ItemStack> ingredients;
    private List<ItemStack> trash;
    private BlockPos transferInput;
    private List<BlockPos> transferOutput;
    private int isExtraction;
    private int isWhitelist;

    public TransferPattern(
            String uuid, int isExtraction, int isWhitelist, List<ItemStack> ingredients, List<ItemStack> trash,
            BlockPos transferInput, List<BlockPos> transferOutput
    ) {
        super(uuid);
        this.isExtraction = isExtraction;
        this.isWhitelist = isWhitelist;
        this.ingredients = ingredients;
        this.trash = trash;
        this.transferInput = transferInput;
        this.transferOutput = transferOutput;
    }


    public List<ItemStack> getIngredients() { return this.ingredients; }
    public List<ItemStack> getTrash() { return this.trash; }
    public BlockPos getTransferInput() { return this.transferInput; }
    public List<BlockPos> getTransferOutput() { return this.transferOutput; }
    public int isExtraction() { return isExtraction; }
    public int isWhitelist() { return isWhitelist; }
}
