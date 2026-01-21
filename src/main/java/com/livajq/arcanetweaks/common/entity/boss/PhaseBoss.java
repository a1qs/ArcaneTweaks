package com.livajq.arcanetweaks.common.entity.boss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class PhaseBoss extends BossBase {
    
    protected int phaseCount;
    protected int currentPhase = 1;
    protected final double[] thresholds;
    protected final boolean[] phaseVisited;
    
    protected PhaseBoss(EntityType<? extends PhaseBoss> type, Level level) {
        this(type, level, 2);
    }
    
    protected PhaseBoss(EntityType<? extends PhaseBoss> type, Level level, int phaseCount) {
        super(type, level);
        this.phaseCount = phaseCount;
        this.thresholds = phaseThresholds();
        this.phaseVisited = new boolean[phaseCount + 1];
    }
    
    @Override
    public void tick() {
        super.tick();
        
        double hpRatio = this.getHealth() / this.getMaxHealth();
        
        int newPhase = 1;
        for (int i = 0; i < thresholds.length; i++) {
            if (hpRatio <= thresholds[i]) newPhase = i + 2;
        }
        
        if (newPhase != currentPhase) {
            int old = currentPhase;
            currentPhase = newPhase;
            
            boolean firstTime = !phaseVisited[newPhase];
            phaseVisited[newPhase] = true;
            
            onPhaseChanged(newPhase, old, firstTime);
        }
        
        tickPhase(currentPhase);
    }
    
    @Override
    public Component getDisplayName() {
        return Component.literal(this.getName().getString() + " (Phase " + currentPhase + ")");
    }
    
    protected double[] phaseThresholds() {
        double[] thresholds = new double[phaseCount - 1];
        for (int i = 1; i < phaseCount; i++) {
            thresholds[i - 1] = 1.0 - (i / (double) phaseCount);
        }
        return thresholds;
    }
    
    protected abstract void tickPhase(int phase);
    
    public abstract void onPhaseChanged(int newPhase, int oldPhase, boolean firstTime);
    
    public int getPhase() {
        return currentPhase;
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("CurrentPhase", currentPhase);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        currentPhase = tag.getInt("CurrentPhase");
        
        for (int i = 1; i < phaseVisited.length; i++) {
            phaseVisited[i] = i <= currentPhase;
        }
    }
}