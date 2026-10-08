package dev.shadowsoffire.fastbench.api;

import net.minecraft.world.inventory.ResultContainer;

/** Extension point for custom crafting menus supported by FastWorkbench. */
public interface ICraftingContainer {
    ResultContainer getResult();
}
