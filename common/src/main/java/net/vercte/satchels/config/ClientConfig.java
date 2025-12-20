package net.vercte.satchels.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.mojang.logging.LogUtils;
import net.vercte.satchels.platform.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ClientConfig {
    private static ClientConfig INSTANCE = null;

    @Comment("The first stack that the Satchel is placed on. 0 <= x <= 3")
    public int satchelOffset = 0;

    public static ClientConfig get() {
        Objects.requireNonNull(ClientConfig.INSTANCE, "Cannot access config value [" + getConfigFilename() + "] before config is loaded");
        return ClientConfig.INSTANCE;
    }

    public static int getSatchelOffset() {
        return get().satchelOffset;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void load() {
        Jankson jankson = Jankson.builder().build();

        try {
            File configFile = getConfigFile();
            JsonObject configJson = jankson.load(configFile);
            ClientConfig.INSTANCE = jankson.fromJson(configJson, ClientConfig.class);
        } catch (IOException | SyntaxError e) {
            if(e instanceof SyntaxError) {
                LogUtils.getLogger().error("Error loading config [{}}]: ", getConfigFilename());
                e.printStackTrace();
                System.exit(1);
            }

            ClientConfig.INSTANCE = new ClientConfig();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void saveConfig() {
        File configFile = getConfigFile();

        Jankson jankson = Jankson.builder().build();

        ClientConfig clientConfig = get();
        String result = jankson.toJson(clientConfig).toJson(true, true);

        try {
            boolean fileIsUsable = configFile.exists() || configFile.createNewFile();
            if (!fileIsUsable) return;
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getConfigFile() {
        return Services.PLATFORM.getConfigDirectory()
                .resolve(getConfigFilename())
                .toFile();
    }

    public static String getConfigFilename() {
        return "satchels_client.json5";
    }
}
