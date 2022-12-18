package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.PatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemNormalRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
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

public class PatternEncoderEntity extends BaseEntityPatternEncoder {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_PATTERN = 6;
    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 6;
    public static final int NUMBER_OF_SLOTS = 7;
    public static final int NUMBER_OF_VARS = 18;

    public PatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PatternEncoderMenu(id, inventory, this, this.data);
    }

    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {

            boolean patternIsEmpty = true;
            ListTag ingredients = new ListTag();
            int[] array = {5,1,2,3,4,6,0,0,0,0,0,0};

            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(ii);
                if (!is.isEmpty()) {
                    patternIsEmpty = false;
                    ingredients.add(Pattern.encodePatternIngredient(array[ii+NUMBER_OF_INGREDIENTS_INPUT_SLOTS], is, array[ii]));
                }

            }

            if (!patternIsEmpty) {
                itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
                itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_NORMAL_ROUTING_PATTERN.get(), 1));
                ItemNormalRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN), ingredients);
            }
        }

    }

    @Override
    public int getNumberOfIngredientsSlots() {
        return NUMBER_OF_INGREDIENTS_INPUT_SLOTS;
    }

    @Override
    public int getSlotPatternSlot() {return SLOT_INPUT_SLOT_PATTERN;}

    @Override
    public int getTeleportationCardSlot0() {return -1;}

    @Override
    public int getItemSlot0() { return SLOT_INPUT_SLOT_0; }

    @Override
    public int getItemSlotN() { return SLOT_INPUT_SLOT_5; }


    public static void tick(Level level, BlockPos pos, BlockState state, PatternEncoderEntity pEntity) {}
}
