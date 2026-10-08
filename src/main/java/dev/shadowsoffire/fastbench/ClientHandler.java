package dev.shadowsoffire.fastbench;

import java.util.concurrent.ThreadLocalRandom;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import dev.shadowsoffire.fastbench.net.RecipePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class ClientHandler implements ClientModInitializer {
    private static final int TOOLTIP_INDEX = ThreadLocalRandom.current().nextInt(7);

    @Override public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RecipePayload.TYPE, (payload, context) -> RecipePayload.handleClient(payload));
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (FastBench.removeBookButton && screen instanceof CraftingScreen) {
                screen.children().stream().filter(ImageButton.class::isInstance).map(ImageButton.class::cast)
                    .filter(button -> button.sprites == RecipeBookComponent.RECIPE_BUTTON_SPRITES)
                    .forEach(button -> button.visible = false);
            }
        });
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (!FastBench.disableToolTip && stack.is(Items.CRAFTING_TABLE)) {
                lines.add(Component.translatable("info.fb.very_fast" + TOOLTIP_INDEX).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        });
    }
}

