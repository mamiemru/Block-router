package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.EnderPatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemEnderRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
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

public class EnderPatternEncoderEntity extends BaseEntityPatternEncoder {

    public static final int SLOT_INPUT_SLOT_0 = 0;
    public static final int SLOT_INPUT_SLOT_1 = 1;
    public static final int SLOT_INPUT_SLOT_2 = 2;
    public static final int SLOT_INPUT_SLOT_3 = 3;
    public static final int SLOT_INPUT_SLOT_4 = 4;
    public static final int SLOT_INPUT_SLOT_5 = 5;
    public static final int SLOT_INPUT_SLOT_6 = 6;
    public static final int SLOT_INPUT_SLOT_7 = 7;
    public static final int SLOT_INPUT_SLOT_8 = 8;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_0 = 9;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_1 = 10;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_2 = 11;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_3 = 12;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_4 = 13;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_5 = 14;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_6 = 15;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_7 = 16;
    public static final int SLOT_INPUT_TELEPORTATION_CARD_SLOT_8 = 17;
    public static final int SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_0 = 18;
    public static final int SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_1 = 19;
    public static final int SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_2 = 20;
    public static final int SLOT_INPUT_SLOT_PATTERN = 21;

    public static final int NUMBER_OF_SLOTS = 22;
    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 9;
    public static final int NUMBER_OF_TELEPORTATION_CARD_OUTPUT_SLOTS = 3;

    public EnderPatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.ENDER_PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Ender Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new EnderPatternEncoderMenu(id, inventory, this, this.data);
    }

    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {

            boolean patternIsEmpty = true;
            ListTag outputs = new ListTag();
            ListTag ingredients = new ListTag();

            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(ii);
                ItemStack bp = ItemTeleportationSlot.getTeleportationCardOrNull(itemStackHandler.getStackInSlot(SLOT_INPUT_TELEPORTATION_CARD_SLOT_0+ii));
                if (!is.isEmpty()) {
                    if (bp != null) {
                        patternIsEmpty = false;
                        ingredients.add(Pattern.encodePatternIngredient(0, is, 0, ItemTeleportationSlot.getCoordinates(bp)));
                    } else {
                        return;
                    }
                }
            }

            for (int ii = 0; ii < 3; ++ii) {
                ItemStack bp = ItemTeleportationSlot.getTeleportationCardOrNull(itemStackHandler.getStackInSlot(SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_0+ii));
                if (bp != null) {
                    outputs.add(Pattern.encodeCoords(bp));
                }
            }

            if (!patternIsEmpty) {
                itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
                itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_ENDER_ROUTING_PATTERN.get(), 1));
                ItemEnderRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN), ingredients, outputs);
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
    public int getTeleportationCardSlot0() {return SLOT_INPUT_TELEPORTATION_CARD_SLOT_0;}

    @Override
    protected int getTeleportationCardSlotN() {return SLOT_OUTPUT_TELEPORTATION_CARD_SLOT_2;}

    @Override
    public int getItemSlot0() { return SLOT_INPUT_SLOT_0; }

    @Override
    public int getItemSlotN() { return SLOT_INPUT_SLOT_8; }


    public static void tick(Level level, BlockPos pos, BlockState state, EnderPatternEncoderEntity pEntity) {}
}
