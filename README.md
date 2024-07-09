# Carpet TakeNeko Addition

##### Carpet TakeNeko Addition aka. ~~竹猫卡佩特额滴神~~ TNCA

A [Carpet mod](https://github.com/gnembon/fabric-carpet) (fabric-carpet) extension, a collection of carpet mod style useful tools and interesting features

Under the default configuration conditions, this mod will not change any vanilla behavior at all

Use with carpet mod in the same Minecraft version. Use newer carpet mod versions whenever possible

## Depedencies

|  Dependency   |                   Link                   |
|:-------------:|:----------------------------------------:|
| Fabric-Carpet | https://github.com/gnembon/fabric-carpet |


## Rules

### commandMobSpawn

Modify mob spawn conditions using `mobSpawn` command  
Hope it works
- Type: `String`

- Default Value: `ops`

- Categories: `TNCA` , `commands`


### commandKillFakePlayer

Use regular expressions or string inclusion rules to remove fake players.

- Type: `String`

- Default Value: `ops`

- Categories: `TNCA` , `commands`

### bypassMessageOrderCheck

Bypass order check of chat messages  
This rule can solve the problem of getting kicked while pasting a litematica into world.  
This rule only appears on Minecraft >= 1.19  
Add jvm arguments `-DdoNotBypassMessageOrderCheck=true` can invalidate this rule and solve some mixin issue

- Type: `Boolean`

- Default Value: `false`

- Categories: `TNCA`

### reintruduceCCESuppression

Re-introduce the CCE Suppression (or Shulker Suppression)  
This rule only appears on Minecraft >= 1.21

- Type: `Boolean`

- Default Value: `false`

- Categories: `TNCA` `reintroduce`