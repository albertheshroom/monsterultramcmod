#!/bin/bash
# SessionStart hook for Claude Code cloud sessions: installs JDK 25 (needed by the
# Fabric 26.3 build) and warms the Gradle / Minecraft caches so `./gradlew build` works.
set -euo pipefail

if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

cd "${CLAUDE_PROJECT_DIR:-.}"

# --- JDK 25 (Temurin), installed once outside the repository -------------------
JDK_DIR="$HOME/.jdks/temurin-25"
if ! "$JDK_DIR/bin/java" -version 2>&1 | grep -q '"25'; then
  rm -rf "$JDK_DIR"
  mkdir -p "$JDK_DIR"
  curl -fsSL "https://api.adoptium.net/v3/binary/latest/25/ga/linux/x64/jdk/hotspot/normal/eclipse" \
    | tar xz -C "$JDK_DIR" --strip-components=1
fi
export JAVA_HOME="$JDK_DIR"
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  echo "export JAVA_HOME=\"$JDK_DIR\"" >> "$CLAUDE_ENV_FILE"
fi

# --- JDK 21 for the NeoForge 1.21.1 build ----------------------------------------
JDK21_DIR="$(ls -d /usr/lib/jvm/java-21-openjdk-* 2>/dev/null | head -1 || true)"
if [ -z "$JDK21_DIR" ]; then
  JDK21_DIR="$HOME/.jdks/temurin-21"
  if ! "$JDK21_DIR/bin/java" -version 2>&1 | grep -q '"21'; then
    rm -rf "$JDK21_DIR"
    mkdir -p "$JDK21_DIR"
    curl -fsSL "https://api.adoptium.net/v3/binary/latest/21/ga/linux/x64/jdk/hotspot/normal/eclipse" \
      | tar xz -C "$JDK21_DIR" --strip-components=1
  fi
fi
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  echo "export JAVA_HOME_21=\"$JDK21_DIR\"" >> "$CLAUDE_ENV_FILE"
fi

# --- JDK 17 for the Forge 1.20.1 build -------------------------------------------
JDK17_DIR="$HOME/.jdks/temurin-17"
if [ -d forge-1.20.1 ] && ! "$JDK17_DIR/bin/java" -version 2>&1 | grep -q '"17'; then
  rm -rf "$JDK17_DIR"
  mkdir -p "$JDK17_DIR"
  curl -fsSL "https://api.adoptium.net/v3/binary/latest/17/ga/linux/x64/jdk/hotspot/normal/eclipse" \
    | tar xz -C "$JDK17_DIR" --strip-components=1
fi
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  echo "export JAVA_HOME_17=\"$JDK17_DIR\"" >> "$CLAUDE_ENV_FILE"
fi

# --- Gradle wrapper, Fabric Loom, Minecraft and mod dependencies ----------------
# Re-run only when a file that decides what gets downloaded has changed.
hashes="$HOME/.gradle/.monsterultra-session-start.sha256"
if ! sha256sum --check --status "$hashes" 2>/dev/null; then
  rm -f "$hashes"
  ./gradlew --no-daemon compileJava
  mkdir -p "$(dirname "$hashes")"
  sha256sum .claude/hooks/session-start.sh build.gradle settings.gradle gradle.properties \
    gradle/wrapper/gradle-wrapper.properties > "$hashes"
fi

# --- NeoForge 1.21.1: ModDevGradle, decompiled Minecraft, Farmer's Delight -------
neo_hashes="$HOME/.gradle/.monsterultra-neoforge-session-start.sha256"
if [ -d neoforge-1.21.1 ] && ! sha256sum --check --status "$neo_hashes" 2>/dev/null; then
  rm -f "$neo_hashes"
  JAVA_HOME="$JDK21_DIR" ./gradlew -p neoforge-1.21.1 --no-daemon compileJava
  sha256sum .claude/hooks/session-start.sh neoforge-1.21.1/build.gradle neoforge-1.21.1/settings.gradle \
    neoforge-1.21.1/gradle.properties gradle/wrapper/gradle-wrapper.properties > "$neo_hashes"
fi

# --- Forge 1.20.1: ModDevGradle legacy, decompiled Minecraft, Farmer's Delight ---
forge_hashes="$HOME/.gradle/.monsterultra-forge-session-start.sha256"
if [ -d forge-1.20.1 ] && ! sha256sum --check --status "$forge_hashes" 2>/dev/null; then
  rm -f "$forge_hashes"
  JAVA_HOME="$JDK21_DIR" ./gradlew -p forge-1.20.1 --no-daemon \
    -Porg.gradle.java.installations.paths="$JDK17_DIR" compileJava
  sha256sum .claude/hooks/session-start.sh forge-1.20.1/build.gradle forge-1.20.1/settings.gradle \
    forge-1.20.1/gradle.properties gradle/wrapper/gradle-wrapper.properties > "$forge_hashes"
fi
