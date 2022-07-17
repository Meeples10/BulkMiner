# BulkMiner

A plugin that allows players to mine an entire vein of ore at once, inspired by the mod [VeinMiner](https://www.curseforge.com/minecraft/mc-mods/veinminer).

## Commands

|Command|Description|Permission|
|-------|-----------|----------|
|`/bulkminer`|Allows players to toggle the effects of the plugin for themselves.|`bulkminer.use`|

## Configuration

The default configuration file can be found [here](https://github.com/Meeples10/BulkMiner/blob/master/src/main/resources/config.yml).

|Key|Description|
|---|-----------|
|`enabled-by-default`|If this is set to false, players will have to use `/bulkminer` before the plugin takes effect.|
|`survival-only`|If this is true, the plugin will not have any effect when the player is in a gamemode other than survival.|
|`drop-xp`|If this is false, blocks broken with the plugin will not drop experience, even if they would when broken normally.|
|`max-distance`|Blocks farther than this distance from the block broken by the player will be ignored. Setting this to large numbers is not recommended.|
|`message-enabled`|The message shown to the player when they enable the plugin.|
|`message-disabled`|The message shown to the player when they disable the plugin.|
|`message-denied`|The message shown to the player if they do not have permission to use a command.|
|`enabled-blocks`|Blocks listed here may be broken en masse using BulkMiner. [See here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) for a list of supported block IDs. Generally, these IDs match Minecraft's block IDs, but in uppercase.|
|`enabled-tools`|The plugin will only be triggered if the player is holding one of these items when breaking an enabled block. [See here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) for a list of supported item IDs. Generally, these IDs match Minecraft's item IDs, but in uppercase.|
