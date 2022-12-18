package fr.mamiemru.blocrouter.entities.custom.routers;

import fr.mamiemru.blocrouter.entities.BaseEntityEnergyRouter;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.routers.SlotRouterMenu;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemSlotRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.SlotPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotRouterEntity extends BaseEntityEnergyRouter {

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
    public static final int NUMBER_OF_SLOTS = 17;

    public SlotRouterEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.SLOT_ROUTER_ENTITY.get(), pos, state);
    }


    @Override
    protected Pattern getCastedPattern(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemSlotRoutingPattern && is.getCount() == 1 && is.hasTag()) ?
            ItemSlotRoutingPattern.decodePatternTag(is) : null;
    }

    @Override
    protected int getSlotPatternSlot0() {
        return SLOT_PATTERN_SLOT_0;
    }

    @Override
    protected int getNumberOfPatternSlots() {
        return 1;
    }

    @Override
    protected int getSlotUpgrade() {
        return SLOT_UPGRADE;
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
    protected boolean isPatternRight(Item item) {
        return item instanceof ItemSlotRoutingPattern;
    }

    @Override
    public int getNumberOfSlots() {return NUMBER_OF_SLOTS;}
    public Component getDisplayName() {return Component.literal("Slot Router");}

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SlotRouterMenu(pContainerId, pPlayerInventory, this, this.data);
    }


    @Override
    protected void handleExtraction(@NotNull Pattern pattern) {
        SlotPattern slotPattern = (SlotPattern) pattern;
        BlockPos entity_pos = slotPattern.getTarget();
        IItemHandler sourceItemHandler = getItemHandler(level, entity_pos, Direction.DOWN);
        IItemHandler targetItemHandler = getItemHandler(level, getBlockPos(), Direction.UP);

        if (sourceItemHandler != null) {
            processExtractionFromDistantToSelf(sourceItemHandler, targetItemHandler, getSlotOutputSlot0(), getSlotOutputSlotN(), 1);
        }
    }

    @Override
    protected void handleProcessing(@NotNull Pattern pattern) {
        SlotPattern slotPattern = (SlotPattern) pattern;
        BlockPos entity_pos = slotPattern.getTarget();
        if (canCraft(this.itemStackHandler, slotPattern, getSlotInputSlot0(), getSlotInputSlotN())) {
            processExtractionFromSelfToDistantWithPattern(this, this.itemStackHandler, slotPattern, entity_pos, getSlotInputSlot0(), getSlotInputSlotN());
        }
    }
}