package net.zifyy.mods.polybius;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import org.joml.Vector3f;
import oshi.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PolybiusOptions {

    public Color buddyColor = new Color(0.1F, 0.9F, 0.1F, 1.0F);
    public boolean buddyBold = false;

    public File options(){
        var f = new File(MinecraftClient.getInstance().runDirectory, "polybius/options.txt");
        if (!f.exists()) {
            try {
                var dir = new File(MinecraftClient.getInstance().runDirectory, "polybius");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public void load(){
        try {
            String content = FileUtils.readFileToString(this.options(), "utf-8");
            for (String s : content.split("\n")) {
                if(s.startsWith("buddyColor")){
                    this.buddyColor = new Color(Integer.parseInt(s.split("buddyColor=")[1]));
                }
                if(s.startsWith("buddyBold")){
                    this.buddyBold = Boolean.parseBoolean(s.split("buddyBold=")[1]);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void save(){
        try {
            FileUtils.write(this.options(), this.toString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "buddyColor="+this.buddyColor().getRGB()+"\n" +
                "buddyBold="+this.buddyBold;
    }

    public Color buddyColor() {
//        return new Color(this.buddyColor.x, this.buddyColor.y, this.buddyColor.z, 1.0F);
        return this.buddyColor;
    }
}
