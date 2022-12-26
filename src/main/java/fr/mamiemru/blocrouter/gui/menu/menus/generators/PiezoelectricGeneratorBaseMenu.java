package fr.mamiemru.blocrouter.gui.menu.menus.generators;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.blocks.custom.generators.PiezoelectricHeavyPiston;
import fr.mamiemru.blocrouter.entities.BaseEntityEnergy;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricGeneratorBaseEntity;
import fr.mamiemru.blocrouter.entities.custom.generators.PiezoelectricHeavyPistonEntity;
import fr.mamiemru.blocrouter.gui.menu.BaseContainerMenuEnergy;
import fr.mamiemru.blocrouter.gui.menu.MenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class PiezoelectricGeneratorBaseMenu extends BaseContainerMenuEnergy {

    public PiezoelectricGeneratorBaseMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(PiezoelectricGeneratorBaseEntity.NUMBER_OF_VARS));
    }

    public PiezoelectricGeneratorBaseMenu(int id, Inventory inv, BaseEntityEnergy entity, ContainerData data) {
        super(id, inv, MenuTypes.PIEZOELECTRIC_GENERATOR_BASE_MENU.get(), data, entity);
        checkContainerSize(inv, PiezoelectricGeneratorBaseEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, PiezoelectricGeneratorBaseEntity.SLOT_FUEL, 134, 64));
        });

        addDataSlots(data);
    }

    @Override
    public PiezoelectricGeneratorBaseEntity getEntity() {
        return (PiezoelectricGeneratorBaseEntity) super.getEntity();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_PIEZOELECTRIC_GENERATOR_BASE.get());
    }

    public List<MutableComponent> getProcessInformation() {
        List<MutableComponent> texts = new ArrayList<>();
        PiezoelectricGeneratorBaseEntity entity = getEntity();

        int tubeDepth = entity.getTubeDepth();
        int cost = entity.getRequiredEnergyToOperate(tubeDepth);
        boolean isHeavyPiston = false;

        int pistonStrength = 0;
        if (level.getBlockEntity(getEntity().getBlockPos().above(tubeDepth)) instanceof PiezoelectricHeavyPistonEntity pistonEntity) {
            pistonStrength = pistonEntity.getPistonStrength();
            texts.add(Component.literal("Piston Strength: " + pistonStrength));
            isHeavyPiston = true;
        }

        BlockPos pistonPos = getEntity().getBlockPos().above(tubeDepth);
        if (level.getBlockState(pistonPos).getBlock() instanceof PiezoelectricHeavyPiston pblock) {
            texts.add(Component.literal("N° Shards: " + pblock.getNumberOfShards(level, pistonPos, tubeDepth-1)));
        }

        texts.add(Component.literal("Tube Depth: " + (tubeDepth-1)));
        if (isHeavyPiston) {
            texts.add(Component.literal("Energy Cost: " + cost + " FE"));
        }
        texts.add(Component.literal("Fuel value: " + entity.getFuelValue()));

        if (pistonStrength <= 0 || tubeDepth <= 0 || (isHeavyPiston && !entity.operateCompression(tubeDepth, cost, true)))  {
            texts.add(Component.literal("Can't operate"));
        } else {
            texts.add(Component.literal("Can operate"));
        }

        return texts;
    }
}

