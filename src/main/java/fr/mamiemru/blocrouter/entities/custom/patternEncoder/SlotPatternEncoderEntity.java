package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.SlotPatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemSlotRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.SlotPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotPatternEncoderEntity extends BaseEntityPatternEncoder {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_INPUT_SLOT_PATTERN = 9;
    public static final int SLOT_INPUT_TELEPORTATION_SLOT = 10;
    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 9;
    public static final int NUMBER_OF_SLOTS = 11;

    private int var_slot_0 = 0;
    private int var_slot_1 = 0;
    private int var_slot_2 = 0;
    private int var_slot_3 = 0;
    private int var_slot_4 = 0;
    private int var_slot_5 = 0;
    private int var_slot_6 = 0;
    private int var_slot_7 = 0;
    private int var_slot_8 = 0;

    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof ItemRoutingPattern) {
            return SLOT_INPUT_SLOT_PATTERN == slot;
        } else if (stack.getItem() instanceof ItemTeleportationSlot) {
            return SLOT_INPUT_TELEPORTATION_SLOT == slot;
        }
        return super.checkIsItemValid(slot, stack);
    }
    protected int containerDataGetter(int index) {
        return switch (index) {
            case 0 -> SlotPatternEncoderEntity.this.var_slot_0;
            case 1 -> SlotPatternEncoderEntity.this.var_slot_1;
            case 2 -> SlotPatternEncoderEntity.this.var_slot_2;
            case 3 -> SlotPatternEncoderEntity.this.var_slot_3;
            case 4 -> SlotPatternEncoderEntity.this.var_slot_4;
            case 5 -> SlotPatternEncoderEntity.this.var_slot_5;
            case 6 -> SlotPatternEncoderEntity.this.var_slot_6;
            case 7 -> SlotPatternEncoderEntity.this.var_slot_7;
            case 8 -> SlotPatternEncoderEntity.this.var_slot_8;
            default -> 0;
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case 0 -> SlotPatternEncoderEntity.this.var_slot_0 = value;
            case 1 -> SlotPatternEncoderEntity.this.var_slot_1 = value;
            case 2 -> SlotPatternEncoderEntity.this.var_slot_2 = value;
            case 3 -> SlotPatternEncoderEntity.this.var_slot_3 = value;
            case 4 -> SlotPatternEncoderEntity.this.var_slot_4 = value;
            case 5 -> SlotPatternEncoderEntity.this.var_slot_5 = value;
            case 6 -> SlotPatternEncoderEntity.this.var_slot_6 = value;
            case 7 -> SlotPatternEncoderEntity.this.var_slot_7 = value;
            case 8 -> SlotPatternEncoderEntity.this.var_slot_8 = value;
        }
    }
    protected int containerDataSize() {
        return NUMBER_OF_INGREDIENTS_INPUT_SLOTS;
    }

    public SlotPatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.SLOT_PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Slot Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SlotPatternEncoderMenu(id, inventory, this, this.data);
    }


    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {

            boolean patternIsEmpty = true;
            ListTag ingredients = new ListTag();
            BlockPos target;
            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(ii);
                if (!is.isEmpty()) {
                    patternIsEmpty = false;
                    ingredients.add(Pattern.encodePatternIngredient(data.get(ii), is, 1));
                }
            }

            ItemStack is = ItemTeleportationSlot.getTeleportationCardOrNull(itemStackHandler.getStackInSlot(SLOT_INPUT_TELEPORTATION_SLOT));
            if (is == null) {
                return;
            }
            target = ItemTeleportationSlot.getCoordinates(is);

            if (!patternIsEmpty) {
                itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
                itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_SLOT_ROUTING_PATTERN.get(), 1));
                ItemSlotRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN), ingredients, target);
            }
        }
    }

    @Override
    public SlotPattern decodePattern(ItemStack is) {
        return ItemSlotRoutingPattern.decodePatternTag(is);
    }

    @Override
    public int getNumberOfIngredientsSlots() {
        return NUMBER_OF_INGREDIENTS_INPUT_SLOTS;
    }

    @Override
    public int getSlotPatternSlot() {
        return SLOT_INPUT_SLOT_PATTERN;
    }

    @Override
    public int getTeleportationCardSlot0() {
        return SLOT_INPUT_TELEPORTATION_SLOT;
    }

    @Override
    public int getItemSlot0() {return SLOT_INPUT_SLOT_0;}

    @Override
    public int getItemSlotN() {return SLOT_INPUT_SLOT_8; }

    public int getTotalSlotsFromTargetedEntity() {
        ItemStack is = ItemTeleportationSlot.getTeleportationCardOrNull(this.itemStackHandler.getStackInSlot(SLOT_INPUT_TELEPORTATION_SLOT));
        if (is != null) {
            BlockPos bp = ItemTeleportationSlot.getCoordinates(is);
            IItemHandler itemHandler = getItemHandler(getLevel(), bp, Direction.UP);
            if (itemHandler != null) {
                return itemHandler.getSlots();
            }
        }
        return 0;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, SlotPatternEncoderEntity pEntity) {}
}
