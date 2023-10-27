package net.zifyy.mods.polybius;

import java.util.UUID;

public record Buddy(String lastUsername, UUID uuid) {

    public static final Buddy NULL = new Buddy("Notch", UUID.randomUUID());

    public static Buddy parse(String string){
        try {
            string = string.trim();
            String name = string.split("=")[0].trim();
            UUID uuid = UUID.fromString(string.split("=")[1].trim());
            return new Buddy(name, uuid);
        } catch (Throwable ignored){}
        return NULL;
    }

    public boolean notNull() {
        return this != NULL;
    }
}
