package com.livajq.arcanetweaks.bossbehavior;

import com.gametechbc.traveloptics.effects.MeteorStormEffect;
import com.gametechbc.traveloptics.init.TravelopticsEffects;
import com.gametechbc.traveloptics.spells.fire.MeteorStormSpell;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.init.ModTag;
import com.github.L_Ender.cataclysm.util.LazyTagLookup;
import com.livajq.arcanetweaks.ArcaneTweaks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITag;

public class IgnisBehavior extends BossBehavior<Ignis_Entity> {
    
    public IgnisBehavior() {
        super(4);
    }
    
    @Override
    public void onPhaseTick(Ignis_Entity boss, int phase) {
    
    }
    
    @Override
    public void onPhaseChange(Ignis_Entity boss, int newPhase, int oldPhase, boolean firstTime) {
        if (newPhase != 2) return;
       
        MeteorStormSpell meteorStorm = new MeteorStormSpell();
        meteorStorm.onCast(boss.level(), 20, boss, CastSource.MOB, MagicData.getPlayerMagicData(boss));
        
        if (boss.hasEffect(TravelopticsEffects.METEOR_STORM.get())) {
            boss.removeEffect(TravelopticsEffects.METEOR_STORM.get());
            boss.addEffect(new MobEffectInstance(TravelopticsEffects.METEOR_STORM.get(), 400, 150));
            
            MobEffectInstance effectInstance = boss.getEffect(TravelopticsEffects.METEOR_STORM.get());
            if (effectInstance != null && effectInstance.getEffect() instanceof MeteorStormEffect stormEffect) stormEffect.setOuterRadius(16);
        }
        
    }
}