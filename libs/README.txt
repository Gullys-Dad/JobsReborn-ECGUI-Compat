REQUIRED: Place plugin JARs here for compilation

To build this plugin, you need to manually add these plugin JAR files to this directory:

1. Jobs.jar
   - Download Jobs Reborn from: https://www.spigotmc.org/resources/jobs-reborn.4216/
   - Rename the downloaded file to: Jobs.jar
   - Place it in this libs/ folder

2. EconomyShopGUI.jar
   - Download EconomyShopGUI from: https://www.spigotmc.org/resources/economyshopgui.69927/
   - OR EconomyShopGUI Premium: https://www.spigotmc.org/resources/economyshopgui-premium.90544/
   - Rename the downloaded file to: EconomyShopGUI.jar
   - Place it in this libs/ folder

After adding both JAR files, run the install script to register them with Maven:

    .\install-libs.ps1

Then build with: mvn clean package

You only need to run the install script once (or again if you update the JARs).

Note: These JAR files are needed at compile-time only (marked as "provided" scope).
They won't be included in the final plugin JAR. Your server must have both
Jobs Reborn and EconomyShopGUI installed separately for this plugin to work.
