package com.livajq.arcanetweaks.client.renderer;

import com.livajq.arcanetweaks.common.entity.boss.TestBoss;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class TestBossRenderer extends MobRenderer<TestBoss, HumanoidModel<TestBoss>> {
    
    public TestBossRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(TestBoss entity) {
        return new ResourceLocation("minecraft", "textures/entity/zombie/zombie.png");
    }
}
