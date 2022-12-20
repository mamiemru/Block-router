package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.gui.menu.itemHandler.CallbackItemHandler;
import fr.mamiemru.blocrouter.gui.menu.menus.ItemFilterMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemFilter extends Item implements MenuProvider {

    public static final String NBT_FILTER_INVENTORY = "ItemFilter.inventory";

    public ItemFilter() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));
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

    public static CallbackItemHandler emptyInventory(ItemStack is) {
        return new CallbackItemHandler(9, is);
    }

    public static CallbackItemHandler loadNbt(ItemStack is) {
        CallbackItemHandler itemStackHandler = emptyInventory(is);
        if (is.hasTag()) {
            CompoundTag nbt = is.getTag();
            itemStackHandler.deserializeNBT(nbt.getCompound(NBT_FILTER_INVENTORY));
        }
        return itemStackHandler;
    }

    public static void saveNbt(ItemStack itemStack, CallbackItemHandler itemStackHandler) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(NBT_FILTER_INVENTORY, itemStackHandler.serializeNBT());
        itemStack.setTag(compoundTag);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.hasTag();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
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

}
