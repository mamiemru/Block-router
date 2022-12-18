package fr.mamiemru.blocrouter.entities.custom.patternEncoder;

import fr.mamiemru.blocrouter.entities.BaseEntityPatternEncoder;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.patternEncoder.DispatcherPatternEncoderMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemDispatcherRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.items.custom.ItemTeleportationSlot;
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

public class DispatcherPatternEncoderEntity extends BaseEntityPatternEncoder {

    public enum VectorType {
        HORIZONTAL,
        VERTICAL,
        DEPTH;

        public static VectorType fromIndex(int index) {
            return switch (index) {
                case 1 -> VERTICAL;
                case 2 -> DEPTH;
                default -> HORIZONTAL;
            };
        }

        public static int toIndex(VectorType vectorType) {
            return switch (vectorType) {
                case HORIZONTAL -> 0;
                case VERTICAL -> 1;
                case DEPTH -> 2;
            };
        }
    }

    public static final int SLOT_INPUT_SLOT_PATTERN = 0;
    public static final int SLOT_INPUT_TELEPORTATION_CARD = 1;
    public static final int SLOT_INPUT_MIN = 2;
    public static final int SLOT_INPUT_MAX = 83;
    public static final int NUMBER_OF_INGREDIENTS_INPUT_SLOTS = 81;
    public static final int NUMBER_OF_SLOTS = 84;

    private VectorType vectorType = VectorType.HORIZONTAL;

    protected int containerDataGetter(int index) {return VectorType.toIndex(DispatcherPatternEncoderEntity.this.vectorType);}
    protected void containerDataSetter(int index, int value) {DispatcherPatternEncoderEntity.this.vectorType = VectorType.fromIndex(value);}
    protected int containerDataSize() {
        return 1;
    }

    public DispatcherPatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.DISPATCHER_PATTERN_ENCODER_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Dispatcher Pattern Encoder");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {return new DispatcherPatternEncoderMenu(id, inventory, this, this.data);}

    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && selectedIS.getCount() == 1) {
            boolean patternIsEmpty = true;

            int vectorType = data.get(0);
            BlockPos referenceBlockPos = getTeleportationCardBlockPos();
            ListTag ingredients = new ListTag();

            for (int slotIndexX = 0; slotIndexX < 9; ++slotIndexX) {
                for (int slotIndexY = 0; slotIndexY < 9; ++slotIndexY) {
                    ItemStack is = itemStackHandler.getStackInSlot(SLOT_INPUT_MIN+(slotIndexX*9)+slotIndexY);
                    if (is != null && !is.isEmpty()) {
                        ingredients.add(ItemDispatcherRoutingPattern.encodeSlotAndCoordinates(this, slotIndexX, slotIndexY, is));
                        patternIsEmpty = false;
                    }
                }
            }

            if (!patternIsEmpty) {
                itemStackHandler.extractItem(SLOT_INPUT_SLOT_PATTERN, 1, false);
                itemStackHandler.setStackInSlot(SLOT_INPUT_SLOT_PATTERN, new ItemStack(ItemsRegistry.ITEM_DISPATCHER_ROUTING_PATTERN.get(), 1));
                ItemDispatcherRoutingPattern.encodePatternTag(itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN), ingredients, vectorType, referenceBlockPos);
            }
        }
    }

    public boolean hasTeleportationCardSlot() {
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_INPUT_TELEPORTATION_CARD);
        return !is.isEmpty() && is.hasFoil();
    }

    public BlockPos getTeleportationCardBlockPos() {return ItemTeleportationSlot.getCoordinates(itemStackHandler.getStackInSlot(SLOT_INPUT_TELEPORTATION_CARD));}

    public int getVectorType() {
        return data.get(0);
    }

    @Override
    public int getNumberOfIngredientsSlots() {
        return NUMBER_OF_INGREDIENTS_INPUT_SLOTS;
    }

    @Override
    public int getSlotPatternSlot() {return SLOT_INPUT_SLOT_PATTERN;}

    @Override
    public int getTeleportationCardSlot0() {return SLOT_INPUT_TELEPORTATION_CARD;}

    @Override
    public int getItemSlot0() { return SLOT_INPUT_MIN; }

    @Override
    public int getItemSlotN() { return SLOT_INPUT_MAX; }

    public static void tick(Level level, BlockPos pos, BlockState state, DispatcherPatternEncoderEntity pEntity) {}
}
