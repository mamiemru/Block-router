package fr.mamiemru.blocrouter.items.custom;

import fr.mamiemru.blocrouter.BlocRouter;
import fr.mamiemru.blocrouter.entities.custom.patternEncoder.PatternEncoderEntity;
import fr.mamiemru.blocrouter.items.ItemsRegistry;
import fr.mamiemru.blocrouter.util.patterns.NormalRoutingPattern;
import fr.mamiemru.blocrouter.util.patterns.Pattern;
import fr.mamiemru.blocrouter.util.patterns.PatternRow;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemRoutingPattern extends Item {

    public static final String NBT_UUID = ".uuid";

    public ItemRoutingPattern() {
        super(new Properties().tab(BlocRouter.RouterCreativeTab));

    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUseHand) {
        ItemStack is = player.getItemInHand(pUseHand);
        if (player.isCrouching()) {
            is.setTag(new CompoundTag());
            player.setItemInHand(pUseHand, new ItemStack(ItemsRegistry.ITEM_ROUTING_PATTERN.get(), 1));
        }
        return super.use(level, player, pUseHand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag();
    }

    public static final String getNbtUuid() {
        return BlocRouter.MOD_ID+NBT_UUID;
    }

    public static ItemStack decodeIngredient(CompoundTag compoundTag) {
        return ItemStack.of(compoundTag);
    }

    public static List<ItemStack> decodeIngredients(ListTag listTag) {
        List<ItemStack> l = new ArrayList<>();
        for (int i = 0; i < listTag.size(); ++i) {
            l.add(decodeIngredient(listTag.getCompound(i)));
        }
        return l;
    }

    public static BlockPos decodeCoords(CompoundTag compoundTag) {
        return ItemTeleportationSlot.decodeCoords(compoundTag);
    }

    public static List<BlockPos> decodeCoords(ListTag listTag) {
        List<BlockPos> l = new ArrayList<>();
        for (int i = 0; i < listTag.size(); ++i) {
            l.add(decodeCoords(listTag.getCompound(i)));
        }
        return l;
    }
}
