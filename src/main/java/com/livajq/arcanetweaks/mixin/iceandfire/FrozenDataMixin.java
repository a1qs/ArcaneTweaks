package com.livajq.arcanetweaks.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.props.FrozenData;
import com.livajq.arcanetweaks.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FrozenData.class)
public class FrozenDataMixin {
    
    @Inject(method = "setFrozen", at = @At("HEAD"), remap = false)
    private void checkImmunity(LivingEntity target, int duration, CallbackInfo ci) {
        String mobId = ForgeRegistries.ENTITY_TYPES.getKey(target.getType()).toString();
        if (Config.mobFreezeImmunitySet.contains(mobId)) ci.cancel();
    }
}