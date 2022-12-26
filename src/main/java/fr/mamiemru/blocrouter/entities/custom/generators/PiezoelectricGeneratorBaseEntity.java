package fr.mamiemru.blocrouter.entities.custom.generators;

import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricHeavyPiston;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricPiston;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricTube;
import fr.mamiemru.blocrouter.config.BlockRouterConfig;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.EntitiesRegistry;
import fr.mamiemru.blocrouter.gui.menu.menus.generators.PiezoelectricGeneratorBaseMenu;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PiezoelectricGeneratorBaseEntity extends BaseEntityEnergy {

    public static final int SLOT_FUEL = 0;
    public static final int NUMBER_OF_SLOTS = 1;
    public static final int NUMBER_OF_VARS = 2;

    public static final Map<Item, Integer> FUEL_VALUES = new HashMap<>();

    static {
        List<? extends String> stringMap = BlockRouterConfig.PIEZOELECTRIC_GENERATOR_FUEL.get();
        stringMap.forEach(s -> {
            String[] split = s.split(":");
            if (split.length == 3 && split[1] instanceof String && NumberUtils.isDigits(split[2])) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(split[1]));
                if (item != null) {
                    FUEL_VALUES.put(item, NumberUtils.toInt(split[2]));
                } else System.out.println("BlockRouter Piezoelectric fuel, unknown item: " + split[1]);
            } else System.out.println("BlockRouter Piezoelectric fuel, malformation of string: " + String.join(":", split));
        });

        FUEL_VALUES.put(Items.COAL, 8);
        FUEL_VALUES.put(Items.COAL_BLOCK, 72);
        FUEL_VALUES.put(ItemsRegistry.ITEM_PIEZOELECTRIC_FUEL.get(), 91);
    }

    @Override
    protected boolean checkIsItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == SLOT_FUEL) {
            return FUEL_VALUES.containsKey(stack.getItem());
        }
        return super.checkIsItemValid(slot, stack);
    }

    public PiezoelectricGeneratorBaseEntity(BlockPos pos, BlockState state) {
        super(EntitiesRegistry.PIEZOELECTRIC_GENERATOR_ENTITY.get(), pos, state,
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_ENERGY_CAPACITY.get(),
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_ENERGY_MAX_TRANSFER.get(),
            BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_COST_PER_OPERATION.get()
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Piezoelectric Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PiezoelectricGeneratorBaseMenu(id, inventory, this, this.data);
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
        return SLOT_FUEL;
    }

    @Override
    protected int getSlotInputSlotN() {
        return SLOT_FUEL;
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

    }

    @Override
    public int getNumberOfSlots() {
        return NUMBER_OF_SLOTS;
    }

    @Override
    protected int processMaxTickWithUpgrade() { return 0; }

    public static void tick(Level level, BlockPos pos, BlockState state, PiezoelectricGeneratorBaseEntity pEntity) {}

    public int getRequiredEnergyToOperate(int tubeDepth) {
        BlockPos pistonPos = getBlockPos().above(tubeDepth);
        if (level.getBlockState(pistonPos).getBlock() instanceof PiezoelectricPiston piezoelectricPiston) {
            int CompressionPower = piezoelectricPiston.getCompressionPower(level, pistonPos);
            return CompressionPower * tubeDepth * BlockRouterConfig.PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_COST_PER_OPERATION.get();
        }
        return 0;
    }

    public int getTubeDepth() {
        int tubeDepth = 1;
        BlockPos tubesPos = getBlockPos().above();
        while (tubeDepth < PiezoelectricHeavyPiston.MAX_TUBE_DEPTH && level.getBlockState(tubesPos).getBlock() instanceof PiezoelectricTube) {
            tubesPos = tubesPos.above();
            ++tubeDepth;
        }
        BlockPos pistonPos = tubesPos;
        if (level.getBlockState(pistonPos).getBlock() instanceof PiezoelectricPiston) {
            return tubeDepth;
        }
        return 0;
    }

    public boolean operateCompression(int tubeDepth, int cost, boolean simulate) {
        BlockPos pistonPos = getBlockPos().above(tubeDepth);
        if (level.getBlockEntity(pistonPos) instanceof PiezoelectricHeavyPistonEntity entity) {
            return entity.operateCompression(cost, simulate);
        }
        return false;
    }

    public boolean canOperate() {
        int tubeDepth = getTubeDepth();
        int cost = getRequiredEnergyToOperate(tubeDepth);
        return operateCompression(tubeDepth, cost, true) ? operateCompression(tubeDepth, cost, false) : false;
    }

    public int getFuelValue() {
        int fuelValue = 0;
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_FUEL);

        if (is != null && !is.isEmpty()) {
            fuelValue = FUEL_VALUES.getOrDefault(is.getItem(), 1);
        }
        return fuelValue;
    }

    private boolean consumeFuel() {
        ItemStack is = itemStackHandler.getStackInSlot(SLOT_FUEL);

        if (is != null && !is.isEmpty()) {
            is.setCount(is.getCount() - 1);
            return true;
        }
        return false;
    }

    public boolean generateEnergy(int compressionPower, int energyPerShard, int tubeDepth, int numberOfShards) {

        if (isEnabled() && !getLevel().isClientSide() && numberOfShards > 0) {

            if (tubeDepth > 0 && (!canOperate() || compressionPower <= 0)) {
                return false;
            }

            int fuelValue = getFuelValue() + 1;
            int generatedEnergy = compressionPower * numberOfShards * fuelValue * energyPerShard;
            System.out.println("p: "+compressionPower + " s: "+numberOfShards+" f: "+fuelValue+ " e:"+ energyPerShard +" -> "+generatedEnergy);
            consumeFuel();
            int gainEnergy = Math.min(ENERGY_STORAGE.getMaxEnergyStored(), ENERGY_STORAGE.receiveEnergy(generatedEnergy, true));
            ENERGY_STORAGE.receiveEnergy(gainEnergy, false);
            return true;
        }
        return false;
    }
}
