package com.livajq.arcanetweaks.common.capability.parry;

import com.livajq.arcanetweaks.Config;
import com.oblivioussp.spartanweaponry.api.ModToolActions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ParryImp implements ParryData {

    Player player;
    
    private long parryWindUpTicks = 0;
    private long parryWindUpTicksMax = Config.parryWindUp;
    private long parryWindowTicks = 0;
    private long parryWindowTicksMax = Config.parryWindow;
    private long parryCooldownTicks = 0;
    private long parryCooldownTicksFail = Config.parryCooldownFail;
    private long ParryCooldownTicksSuccess = Config.parryCooldownSuccess;
    
    private boolean parryWindowActive = false;
    
    public ParryImp(Player player) {
        this.player = player;
    }
    
    @Override
    public boolean isUsingParryItem() {
        if (player.isUsingItem()) {
            ItemStack stack = player.getUseItem();
            if (stack.isEmpty()) return false;
            return stack.getItem().canPerformAction(stack, ModToolActions.MELEE_BLOCK);
        }
        return false;
    }
    
    @Override
    public boolean isParryWindowActive() {
        return parryWindowActive;
    }
    
    @Override
    public boolean canParrySource(DamageSource source) {
        return source.getMsgId().equals("player")
                || source.getMsgId().equals("mob")
                && !source.is(DamageTypeTags.IS_EXPLOSION)
                && !source.is(DamageTypeTags.IS_FIRE)
                && !source.is(DamageTypeTags.IS_PROJECTILE)
                && !source.is(DamageTypeTags.BYPASSES_ARMOR);
    }
    
    @Override
    public void setParryWindowActive(boolean parryWindowActive) {
        this.parryWindowActive = parryWindowActive;
    }
    
    @Override
    public void tickParry() {
        if (parryCooldownTicks > 0) {
            parryCooldownTicks--;
            return;
        }
        
        if (!isUsingParryItem()) {
            if (parryWindUpTicks > 0 || parryWindowTicks > 0 || isParryWindowActive()) performParryFail();
            return;
        }
        
        if (!isParryWindowActive()) {
            if (parryWindUpTicks <= 0) parryWindUpTicks = parryWindUpTicksMax;
            else {
                parryWindUpTicks--;
                if (parryWindUpTicks <= 0) {
                    setParryWindowActive(true);
                    parryWindowTicks = parryWindowTicksMax;
                }
            }
        }
        else {
            parryWindowTicks--;
            if (parryWindowTicks <= 0) performParryFail();
        }
    }
    
    @Override
    public void performParryFail() {
        parryCooldownTicks = parryCooldownTicksFail;
        parryWindUpTicks = 0;
        parryWindowTicks = 0;
        setParryWindowActive(false);
    }
    
    @Override
    public void performParrySuccess(LivingEntity target) {
        parryCooldownTicks = ParryCooldownTicksSuccess;
        parryWindUpTicks = 0;
        parryWindowTicks = 0;
        setParryWindowActive(false);
        
        //stuff
    }
    
    @Override
    public void setParryWindUpTicksMax(int ticks) {
        parryWindUpTicksMax = ticks;
    }
    
    @Override
    public void setParryWindowTicksMax(int ticks) {
        parryWindowTicksMax = ticks;
    }
    
    @Override
    public void setParryCooldownTicksFail(int ticks) {
        parryCooldownTicksFail = ticks;
    }
    
    @Override
    public void setParryCooldownTicksSuccess(int ticks) {
        ParryCooldownTicksSuccess = ticks;
    }
    
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("ParryWindUPTicks", parryWindUpTicks);
        tag.putLong("ParryWindowTicks", parryWindowTicks);
        tag.putLong("ParryCooldownTicks", parryCooldownTicks);
        tag.putBoolean("ParryWindowActive", parryWindowActive);
        return tag;
    }
   
    public void deserializeNBT(CompoundTag tag) {
        parryWindUpTicks = tag.getLong("ParryWindUPTicks");
        parryWindowTicks = tag.getLong("ParryWindowTicks");
        parryCooldownTicks = tag.getLong("ParryCooldownTicks");
        parryWindowActive = tag.getBoolean("ParryWindowActive");
    }
}