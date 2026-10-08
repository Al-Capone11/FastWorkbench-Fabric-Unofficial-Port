package dev.shadowsoffire.fastbench;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import dev.shadowsoffire.fastbench.net.RecipePayload;
import net.minecraft.resources.Identifier;
import dev.shadowsoffire.fastbench.util.SlotUpdateManager;

public class FastBench implements ModInitializer {
    public static final String MODID = "fastbench";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static boolean removeBookButton = true;
    public static boolean disableToolTip = false;
    public static int gridUpdateInterval = 1;

    @Override public void onInitialize() {
        loadConfig();
        PayloadTypeRegistry.clientboundPlay().register(RecipePayload.TYPE, RecipePayload.CODEC);
        ServerTickEvents.END_SERVER_TICK.register(server -> SlotUpdateManager.tick());
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> SlotUpdateManager.clear());
    }

    private static void loadConfig() {
        Path path = Path.of("config", MODID + ".properties");
        Properties p = new Properties();
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) try (var in = Files.newInputStream(path)) { p.load(in); }
            removeBookButton = Boolean.parseBoolean(p.getProperty("removeBookButton", "true"));
            disableToolTip = Boolean.parseBoolean(p.getProperty("disableTooltip", "false"));
            gridUpdateInterval = Math.max(1, Math.min(100, Integer.parseInt(p.getProperty("gridUpdateInterval", "1"))));
            p.setProperty("removeBookButton", Boolean.toString(removeBookButton));
            p.setProperty("disableTooltip", Boolean.toString(disableToolTip));
            p.setProperty("gridUpdateInterval", Integer.toString(gridUpdateInterval));
            try (var out = Files.newOutputStream(path)) { p.store(out, "FastWorkbench configuration"); }
        } catch (IOException | NumberFormatException e) { LOGGER.error("Could not load FastWorkbench configuration", e); }
    }

    public static Identifier loc(String string) { return Identifier.fromNamespaceAndPath(MODID, string); }
}

