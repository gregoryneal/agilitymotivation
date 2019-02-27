package net.runelite.client.plugins.agilitymotivation;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Agility Motivation Config")
public interface AgilityMotivationConfig extends Config {
    @ConfigItem(
            keyName = "bigRedJapanTextOverlay",
            name = "BigRedJapan Text Overlay",
            description = "Toggles a BigRedJapan saying at the end of Seers Rooftop Course"
    )
    default boolean bigRedJapanSaying() { return false; }

    @ConfigItem(
            keyName = "bigRedJapanTextOverlayChangeTime",
            name = "BigRedJapan Text Overlay Change Time",
            description = "How long before BigRedJapan changes his message (seconds)."
    )
    default int bigRedJapanChangeTime() { return 5; }

    @ConfigItem(
            keyName = "bigRedJapanTextOverlayRenderDistance",
            name = "BigRedJapan Text Overlay Render Distance",
            description = "The minimum distance at which to render the Big Red Japan agility saying."
    )
    default int bigRedJapanRenderDistance() { return 640; }
}
