package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.itemHandler.CallbackItemHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFilter extends Item implements MenuProvider {

    public static final String NBT_FILTER_INVENTORY = "ItemFilter.inventory";
    public static final int NUMBER_OF_ITEMS = 9;

    public ItemFilter() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));
    }

    public static void setItemAt(ItemStack is, int index, ItemStack itemStack) {
        ItemStackHandler callbackItemHandler = loadNbt(is);
        callbackItemHandler.setStackInSlot(index, itemStack);
        saveNbt(is, callbackItemHandler);
    }

    public static void removeItemAt(ItemStack is, int index) {
        ItemStackHandler callbackItemHandler = loadNbt(is);
        callbackItemHandler.setStackInSlot(index, ItemStack.EMPTY);
        saveNbt(is, callbackItemHandler);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUseHand) {
        ItemStack is = player.getItemInHand(pUseHand);

        if (!level.isClientSide()) {
            NetworkHooks.openScreen((ServerPlayer) player, this);
        }
        return InteractionResultHolder.sidedSuccess(is, level.isClientSide());
    }

    public static ItemStackHandler emptyInventory(ItemStack is) {
        return new ItemStackHandler(NUMBER_OF_ITEMS);
    }

    public static ItemStackHandler loadNbt(ItemStack is) {
        ItemStackHandler itemStackHandler = emptyInventory(is);
        if (is.hasTag()) {
            CompoundTag nbt = is.getTag();
            itemStackHandler.deserializeNBT(nbt.getCompound(NBT_FILTER_INVENTORY));
        }
        return itemStackHandler;
    }

    public static void saveNbt(ItemStack itemStack, ItemStackHandler itemStackHandler) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(NBT_FILTER_INVENTORY, itemStackHandler.serializeNBT());
        itemStack.setTag(compoundTag);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.hasTag() && !pStack.getTag().getCompound(NBT_FILTER_INVENTORY).isEmpty();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            ItemStackHandler callbackItemHandler = loadNbt(pStack);
            if (callbackItemHandler.getSlots() > 0) {
                pTooltipComponents.add(Component.literal("Filter on:"));
                for (int slotIndex = 0; slotIndex < callbackItemHandler.getSlots(); ++slotIndex) {
                    ItemStack is = callbackItemHandler.getStackInSlot(slotIndex);
                    if (is != null && !is.isEmpty()) {
                        pTooltipComponents.add(Component.literal(is.getItem() + " x " + is.getCount()));
                    }
                }
            }
        }
        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Filter");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inventory, Player pPlayer) {
        return new ItemFilterMenu(pContainerId, inventory, pPlayer);
    }

    public static boolean isItemOnFilter(ItemStack filter, ItemStack is) {
        if (filter.getItem() instanceof ItemFilter) {
            ItemStackHandler itemHandler = loadNbt(filter);
            for (int slotIndex = 0; slotIndex < itemHandler.getSlots(); ++slotIndex) {
                ItemStack test = itemHandler.getStackInSlot(slotIndex);
                if (test != null && test.getItem() == is.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        System.out.println("===== useOn =====");
        System.out.println(pContext);
        return super.useOn(pContext);
    }
}
