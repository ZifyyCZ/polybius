package net.zifyy.mods.polybius.mixin;

import net.minecraft.client.MinecraftClient;
import net.zifyy.mods.polybius.PolybiusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "close")
    private void onClose(CallbackInfo ci){
        PolybiusClient.onClientClose((MinecraftClient) (Object)this);
    }

}
