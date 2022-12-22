package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.RouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemNormalRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.NormalRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RouterEntity extends BaseEntityEnergyRouter {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_OUTPUT_SLOT_0 = 9;
    public static final int SLOT_OUTPUT_SLOT_1 = 10;
    public static final int SLOT_OUTPUT_SLOT_2 = 11;
    public static final int SLOT_OUTPUT_SLOT_3 = 12;
    public static final int SLOT_OUTPUT_SLOT_4 = 13;
    public static final int SLOT_OUTPUT_SLOT_5 = 14;
    public static final int SLOT_UPGRADE = 15;
    public static final int SLOT_PATTERN_SLOT_0 = 16;
    public static final int SLOT_PATTERN_SLOT_1 = 17;
    public static final int SLOT_PATTERN_SLOT_2 = 18;
    public static final int SLOT_PATTERN_SLOT_3 = 19;
    public static final int NUMBER_OF_SLOTS = 20;

    public RouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ROUTER_ENTITY.get(), pos, state);
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
    }

    @Override
    protected int getSlotPatternSlot0() {
        return SLOT_PATTERN_SLOT_0;
    }

    @Override
    protected int getNumberOfPatternSlots() {
        return 4;
    }

    @Override
    protected int getSlotInputSlot0() {
        return SLOT_INPUT_SLOT_0;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_INPUT_SLOT_8;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return SLOT_OUTPUT_SLOT_0;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return SLOT_OUTPUT_SLOT_5;
    }

    @Override
    protected Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemNormalRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
                ItemNormalRoutingPattern.decodePatternTag(is) : null;
    }

    @Override
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemNormalRoutingPattern;
    }

    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {}

    private static void forEveryMachines(@NotNull RouterEntity pEntity, @NotNull NormalRoutingPattern pattern, BlockEntity block_entity_reference) {

        Level level = pEntity.getLevel();
        BlockEntity block_entity_selected  = block_entity_reference;
        IItemHandler targetItemHandler = pEntity.getItemHandler(level, pEntity.getBlockPos(), Direction.UP);
        while (block_entity_selected != null && block_entity_reference.getClass().equals(block_entity_selected.getClass())) {

            BlockPos entity_pos = block_entity_selected.getBlockPos();

            if (canCraft(pEntity.itemStackHandler, pattern, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8)) {
                processExtractionFromSelfToDistantWithPattern(pEntity, pEntity.itemStackHandler, pattern, entity_pos, SLOT_INPUT_SLOT_0, SLOT_INPUT_SLOT_8);
            }

            IItemHandler sourceItemHandler = pEntity.getItemHandler(level, entity_pos, Direction.DOWN);
            if (sourceItemHandler != null) {
                processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, SLOT_OUTPUT_SLOT_0, SLOT_OUTPUT_SLOT_5, 1);
            }
            block_entity_selected = level.getBlockEntity(block_entity_selected.getBlockPos().above());
        }
    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {
        BlockPos pos = getBlockPos();
        NormalRoutingPattern normalRoutingPattern = (NormalRoutingPattern) pattern;
        BlockState block_reference = level.getBlockState(pos.above());
        BlockEntity block_entity_reference = level.getBlockEntity(pos.above());

        if (block_reference != null && block_reference.hasBlockEntity() && itemStackHandler != null) {
            forEveryMachines(this, normalRoutingPattern, block_entity_reference);
        }
    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Router");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new RouterMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
