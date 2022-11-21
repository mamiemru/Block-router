package fr.mamiemru.blocrouter.entities.custom;

import fr.mamiemru.blocrouter.entities.Container.ContainerListData;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.PatternEncoderMenu;
import fr.mamiemru.blocrouter.items.custom.ItemRoutingPattern;
import fr.mamiemru.blocrouter.util.Pattern;
import fr.mamiemru.blocrouter.util.PatternUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.StyleSheet;

public class PatternEncoderEntity extends BaseEntityWithMenuProvider {

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

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected  final ContainerData data;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(NUMBER_OF_SLOTS) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            if (stack.getItem() instanceof ItemRoutingPattern) {
                return SLOT_INPUT_SLOT_PATTERN == slot;
            }

            return super.isItemValid(slot, stack);
        }
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public PatternEncoderEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.PATTERN_ENCODER_ENTITY.get(), pos, state);

        int[] array = {5,1,2,3,4,6,0,0,0,0,0,0};
        this.data = new ContainerListData(array);

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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PatternEncoderEntity pEntity) {
    }

    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    public void encodePattern() {
        ItemStack selectedIS = itemStackHandler.getStackInSlot(SLOT_INPUT_SLOT_PATTERN);
        if (selectedIS.getItem() instanceof ItemRoutingPattern && !selectedIS.isEmpty()) {

            boolean patternIsEmpty = true;
            ListTag ingredients = new ListTag();
            for (int ii = 0; ii < NUMBER_OF_INGREDIENTS_INPUT_SLOTS; ++ii) {
                ItemStack is = itemStackHandler.getStackInSlot(ii);
                if (!is.isEmpty()) {
                    patternIsEmpty = false;
                    ingredients.add(PatternUtil.encodePatternIngredient(data.get(ii+NUMBER_OF_INGREDIENTS_INPUT_SLOTS), is, data.get(ii)));
                }

            }

            if (!patternIsEmpty) {
                ItemRoutingPattern.encodePatternTag(selectedIS, ingredients);
            }
        }

    }
}
