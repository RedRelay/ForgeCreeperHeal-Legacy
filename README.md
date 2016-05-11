# Forge Creeper Heal

An open source mod for Minecraft Forge to auto-heal world after an explosion inspired by the famous bukkit pluggin [CreeperHeal](http://dev.bukkit.org/server-mods/creeperheal-nitnelave/) from [Nitnelave](https://github.com/nitnelave).

## Overview

[![Overview video](http://img.youtube.com/vi/KBzI7iXmbx0/0.jpg)](http://www.youtube.com/watch?v=KBzI7iXmbx0)
[![1.1.0 video](http://img.youtube.com/vi/3M5EytpMjP4/0.jpg)](http://www.youtube.com/watch?v=3M5EytpMjP4)

Clients don't need Forge Creeper Heal installed to join your server BUT they need have Forge.

## Customizable Settings

See : https://github.com/EyZox/ForgeCreeperHeal/wiki/Customizable-Settings
**Warn : implemented in 2.0 but wiki not updated yet for 2.0**

## Contribute/Compilation Set Up

* Used libs :
  * [DependencyGraph](https://github.com/EyZox/DependencyGraph)
  * [TickTimeline](https://github.com/EyZox/TickTimeline)

## Planned improvements

* Config
  * « fromEntityException » : Choose entities by name not class.
  * Change config by command
  * Change config by GUI
  * Get a per world configuration

* Heal
  * Replace grass by dirt (*)
  * Replace stone by cobblestone (*)
  * Replace bricks by used bricks (*)
  * New heal algorithms (*)

* Performances **Warn : removed in 2.0**
  * ~~Add a profiler to know mod consumption~~ Done in 1.1.0

(*) = Customizable
Feel you free to suggest some improvements.

## Known bugs :

### 2.0 :

* Banner becomes black when healed
* Bugs/Glitches when handle massive explosion (for normal use, you shouldn't have these issues)

### 1.X
* Doors
* Somes blocks like crops or redstone

