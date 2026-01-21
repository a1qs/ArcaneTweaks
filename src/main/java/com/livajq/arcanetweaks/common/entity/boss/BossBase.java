package com.livajq.arcanetweaks.common.entity.boss;

import com.livajq.arcanetweaks.ArcaneTweaks;
import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class BossBase extends Monster {
    private final ServerBossEvent bossEvent =
            new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.NOTCHED_10
            );
    
    protected final Set<UUID> bossMinions = new HashSet<>();
    
    protected BossBase(EntityType<? extends BossBase> type, Level level) {
        super(type, level);
        this.bossEvent.setVisible(true);
        this.setPersistenceRequired();
    }
    
    @Override
    public void tick() {
        super.tick();

        bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        bossEvent.setName(this.getDisplayName());
    }
    
    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }
    
    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }
    
    public void onMinionAdded(Mob minion) {
    
    }
    
    public void onMinionDied(Mob minion) {
    
    }
    
    protected <T extends Mob> void spawnMinions(EntityType<T> type, int count, double radius) {
        Level level = this.level();
        if (level.isClientSide) return;
        
        int spawned = 0;
        int attempts = 0;
        int maxAttempts = 100;
        
        while (spawned < count && attempts < maxAttempts) {
            attempts++;
            
            double angle = level.random.nextDouble() * Math.PI * 2;
            double x = this.getX() + radius * Math.cos(angle);
            double z = this.getZ() + radius * Math.sin(angle);
            
            BlockPos base = BlockPos.containing(x, this.getY(), z);
            BlockPos safe = level.getHeightmapPos(
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    base
            );
            
            T minion = type.create(level);
            if (minion == null) continue;
            
            minion.moveTo(
                    safe.getX() + 0.5,
                    safe.getY(),
                    safe.getZ() + 0.5,
                    level.random.nextFloat() * 360F,
                    0F
            );
            
            minion.getCapability(ArcaneCapabilities.BOSS_MINION)
                    .ifPresent(cap -> cap.setBoss(this));
            
            minion.setPersistenceRequired();
            
            if (level.addFreshEntity(minion)) spawned++;
        }
    }
    
    protected void reconcileMinions() {
        if (!(level() instanceof ServerLevel level)) return;
        
        AABB box = this.getBoundingBox().inflate(128);
        
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, box, mob ->
                mob.getCapability(ArcaneCapabilities.BOSS_MINION)
                        .map(cap -> this.equals(cap.getBoss()))
                        .orElse(false)
        );
        
        for (Mob mob : mobs) {
            UUID id = mob.getUUID();
            if (bossMinions.add(id)) {
                onMinionAdded(mob);
            }
        }
    }
    
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        reconcileMinions();
    }
    
    @Mod.EventBusSubscriber(modid = ArcaneTweaks.MODID)
    static class BossHandler {
        
        @SubscribeEvent
        public static void onMinionDeath(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Mob mob)) return;
            
            mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
                BossBase boss = cap.getBoss();
                if (boss != null) {
                    boss.onMinionDied(mob);
                }
            });
        }
        
        @SubscribeEvent
        public static void onMinionJoin(EntityJoinLevelEvent event) {
            if (!(event.getEntity() instanceof Mob mob)) return;
            
            mob.getCapability(ArcaneCapabilities.BOSS_MINION).ifPresent(cap -> {
                BossBase boss = cap.getBoss();
                if (boss != null) {
                    UUID id = mob.getUUID();

                    if (boss.bossMinions.add(id)) {
                        boss.onMinionAdded(mob);
                    }
                }
            });
        }
    }
}