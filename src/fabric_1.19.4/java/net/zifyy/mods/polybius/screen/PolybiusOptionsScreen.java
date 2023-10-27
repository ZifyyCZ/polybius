package net.zifyy.mods.polybius.screen;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.zifyy.mods.polybius.Buddy;
import net.zifyy.mods.polybius.PolybiusClient;

import java.awt.*;

public class PolybiusOptionsScreen extends Screen {
    private CheckboxWidget checkBoxBold;
    public boolean showPlayers;
    private PressableTextWidget buttonPlayers;
    private TextFieldWidget inputColor;
    private int colorError;

    public PolybiusOptionsScreen() {
        super(Text.of("Polybius options"));
    }

    private void toggleBuddyList(ButtonWidget buttonWidget) {
        this.showPlayers = !this.showPlayers;
    }

    @Override
    protected void init() {
        super.init();
        this.inputColor = new TextFieldWidget(this.textRenderer, 10, 70, 120, 14, Text.empty());
        this.inputColor.setMaxLength(6);
        this.inputColor.setText(PolybiusClient.colorToHex(PolybiusClient.OPTIONS.buddyColor()));
        this.checkBoxBold = new CheckboxWidget(10, 100, 300, 20, Text.literal("§7Make names of buddies bold"), PolybiusClient.OPTIONS.buddyBold, true);
        this.buttonPlayers = new PressableTextWidget(10, 40, 300, 14, Text.literal("§aClick here to toggle between buddy list and config."), this::toggleBuddyList, this.textRenderer);
        this.addDrawableChild(new PressableTextWidget(10, 30, 300, 14, Text.literal("§cClick here to refresh the list (might lag the game)"), this::refresh, this.textRenderer));
        this.addDrawableChild(this.checkBoxBold);
        this.addDrawableChild(this.inputColor);
        this.addDrawableChild(this.buttonPlayers);
    }

    private void refresh(ButtonWidget buttonWidget) {
        PolybiusClient.BUDDIES.refresh();
    }

    @Override
    public void tick() {
        PolybiusClient.OPTIONS.buddyBold = this.checkBoxBold.isChecked();
        var color = this.inputColor.getText();
        this.colorError = 0;
        if(color.length() == 6){
            try {
                PolybiusClient.OPTIONS.buddyColor = new Color(Integer.parseInt(color, 16));
            } catch (Throwable ignored){
                this.colorError = 1;
            }
        } else {
            this.colorError = 1;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        int y = 0;
        int x = 10;
        if(showPlayers){
            drawTextWithShadow(matrices, this.textRenderer, Text.literal("§fAll of the buddies ("+PolybiusClient.BUDDIES.all().size()+")"), x, 150, 0xFFFFFF);
            for (Buddy buddy : PolybiusClient.BUDDIES.all()) {
                var name = buddy.lastUsername();
                if(buddy.uuid().equals(this.client.player.getUuid())){
                    name = "§6"+name;
                }
                if(PolybiusClient.isImportant(buddy)){
                    name = "§l"+name;
                }
                if(y + 160 + 10 >= this.height){
                    y = 0;
                    x += 150;
                }

                drawTextWithShadow(matrices, this.textRenderer, Text.literal("§7"+name), x, y + 160, 0xFFFFFF);
                y += 10;
            }
        }
        drawTextWithShadow(matrices, this.textRenderer, Text.literal("§7Nametag color of buddies (HEX without the #) - default: #19E619"), 10, 60, -1);
        if(this.colorError == 1){
            drawTextWithShadow(matrices, this.textRenderer, Text.literal("Invalid color!"), 135, 73, 0xEC3223);
        }
        drawTextWithShadow(matrices, this.textRenderer, Text.literal("DISCLAIMER: i'm too lazy to make this screen look actually good"), 10, 10, 1191182335);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(PolybiusClient.KEY_OPTIONS.matchesKey(keyCode, scanCode)){
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
