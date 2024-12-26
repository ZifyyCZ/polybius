package net.zifyy.mods.polybius.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.text.Text;
import net.zifyy.mods.polybius.PolybiusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends EntityRenderer<AbstractClientPlayerEntity> {

    protected PlayerEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    //    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = ""))
    //    ^ too lazy to mess with the targets and such :sherbert:

//    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
//    private void renderLabel(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
//        double d = this.dispatcher.getSquaredDistanceToCamera(abstractClientPlayerEntity);
//        matrices.push();
//        var isBuddy = PolybiusClient.BUDDIES.isBuddy(abstractClientPlayerEntity);
//        var style = PolybiusClient.style();
////        if (d < 100.0) {
////            Scoreboard scoreboard = abstractClientPlayerEntity.getScoreboard();
////            ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(2);
////            if (scoreboardObjective != null) {
////                ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(abstractClientPlayerEntity.getEntityName(), scoreboardObjective);
////                var displayName = scoreboardObjective.getDisplayName();
////                if(isBuddy){
////                    displayName = displayName.copy().setStyle(style);
////                }
////                var name = Text.literal(Integer.toString(scoreboardPlayerScore.getScore())).append(ScreenTexts.SPACE).append(displayName);
////                super.renderLabelIfPresent(abstractClientPlayerEntity, name, matrices, vertexConsumers, light);
////                Objects.requireNonNull(this.getTextRenderer());
////                matrices.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
////            }
////        }
//
//        if(isBuddy){
//            text = text.copy().setStyle(style);
//        }
//        super.renderLabelIfPresent(abstractClientPlayerEntity, text, matrices, vertexConsumers, light);
//        matrices.pop();
//        ci.cancel();
//    }

    @ModifyArgs(method = "renderLabelIfPresent*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 1))
    private void renderLabelIfPresent(Args args){
        var isBuddy = PolybiusClient.BUDDIES.isBuddy(args.get(0));
        if (isBuddy) {
            var style = PolybiusClient.style();
            args.set(1, ((Text)args.get(1)).copy().setStyle(style));
        }
    }

}
