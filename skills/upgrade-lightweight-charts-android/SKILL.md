---
name: upgrade-lightweight-charts-android
description: Use when upgrading the TradingView lightweight-charts Android wrapper across upstream JavaScript lightweight-charts versions, checking version deltas, API parity, bridge changes, examples, tests, docs, or migration risk.
---

# Upgrade Lightweight Charts Android

## Overview

Use this for end-to-end upgrades of the Android wrapper around upstream JavaScript `lightweight-charts`. Success requires source-backed API research, scoped implementation, compatibility decisions, and verification evidence—not just a version bump.

## When to Use

- Upgrading `lightweightlibrary/package.json` to a newer `lightweight-charts` release.
- Reviewing wrapper parity after upstream major/minor releases.
- Planning or changing Kotlin APIs, JS bridge code, serializers, examples, Gradle/WebView assets, docs, or migration notes caused by upstream changes.

Do not use for Android dependency upgrades unrelated to `lightweight-charts`.

## Companion Workflow

- Use official upstream docs, release notes, package typings, and source checks for upstream API research. If your Codex environment has a source/research skill, it can help, but this skill must remain usable without one.
- Use a written plan before non-trivial implementation.
- Use fresh verification evidence before claiming upgrade success.
- **RECOMMENDED:** Use code review skills before merge when bridge/API surfaces change.

## Workflow

1. **Inspect first.** Locate `lightweightlibrary/package.json` and `app/src/main`; do not assume a checkout path. Run `git status --short --untracked-files=all` and protect unrelated user changes.
2. **Discover versions.** Run `scripts/inspect_versions.py <repo-root>` from this skill. Add `--npm-latest` only when shell network access is available. Verify “latest” live through npm, GitHub releases, and official docs when network state matters.
3. **Inventory deltas.** Compare releases from current to target. Audit package typings/source and wrapper surfaces for chart, series, panes, time/price scales, markers, watermarks, localization/formatters, options, events, and generated assets.
4. **Plan non-trivial work.** Include target version, release delta, Kotlin API, JS bridge, serialization, examples, tests, docs, compatibility/deprecation choices, unsupported custom/plugin internals, risks, and commands.
5. **Implement in slices.** Update package/lock files through the package manager, bridge imports, caches/managers for stable handles, Kotlin interfaces/delegates/models/enums/serializers, examples, docs, tests, and generated assets.
6. **Verify with evidence.** Run JS package scripts after bridge changes, Android tests/builds after Kotlin/example changes, and inspect generated WebView assets when asset loading changes.

## Migration Checklist

- Dependency/build/runtime: npm version, lockfile, module format, webpack, Node/WebView compatibility, generated assets.
- APIs: chart, series, panes, time scale, price scale, event payloads, options, localization, formatter hooks.
- Built-ins: markers, watermarks, new chart modes, horizontal-scale entry points that do not require custom behavior authoring.
- Examples, tests, docs, compatibility/deprecation strategy, rollout risks.

## Guardrails

- Prefer official upstream docs, package typings, and source over secondary summaries.
- Treat “latest” as unstable; verify it live or state that it could not be checked.
- Do not hand-wave parity. Search typings and wrapper interfaces, then add focused tests for important claims.
- Do not replace an existing public Kotlin signature with a narrower/newer type unless a deprecated compatibility overload remains.
- Preserve existing callback style and wrapper naming unless the migration intentionally breaks API.
- Bridge source changes require package script runs and generated `src/main/assets/.../main.js` review.
- Do not expose generic custom plugins, custom series, or custom primitives unless explicitly scoped.
- Use portable paths in plans/docs; do not use machine-specific absolute paths.
- Do not revert unrelated dirty worktree changes.

## Validation Scenarios

| Scenario | Expected behavior |
| --- | --- |
| “Update wrapper to latest lightweight-charts” | Verifies latest live, compares local version, inventories upstream deltas, plans before implementation. |
| “Make v5 panes work” | Checks upstream typings/docs and wrapper bridge/Kotlin/example surfaces before coding. |
| “Replace old API with new v5 type” | Preserves source compatibility with deprecated overloads when feasible. |
| Dirty worktree with unrelated files | Lists and avoids unrelated changes. |

## Common Mistakes

| Mistake | Fix |
| --- | --- |
| Bumping npm version only | Inventory release deltas and wrapper parity before declaring success. |
| Trusting stale local checkout | Verify npm/GitHub/docs live or state that latest could not be checked. |
| Exposing upstream custom internals by default | Defer custom plugins/series/primitives unless explicitly scoped. |
| Forgetting generated assets | Run package scripts and include expected asset diffs. |
| Breaking Kotlin source compatibility | Add deprecated compatibility overloads or document intentional breakage. |
