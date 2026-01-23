package com.livajq.arcanetweaks.init;


import com.livajq.arcanetweaks.ArcaneTweaks;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcaneEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArcaneTweaks.MODID);
    
    /*
    public static final RegistryObject<EntityType<TestBoss>> TEST_BOSS =
            ENTITY_TYPES.register("test_boss",
                    () -> EntityType.Builder.of(TestBoss::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build(ArcaneTweaks.MODID + ":test_boss"));
                            
     */
    
    public static void onRegisterAttributes(EntityAttributeCreationEvent event) {
        //event.put(TEST_BOSS.get(), TestBoss.createAttributes().build());
    }
    
}