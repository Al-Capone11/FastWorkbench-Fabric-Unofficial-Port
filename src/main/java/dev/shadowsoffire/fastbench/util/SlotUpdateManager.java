package dev.shadowsoffire.fastbench.util;

import java.util.IdentityHashMap;
import java.util.Map;
import dev.shadowsoffire.fastbench.FastBench;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.level.Level;

public class SlotUpdateManager {
    private static final Map<CraftingInventoryExt, Runnable> UPDATES = new IdentityHashMap<>();
    private static long serverTicks;
    public static void queueSlotUpdate(Level level, Player player, CraftingInventoryExt inv, ResultContainer result) {
        if (level instanceof ServerLevel sl) UPDATES.putIfAbsent(inv, () -> FastBenchUtil.slotChangedCraftingGrid(sl, player, inv, result));
    }
    public static void tick() {
        if (++serverTicks % FastBench.gridUpdateInterval == 0) { UPDATES.values().forEach(Runnable::run); UPDATES.clear(); }
    }
    public static void clear() { UPDATES.clear(); serverTicks = 0; }
}
