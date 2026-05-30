# Jobs Points Addon

A Paper plugin that bridges **Jobs Reborn** job points with **EconomyShopGUI**, allowing players to spend and earn job points through shops.

## Requirements

- **Paper** 1.20+
- **Java** 17+
- **Jobs Reborn** 5.0+
- **EconomyShopGUI** (Free or Premium) 3.9+

## Installation

1. Place `JobsPointsAddon.jar` in your server's `plugins` folder.
2. Ensure **Jobs Reborn** and **EconomyShopGUI** are installed.
3. Restart your server.
4. (Optional) Edit `plugins/JobsPointsAddon/config.yml` — see below.

## Configuration

`plugins/JobsPointsAddon/config.yml`:

```yaml
# Color/formatting prefix placed BEFORE the price number in shop lore.
price-prefix: "&6"

# The display name for the currency (plural / singular).
currency-name: "Job Points"
currency-name-singular: "Job Point"

# Enable debug logging to console.
debug-mode: false
```

## Setting up Shops

In your EconomyShopGUI shop `.yml` files, set the economy to `EXTERNAL:JobsPointsAddon` at the section or item level:

```yaml
# All items in this section use Job Points
jobs_shop:
  economy: EXTERNAL:JobsPointsAddon
  page1:
    items:
      GOLDEN_CARROT_1_2:
        material: GOLDEN_CARROT
        buy: 10

# Or override a single item's economy
mixed_shop:
  page1:
    items:
      DIAMOND_1_3:
        material: DIAMOND
        economy: EXTERNAL:JobsPointsAddon
        buy: 100
        sell: 50
```

See `examples/economyshopgui-example.yml` for more details.

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/jobspoints reload` | Reload the plugin config | `jobspoints.admin` (default: op) |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `jobspoints.admin` | Reload the plugin config | Operators |

## API Usage

For developers wanting to integrate with this plugin:

```java
JobsPointsAddon plugin = (JobsPointsAddon) Bukkit.getPluginManager().getPlugin("JobsPointsAddon");
JobsPointsProvider provider = plugin.getEconomyProvider();

// Get balance
double balance = provider.getBalance(player);

// Withdraw points (void — no-op if balance is insufficient)
provider.withdrawBalance(player, 100.0);

// Deposit points
provider.depositBalance(player, 50.0);
```

## Building from Source

### Requirements

- Java 17+
- Maven 3.6+

### Steps

1. Place the **EconomyShopGUI** and **Jobs Reborn** JARs into the `libs/` folder.
2. Run the install script to register them in your local Maven repo:
   ```powershell
   ./install-libs.ps1
   ```
3. Build:
   ```bash
   mvn clean package
   ```

The compiled JAR will be in the `target/` folder.

## Support

If you encounter issues:

1. Check that all dependencies are installed and up-to-date
2. Verify your configuration is correct
3. Enable `debug-mode` in config.yml for detailed logging
4. Check console for error messages

## License

This plugin is provided as-is for use with Minecraft servers running Jobs Reborn and EconomyShopGUI.

## Credits

- **Jobs Reborn**: [Zrips](https://www.zrips.net/jobs/)
- **EconomyShopGUI**: [Gypopo](https://wiki.gpplugins.com/economyshopgui/)
