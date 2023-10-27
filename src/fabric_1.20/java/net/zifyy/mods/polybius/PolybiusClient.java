package net.zifyy.mods.polybius;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.zifyy.mods.polybius.screen.PolybiusOptionsScreen;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class PolybiusClient implements ClientModInitializer {

    public static final BuddiesInstance BUDDIES = new BuddiesInstance();
    public static final PolybiusOptions OPTIONS = new PolybiusOptions();
    public static final KeyBinding KEY_OPTIONS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            // i aint making a lang file
            "Open options", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F12, "Polybius"
    ));

    public static Style style() {
        var style = Style.EMPTY.withColor(OPTIONS.buddyColor().getRGB());
        if(PolybiusClient.OPTIONS.buddyBold){
            style = style.withBold(true);
        }
        return style;
    }

    public static boolean isImportant(Buddy buddy) {
        return buddy.lastUsername().equalsIgnoreCase("Greatyork")
                || buddy.lastUsername().equalsIgnoreCase("AndyRusso");
    }

    public static void onClientClose(MinecraftClient minecraftClient) {
        OPTIONS.save();
    }

    @Override
    public void onInitializeClient() {
        BUDDIES.refresh();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KEY_OPTIONS.wasPressed()) {
                if(client.currentScreen == null){
                    client.setScreen(new PolybiusOptionsScreen());
                }
            }
        });

        OPTIONS.load();
    }

    public static String colorToHex(Color color){
        return Integer.toHexString(color.getRGB()).substring(2);
    }
}
