package fr.mamiemru.blocrouter.blocks.custom.generators;

import fr.mamiemru.blocrouter.blocks.custom.statesProperties.ContentLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class PiezoelectricTube extends GlassBlock {

    public static final EnumProperty CONTENT = EnumProperty.create("content_level", ContentLevel.class);

    protected static final Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return false;
    }

    protected static final boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    public PiezoelectricTube() {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).noOcclusion()
                .isValidSpawn(PiezoelectricTube::never)
                .isRedstoneConductor(PiezoelectricTube::never)
                .isSuffocating(PiezoelectricTube::never)
                .isViewBlocking(PiezoelectricTube::never));
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTENT, ContentLevel.EMPTY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        super.createBlockStateDefinition(blockStateBuilder);
        blockStateBuilder.add(CONTENT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(CONTENT, ContentLevel.EMPTY);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                          Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {


        ItemStack is = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (is.isEmpty()) {
            emptyContent(pLevel, pPos, pPlayer);
            return InteractionResult.SUCCESS;
        } else if (!is.isEmpty() && ContentLevel.itemToIndex(is.getItem()) > 0){
            fillContent(is, pLevel, pPos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static Comparable getContentLevel(Level pLevel, BlockPos blockPos) {
        return pLevel.getBlockState(blockPos).getValue(PiezoelectricTube.CONTENT);
    }

    public void incrementContentLevel(Level pLevel, BlockPos blockPos) {
        setContentLevel(pLevel, blockPos, ContentLevel.nextLevel(getContentLevel(pLevel, blockPos)));
    }

    public void decrementContentLevel(Level pLevel, BlockPos blockPos) {
        while (pLevel.getBlockState(blockPos).getBlock() instanceof PiezoelectricTube) {
            ContentLevel level = (ContentLevel) getContentLevel(pLevel, blockPos);
            if (!ContentLevel.EMPTY.equals(level)) {
                setContentLevel(pLevel, blockPos, ContentLevel.previousLevel(level));
                return;
            }
            blockPos = blockPos.below();
        }
    }

    private void emptyContent(Level pLevel, BlockPos pPos, Player pPlayer) {
        pPlayer.setItemInHand(InteractionHand.MAIN_HAND, getInnerContent(pLevel, pPos));
        setContentLevel(pLevel, pPos, ContentLevel.EMPTY);
    }

    private ItemStack getInnerContent(Level pLevel, BlockPos pPos) {
        return switch ((ContentLevel) getContentLevel(pLevel, pPos)) {
            case FULL -> new ItemStack(Items.AMETHYST_SHARD, 4);
            case LARGE -> new ItemStack(Items.AMETHYST_SHARD, 3);
            case MEDIUM -> new ItemStack(Items.AMETHYST_SHARD, 2);
            case SMALL -> new ItemStack(Items.AMETHYST_SHARD, 1);
            default -> ItemStack.EMPTY;
        };
    }

    private void fillContent(ItemStack is, Level pLevel, BlockPos pPos) {

        int isIndex = ContentLevel.itemToIndex(is.getItem());
        if (isIndex > 0) {
            boolean state = true;
            BlockPos cursor = pPos;
            while (state) {
                boolean isBelowBlockATube = pLevel.getBlockState(cursor.below()).getBlock() instanceof PiezoelectricTube;
                if (isBelowBlockATube && !ContentLevel.FULL.equals(getContentLevel(pLevel, cursor.below()))) {
                    cursor = cursor.below();
                } else {
                    state = false;
                }
            }


            int currentIndex = ContentLevel.toIndex(getContentLevel(pLevel, cursor));
            if (currentIndex == 0) {
                setContentLevel(pLevel, cursor, ContentLevel.fromIndex(isIndex));
                is.setCount(is.getCount() - 1);
            } else {
                int sumIndex = isIndex + currentIndex;
                if (sumIndex <= ContentLevel.MAX_SIZE_INDEX) {
                    setContentLevel(pLevel, cursor, ContentLevel.fromIndex(sumIndex));
                    is.setCount(is.getCount() - 1);
                }
            }
        }
    }

    public void setContentLevel(Level pLevel, BlockPos blockPos, Comparable state) {
        pLevel.setBlock(blockPos, pLevel.getBlockState(blockPos).setValue(PiezoelectricTube.CONTENT, state), 1 | 2 | 8);
    }
}
