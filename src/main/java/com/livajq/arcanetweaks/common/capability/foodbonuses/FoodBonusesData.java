package com.livajq.arcanetweaks.common.capability.foodbonuses;

public interface FoodBonusesData {
    boolean hasNoThirst();
    void setNoThirst(boolean value);
    
    boolean hasNoTemperature();
    void setNoTemperature(boolean value);
    
    boolean hasNoExhaustion();
    void setNoExhaustion(boolean value);
}