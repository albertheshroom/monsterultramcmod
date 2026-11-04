# Monster Ultra (Fabric mod)

Fabric mod pro **Minecraft 26.3**, který přidává energy drinky Monster Ultra.

Verze: **0.0.3-alpha**

## Obsah

16 příchutí – každá má **plechovku** (`<id>_can`) a **kbelík** (`<id>_bucket`):

| id | Název |
|---|---|
| `ultra_white` | Ultra White |
| `ultra_strawberry_dreams` | Ultra Strawberry Dreams |
| `ultra_rosa` | Ultra Rosá |
| `ultra_paradise` | Ultra Paradise |
| `ultra_violet` | Ultra Violet |
| `ultra_peachy_keen` | Ultra Peachy Keen |
| `ultra_watermelon` | Ultra Watermelon |
| `ultra_red` | Ultra Red |
| `ultra_blue` | Ultra Blue |
| `ultra_fantasy_ruby_red` | Ultra Fantasy Ruby Red |
| `lando_norris` | Lando Norris |
| `ultra_vice_guava` | Ultra Vice Guava |
| `ultra_fiesta_mango` | Ultra Fiesta Mango |
| `vr46_zero_sugar` | VR46 Zero Sugar |
| `ultra_gold_pineapple` | Ultra Gold Pineapple |
| `ultra_black` | Ultra Black |

Navíc `empty_can` (prázdná plechovka).

### Suroviny
- **Essence** (`<id>_essence`): blue raspberry, cherry, citrus, coldrex, dreamberry, grape, grapefruit, guava, kiwi, mango, peach, pineapple, redberry, rosa, watermelon, yuzu – zatím jen itemy.
- **Sucralose**, **Empty Vial**, **Sucralose Solution**.
- Recept (bez tvaru, jde i v craftingu v inventáři): **lahev s vodou + sucralose + empty vial → sucralose solution**, vrátí prázdnou glass bottle.
  Typ receptu `monsterultra:crafting_shapeless_bottle_remainder` = shapeless recept, který za každý lektvar vrátí prázdnou lahev. Vše je ve vlastním creative tabu **Monster Ultra**.

- Plechovky se jmenují **Monster Energy**, kbelíky **Monster Energy Bucket** (česky „Kbelík Monster Energy“); příchuť je vidět na textuře a v tooltipu (např. „VR46 Zero Sugar“).
- **Plechovka**: vypije se (jako lektvar), dá 2 jídla + Speed I a Haste I na 30 s, zůstane prázdná plechovka. Stackuje se po 16.
- **Kbelík**: zatím jen item (nejde vylít). Vaří se v Cooking Pot z Farmer's Delight (viz níže).

### Chemistry Table (Chemický stůl)

Blok s placeholder texturou. GUI jako brewing stand: nahoře surovina, dole 2 sloty na **empty vial**, vlevo palivo.

- **Palivo:** houby – red/brown mushroom, crimson/warped fungus = 1 vaření; red/brown mushroom block, mushroom stem = 4 vaření
  (item tagy `monsterultra:chemistry_fuel_small` / `chemistry_fuel_large`).
- **Voda:** vaří jen když se některé z 6 stěn dotýká voda (zdrojová i tekoucí, waterlogged blok, kotel s vodou). Jinak GUI ukáže „Chybí voda“.
- **Cukr:** 2 empty vialy → 2× sucralose solution; bez vialů → 1 sukralóza do 1. spodního slotu.
- **Ovoce → 2 vialy essence** (musí být 2 empty vialy). Essence se vybírá tlačítky v GUI:

| Ovoce | Essence |
|---|---|
| Golden apple | Pineapple / Rosá |
| Glow berries | Citrus / Dreamberry |
| Glistering melon slice | Coldrex / Yuzu |
| Melon slice | Watermelon / Mango |
| Apple | Grapefruit / Peach |
| Beetroot | Guava / Cherry |
| Sweet berries | Blue Raspberry / Redberry |
| Chorus fruit | Grape / Kiwi |

Vaření trvá 20 s. Hopper shora vkládá surovinu, z boku vialy a palivo, zespodu odebírá hotové výrobky.

### Recepty (crafting table)

**Empty Vial ×16**
```
.  Rabbit hide  .
Glass pane  .  Glass pane
.  Glass pane  .
```

**Chemistry Table**
```
Iron ingot   Redstone        Iron ingot
Empty vial   Brewing stand   Empty vial
Iron ingot   Cauldron        Iron ingot
```

### Monster Energy Bucket (Cooking Pot z Farmer's Delight)

Vaří se v **Cooking Pot** z Farmer's Delight (nad zdrojem tepla). Nádoba: **kbelík s vodou**.
Vždy: essence + 2× sucralose + 2× cocoa beans + doplňkový item.

| Bucket | Essence | Doplněk |
|---|---|---|
| Ultra White | Citrus | White dye |
| Ultra Strawberry Dreams | Dreamberry | Pink dye |
| Ultra Rosá | Rosá | Rose bush |
| Ultra Paradise | Kiwi | Lime dye |
| Ultra Violet | Grape | Purple dye |
| Ultra Peachy Keen | Peach | Honeycomb |
| Ultra Watermelon | Watermelon | Melon slice |
| Ultra Red | Redberry | Red dye |
| Ultra Blue | Blue Raspberry | Light blue dye |
| Ultra Fantasy Ruby Red | Grapefruit | Magenta dye |
| Lando Norris | Yuzu | Clay ball |
| Ultra Vice Guava | Guava | Nether wart |
| Ultra Fiesta Mango | Mango | Orange dye |
| VR46 Zero Sugar | Coldrex | Yellow dye |
| Ultra Gold Pineapple | Pineapple | Gold nugget |
| Ultra Black | Cherry | Beetroot |

Recepty: `src/main/resources/data/monsterultra/recipe/cooking/`.

### Brewing stand

Kbelík Monsteru nahoru, do 3 spodních slotů **iron nugget** → po 20 s jsou z nuggetů plechovky dané příchutě a nahoře zůstane prázdný kbelík.
**Blaze powder ani žádné palivo není potřeba.**

Recepty jsou datové: `src/main/resources/data/monsterultra/recipe/brewing/<id>_can.json`.
Spuštění bez paliva zajišťuje mixin `BrewingStandBlockEntityMixin`.

## Textury

Textury itemů (16×16 PNG) jsou v následující složce. Pro výměnu stačí nahradit soubor se stejným jménem:

```
src/main/resources/assets/monsterultra/textures/item/
  <id>_can.png       – plechovka (např. ultra_rosa_can.png)
  <id>_bucket.png    – kbelík   (např. ultra_rosa_bucket.png)
  empty_can.png      – prázdná plechovka
```

Ikona modu: `src/main/resources/assets/monsterultra/icon.png`.

## Úprava efektů

Efekty plechovek se nastavují v `src/main/java/cz/albert/monsterultra/MonsterFlavor.java` (metoda `effects()`).

## Vydávání

Vydání na GitHub, CurseForge a Modrinth: viz [docs/vydavani.md](docs/vydavani.md).

## Build

Potřeba **Java 25**.

```bash
./gradlew build
```

Hotový mod je v `build/libs/monsterultra-0.0.3-alpha.jar` (do složky `mods` spolu s Fabric API a Farmer's Delight Refabricated).

Spuštění klienta pro testování:

```bash
./gradlew runClient
```
