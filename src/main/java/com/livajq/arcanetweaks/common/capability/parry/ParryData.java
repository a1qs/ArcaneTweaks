package com.livajq.arcanetweaks.common.capability.parry;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface ParryData {
    
    boolean isUsingParryItem();
    boolean isParryWindowActive();
    boolean canParrySource(DamageSource source);
    void setParryWindowActive(boolean parryWindowActive);
    void tickParry();
    void performParryFail();
    void performParrySuccess(LivingEntity target);
    void setParryWindUpTicksMax(int ticks);
    void setParryWindowTicksMax(int ticks);
    void setParryCooldownTicksFail(int ticks);
    void setParryCooldownTicksSuccess(int ticks);
}