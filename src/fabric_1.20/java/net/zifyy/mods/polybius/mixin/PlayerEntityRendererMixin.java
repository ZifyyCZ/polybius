package net.zifyy.mods.polybius.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.zifyy.mods.polybius.PolybiusClient;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends EntityRenderer<AbstractClientPlayerEntity> {

    protected PlayerEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    //    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = ""))
    //    ^ too lazy to mess with the targets and such :sherbert:

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void renderLabel(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci){
        double d = this.dispatcher.getSquaredDistanceToCamera(abstractClientPlayerEntity);
        matrices.push();
        var isBuddy = PolybiusClient.BUDDIES.isBuddy(abstractClientPlayerEntity);
        var style = PolybiusClient.style();
        if (d < 100.0) {
            Scoreboard scoreboard = abstractClientPlayerEntity.getScoreboard();
            ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(2);
            if (scoreboardObjective != null) {
                ScoreboardPlayerScore scoreboardPlayerScore = scoreboard.getPlayerScore(abstractClientPlayerEntity.getEntityName(), scoreboardObjective);
                var displayName = scoreboardObjective.getDisplayName();
                if(isBuddy){
                    displayName = displayName.copy().setStyle(style);
                }
                var name = Text.literal(Integer.toString(scoreboardPlayerScore.getScore())).append(ScreenTexts.SPACE).append(displayName);
                super.renderLabelIfPresent(abstractClientPlayerEntity, name, matrices, vertexConsumers, light);
                Objects.requireNonNull(this.getTextRenderer());
                matrices.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
            }
        }

        if(isBuddy){
            text = text.copy().setStyle(style);
        }
        super.renderLabelIfPresent(abstractClientPlayerEntity, text, matrices, vertexConsumers, light);
        matrices.pop();
        ci.cancel();
    }

}
