---
name: ionet-release
description: Manual-only skill for publishing the current iohao/ionet project version to GitHub Releases. Do not invoke implicitly from ordinary prompts; use only when explicitly selected or invoked as $ionet-release.
---

# ionet Release

## Purpose

Publish the current `iohao/ionet` version to GitHub Releases using the root Maven `pom.xml` version as both the tag
and release title.

Use this skill only when the user explicitly selects it or invokes `$ionet-release`. Do not trigger it merely because a
prompt mentions publishing, release notes, changelogs, or GitHub Releases.

Run it only from the ionet repository. Release notes are written in English and follow the existing `25.5` release style.

## Workflow

1. Run the release helper from the repository root:

```bash
python3 .codex/skills/ionet-release/scripts/ionet_release.py
```

2. The helper performs these checks before publishing:
   - Confirms the repository remote is `iohao/ionet`.
   - Reads the current version from the root `pom.xml`.
   - Fetches remote tags.
   - Requires the current branch to be `main`.
   - Requires local `HEAD` to match `origin/main`.
   - Requires a clean working tree.
   - Fails if a GitHub release for the version already exists.

3. Tag handling:
   - If the version tag already exists, it must point to current `HEAD`.
   - If the version tag does not exist, the helper creates an annotated tag at `HEAD` and pushes it to `origin`.

4. Release note generation:
   - Finds the previous numeric version tag before the current version.
   - Uses commits from `<previous-tag>..<current-version>` to produce a short overview and grouped version content.
   - Reads changed Maven `pom.xml` files to list dependency and plugin upgrades.
   - Adds `Dependency upgrades:` before the final changelog link.
   - Adds `**Full Changelog**: https://github.com/iohao/ionet/compare/<previous>...<current>`.

5. Publishing:
   - Creates the public GitHub release directly with `gh release create`.
   - Verifies the result with `gh release view`.

## Dry Run

Before a real release, inspect generated notes without creating tags or releases:

```bash
python3 .codex/skills/ionet-release/scripts/ionet_release.py --dry-run
```

To inspect a historical range, pass explicit tags:

```bash
python3 .codex/skills/ionet-release/scripts/ionet_release.py --dry-run --previous-tag 25.4 --version 25.5
```

## Requirements

- `git` must be available.
- GitHub CLI `gh` must be installed and authenticated.
- The working copy must be up to date with `origin/main` for real publication.
