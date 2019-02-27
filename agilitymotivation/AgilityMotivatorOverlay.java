package net.runelite.client.plugins.agilitymotivation;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class AgilityMotivatorOverlay extends Overlay {
    private AgilityMotivationPlugin plugin;
    private AgilityMotivationConfig config;
    private Client client;

    @Inject
    public AgilityMotivatorOverlay(Client client, AgilityMotivationPlugin plugin, AgilityMotivationConfig config)
    {
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
        this.plugin = plugin;
        this.config = config;
        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.bigRedJapanSaying() && plugin.shouldDisplayText) {
            NPC brj = plugin.bigredjapan;
            if (brj != null) {
                String s = plugin.getCurrentText();
                graphics.setFont(FontManager.getRunescapeBoldFont());
                /*net.runelite.api.Point textLocation = brj.getCanvasTextLocation(graphics, brj.getName(), brj.getLogicalHeight() + 20);
                if (textLocation != null) {
                    OverlayUtil.renderTextLocation(graphics, textLocation, s, Color.yellow);
                }*/
                Point textLocation = brj.getCanvasTextLocation(graphics, s, brj.getLogicalHeight() + 40);
                if (textLocation != null)
                {
                    OverlayUtil.renderTextLocation(graphics, textLocation, s, Color.yellow);
                }
            }
        }
        return null;
    }
}
