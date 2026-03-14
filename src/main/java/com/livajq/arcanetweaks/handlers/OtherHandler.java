package com.livajq.arcanetweaks.handlers;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.Config;
import com.livajq.arcanetweaks.mixin.vanilla.ChunkGeneratorAccessor;
import com.livajq.arcanetweaks.world.district.DistrictBiomeSource;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
public class OtherHandler {
    
    private static final ResourceKey<Level> ABYSS = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostworlds", "abyss"));
    
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
        else targetDim = ABYSS;
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
        if (Config.worldgenType == 0) return;
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
    
    //constantly spawn particles on the player model
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        Player player = event.player;
        if (player == null) return;
        
        if (player.tickCount % 2 != 0) return;
        if (!player.getGameProfile().getName().equals("LunarEclipseV2")) return;
        
        if (!player.level().isClientSide) {
            if (!(player instanceof ServerPlayer serverPlayer)) return;
            ServerLevel level = serverPlayer.serverLevel();
            
            AABB box = player.getBoundingBox();
            
            for (ServerPlayer other : level.players()) {
                if (other == serverPlayer) continue;
                
                double x = randomInside(box.minX, box.maxX);
                double y = randomInside(box.minY, box.maxY);
                double z = randomInside(box.minZ, box.maxZ);

                level.sendParticles(
                        other,
                        ParticleTypes.GLOW,
                        false,
                        x, y, z,
                        1,
                        0, 0, 0,
                        0
                );
                
                spawnWings(other, 1.0F);
            }
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (player == mc.player && !mc.options.getCameraType().isFirstPerson()) {
            
            AABB box = player.getBoundingBox();
            
            double x = randomInside(box.minX, box.maxX);
            double y = randomInside(box.minY, box.maxY);
            double z = randomInside(box.minZ, box.maxZ);

            player.level().addParticle(
                    ParticleTypes.GLOW,
                    x, y, z,
                    0, 0, 0
            );
            
            spawnWings(player, Minecraft.getInstance().getFrameTime());
        }
    }
    
    private static double randomInside(double min, double max) {
        return min + Math.random() * (max - min);
    }
    
    private static void spawnWings(Player player, float partialTick) {
        
        float yaw = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot);
        double rad = Math.toRadians(yaw);
        
        Vec3 pos = player.position();
        
        double baseX = pos.x + Math.sin(rad) * 0.25;
        double baseY = pos.y + 1.2;
        double baseZ = pos.z - Math.cos(rad) * 0.25;
        
        Level level = player.level();
        
        for (int i = 0; i < 10; i++) {
            
            double t = i / 10.0;
            
            double x = 0.2 + t * 1.2;
            double y = Math.sin(t * Math.PI) * 0.8;
            
            spawnWingParticle(level, baseX, baseY, baseZ, x, y, rad);
            spawnWingParticle(level, baseX, baseY, baseZ, -x, y, rad);
        }
    }
    
    private static void spawnWingParticle(Level level, double baseX, double baseY, double baseZ, double x, double y, double rad) {
        double rx = x * Math.cos(rad);
        double rz = x * Math.sin(rad);
        
        level.addParticle(
                new DustParticleOptions(new Vector3f(1.0f, 0.85f, 0.2f), 1.2f),
                baseX + rx,
                baseY + y,
                baseZ + rz,
                0, 0, 0
        );
    }
}