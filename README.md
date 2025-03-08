# Riamphur

A Minecraft mod that provides various utility items and code for other mods to build upon.

## Features

* **Entity souls** that can be acquired by killing mobs with the **Soul Extractor item** or the **Soul Reaper enchantment**.

> [!IMPORTANT]
> To craft soul bundles, you need to have the vanilla bundle item recipe enabled. This is because, until version `1.21.2`, Minecraft does not have a built-in recipe for crafting the bundle item. You can install another mod or datapack that adds the recipe as a workaround.

## For developers

If you want to use this mod as a dependency in your own mod, you can add it to your `build.gradle` file like so:

```groovy
// Make sure to add the maven modrinth repository
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}
dependencies {
    modImplementation "maven.modrinth:riamphur:%VERSION_HERE%"
}
```

Source: https://support.modrinth.com/en/articles/8801191-modrinth-maven

**OR if maven is not your taste:**

- Get the latest version `.jar`
- Put it in your `libs` folder
```groovy
dependencies {
    implementation files('libs/riamphur-%VERSION_HERE%.jar')
}
```

### What you can tamper with

* [EntitySoulAcceptor](./src/main/java/dev/oxydien/riamphur/interfaces/EntitySoulAcceptor.java)
  * Which get's activated when player interacts with an entity using the [Soul Bundle](./src/main/java/dev/oxydien/riamphur/items/SoulBundle.java)
* [SoulAccessor](./src/main/java/dev/oxydien/riamphur/interfaces/SoulAccessor.java)
  * Which can override the [Soul Type](./src/main/java/dev/oxydien/riamphur/enums/SoulType.java) of entity. Setting the `SoulType` to `NONE` will result in no soul being dropped.
