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
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.minecraft.world.level.block.PointedDripstoneBlock.THICKNESS;
import static net.minecraft.world.level.block.PointedDripstoneBlock.TIP_DIRECTION;

public class IslcttcEvent {
    private final Map<UUID, BlockPos> fallingStalactites = new HashMap<>();

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);

        if (level != null) {
            for (Player player : level.players()) {
                for (int i = 0; i <= IslcttcConfig.DETECTION_VERTICAL_RANGE.getAsInt(); i++) {
                    BlockPos pos = player.blockPosition().above(i);
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() instanceof PointedDripstoneBlock
                            && state.getValue(TIP_DIRECTION) == Direction.DOWN
                            && Math.abs(player.getX() - (pos.getX() + 0.5)) <= 1.5
                            && Math.abs(player.getZ() - (pos.getZ() + 0.5)) <= 1.5) {

                        spawnFallingStalactite(state, level, pos);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof FallingBlockEntity fallingBlock) {
            BlockState blockState = fallingBlock.getBlockState();
            if (blockState.is(Blocks.POINTED_DRIPSTONE)) {
                BlockPos startPos = fallingBlock.blockPosition();
                fallingStalactites.put(fallingBlock.getUUID(), startPos);
            }
        }
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

        for(BlockState blockstate = state;
            isStalactite(blockstate);
            blockstate = level.getBlockState(blockpos$mutableblockpos)) {
            FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, blockpos$mutableblockpos, blockstate);
            if (isTip(blockstate)) {
                int i = Math.max((1 + pos.getY() - blockpos$mutableblockpos.getY()) * IslcttcConfig.DAMAGE_MULTIPLIER.get(), 1);                float f = (float) i;
                fallingblockentity.setHurtsEntities(f, IslcttcConfig.MAX_DAMAGE.get());
                break;
            }
            blockpos$mutableblockpos.move(Direction.DOWN);
        }
    }

    private static boolean isStalactite(BlockState state) {
        return isPointedDripstoneWithDirection(state);
    }

    private static boolean isPointedDripstoneWithDirection(BlockState state) {
        return state.is(Blocks.POINTED_DRIPSTONE)
                && state.getValue(TIP_DIRECTION) == Direction.DOWN;
    }

    private static boolean isTip(BlockState state) {
        if (!state.is(Blocks.POINTED_DRIPSTONE)) {
            return false;
        } else {
            DripstoneThickness dripstonethickness = state.getValue(THICKNESS);
            return dripstonethickness == DripstoneThickness.TIP
                    || dripstonethickness == DripstoneThickness.TIP_MERGE;
        }
    }
}
