package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.util.Pattern;
import fr.mamiemru.blocrouter.util.PatternUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemTeleportationSlot extends Item{

    public static final String NBT_COORDINATE_X = ".coordinates_x";
    public static final String NBT_COORDINATE_Y = ".coordinates_y";
    public static final String NBT_COORDINATE_Z = ".coordinates_Z";

    public ItemTeleportationSlot() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));

    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUseHand) {
        ItemStack is = player.getItemInHand(pUseHand);
        if (is.hasTag()) {
            if (player.isCrouching()) {
                is.setTag(new CompoundTag());
            }
        } else {
            Entity entity = Minecraft.getInstance().getCameraEntity();
            HitResult block = entity.pick(20.0D, 0.0F, false);
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            Minecraft.getInstance().player.displayClientMessage(Component.literal(blockpos.toShortString()), true);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt(getNbtCoordinateX(), blockpos.getX());
            compoundTag.putInt(getNbtCoordinateY(), blockpos.getY());
            compoundTag.putInt(getNbtCoordinateZ(), blockpos.getZ());
            is.setTag(compoundTag);
        }
        return super.use(level, player, pUseHand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            String coordinates = "x="+getCoordinateX(pStack)+" y="+getCoordinateY(pStack)+" z="+getCoordinateZ(pStack);
            pTooltipComponents.add(Component.literal(coordinates));
        }
        pTooltipComponents.add(Component.literal("Disabled.."));

        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public static final String getNbtCoordinateX() {
        return BlocRouter.MOD_ID+NBT_COORDINATE_X;
    }
    public static final String getNbtCoordinateY() {
        return BlocRouter.MOD_ID+NBT_COORDINATE_Y;
    }
    public static final String getNbtCoordinateZ() {
        return BlocRouter.MOD_ID+NBT_COORDINATE_Z;
    }

    private static int getCoordinateX(ItemStack itemStack) {
        return Integer.valueOf(Objects.requireNonNull(itemStack.getTag().get(getNbtCoordinateX())).getAsString());
    }

    private static int getCoordinateY(ItemStack itemStack) {
        return Integer.valueOf(Objects.requireNonNull(itemStack.getTag().get(getNbtCoordinateY())).getAsString());
    }

    private static int getCoordinateZ(ItemStack itemStack) {
        return Integer.valueOf(Objects.requireNonNull(itemStack.getTag().get(getNbtCoordinateZ())).getAsString());
    }

    public static BlockPos getCoordinates(ItemStack itemStack) {
        return new BlockPos(
                getCoordinateX(itemStack),
                getCoordinateY(itemStack),
                getCoordinateZ(itemStack)
        );
    }
}
