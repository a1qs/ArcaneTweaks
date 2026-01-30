package com.livajq.arcanetweaks.common.capability.foodbonuses;

import com.livajq.arcanetweaks.common.capability.ArcaneCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class FoodBonusesProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final FoodBonusesImp backend = new FoodBonusesImp();
    private final LazyOptional<FoodBonusesData> optional = LazyOptional.of(() -> backend);
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ArcaneCapabilities.FOOD_BONUSES ? optional.cast() : LazyOptional.empty();
    }
    
    @Override
    public CompoundTag serializeNBT() {
        return backend.serializeNBT();
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        backend.deserializeNBT(tag);
    }
    
    public FoodBonusesImp getBackend() {
        return backend;
    }
}
