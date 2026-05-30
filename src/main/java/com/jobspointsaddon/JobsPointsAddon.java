package com.jobspointsaddon;

import com.jobspointsaddon.economy.EconomyHook;
import com.jobspointsaddon.economy.JobsPointsProvider;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Bridges Jobs Reborn points with EconomyShopGUI.
 * <p>
 * Players can spend / earn job points through EconomyShopGUI shops
 * by setting {@code economy: EXTERNAL:JobsPointsAddon} on shop items.
 */
public class JobsPointsAddon extends JavaPlugin implements TabCompleter {

    private JobsPointsProvider economyProvider;
    private boolean debugMode;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Verify EconomyShopGUI-Premium is present (external economies are premium-only)
        if (getServer().getPluginManager().getPlugin("EconomyShopGUI-Premium") == null) {
            getLogger().severe("EconomyShopGUI-Premium is required but not found! Disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load config
        debugMode = getConfig().getBoolean("debug-mode", false);
        String currencyName = getConfig().getString("currency-name", "Job Points");
        String currencyNameSingular = getConfig().getString("currency-name-singular", "Job Point");
        String pricePrefix = getConfig().getString("price-prefix", "&6");
        economyProvider = new JobsPointsProvider(currencyName, currencyNameSingular, pricePrefix);

        // EconomyHook is a separate class so EconomyShopGUI classes are only
        // loaded here (in onEnable) — not when the JVM loads this main class.
        EconomyHook hook = new EconomyHook(economyProvider, getLogger(), debugMode);
        getServer().getPluginManager().registerEvents(hook, this);
        hook.register();

        getLogger().info("JobsPointsAddon enabled — use economy: EXTERNAL:JobsPointsAddon in shop configs.");
    }

    @Override
    public void onDisable() {
        economyProvider = null;
        getLogger().info("JobsPointsAddon disabled.");
    }

    // ── /jobspoints reload ─────────────────────────────────────────────────

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("jobspoints.admin")) {
                sender.sendMessage(ChatColor.RED + "No permission.");
                return true;
            }
            reload();
            sender.sendMessage(ChatColor.GREEN + "JobsPointsAddon config reloaded.");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Usage: /jobspoints reload");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission("jobspoints.admin")) {
            List<String> completions = new ArrayList<>();
            if ("reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
            return completions;
        }
        return Collections.emptyList();
    }

    // ── Reload ────────────────────────────────────────────────────────────

    /** Reload config and update the provider's settings. */
    public void reload() {
        reloadConfig();
        debugMode = getConfig().getBoolean("debug-mode", false);
        String currencyName = getConfig().getString("currency-name", "Job Points");
        String currencyNameSingular = getConfig().getString("currency-name-singular", "Job Point");
        String pricePrefix = getConfig().getString("price-prefix", "&6");
        economyProvider.setDisplaySettings(currencyName, currencyNameSingular, pricePrefix);
        getLogger().info("Configuration reloaded.");
    }

    // ── Accessors ─────────────────────────────────────────────────────────

    public JobsPointsProvider getEconomyProvider() {
        return economyProvider;
    }
}
