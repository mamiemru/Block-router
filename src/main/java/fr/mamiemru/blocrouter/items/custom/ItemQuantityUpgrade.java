package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.util.MouseUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemQuantityUpgrade extends Item {

    private static final String NBT_QUANTITY = "NBT_QUANTITY";
    private static final int DEFAULT_QUANTITY = 32;

    public ItemQuantityUpgrade() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack is = new ItemStack(this);
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(NBT_QUANTITY, DEFAULT_QUANTITY);
        is.setTag(compoundTag);
        return is;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUseHand) {
        ItemStack is = player.getItemInHand(pUseHand);
        if (!is.hasTag()) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt(NBT_QUANTITY, DEFAULT_QUANTITY);
            is.setTag(compoundTag);
        } else {
            int quanta = getQuantity(is);
            int newQuanta;
            CompoundTag compoundTag = is.getTag();
            if (!player.isCrouching()) {
                newQuanta = Math.min(64, quanta+1);
            } else {
                newQuanta = Math.max(1, quanta-1);
            }
            compoundTag.putInt(NBT_QUANTITY, newQuanta);
            Minecraft.getInstance().player.displayClientMessage(Component.literal(newQuanta+""), true);
            is.setTag(compoundTag);
        }
        return super.use(level, player, pUseHand);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        String coordinates = "transfer "+getQuantity(pStack)+" per operation";
        pTooltipComponents.add(Component.literal(coordinates));
        pTooltipComponents.add(Component.literal("Disabled.."));
        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public static int getQuantity(ItemStack is) {
        if (is.hasTag()) {
            CompoundTag c = is.getTag();
            return (c != null) ? c.getInt(NBT_QUANTITY) : DEFAULT_QUANTITY;
        }
        return DEFAULT_QUANTITY;
    }
}
