package net.runelite.client.plugins.agilitymotivation;

import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@PluginDescriptor(
        name = "Agility Motivation",
        description = "Enable motivation by BigRedJapan on Seers Rooftop course",
        tags = {"panel"}
)
public class AgilityMotivationPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private AgilityMotivatorOverlay overlay;
    @Inject
    private AgilityMotivationConfig config;

    @Provides
    AgilityMotivationConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(AgilityMotivationConfig.class);
    }

    private ArrayList<String> sayings = new ArrayList<>(Arrays.asList("It takes courage to grow up and become who you really are.","You have brains in your head. You have feet in your shoes. You can steer yourself any direction you choose. You're on your own. And you know what you know. And you are the guy who'll decide where to go.", "To bring about change, you must not be afraid to take the first step. We will fail when we fail to try.", "If you don't like the road you're walking, start paving another one.", "Hard things will happen to us. We will recover. We will learn from it. We will grow more resilient because of it."));
    public NPC bigredjapan = null;
    private int changeTextSeconds = 5;
    private LocalDateTime lastTextChange;
    private String currentSaying = null;
    public boolean shouldDisplayText = false;

    @Override
    protected void startUp() throws Exception {
        super.startUp();

        overlayManager.add(overlay);

        this.changeTextSeconds = config.bigRedJapanChangeTime();
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        updateConfig();
    }

    private void updateConfig() {
        this.changeTextSeconds = config.bigRedJapanChangeTime();
    }

    @Subscribe
    public void onGameTick(GameTick e) {
        //Check if big red japan is nearby
        if (bigredjapan != null) {
            int dist = client.getLocalPlayer().getLocalLocation().distanceTo(bigredjapan.getLocalLocation());
            if (dist <= config.bigRedJapanRenderDistance()) {
                if (!shouldDisplayText) shouldDisplayText = true;
            } else {
                if (shouldDisplayText) shouldDisplayText = false;
            }
        } else {
            //reset values
            if (shouldDisplayText) shouldDisplayText = false;
        }
    }

    @Subscribe
    public void onNpcSpawned(NpcSpawned e) {
        if (e.getNpc().getId() == NpcID.BIGREDJAPAN) {
            this.bigredjapan = e.getNpc();
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned e) {
        if (e.getNpc().getId() == NpcID.BIGREDJAPAN) {
            this.bigredjapan = null;
        }
    }

    public String getCurrentText() {
        if (!shouldDisplayText) return null;
        LocalDateTime curr = LocalDateTime.now();

        if (currentSaying == null) {
            currentSaying = randomSaying();
            lastTextChange = curr;
        }
        if (ChronoUnit.SECONDS.between(lastTextChange, curr) >= changeTextSeconds) {
            currentSaying = randomSaying();
            lastTextChange = curr;
        }

        return currentSaying;
    }

    private String randomSaying() {
        int max = sayings.size();
        Random r = new Random();
        int i = r.nextInt(max);
        String s = sayings.get(i);
        while (s == currentSaying) {
            i = r.nextInt(max);
            s = sayings.get(i);
        }
        return s;
    }
}
