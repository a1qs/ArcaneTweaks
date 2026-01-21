package com.livajq.arcanetweaks.common.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TestBoss extends PhaseBoss {
    private int skeletonMinions = 0;
    private int witherSkeletonMinions = 0;
    private boolean shieldActive = false;
    
    public TestBoss(EntityType<? extends TestBoss> type, Level level) {
        super(type, level, 3);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 40.0D);
    }
    
    @Override
    protected void tickPhase(int phase) {
        if (witherSkeletonMinions <= 0) shieldActive = false;
        
        if (phase == 2) {
            if (skeletonMinions > 0) {
                for (Player player : this.level().players()) {
                    if (player.distanceTo(this) < 64) {
                        player.sendSystemMessage(Component.literal("Phase 2 active. Some buffs or whatever. Minion count: " + skeletonMinions));
                    }
                }
                
            }
        }
        
        if (phase == 3) {
            if (witherSkeletonMinions > 0) {
                this.shieldActive = true;
                
                for (Player player : this.level().players()) {
                    if (player.distanceTo(this) < 64) {
                        player.sendSystemMessage(Component.literal("Phase 3 active. Asmo style shield. Minion count: " + witherSkeletonMinions));
                    }
                }
            }
        }
    }
    
    @Override
    public void onPhaseChanged(int newPhase, int oldPhase, boolean firstTime) {
        if (!firstTime) return;
        if (newPhase == 2) summonSkeletons();
        else if (newPhase == 3) summonWitherSkeletons();
    }
    
    @Override
    public void onMinionAdded(Mob minion) {
        if (minion instanceof Skeleton) skeletonMinions++;
        else if (minion instanceof WitherSkeleton) witherSkeletonMinions++;
    }
    
    @Override
    public void onMinionDied(Mob minion) {
        if (minion instanceof Skeleton) skeletonMinions--;
        else if (minion instanceof WitherSkeleton) witherSkeletonMinions--;
    }
    
    private void summonSkeletons() {
        this.spawnMinions(EntityType.SKELETON, 3, 10.0D);
    }
    
    private void summonWitherSkeletons() {
        this.spawnMinions(EntityType.WITHER_SKELETON, 3, 10.0D);
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (shieldActive && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) return false;
        return super.hurt(source, amount);
    }
}