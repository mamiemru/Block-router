package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.VacuumPatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemVacuumRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.VacuumPattern;
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

public class VacuumPatternEncoderEntity extends BaseEntityPatternEncoder {

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
    public static final int SLOT_INPUT_SLOT_PATTERN = 13;
    public static final int NUMBER_OF_SLOTS = 14;

    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 9;
    public static final int NUMBER_OF_INGREDIENTS_OUTPUT_SLOTS = 4;

    public VacuumPatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.VACUUM_PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Vacuum Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new VacuumPatternEncoderMenu(id, inventory, this, this.data);
    }

    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {

            boolean patternIsEmpty = true;
            ListTag ingredients = new ListTag();
            ListTag result = new ListTag();

            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(ii);
                if (!is.isEmpty()) {
                    patternIsEmpty = false;
                    ingredients.add(Pattern.encodeIngredient(is));
                }
            }

            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_OUTPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(SLOT_OUTPUT_SLOT_0 + ii);
                if (!is.isEmpty()) {
                    result.add(Pattern.encodeIngredient(is));
                }
            }

            if (!patternIsEmpty) {
                itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
                itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_VACUUM_ROUTING_PATTERN.get(), 1));
                ItemVacuumRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN), ingredients, result);
            }
        }

    }

    @Override
    public VacuumPattern decodePattern(ItemStack is) {
        return ItemVacuumRoutingPattern.decodePatternTag(is);
    }

    @Override
    public int getNumberOfIngredientsSlots() {
        return NUMBER_OF_INGREDIENTS_INPUT_SLOTS;
    }

    @Override
    public int getSlotPatternSlot() { return SLOT_INPUT_SLOT_PATTERN; }

    @Override
    public int getTeleportationCardSlot0() { return -1; }

    @Override
    public int getItemSlot0() {return SLOT_INPUT_SLOT_0;}

    @Override
    public int getItemSlotN() {return SLOT_OUTPUT_SLOT_3;}


    public static void tick(Level level, BlockPos pos, BlockState state, VacuumPatternEncoderEntity pEntity) {}
}
