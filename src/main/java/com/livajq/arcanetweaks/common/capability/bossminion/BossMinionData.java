package com.livajq.arcanetweaks.common.capability.bossminion;

import com.livajq.arcanetweaks.common.entity.boss.BossBase;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface BossMinionData {
    void setBoss(BossBase boss);
    BossBase getBoss();
    
    void setBossId(UUID id);
    UUID getBossId();
    
    void setLevel(Level level);
}
