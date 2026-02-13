package com.livajq.arcanetweaks.handlers;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.mixin.vanilla.ChunkGeneratorAccessor;
import com.livajq.arcanetweaks.world.district.DistrictBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class OtherHandler {
    
    //certain eyes used as dimension teleporters instead
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        Item item = event.getItemStack().getItem();
        if (item != ItemInit.ANCIENT_TOMB_EYE.get() && item != ItemInit.BLOODY_ALTAR_EYE.get()) return;
        event.setCanceled(true);
        Player player = event.getEntity();
        if (player.level().isClientSide() || !player.isCrouching()) return;
        
        ServerPlayer serverPlayer = (ServerPlayer) player;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return;
        
        ResourceKey<Level> targetDim;
        if (item == ItemInit.ANCIENT_TOMB_EYE.get()) targetDim = Level.NETHER;
        else targetDim = Level.END;
        if (serverPlayer.level().dimension() == targetDim) return;
        
        ServerLevel targetLevel = server.getLevel(targetDim);
        if (targetLevel == null) return;
        
        serverPlayer.changeDimension(targetLevel, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                Entity e = repositionEntity.apply(false);

                BlockPos spawn = destWorld.getSharedSpawnPos();
                Vec3 pos = Vec3.atBottomCenterOf(spawn);
                
                e.teleportTo(pos.x, pos.y, pos.z);
                return e;
            }
        });
    }
    
    //replace vanilla BiomeSource
    //I probably just don't know how to do it like a normal person but json overrides caused modded biomes to stop generating
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;
        
        replaceOverworldBiomeSource(level);
    }
    
    private static void replaceOverworldBiomeSource(ServerLevel level) {
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        
        BiomeSource vanilla = generator.getBiomeSource();
        BiomeSource wrapped = new DistrictBiomeSource(vanilla, level.getSeed());

        ((ChunkGeneratorAccessor) generator).setBiomeSource(wrapped);
    }
}