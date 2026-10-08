package dev.shadowsoffire.fastbench.net;

import dev.shadowsoffire.fastbench.FastBench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public record RecipePayload(ItemStack output) implements CustomPacketPayload {
    public static final Type<RecipePayload> TYPE = new Type<>(FastBench.loc("recipe"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipePayload> CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC, RecipePayload::output, RecipePayload::new);

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleClient(RecipePayload payload) {
        var screen = Minecraft.getInstance().gui.screen();
        if (screen instanceof dev.shadowsoffire.fastbench.api.ICraftingScreen craftingScreen) {
            updateLastRecipe(craftingScreen.getContainer().getResult(), payload.output());
        } else if (screen instanceof InventoryScreen inventoryScreen) {
            InventoryMenu menu = inventoryScreen.getMenu();
            updateLastRecipe(menu.resultSlots, payload.output());
        }
    }

    private static void updateLastRecipe(ResultContainer result, ItemStack output) {
        result.setItem(0, output);
    }
}

