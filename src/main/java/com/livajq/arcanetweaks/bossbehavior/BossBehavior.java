package com.livajq.arcanetweaks.bossbehavior;

import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;

import java.util.*;

public abstract class BossBehavior {
    
    public final int phaseCount;
    public final double[] thresholds;
    public static final Map<LivingEntity, Set<UUID>> MINIONS = new WeakHashMap<>();
    
    protected BossBehavior(int phaseCount) {
        this.phaseCount = phaseCount;
        this.thresholds = phaseThresholds();
    }
    
    public void onPhaseTick(LivingEntity boss, int phase) {}
    public void onPhaseChange(LivingEntity boss, int newPhase, int oldPhase, boolean firstTime) {}
    public HurtResult onHurt(LivingEntity boss, DamageSource src, float amount) {return HurtResult.pass();}
    public void onMinionAdded(LivingEntity boss, Mob minion) {}
    public void onMinionDied(LivingEntity boss, Mob minion) {}
    
    protected double[] phaseThresholds() {
        double[] thresholds = new double[phaseCount - 1];
        for (int i = 1; i < phaseCount; i++) {
            thresholds[i - 1] = 1.0 - (i / (double) phaseCount);
        }
        return thresholds;
    }
    
    public void reconcileMinions(LivingEntity boss) {
        ServerLevel level = (ServerLevel) boss.level();
        
        AABB box = boss.getBoundingBox().inflate(128);
        
        List<Mob> mobs = level.getEntitiesOfClass(Mob.class, box, mob ->
                mob.getCapability(ArcaneCapabilities.BOSS_MINION)
                        .map(cap -> boss.equals(cap.getBoss()))
                        .orElse(false)
        );
        
        Set<UUID> set = MINIONS.computeIfAbsent(boss, b -> new HashSet<>());
        
        for (Mob mob : mobs) {
            if (set.add(mob.getUUID())) {
                onMinionAdded(boss, mob);
            }
        }
    }
}