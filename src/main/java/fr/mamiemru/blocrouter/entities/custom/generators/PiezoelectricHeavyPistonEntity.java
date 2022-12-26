package fr.mamiemru.blocrouter.entities.custom.generators;

import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricHeavyPiston;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricTube;
import fr.mamiemru.blocrouter.blocks.custom.statesProperties.PistonTypes;
import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.generators.PiezoelectricHeavyPistonMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.items.custom.ItemPiezoelectricPiston;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PiezoelectricHeavyPistonEntity extends BaseEntityEnergy {

    public static final int SLOT_PISTON = 0;
    public static final int NUMBER_OF_SLOTS = 1;
    public static final int NUMBER_OF_VARS = 2;


    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == SLOT_PISTON) {
            return stack.getItem() instanceof ItemPiezoelectricPiston;
        }
        return super.checkIsItemValid(slot, stack);
    }

    public PiezoelectricHeavyPistonEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.PIEZOELECTRIC_HEAVY_PISTON_ENTITY.get(), pos, state,
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_ENERGY_CAPACITY.get(),
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_ENERGY_MAX_TRANSFER.get(),
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_COST_PER_OPERATION.get()
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Heavy Piezoelectric Piston");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PiezoelectricHeavyPistonMenu(id, inventory, this, this.data);
    }

    @Override
    protected int getSlotUpgrade() {
        return -1;
    }

    @Override
    protected void handleExtraction() {}

    @Override
    protected void handleProcessing() {}

    @Override
    protected int getSlotInputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotInputSlotN() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlot0() {
        return -1;
    }

    @Override
    protected int getSlotOutputSlotN() {
        return -1;
    }

    @Override
    protected void onSlotContentChanged(int slot) {
        if (slot != SLOT_PISTON) {
            return;
        }

        ItemStack is = itemStackHandler.getStackInSlot(SLOT_PISTON);
        if (is == null) {
            return;
        }

        Item item = is.getItem();
        PistonTypes pistonType = PistonTypes.EMPTY;
        if (ItemsRegistry.ITEM_PIEZOELECTRIC_IRON_PISTON.get().equals(item)) {
            pistonType = PistonTypes.IRON;
        } else if (ItemsRegistry.ITEM_PIEZOELECTRIC_DIAMOND_PISTON.get().equals(item)) {
            pistonType = PistonTypes.DIAMOND;
        } else if (ItemsRegistry.ITEM_PIEZOELECTRIC_NETHERITE_PISTON.get().equals(item)) {
            pistonType = PistonTypes.NETHERITE;
        } else if (ItemsRegistry.ITEM_PIEZOELECTRIC_DIAMONDIUM_PISTON.get().equals(item)) {
            pistonType = PistonTypes.DIAMONDIUM;
        }

        level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(PiezoelectricHeavyPiston.PISTON_TYPE, pistonType), 1 | 2 | 8);
    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    protected int processMaxTickWithUpgrade() { return 0; }

    public static void tick(Level level, BlockPos pos, BlockState state, PiezoelectricHeavyPistonEntity pEntity) {}

    public int getPistonStrength() {
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_PISTON);
        if (is == null || is.isEmpty()) {
            return 0;
        }

        Item item = is.getItem();
        return (item instanceof ItemPiezoelectricPiston) ? ((ItemPiezoelectricPiston) item).getEfficiency() : 0;
    }

    public boolean operateCompression(int cost, boolean simulate) {
        return ENERGY_STORAGE.extractEnergy(cost, simulate) == cost;
    }
}
