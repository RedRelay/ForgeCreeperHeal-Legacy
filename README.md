# Forge Creeper Heal

An open source mod for Minecraft Forge to auto-heal world after an explosion inspired by the famous bukkit pluggin [CreeperHeal](http://dev.bukkit.org/server-mods/creeperheal-nitnelave/) from [Nitnelave](https://github.com/nitnelave).

## Overview

[![Overview video](http://img.youtube.com/vi/KBzI7iXmbx0/0.jpg)](http://www.youtube.com/watch?v=KBzI7iXmbx0)

Clients don't need Forge Creeper Heal installed to join your server BUT they need have Forge.

## Customizable Settings

Config file is in your game directory as forgecreeperheal.cfg .
This is JSON so you have to know how write JSON correctly to cusomize settings. See https://wikipedia.org/wiki/JSON .

Setting | Default value | Description
------- | ------------- | -----------
minimumTicksBeforeHeal | 6000 | Minimum ticks before begin healing terrain
randomTickVar | 12000 | For each block a random number beetween 0 and this value is added to the minimum ticks setting to fix time before heal
override | false | If true and a block was placed at the location before healing, the broken block override it
overrideFluid | true | See override but for fluids (included flowing fluids)
dropItemsFromContainer | true | If true, items in a broken block are droped else nothing is droped but block will be heal with its content
dropIfAlreadyBlock | false | If true, a block wich cannot be healed or an overrided block, and their contents are droped
removeException | minecraft:tnt | Blocks which don't be removed by the explosion
healException | minecraft:tnt | Blocks which don't be healed
fromEntityException | [] | Entity classes ignored by the mod

About « removeException » :
While explosion, Forge Creeper Heal removes all affected blocks not contained in this list. Removed blocks don't take explosion effect, so no items droped for basic blocks and for TNT its doesn't activate. So if you remove TNT from this list you will not have chain reaction anymore. 

About « fromEntityException » :
Arguments is Java cannonical class name, so for Creeper this is net.minecraft.entity.monster.EntityCreeper.

## Planned improvement

* Config
  * « fromEntityException » : Choose entities by name not class.
  * ~~Reload config without relaunch Minecraft~~ Done in 1.1.0
  * Change config by command
  * Change config by GUI
  * Get a per world configuration

* Heal
  * Replace grass by dirt (*)
  * Replace stone by cobblestone (*)
  * Replace bricks by used bricks (*)
  * New heal algorithm (*)

* Performances
  * ~~Add a profiler to know mod consumption~~ Done in 1.1.0

(*) = Customizable
Feel you free to suggest some improvements.

