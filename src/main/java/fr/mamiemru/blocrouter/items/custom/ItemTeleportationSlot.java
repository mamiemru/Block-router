package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
            is.setTag(encodeCoords(blockpos));
        }
        return super.use(level, player, pUseHand);
    }

    public static CompoundTag encodeCoords(BlockPos blockPos) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt(getNbtCoordinateX(), blockPos.getX());
        compoundTag.putInt(getNbtCoordinateY(), blockPos.getY());
        compoundTag.putInt(getNbtCoordinateZ(), blockPos.getZ());
        return compoundTag;
    }

    public static BlockPos decodeCoords(CompoundTag compoundTag) {
        return new BlockPos(
            compoundTag.getInt(getNbtCoordinateX()),
            compoundTag.getInt(getNbtCoordinateY()),
            compoundTag.getInt(getNbtCoordinateZ())
        );
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            int x = getCoordinateX(pStack);
            int y = getCoordinateY(pStack);
            int z = getCoordinateZ(pStack);
            pTooltipComponents.add(Component.literal("x:"+x+" y:"+y+" z:"+z));
            pTooltipComponents.add(Component.literal("Target:"+ level.getBlockState(new BlockPos(x,y,z)).getBlock().getDescriptionId()));
        }

        super.appendHoverText(pStack, level, pTooltipComponents, pIsAdvanced);
    }

    public static final String getNbtCoordinateX() {return BlocRouter.MOD_ID+NBT_COORDINATE_X; }
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

    public static ItemStack getTeleportationCardOrNull(ItemStack is) {
        return (is != null && is.getItem() instanceof ItemTeleportationSlot && is.hasTag() && is.getCount() == 1)? is : null;
    }
}
