package com.blackmoss.islcttc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class IslcttcEvent {

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent event) {
        Level level = event.getLevel();

        // 遍历所有玩家
        for (Player player : level.players()) {
            // 检测范围
            for (int i = 0; i <= IslcttcConfig.DETECTION_VERTICAL_RANGE.getAsInt(); i++) {
                BlockPos pos = player.blockPosition().above(i);
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof PointedDripstoneBlock
                        && state.getValue(PointedDripstoneBlock.TIP_DIRECTION) == Direction.DOWN
                        && Math.abs(player.getX() - (pos.getX() + 0.5)) <= 1.5
                        && Math.abs(player.getZ() - (pos.getZ() + 0.5)) <= 1.5) {

                    // 掉落
                    spawnFallingStalactite(state, (ServerLevel) level, pos);
                }
            }
        }
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        // 遍历一连串
        for(BlockState blockstate = state;
            isStalactite(blockstate);
            blockstate = level.getBlockState(blockpos$mutableblockpos)) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, blockpos$mutableblockpos, blockstate);
            // 如果是尖端就造成伤害
            if (isTip(blockstate)) {
                int i = Math.max((1 + pos.getY() - blockpos$mutableblockpos.getY()) * IslcttcConfig.DAMAGE_MULTIPLIER.get(), 1);                float f = (float) i;
                fallingblockentity.setHurtsEntities(f, IslcttcConfig.MAX_DAMAGE.get());
                break;
            }
            blockpos$mutableblockpos.move(Direction.DOWN);
        }
    }

    // 判断是不是滴水石锥
    private static boolean isStalactite(BlockState state) {
        return isPointedDripstoneWithDirection(state);
    }

    // 检测是不是向下的滴水石锥
    private static boolean isPointedDripstoneWithDirection(BlockState state) {
        return state.is(Blocks.POINTED_DRIPSTONE)
                && state.getValue(PointedDripstoneBlock.TIP_DIRECTION) == Direction.DOWN;
    }

    // 检测是不是尖端
    private static boolean isTip(BlockState state) {
        DripstoneThickness dripstonethickness = state.getValue(PointedDripstoneBlock.THICKNESS);
        return state.is(Blocks.POINTED_DRIPSTONE)
                && (dripstonethickness == DripstoneThickness.TIP
                || dripstonethickness == DripstoneThickness.TIP_MERGE);
    }
}
