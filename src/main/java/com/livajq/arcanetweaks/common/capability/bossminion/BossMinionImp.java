package com.livajq.arcanetweaks.common.capability.bossminion;

import com.livajq.arcanetweaks.common.entity.boss.BossBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class BossMinionImp implements BossMinionData {
    
    private UUID bossId;
    private BossBase bossRef;
    private Level level;
    
    @Override
    public void setBoss(BossBase boss) {
        this.bossRef = boss;
        this.bossId = boss.getUUID();
    }
    
    @Override
    public BossBase getBoss() {
        if (bossRef != null) return bossRef;
        if (bossId == null || !(level instanceof ServerLevel server)) return null;
        
        Entity e = server.getEntity(bossId);
        if (e instanceof BossBase b) {
            bossRef = b;
            return b;
        }
        return null;
    }
    
    @Override
    public void setBossId(UUID id) {
        this.bossId = id;
    }
    
    @Override
    public UUID getBossId() {
        return bossId;
    }
    
    @Override
    public void setLevel(Level level) {
        this.level = level;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (bossId != null) tag.putUUID("BossId", bossId);
        return tag;
    }
    
    public void deserializeNBT(CompoundTag tag) {
        if (tag.hasUUID("BossId")) bossId = tag.getUUID("BossId");
    }
}
