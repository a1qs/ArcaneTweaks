package com.livajq.arcanetweaks.common.capability.bossminion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface BossMinionData {
    void setBoss(LivingEntity boss);
    LivingEntity getBoss();
    
    void setBossId(UUID id);
    UUID getBossId();
    
    void setLevel(Level level);
}
