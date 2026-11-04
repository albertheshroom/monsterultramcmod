# Vydávání (pro správce)

## Verze
- Jediný zdroj verze pro build je `version` v `gradle.properties`; při vydání ji workflow přepíše verzí z tagu
  (`v0.0.2-alpha` → `0.0.2-alpha`).
- Verze s `beta` nebo `rc` (např. `0.1.0-beta`) se vydávají jako pre-release / *beta*, ostatní verze 0.x a verze s pomlčkou jako pre-release / *alpha*, zbytek jako plné vydání.
- Poznámky k vydání jsou v `.github/release-notes/<tag>.md` (např. `v0.0.2-alpha.md`).

## Jak vydat verzi
1. Ve výchozí větvi: `version` v `gradle.properties` = nová verze a nový soubor `.github/release-notes/vX.Y.Z.md`.
2. Vydání jedním ze dvou způsobů:
   - *Actions → Release → Run workflow*, tag `vX.Y.Z` (cíl nech prázdný = výchozí větev): workflow tag vytvoří sám;
   - nebo lokálně `git tag vX.Y.Z && git push origin vX.Y.Z`.
3. Workflow **Release** sestaví jar, vytvoří GitHub Release s poznámkami a pokud je nastavené publikování,
   nahraje jar na CurseForge a Modrinth.
4. Už existující tag jde vydat znovu stejně ručně (cíl se pak ignoruje).

## Publikování na CurseForge a Modrinth
Každé vydání se samo nahraje na CurseForge a Modrinth, jakmile jsou nastavené tokeny a ID projektů.
Platí to pro pushnutý tag i pro ruční *Run workflow*: volba **Nahrát na CurseForge a Modrinth** je
zaškrtnutá předem. Bez nastavení se krok přeskočí. Stačí nastavit jednu platformu, druhá se přeskočí.

### 1. Založit projekty
Text stránky je připravený v [project-page.md](project-page.md) (anglicky). Ikona je
`src/main/resources/assets/monsterultra/icon.png`.

| Pole | CurseForge | Modrinth |
|---|---|---|
| Název | Monster Ultra | Monster Ultra |
| Krátký popis | Monster Ultra energy drinks: 16 flavors in cans and buckets, brewed in the brewing stand. | stejný |
| Typ | Mod (Minecraft) | Mod |
| Kategorie | Food | Food |
| Loader, verze | Fabric, 26.3 | Fabric, 26.3 |
| Strana | — | Client and server (required on both) |
| Licence | All Rights Reserved | All Rights Reserved (ARR) |
| Závislosti | Fabric API, Farmer's Delight Refabricated (required) | Fabric API, Farmer's Delight Refabricated (required) |

- CurseForge: <https://authors.curseforge.com/#/projects/create>. Projekt projde ručním schválením,
  obvykle do pár dní. Soubory lze nahrávat až po schválení.
- Modrinth: <https://modrinth.com/dashboard/projects> → *Create a project*. Verze lze nahrávat hned;
  projekt se zveřejní po kontrole moderátory (*Submit for review*).

### 2. Vytvořit tokeny
- CurseForge: <https://authors.curseforge.com/account/api-tokens> → *Generate token*.
- Modrinth: <https://modrinth.com/settings/pats> → *Create a PAT* s oprávněními *Create versions*,
  *Read projects* a *Write projects*.

Tokeny nikam nekopíruj, jen do GitHubu (další krok).

### 3. Nastavit GitHub
V repozitáři *Settings → Secrets and variables → Actions*:
- záložka **Secrets**: `CURSEFORGE_TOKEN`, `MODRINTH_TOKEN`;
- záložka **Variables**:
  - `CURSEFORGE_PROJECT_ID`: číslo z pravého sloupce stránky projektu, *Project ID*;
  - `MODRINTH_PROJECT_ID`: ID ze *Settings* projektu, nebo slug.

### 4. Ověřit nastavení
*Actions → Publish check → Run workflow*. Workflow nic nenahrává, jen zkontroluje:
- že tokeny platí;
- že ID projektu má správný tvar;
- že token na Modrinthu patří členovi projektu.

Výsledek je v souhrnu běhu: ✅, ❌ s důvodem, nebo „přeskočeno“ pro nenastavenou platformu.

### 5. První nahrání
*Actions → Release → Run workflow*, tag `v0.0.1-alpha`. Workflow existující release jen obnoví a jar
nahraje na obě platformy jako *alpha* se závislostí na Fabric API a Farmer's Delight Refabricated.

Každé další vydání se pak nahraje samo.
