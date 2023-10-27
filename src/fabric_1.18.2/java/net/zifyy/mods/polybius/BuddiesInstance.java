package net.zifyy.mods.polybius;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuddiesInstance {
    private final MinecraftClient minecraft = MinecraftClient.getInstance();
    private final List<Buddy> buddies = new ArrayList<>();
    private static final String URL = "https://raw.githubusercontent.com/ZifyyCZ/polybius/main/data/users.properties";
    private boolean success;

    public BuddiesInstance() {

    }

    public boolean isSuccess(){
        return this.success;
    }

    public boolean refresh(){
        String content;
        this.success = false;
        try {
            java.net.URL url_ = new URL(URL);
            URLConnection con = url_.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            content = IOUtils.toString(in, encoding);
        } catch (Throwable t){
            return false;
        }
        this.buddies.clear();
        for (String s : content.split("\n")) {
            var buddy = Buddy.parse(s);
            if (buddy.notNull()) {
                this.buddies.add(buddy);
            }
        }
        this.success = true;
        if (this.minecraft.player != null) {
            this.minecraft.player.sendMessage(Text.of("§aSuccessfully reload the buddies list!"), false);
        }
        return true;
    }

    public boolean isBuddy(Entity entity) {
        return this.isBuddy(entity.getEntityName(), entity.getUuid());
    }

    public boolean isBuddy(String fallbackName, UUID uuid) {
        for (Buddy buddy : this.buddies) {
            if(buddy.uuid().equals(uuid) || List.of(fallbackName.split(" ")).contains(buddy.lastUsername()))
                return true;
        }
        return false;
    }

    public List<Buddy> all() {
        return this.buddies;
    }
}
