package com.livajq.arcanetweaks.common.capability.foodbonuses;

import net.minecraft.nbt.CompoundTag;

public class FoodBonusesImp implements FoodBonusesData {
    private boolean noThirst;
    private boolean noTemperature;
    private boolean noExhaustion;
    
    @Override
    public boolean hasNoThirst() {
        return noThirst;
    }
    
    @Override
    public void setNoThirst(boolean value) {
        noThirst = value;
    }
    
    @Override
    public boolean hasNoTemperature() {
        return noTemperature;
    }
    
    @Override
    public void setNoTemperature(boolean value) {
        noTemperature = value;
    }
    
    @Override
    public boolean hasNoExhaustion()
    {
        return noExhaustion;
    }
    
    @Override
    public void setNoExhaustion(boolean value) {
        noExhaustion = value;
    }
    
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("NoThirst", noThirst);
        tag.putBoolean("NoTemperature", noTemperature);
        tag.putBoolean("NoExhaustion", noExhaustion);
        return tag;
    }
    
    public void deserializeNBT(CompoundTag tag) {
        noThirst = tag.getBoolean("NoThirst");
        noTemperature =  tag.getBoolean("NoTemperature");
        noExhaustion =  tag.getBoolean("NoExhaustion");
    }
}