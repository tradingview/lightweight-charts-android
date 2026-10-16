#!/usr/bin/env bash
# Builds a signed bundle of com.tradingview:lightweightcharts that can be uploaded
# manually at https://central.sonatype.com/publishing ("Publish Component").
#
# Usage: scripts/build-maven-central-bundle.sh
# Env:   GPG_KEY_ID   - signing key to use (defaults to gpg's default secret key)
#        ANDROID_HOME - Android SDK (defaults to ~/Library/Android/sdk when there is no local.properties)
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

GROUP_PATH="com/tradingview/lightweightcharts"
VERSION="$(sed -nE 's/^lightweightcharts = "([^"]+)"/\1/p' libs.versions.toml)"
if [ -z "$VERSION" ]; then
    echo "Cannot read 'lightweightcharts' version from libs.versions.toml" >&2
    exit 1
fi

# Maven Central never allows overwriting a released version.
if curl -sfI "https://repo1.maven.org/maven2/$GROUP_PATH/$VERSION/lightweightcharts-$VERSION.pom" >/dev/null; then
    echo "Version $VERSION is already on Maven Central. Bump 'lightweightcharts' in libs.versions.toml first." >&2
    exit 1
fi

if [ -z "${ANDROID_HOME:-}" ] && [ ! -f local.properties ]; then
    export ANDROID_HOME="$HOME/Library/Android/sdk"
fi

echo "==> Building com.tradingview:lightweightcharts:$VERSION"
./gradlew :lightweightlibrary:clean \
    :lightweightlibrary:testDebugUnitTest \
    :lightweightlibrary:publishReleasePublicationToMavenRepository

SRC_DIR="lightweightlibrary/build/repo/$GROUP_PATH/$VERSION"
OUT_DIR="build/maven-central"
STAGE_DIR="$OUT_DIR/staging"
TARGET_DIR="$STAGE_DIR/$GROUP_PATH/$VERSION"
BUNDLE="$OUT_DIR/lightweightcharts-$VERSION-bundle.zip"

rm -rf "$STAGE_DIR" "$BUNDLE"
mkdir -p "$TARGET_DIR"
# Gradle already generates .md5/.sha1/.sha256/.sha512; maven-metadata.xml is owned by Central, so it is not copied.
cp "$SRC_DIR"/* "$TARGET_DIR/"

echo "==> Signing artifacts (gpg will ask for the key passphrase once)"
export GPG_TTY="${GPG_TTY:-$(tty 2>/dev/null || true)}"
for file in "$TARGET_DIR"/*.aar "$TARGET_DIR"/*.jar "$TARGET_DIR"/*.pom "$TARGET_DIR"/*.module; do
    gpg --yes --armor --detach-sign ${GPG_KEY_ID:+--local-user "$GPG_KEY_ID"} "$file"
    gpg --verify "$file.asc" "$file" 2>/dev/null
done

(cd "$STAGE_DIR" && zip -qr "$ROOT_DIR/$BUNDLE" com -x '*.DS_Store')

echo "==> Bundle contents"
unzip -l "$BUNDLE"
echo
echo "Bundle ready: $ROOT_DIR/$BUNDLE"
echo "Upload it at https://central.sonatype.com/publishing -> Publish Component"
