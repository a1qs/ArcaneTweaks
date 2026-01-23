package com.livajq.arcanetweaks.bossbehavior;

import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class BossBehaviorRegistry {
    
    private static final Map<EntityType<?>, BossBehavior> BEHAVIORS = new HashMap<>();
    
    public static void register(EntityType<?> type, BossBehavior behavior) {
        BEHAVIORS.put(type, behavior);
    }
    
    public static BossBehavior get(LivingEntity entity) {
        return BEHAVIORS.get(entity.getType());
    }
    
    public static void init() {
        BossBehaviorRegistry.register(ModEntities.IGNIS.get(), new IgnisBehavior());
    }
}
