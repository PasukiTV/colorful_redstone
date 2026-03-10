# Colorful Redstone

**Color channels for redstone logic on Minecraft 1.21.1.**

Colorful Redstone adds full colored variants of redstone components so you can route multiple signal networks side by side without cross-interference.

## Supported Versions
- Minecraft: **1.21.1**
- Loaders: **Fabric** and **NeoForge**

## What This Mod Adds
- Colored Dust
- Colored Block of Redstone
- Colored Torch
- Colored Repeater
- Colored Comparator

Vanilla redstone (red) stays vanilla.  
Colored systems are meant to feel like native redstone behavior, but split into independent color channels.

## Gameplay Behavior
- Same color connects and transmits together.
- Different colors do not merge into one shared line.
- Repeaters and comparators work in their matching color network.
- Vanilla redstone remains its own channel.

This makes compact, parallel logic builds much easier and cleaner.

## Crafting
All recipes follow vanilla-style progression, but with colored ingredients/results.

### Colored Dust (8x)
`r = redstone dust`, `x = dye`
```text
rrr
rxr
rrr
```

### Colored Dust -> Redstone Dust
`1 colored dust -> 1 redstone dust`

### Block of Colored Redstone
`x = matching colored dust`
```text
xxx
xxx
xxx
```

### Block -> Dust
`1 colored block -> 9 matching colored dust`

### Colored Torch
`x = matching colored dust`, `s = stick`
```text
x
s
```

### Colored Repeater
Vanilla repeater layout with colored torches as the torch ingredient.

### Colored Comparator
Vanilla comparator layout with colored torches as the torch ingredient.

## JEI
JEI integration is included for recipe lookup and fast browsing in-game.

## Installation
1. Install Minecraft 1.21.1
2. Install Fabric Loader or NeoForge
3. Install Architectury API
4. Place `Colorful Redstone` in your `mods` folder
5. (Recommended) Install JEI

## Modpack Policy
You may include Colorful Redstone in modpacks.

## Issue Reporting
If you find a bug, include:
- Minecraft version
- Loader + loader version
- Mod version
- Steps to reproduce
- Log/crash report (if available)
