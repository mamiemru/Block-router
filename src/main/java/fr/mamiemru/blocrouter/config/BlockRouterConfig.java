package fr.mamiemru.blocrouter.config;

import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;


public final class BlockRouterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Integer> ALL_MACHINES_IDLE_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> ALL_MACHINES_PROCESSING_UPGRADE_LOW;
    public static final ForgeConfigSpec.ConfigValue<Integer> ALL_MACHINES_PROCESSING_UPGRADE_MEDIUM;
    public static final ForgeConfigSpec.ConfigValue<Integer> ALL_MACHINES_PROCESSING_UPGRADE_HIGH;
    public static final ForgeConfigSpec.ConfigValue<Integer> ALL_MACHINES_PROCESSING_UPGRADE_EXTRA;
    public static final ForgeConfigSpec.ConfigValue<Integer> ROUTER_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> ROUTER_ENERGY_MAX_TRANSFER;
    public static final ForgeConfigSpec.ConfigValue<Integer> ROUTER_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> OTHER_MACHINES_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> OTHER_MACHINES_ENERGY_MAX_TRANSFER;
    public static final ForgeConfigSpec.ConfigValue<Integer> OTHER_MACHINES_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> SCATTER_RETRIEVER_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> SCATTER_RETRIEVER_ENERGY_MAX_TRANSFER;
    public static final ForgeConfigSpec.ConfigValue<Integer> SCATTER_RETRIEVER_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENERGY_SCATTER_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENERGY_SCATTER_ENERGY_MAX_TRANSFER;
    public static final ForgeConfigSpec.ConfigValue<Integer> ENERGY_SCATTER_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_WEAK_PISTON_BREAK_CRYSTAL_RANDOM_BOUND;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_WEAK_PISTON_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_WEAK_PISTON_ENERGY_GENERATED_PER_SHARD;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_BREAK_CRYSTAL_RANDOM_BOUND;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_GENERATED_PER_SHARD;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_COST_PER_OPERATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_MAX_TUBE_DEPTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_ENERGY_CAPACITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_ENERGY_MAX_TRANSFER;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_IRON_PISTON_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_DIAMOND_PISTON_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_NETHERITE_PISTON_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<Integer> PIEZOELECTRIC_GENERATOR_DIAMONDIUM_PISTON_STRENGTH;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PIEZOELECTRIC_GENERATOR_FUEL;

    static {

        BUILDER.push("All Machines");
        ALL_MACHINES_IDLE_TIME = BUILDER.comment("Number of tick between operations (without upgrades)").define("processMaxTick", 96);
        ALL_MACHINES_PROCESSING_UPGRADE_LOW = BUILDER.comment("will perform Math.floordiv(processMaxTick, processingUpgradeLow)").define("processingUpgradeLow", 2);
        ALL_MACHINES_PROCESSING_UPGRADE_MEDIUM = BUILDER.comment("will perform Math.floordiv(processMaxTick, processingUpgradeMedium)").define("processingUpgradeMedium", 3);
        ALL_MACHINES_PROCESSING_UPGRADE_HIGH = BUILDER.comment("will perform Math.floordiv(processMaxTick, processingUpgradeHigh)").define("processingUpgradeHigh", 8);
        ALL_MACHINES_PROCESSING_UPGRADE_EXTRA = BUILDER.comment("will perform Math.floordiv(processMaxTick, processingUpgradeExtra)").define("processingUpgradeExtra", 96);
        BUILDER.pop();

        BUILDER.push("Routers");
        ROUTER_ENERGY_CAPACITY = BUILDER.comment("How much energy every router will store").define("EnergyCapacity", 64000);
        ROUTER_ENERGY_MAX_TRANSFER = BUILDER.comment("Max transfer in input").define("EnergyTransferIO", 1024);
        ROUTER_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 16);
        BUILDER.pop();

        BUILDER.push("Scatters and retrievers");
        SCATTER_RETRIEVER_ENERGY_CAPACITY = BUILDER.comment("How much energy Scatters and retrievers will store").define("EnergyCapacity", 32000);
        SCATTER_RETRIEVER_ENERGY_MAX_TRANSFER = BUILDER.comment("Max transfer in input").define("EnergyTransferIO", 1024);
        SCATTER_RETRIEVER_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 8);
        BUILDER.pop();

        BUILDER.push("Energy scatter");
        ENERGY_SCATTER_ENERGY_CAPACITY = BUILDER.comment("How much energy scatters will store").define("EnergyCapacity", 2048000);
        ENERGY_SCATTER_ENERGY_MAX_TRANSFER = BUILDER.comment("Max transfer in input").define("EnergyTransferIO", 18432);
        ENERGY_SCATTER_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 8);
        BUILDER.pop();

        BUILDER.push("Other machines");
        OTHER_MACHINES_ENERGY_CAPACITY = BUILDER.comment("How much energy machines will store").define("EnergyCapacity", 32000);
        OTHER_MACHINES_ENERGY_MAX_TRANSFER = BUILDER.comment("Max transfer in input").define("EnergyTransferIO", 1024);
        OTHER_MACHINES_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 8);
        BUILDER.pop();

        BUILDER.push("Piezoelectric generator with weak pistons");
        PIEZOELECTRIC_GENERATOR_WEAK_PISTON_BREAK_CRYSTAL_RANDOM_BOUND = BUILDER.comment("change to break one crystal per operation, 0=every time, 1=half, 10=1/10....").define("randIntBound", 1);
        PIEZOELECTRIC_GENERATOR_WEAK_PISTON_ENERGY_GENERATED_PER_SHARD = BUILDER.comment("Energy generated by only one shard during compression").define("BaseEnergyGeneratedPerSHard", 256);
        PIEZOELECTRIC_GENERATOR_WEAK_PISTON_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 2048);
        BUILDER.pop();

        BUILDER.push("Piezoelectric generator with heavy pistons");
        PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_BREAK_CRYSTAL_RANDOM_BOUND = BUILDER.comment("change to break one crystal per operation, 0=every time, 1=half, 10=1/10....").define("randIntBound", 5);
        PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_GENERATED_PER_SHARD = BUILDER.comment("Energy generated by only one shard during compression").define("BaseEnergyGeneratedPerSHard", 2000);
        PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_ENERGY_COST_PER_OPERATION = BUILDER.comment("Cost of one operation, can be a check, move...").define("EnergyCostPerOperation", 2048);
        PIEZOELECTRIC_GENERATOR_HEAVY_PISTON_MAX_TUBE_DEPTH = BUILDER.comment("How many tubes can be stacks").define("MaxTubeDepth", 8);
        PIEZOELECTRIC_GENERATOR_IRON_PISTON_STRENGTH = BUILDER.comment("efficiency of compression using iron piston").define("IronPistonStrength", 2);
        PIEZOELECTRIC_GENERATOR_DIAMOND_PISTON_STRENGTH = BUILDER.comment("efficiency of compression using diamond piston").define("DiamondPistonStrength", 4);
        PIEZOELECTRIC_GENERATOR_NETHERITE_PISTON_STRENGTH = BUILDER.comment("efficiency of compression using netherite piston").define("NetheritePistonStrength", 8);
        PIEZOELECTRIC_GENERATOR_DIAMONDIUM_PISTON_STRENGTH = BUILDER.comment("efficiency of compression using diamondium piston").define("DiamondiumPistonStrength", 16);

        BUILDER.pop();

        BUILDER.push("Piezoelectric generator base");
        PIEZOELECTRIC_GENERATOR_ENERGY_CAPACITY = BUILDER.comment("How much energy every router will store").define("EnergyCapacity", 12400000);
        PIEZOELECTRIC_GENERATOR_ENERGY_MAX_TRANSFER = BUILDER.comment("Max transfer in output").define("EnergyTransferIO", 1000000);
        BUILDER.pop();

        BUILDER.push("Piezoelectric generator fuel");
        PIEZOELECTRIC_GENERATOR_FUEL = BUILDER.comment("fuel with descriptionId and fuel value").defineList("AllowedFuelsWithValues",
                List.of(
                        "I:"+ Items.COAL.getDescriptionId()+":9",
                        "I:"+ Items.COAL_BLOCK.getDescriptionId()+":91",
                        "I:"+ Items.BLAZE_ROD.getDescriptionId()+":10",
                        "I:"+ Items.GUNPOWDER.getDescriptionId()+":5",
                        "I:"+ Items.FIRE_CHARGE.getDescriptionId()+":25",
                        "I:block_router.item_piezoelectric_fuel:256"
                ),
                o -> true
        );
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
