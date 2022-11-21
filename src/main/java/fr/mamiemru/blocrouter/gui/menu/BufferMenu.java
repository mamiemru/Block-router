package fr.mamiemru.blocrouter.gui.menu;

import fr.mamiemru.blocrouter.blocks.BlocksRegistry;
import fr.mamiemru.blocrouter.entities.custom.BaseEntityWithMenuProvider;
import fr.mamiemru.blocrouter.entities.custom.BufferEntity;
import fr.mamiemru.blocrouter.gui.MenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class BufferMenu extends AbstractAbstractContainerMenu {

    public BufferMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, getCasterEntity(inv, extraData), new SimpleContainerData(BufferEntity.NUMBER_OF_VARS));
    }

    public BufferMenu(int id, Inventory inv, BaseEntityWithMenuProvider entity, ContainerData data) {
        super(id, inv, MenuTypes.BUFFER_MENU.get(), data, entity);
        checkContainerSize(inv, BufferEntity.NUMBER_OF_SLOTS);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler,BufferEntity.SLOT_STORAGE_INPUT, 14  , 18));
            this.addSlot(new SlotItemHandler(handler,BufferEntity.SLOT_STORAGE_OUTPUT, 14  , 51));
        });

        addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, BlocksRegistry.BLOCK_ITEM_BUFFER.get());
    }

    public int getItemCount() {
        return data.get(BufferEntity.VAR_ITEM_COUNT);
    }

    public int getItemMaxCount() {
        return data.get(BufferEntity.VAR_ITEM_MAX_COUNT);
    }

    public String getBookedItem() {
        return ((BufferEntity)this.blockEntity).getRenderStack().getDescriptionId();
    }
}

