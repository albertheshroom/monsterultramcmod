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
