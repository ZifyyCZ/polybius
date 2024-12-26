package net.zifyy.mods.polybius.mixin;

import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.zifyy.mods.polybius.PolybiusClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {

    @Shadow protected abstract Text applyGameModeFormatting(PlayerListEntry entry, MutableText name);

    @Inject(method = "getPlayerName", cancellable = true, at = @At("HEAD"))
    private void m(PlayerListEntry entry, CallbackInfoReturnable<Text> cir){
        if(PolybiusClient.BUDDIES.isBuddy(entry.getProfile().getName(), entry.getProfile().getId())){
            var text = this.applyGameModeFormatting(entry, Team.decorateName(entry.getScoreboardTeam(), Text.literal(entry.getProfile().getName()).fillStyle(PolybiusClient.style())));
            if (entry.getDisplayName() != null) {
                var _displayName = entry.getDisplayName().copy();
                var displayName = _displayName.getString();
                var s = displayName.split(" ");
                var prefix = Text.literal("");
                var name = displayName;
                if(s.length > 1){
                    var playerName = entry.getProfile().getName();
                    if(displayName.startsWith(playerName)){
                        prefix = _displayName.getSiblings().get(1).copy().append("§r");
                        name = Formatting.RESET + s[0].trim();
                    } else {
                        prefix = _displayName.getSiblings().get(0).copy().append("§r");
                        name = Formatting.RESET + s[1].trim();
                    }
                }
//                var textName = Text.literal(name).copyContentOnly().fillStyle(PolybiusClient.style());
//                // gonna try everything to fix username being bold if the user has Mystic (anything bold before their name)
//                var prefixErmmekjdrkjg = Text.literal(prefix.getString()).fillStyle(prefix.getStyle());
//                text = this.applyGameModeFormatting(entry, prefixErmmekjdrkjg.copy().append("§r").copy().append(textName));

                // attempt #44889411332
                var textName = Text.literal(name).copyContentOnly().fillStyle(PolybiusClient.style());
                text = this.applyGameModeFormatting(entry, Text.literal(prefix.getString()).fillStyle(Style.EMPTY.withColor(prefix.getStyle().getColor()).withBold(prefix.getStyle().isBold()))
                        .append(Text.literal("§r").fillStyle(Style.EMPTY)).append(textName));
            }
            cir.setReturnValue(text);
        }
    }

}
