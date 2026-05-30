package com.jobspointsaddon.economy;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.api.events.EconomyPreLoadEvent;
import me.gypopo.economyshopgui.api.objects.ExternalEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * Handles EconomyShopGUI integration — kept in a separate class so that
 * ExternalEconomy / EconomyPreLoadEvent are only loaded when this class
 * is explicitly instantiated (in onEnable, after EconomyShopGUI is available).
 */
public class EconomyHook implements Listener {

    private final JobsPointsProvider provider;
    private final Logger logger;
    private final boolean debugMode;

    public EconomyHook(JobsPointsProvider provider, Logger logger, boolean debugMode) {
        this.provider = provider;
        this.logger = logger;
        this.debugMode = debugMode;
    }

    /** Called on /sreload — re-register with EconomyShopGUI. */
    @EventHandler
    public void onEconomyPreLoad(EconomyPreLoadEvent event) {
        event.registerExternal(provider);
        if (debugMode)
            logger.info("Re-registered via EconomyPreLoadEvent (shop reload).");
    }

    /** Initial registration with EconomyShopGUI. */
    public void register() {
        try {
            EconomyShopGUI.getInstance().getEcoHandler().registerExternalProvider(provider);
            if (debugMode)
                logger.info("Registered provider with EconomyShopGUI.");
        } catch (Exception e) {
            logger.warning("Provider registration failed: " + e.getMessage());
            if (debugMode)
                e.printStackTrace();
        }
    }
}
