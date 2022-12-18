package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.TransferPatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
import fr.mamiemru.blocrouter.items.custom.ItemTransferRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TransferPatternEncoderEntity extends BaseEntityPatternEncoder {

    public static final int SLOT_INPUT_TRANSFER_SLOT_0 = 0;
    public static final int SLOT_INPUT_FILTER_SLOT_0 = 1;
    public static final int SLOT_INPUT_FILTER_SLOT_1 = 2;
    public static final int SLOT_INPUT_FILTER_SLOT_2 = 3;
    public static final int SLOT_INPUT_FILTER_SLOT_3 = 4;
    public static final int SLOT_INPUT_FILTER_SLOT_4 = 5;
    public static final int SLOT_INPUT_FILTER_SLOT_5 = 6;
    public static final int SLOT_INPUT_FILTER_SLOT_6 = 7;
    public static final int SLOT_INPUT_FILTER_SLOT_7 = 8;
    public static final int SLOT_INPUT_FILTER_SLOT_8 = 9;
    public static final int SLOT_INPUT_TRASH_SLOT_0 = 10;
    public static final int SLOT_INPUT_TRASH_SLOT_1 = 11;
    public static final int SLOT_INPUT_TRASH_SLOT_2 = 12;
    public static final int SLOT_INPUT_TRASH_SLOT_3 = 13;
    public static final int SLOT_INPUT_TRASH_SLOT_4 = 14;
    public static final int SLOT_INPUT_TRASH_SLOT_5 = 15;
    public static final int SLOT_OUTPUT_TRANSFER_SLOT_0 = 16;
    public static final int SLOT_OUTPUT_TRANSFER_SLOT_1 = 17;
    public static final int SLOT_OUTPUT_TRANSFER_SLOT_2 = 18;
    public static final int SLOT_INPUT_SLOT_PATTERN = 19;
    public static final int VAR_WHITELIST_INDEX = 0;
    public static final int VAR_INSERT_OR_EXTRACT = 1;

    public static final int NUMBER_OF_VARS = 2;
    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 15;
    public static final int NUMBER_OF_SLOTS = 20;

    private int var_insert_or_extract = 0;
    private int var_whitelist_index   = 0;

    protected int containerDataGetter(int index) {
        return switch (index) {
            case VAR_INSERT_OR_EXTRACT -> TransferPatternEncoderEntity.this.var_insert_or_extract;
            case VAR_WHITELIST_INDEX -> TransferPatternEncoderEntity.this.var_whitelist_index;
            default -> 0;
        };
    }
    protected void containerDataSetter(int index, int value) {
        switch (index) {
            case VAR_INSERT_OR_EXTRACT -> TransferPatternEncoderEntity.this.var_insert_or_extract = value;
            case VAR_WHITELIST_INDEX -> TransferPatternEncoderEntity.this.var_whitelist_index = value;
        }
    }
    protected int containerDataSize() {
        return NUMBER_OF_VARS;
    }

    public TransferPatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.TRANSFER_PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TransferPatternEncoderMenu(id, inventory, this, this.data);
    }

    public int getNumberOfSlots() {
        return (this.isInsertion()) ? NUMBER_OF_SLOTS : NUMBER_OF_SLOTS - 2;
    }
    public boolean isInsertion() {
        return getInsertOrExtract() == 0;
    }
    public boolean isExtract() {return getInsertOrExtract() == 1;}
    public int getInsertOrExtract() {
        return var_insert_or_extract;
    }

    public void encodePattern() {

        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {
            ItemStack is;
            ListTag transferInput = new ListTag();
            ListTag transferOutput = new ListTag();
            ListTag ingredients = new ListTag();
            ListTag trash = new ListTag();

            for (int i = SLOT_OUTPUT_TRANSFER_SLOT_0; i <= SLOT_OUTPUT_TRANSFER_SLOT_2; ++i) {
                is = itemStackHandler.getStackInSlot(i);
                if (!is.isEmpty()) {
                    if (is.getItem() instanceof ItemTeleportationSlot) {
                        transferOutput.add(Pattern.encodeCoords(is));

                        if (isInsertion()) {
                            for (int slot = 0; slot < 3; ++slot) {
                                int s = SLOT_INPUT_FILTER_SLOT_0 + (slot*3);
                                ingredients.add(Pattern.encodeIngredient(itemStackHandler.getStackInSlot(s)));
                            }
                        }
                    } else {
                        return;
                    }
                }
            }

            if (transferOutput.isEmpty()) {
                return;
            }

            is = itemStackHandler.getStackInSlot(SLOT_INPUT_TRANSFER_SLOT_0);
            if (!is.isEmpty()) {
                if (is.getItem() instanceof ItemTeleportationSlot) {
                    transferInput.add(Pattern.encodeCoords(is));
                } else {
                    return;
                }
            }

            for (int i = SLOT_INPUT_TRASH_SLOT_0; i <= SLOT_INPUT_TRASH_SLOT_5; ++i) {
                if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                    trash.add(Pattern.encodeIngredient(itemStackHandler.getStackInSlot(i)));
                }
            }

            if (isExtract()) {
                for (int i = SLOT_INPUT_FILTER_SLOT_0; i <= SLOT_INPUT_FILTER_SLOT_8; ++i) {
                    ingredients.add(Pattern.encodeIngredient(itemStackHandler.getStackInSlot(i)));
                }
            }

            itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
            itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_TRANSFER_ROUTING_PATTERN.get(), 1));
            ItemTransferRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN),
                    var_insert_or_extract, var_whitelist_index, transferInput, transferOutput, trash, ingredients
            );
        }
    }

    public void dropToggleInsertion() {
        for (int i = SLOT_OUTPUT_TRANSFER_SLOT_0; i <= SLOT_OUTPUT_TRANSFER_SLOT_2; ++i) {
            ItemStack is = itemStackHandler.getStackInSlot(i);
            if (!is.isEmpty() && !(is.getItem() instanceof ItemTeleportationSlot)) {
                for (int slot = 0; slot < 3; ++slot) {
                    dropSlot(SLOT_INPUT_FILTER_SLOT_0 + (slot*3));
                }
            }
        }
    }

    public void dropToggleExtraction() {
        dropSlot(TransferPatternEncoderEntity.SLOT_OUTPUT_TRANSFER_SLOT_1);
        dropSlot(TransferPatternEncoderEntity.SLOT_OUTPUT_TRANSFER_SLOT_2);
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
        return SLOT_INPUT_TRANSFER_SLOT_0;
    }

    @Override
    public int getItemSlot0() {
        return 0;
    }

    @Override
    public int getItemSlotN() {
        return 0;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, TransferPatternEncoderEntity pEntity) {}
}
