package com.livajq.arcanetweaks.common.capability.foodbonuses;

import net.minecraft.nbt.CompoundTag;

public class FoodBonusesImp implements FoodBonusesData {
    private boolean noThirst;
    private boolean noTemperature;
    
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
        return noTemperature; }
    
    @Override
    public void setNoTemperature(boolean value) {
        noTemperature = value;
    }
    
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("NoThirst", noThirst);
        tag.putBoolean("NoTemperature", noTemperature);
        return tag;
    }
    
    public void deserializeNBT(CompoundTag tag) {
        noThirst = tag.getBoolean("NoThirst");
        noTemperature =  tag.getBoolean("NoTemperature");
    }
}