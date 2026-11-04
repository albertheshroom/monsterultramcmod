# Monster Ultra (Fabric mod)

Fabric mod pro **Minecraft 26.3**, který přidává energy drinky Monster Ultra.

Verze: **0.0.2-alpha**

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

Navíc `empty_can` (prázdná plechovka). Vše je ve vlastním creative tabu **Monster Ultra**.

- Všechny plechovky i kbelíky se jmenují **Monster Energy**; příchuť je vidět na textuře a v tooltipu (např. „VR46 Zero Sugar“).
- **Plechovka**: vypije se (jako lektvar), dá 2 jídla + Speed I a Haste I na 30 s, zůstane prázdná plechovka. Stackuje se po 16.
- **Kbelík**: zatím jen item (nejde vylít). Recept na kbelík bude doplněn později.

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

Hotový mod je v `build/libs/monsterultra-0.0.2-alpha.jar` (do složky `mods` spolu s Fabric API).

Spuštění klienta pro testování:

```bash
./gradlew runClient
```
