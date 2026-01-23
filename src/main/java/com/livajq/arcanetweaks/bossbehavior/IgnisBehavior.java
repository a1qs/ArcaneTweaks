package com.livajq.arcanetweaks.bossbehavior;

import com.livajq.arcanetweaks.util.BossBehaviorUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;

import java.util.Set;
import java.util.UUID;

public class IgnisBehavior extends BossBehavior {
    
    public IgnisBehavior() {
        super(3);
    }
    
    @Override
    public void onPhaseTick(LivingEntity boss, int phase) {
        Set<UUID> minions = MINIONS.getOrDefault(boss, Set.of());
        
        int witherSkeletons = 0;
        
        for (UUID id : minions) {
            Entity e = ((ServerLevel) boss.level()).getEntity(id);
            if (e instanceof WitherSkeleton) witherSkeletons++;
        }
        
        boolean shieldActive = witherSkeletons > 0;
        
        boss.getPersistentData().putBoolean("Arcane_ShieldActive", shieldActive);
    }
    
    @Override
    public void onPhaseChange(LivingEntity boss, int newPhase, int oldPhase, boolean firstTime) {
        if (!firstTime) return;
        
        if (newPhase == 2) BossBehaviorUtils.spawnMinions(boss, EntityType.SKELETON, 3, 10.0);
        else if (newPhase == 3) BossBehaviorUtils.spawnMinions(boss, EntityType.WITHER_SKELETON, 3, 10.0);
    }
    
    @Override
    public HurtResult onHurt(LivingEntity boss, DamageSource src, float amount) {
        boolean shieldActive = boss.getPersistentData().getBoolean("Arcane_ShieldActive");
        return shieldActive && !src.is(DamageTypes.FELL_OUT_OF_WORLD) ? HurtResult.cancel() : HurtResult.pass();
    }
}