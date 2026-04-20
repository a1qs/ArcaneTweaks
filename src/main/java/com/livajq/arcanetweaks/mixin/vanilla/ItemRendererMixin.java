package com.livajq.arcanetweaks.mixin.vanilla;

import com.Harbinger.Spore.Sitems.BaseWeapons.SporeWeaponData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    
    //I don't understand what spore does that vanilla items don't. Nothing in model jsons, both get registered with RegisterColorHandlersEvent
    //No custom models as far as I'm aware of. And to add to that armor icons work normally, it's only a SwordItem issue because of course
    //Further testing: non spore items get caught by ordinal 1 here, so they DO pass the tintable check. What fails is the renderer trying to pull
    //the right color from itemColors. Only for non spore weapons. Why? IDFK
    @ModifyVariable(
            method = "renderQuadList",
            at = @At(value = "STORE", ordinal = 1),
            name = "i"
    )
    private int forceTintIfMutated(int original, PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack stack) {
        if (stack.getItem() instanceof SporeWeaponData data) return data.getVariant(stack).getColor();
        return original;
    }
}