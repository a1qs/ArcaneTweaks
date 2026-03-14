package com.livajq.arcanetweaks.mixin.lendercataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.world.data.CMWorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Ignis_Entity.class)
public class Ignis_EntityMixin {
    
    @Redirect(
            method = "AfterDefeatBoss",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/github/L_Ender/cataclysm/world/data/CMWorldData;setIgnisDefeatedOnce(Z)V"
            ),
            remap = false
    )
    private void cancelIgnisFlag(CMWorldData instance, boolean value) {
        //skip
    }
}