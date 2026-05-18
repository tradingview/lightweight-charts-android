#!/usr/bin/env python3
"""Inspect local lightweight-charts Android wrapper dependency facts.

The default output intentionally uses repository-relative paths so copied plans do
not leak machine-specific usernames or checkout locations.
"""

from __future__ import annotations

import argparse
import json
import pathlib
import subprocess


def read_json(path: pathlib.Path) -> dict:
    try:
        return json.loads(path.read_text())
    except FileNotFoundError:
        return {}


def dependency_version(package_json: dict, name: str) -> str | None:
    for section in ("dependencies", "devDependencies", "peerDependencies"):
        value = package_json.get(section, {}).get(name)
        if value:
            return value
    return None


def rel(path: pathlib.Path, root: pathlib.Path, absolute: bool) -> str:
    if absolute:
        return str(path)
    try:
        return str(path.relative_to(root))
    except ValueError:
        return path.name


def detect_package_manager(library: pathlib.Path) -> str | None:
    lockfiles = (
        ("package-lock.json", "npm"),
        ("npm-shrinkwrap.json", "npm"),
        ("pnpm-lock.yaml", "pnpm"),
        ("yarn.lock", "yarn"),
    )
    for filename, manager in lockfiles:
        if (library / filename).exists():
            return manager
    return None


def existing_paths(root: pathlib.Path, paths: list[str], absolute: bool) -> list[str]:
    return [rel(root / path, root, absolute) for path in paths if (root / path).exists()]


def npm_latest(package_name: str) -> dict:
    try:
        result = subprocess.run(
            ["npm", "view", package_name, "version", "--json"],
            check=False,
            capture_output=True,
            text=True,
            timeout=30,
        )
    except (OSError, subprocess.TimeoutExpired) as exc:
        return {"ok": False, "error": str(exc)}

    if result.returncode != 0:
        return {"ok": False, "error": result.stderr.strip() or result.stdout.strip()}

    try:
        version = json.loads(result.stdout)
    except json.JSONDecodeError:
        version = result.stdout.strip().strip('"')
    return {"ok": True, "version": version}


def upstream_checkout_hint(repo: pathlib.Path, provided: str | None, absolute: bool) -> dict | None:
    candidates = []
    if provided:
        candidates.append(("provided", pathlib.Path(provided).expanduser().resolve()))
    candidates.append(("sibling", repo.parent / "lightweight-charts"))

    for source, path in candidates:
        if path.exists():
            if absolute:
                display_path = str(path)
            elif path.parent == repo.parent:
                display_path = f"../{path.name}"
            else:
                display_path = path.name
            return {"source": source, "path": display_path}
    return None


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Inspect local lightweight-charts Android wrapper dependency facts."
    )
    parser.add_argument("repo", nargs="?", default=".", help="Path to the Android wrapper repository")
    parser.add_argument(
        "--absolute-paths",
        action="store_true",
        help="Print absolute paths instead of repository-relative paths",
    )
    parser.add_argument(
        "--npm-latest",
        action="store_true",
        help="Also query npm for the latest lightweight-charts version",
    )
    parser.add_argument(
        "--upstream-checkout",
        help="Optional local lightweight-charts checkout to report if it exists",
    )
    args = parser.parse_args()

    repo = pathlib.Path(args.repo).resolve()
    library = repo / "lightweightlibrary"
    package_json_path = library / "package.json"
    package_lock_path = library / "package-lock.json"
    package_json = read_json(package_json_path)
    package_lock = read_json(package_lock_path)

    lock_version = None
    lock_packages = package_lock.get("packages", {})
    locked_package = lock_packages.get("node_modules/lightweight-charts", {})
    if isinstance(locked_package, dict):
        lock_version = locked_package.get("version")

    facts = {
        "repo_name": repo.name,
        "package_manager": detect_package_manager(library),
        "package_json": rel(package_json_path, repo, args.absolute_paths),
        "package_json_lightweight_charts": dependency_version(package_json, "lightweight-charts"),
        "package_lock_lightweight_charts": lock_version,
        "key_paths": existing_paths(
            repo,
            [
                "lightweightlibrary/webpack.config.js",
                "lightweightlibrary/lib/app/index.js",
                "lightweightlibrary/src/main/java",
                "app/src/main",
                "README.md",
                "libs.versions.toml",
            ],
            args.absolute_paths,
        ),
        "package_scripts": sorted(package_json.get("scripts", {}).keys()),
        "upstream_checkout": upstream_checkout_hint(repo, args.upstream_checkout, args.absolute_paths),
    }

    if args.npm_latest:
        facts["npm_latest_lightweight_charts"] = npm_latest("lightweight-charts")

    print(json.dumps(facts, indent=2, sort_keys=True))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
