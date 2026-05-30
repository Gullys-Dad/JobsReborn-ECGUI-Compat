# install-libs.ps1
# Installs the local JAR dependencies into your local Maven repository.
# Run this ONCE before building, or whenever you update the JARs in libs/.
#
# Usage: .\install-libs.ps1

$ErrorActionPreference = "Stop"
$libsDir = Join-Path $PSScriptRoot "libs"

Write-Host "Installing EconomyShopGUI.jar into local Maven repo..."
mvn install:install-file "-Dfile=$libsDir\EconomyShopGUI.jar" "-DgroupId=me.gypopo" "-DartifactId=economyshopgui" "-Dversion=1.0" "-Dpackaging=jar" "-DgeneratePom=true"

Write-Host "Installing Jobs.jar into local Maven repo..."
mvn install:install-file "-Dfile=$libsDir\Jobs.jar" "-DgroupId=com.gamingmesh" "-DartifactId=jobs" "-Dversion=1.0" "-Dpackaging=jar" "-DgeneratePom=true"

Write-Host "Done! You can now build with: mvn clean package"
