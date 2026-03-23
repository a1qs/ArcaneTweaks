package com.livajq.arcanetweaks.mixin.namelesstrinkets;

import com.cozary.nameless_trinkets.utils.LootTableHandler;
import net.minecraftforge.event.LootTableLoadEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootTableHandler.class)
public abstract class LootTableHandlerMixin {
    
    @Inject(method = "onLootTableLoad", at = @At("HEAD"), cancellable = true, remap = false)
    private static void disableTrinketLoot(LootTableLoadEvent event, CallbackInfo ci) {
        ci.cancel();
    }
}