package com.radimous.newtrinketchancetooltip;

import iskallia.vault.client.ClientDiscoveredEntriesData;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.TrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Mod.EventBusSubscriber(modid = "newtrinketchancetooltip", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)

public class TrinketTooltip {

    /**
     * Calculates chance to discover new trinket and adds it to unidentified trinket tooltip
     * this calculation is based on {@link TrinketItem#tickRoll(ItemStack, Player)}
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent toolTipEvent) {
        ItemStack itemStack = toolTipEvent.getItemStack();
        Item item = itemStack.getItem();
        if (item instanceof TrinketItem) {
            List<Component> tooltip = toolTipEvent.getToolTip();
            if (!TrinketItem.isIdentified(itemStack)) {
                Set<ResourceLocation> discoveredSet = ClientDiscoveredEntriesData.Trinkets.getDiscoveredTrinkets();

                int totalWeight = 0;
                int undiscoveredWeight = 0;
                int discoveredCount = 0;
                int totalCount = 0;

                for (ResourceLocation trinket : ModConfigs.TRINKET.TRINKETS.keySet()) {
                    int weight = ModConfigs.TRINKET.TRINKETS.get(trinket).getWeight();
                    if (discoveredSet.contains(trinket)) {
                        discoveredCount++;
                        totalWeight += Math.ceil(weight * 0.16666667F);
                    } else {
                        undiscoveredWeight += weight;
                        totalWeight += weight;
                    }
                    totalCount++;
                }
                double chance = (double) undiscoveredWeight / totalWeight;
                if (totalCount != discoveredCount) {
                    tooltip.add(new TextComponent("Chance to discover new trinket: " + String.format(Locale.ROOT, "%.2f", chance * 100) + "% ").withStyle(ChatFormatting.GOLD));
                    tooltip.add(new TextComponent(discoveredCount + "/" + totalCount + " discovered").withStyle(ChatFormatting.GOLD));
                }
            }
        }
    }
}


