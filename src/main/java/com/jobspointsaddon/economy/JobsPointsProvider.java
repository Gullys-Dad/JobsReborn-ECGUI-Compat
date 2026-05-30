package com.jobspointsaddon.economy;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobsPlayer;
import com.gamingmesh.jobs.container.PlayerPoints;
import me.gypopo.economyshopgui.api.objects.ExternalEconomy;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Economy provider that bridges Jobs Reborn points with EconomyShopGUI.
 * <p>
 * Registered as EXTERNAL:JobsPointsAddon in shop configs.
 */
public class JobsPointsProvider extends ExternalEconomy {

    /** Immutable holder for display settings to ensure atomic reads. */
    private static class DisplaySettings {
        final String currencyName;
        final String currencyNameSingular;
        final String pricePrefix;

        DisplaySettings(String currencyName, String currencyNameSingular, String pricePrefix) {
            this.currencyName = ChatColor.translateAlternateColorCodes('&', currencyName);
            this.currencyNameSingular = ChatColor.translateAlternateColorCodes('&', currencyNameSingular);
            this.pricePrefix = ChatColor.translateAlternateColorCodes('&', pricePrefix);
        }
    }

    private volatile DisplaySettings settings;

    public JobsPointsProvider(String currencyName, String currencyNameSingular, String pricePrefix) {
        this.settings = new DisplaySettings(currencyName, currencyNameSingular, pricePrefix);
    }

    /** Update display settings (used on config reload). */
    public void setDisplaySettings(String currencyName, String currencyNameSingular, String pricePrefix) {
        this.settings = new DisplaySettings(currencyName, currencyNameSingular, pricePrefix);
    }

    public String getCurrencyDisplayName() {
        return settings.currencyName;
    }

    // ── EconomyShopGUI provider identity ──────────────────────────────────

    @Override
    public String getName() {
        return "JobsPointsAddon";
    }

    @Override
    public String getPlural() {
        DisplaySettings s = settings;
        return s.pricePrefix + "%price% " + s.currencyName;
    }

    @Override
    public String getSingular() {
        DisplaySettings s = settings;
        return s.pricePrefix + "%price% " + s.currencyNameSingular;
    }

    @Override
    public String getFriendly() {
        return settings.currencyName;
    }

    @Override
    public boolean isDecimal() {
        return true;
    }

    // ── Balance operations ────────────────────────────────────────────────

    @Override
    public double getBalance(OfflinePlayer player) {
        if (player == null) return 0.0;
        JobsPlayer jp = getJobsPlayer(player);
        if (jp == null) return 0.0;
        PlayerPoints pts = jp.getPointsData();
        return pts != null ? pts.getCurrentPoints() : 0.0;
    }

    @Override
    public synchronized void depositBalance(OfflinePlayer player, double amount) {
        if (player == null || amount <= 0) return;
        JobsPlayer jp = getJobsPlayer(player);
        if (jp != null) jp.addPoints(amount);
    }

    @Override
    public synchronized void withdrawBalance(OfflinePlayer player, double amount) {
        if (player == null || amount <= 0) return;
        JobsPlayer jp = getJobsPlayer(player);
        if (jp == null) return;
        // Atomic check-and-deduct to prevent TOCTOU double-spend
        PlayerPoints pts = jp.getPointsData();
        double balance = (pts != null) ? pts.getCurrentPoints() : 0.0;
        if (balance >= amount) {
            jp.takePoints(amount);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private JobsPlayer getJobsPlayer(OfflinePlayer player) {
        if (player.isOnline()) {
            return Jobs.getPlayerManager().getJobsPlayer((Player) player);
        }
        // For offline players, use UUID-only lookup (safer than name-based)
        return Jobs.getPlayerManager().getJobsPlayer(player.getUniqueId());
    }
}
