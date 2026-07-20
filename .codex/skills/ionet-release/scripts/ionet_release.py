#!/usr/bin/env python3
"""
Publish the current ionet version to GitHub Releases.

The script intentionally depends only on the Python standard library plus local
`git` and `gh` commands so it can run in a normal repository checkout.
"""

from __future__ import annotations

import argparse
import os
import re
import subprocess
import sys
import tempfile
import xml.etree.ElementTree as ET
from dataclasses import dataclass
from pathlib import Path


REPO = "iohao/ionet"
REMOTE = "origin"
POM_NS = {"m": "http://maven.apache.org/POM/4.0.0"}

FRIENDLY_NAMES = {
    "aeron-all": "Aeron",
    "netty-all": "Netty",
    "netty-common": "Netty",
    "slf4j-api": "SLF4J API",
    "junit-jupiter": "JUnit Jupiter",
    "jprotobuf": "JProtobuf",
    "protobuf-java": "Protobuf Java",
    "disruptor": "Disruptor",
    "commons-io": "Commons IO",
    "Java-WebSocket": "Java-WebSocket",
    "spring-context": "Spring Context",
    "beetl": "Beetl",
    "jakarta.validation-api": "Jakarta Validation API",
    "logback-classic": "Logback",
    "lombok": "Lombok",
    "maven-javadoc-plugin": "Maven Javadoc Plugin",
    "maven-source-plugin": "Maven Source Plugin",
    "maven-surefire-plugin": "Maven Surefire Plugin",
    "maven-failsafe-plugin": "Maven Failsafe Plugin",
    "maven-compiler-plugin": "Maven Compiler Plugin",
    "central-publishing-maven-plugin": "Central Publishing Maven Plugin",
    "lombok-maven-plugin": "Lombok Maven Plugin",
}

GROUP_TITLES = {
    "publisher": "Publisher reliability and backpressure",
    "routing": "Communication and routing hardening",
    "external": "External networking and client fixes",
    "core": "Core framework behavior",
    "docs": "Documentation, source parsing, and build updates",
    "other": "Other improvements",
}


@dataclass(frozen=True)
class PomArtifact:
    kind: str
    group_id: str
    artifact_id: str
    version_raw: str
    version: str

    @property
    def key(self) -> tuple[str, str, str]:
        return self.kind, self.group_id, self.artifact_id


@dataclass(frozen=True)
class Upgrade:
    name: str
    old: str
    new: str


@dataclass(frozen=True)
class Commit:
    subject: str
    body: str


def run(args: list[str], *, cwd: Path, check: bool = True) -> str:
    try:
        result = subprocess.run(args, cwd=cwd, check=False, text=True, capture_output=True)
    except FileNotFoundError as exc:
        raise RuntimeError(f"Required command not found: {args[0]}") from exc
    if check and result.returncode != 0:
        command = " ".join(args)
        stderr = result.stderr.strip()
        stdout = result.stdout.strip()
        details = stderr or stdout or f"exit code {result.returncode}"
        raise RuntimeError(f"{command} failed: {details}")
    return result.stdout.strip()


def git(args: list[str], *, cwd: Path, check: bool = True) -> str:
    return run(["git", *args], cwd=cwd, check=check)


def gh(args: list[str], *, cwd: Path, check: bool = True) -> str:
    return run(["gh", *args], cwd=cwd, check=check)


def repo_root(cwd: Path) -> Path:
    return Path(git(["rev-parse", "--show-toplevel"], cwd=cwd)).resolve()


def read_current_version(root: Path) -> str:
    root_xml = ET.parse(root / "pom.xml").getroot()
    version = text(root_xml.find("m:version", POM_NS))
    if not version:
        raise RuntimeError("Root pom.xml does not contain a project version")
    return version


def text(element: ET.Element | None) -> str:
    if element is None or element.text is None:
        return ""
    return element.text.strip()


def local_name(tag: str) -> str:
    return tag.rsplit("}", 1)[-1]


def parse_version_key(version: str) -> tuple[int, ...]:
    if not re.fullmatch(r"\d+(?:\.\d+)*", version):
        return ()
    return tuple(int(part) for part in version.split("."))


def numeric_tags(root: Path) -> list[str]:
    tags = git(["tag", "--list"], cwd=root).splitlines()
    return sorted(
        [tag for tag in tags if parse_version_key(tag)],
        key=parse_version_key,
    )


def previous_tag(root: Path, version: str) -> str:
    current_key = parse_version_key(version)
    if not current_key:
        raise RuntimeError(f"Version {version!r} is not a numeric release tag")

    candidates = [tag for tag in numeric_tags(root) if parse_version_key(tag) < current_key]
    if not candidates:
        raise RuntimeError(f"No previous numeric version tag found before {version}")
    return candidates[-1]


def tag_exists(root: Path, tag: str) -> bool:
    result = subprocess.run(
        ["git", "rev-parse", "-q", "--verify", f"refs/tags/{tag}"],
        cwd=root,
        text=True,
        capture_output=True,
        check=False,
    )
    return result.returncode == 0


def ref_commit(root: Path, ref: str) -> str:
    return git(["rev-list", "-n", "1", ref], cwd=root)


def assert_remote(root: Path, repo: str) -> None:
    remotes = git(["remote", "-v"], cwd=root)
    expected = repo.replace("/", r"[/:]")
    if not re.search(expected + r"(?:\.git)?(?:\s|\n|$)", remotes):
        raise RuntimeError(f"This checkout does not appear to use the {repo} remote")


def assert_publishable(root: Path, version: str, repo: str) -> None:
    assert_remote(root, repo)

    branch = git(["rev-parse", "--abbrev-ref", "HEAD"], cwd=root)
    if branch != "main":
        raise RuntimeError(f"Release publishing must run from main, current branch is {branch}")

    status = git(["status", "--porcelain"], cwd=root)
    if status:
        raise RuntimeError("Working tree must be clean before publishing a release")

    git(["fetch", REMOTE, "--tags"], cwd=root)
    head = git(["rev-parse", "HEAD"], cwd=root)
    origin_main = git(["rev-parse", f"{REMOTE}/main"], cwd=root)
    if head != origin_main:
        raise RuntimeError("Local HEAD must match origin/main before publishing")

    if gh(["release", "view", version, "--repo", repo], cwd=root, check=False):
        raise RuntimeError(f"GitHub release {version} already exists")


def ensure_tag(root: Path, version: str, *, dry_run: bool) -> None:
    head = git(["rev-parse", "HEAD"], cwd=root)
    if tag_exists(root, version):
        tag_commit = ref_commit(root, version)
        if tag_commit != head:
            raise RuntimeError(f"Tag {version} points to {tag_commit}, not current HEAD {head}")
        return

    if dry_run:
        print(f"[dry-run] Would create and push annotated tag {version} at {head}")
        return

    git(["tag", "-a", version, "-m", version], cwd=root)
    git(["push", REMOTE, version], cwd=root)


def git_show(root: Path, ref: str, path: str) -> str:
    return git(["show", f"{ref}:{path}"], cwd=root)


def pom_paths(root: Path, previous: str, current_ref: str) -> list[str]:
    paths: set[str] = set()
    for ref in (previous, current_ref):
        output = git(["ls-tree", "-r", "--name-only", ref], cwd=root)
        paths.update(line for line in output.splitlines() if line == "pom.xml" or line.endswith("/pom.xml"))
    return sorted(paths)


def parse_pom(content: str, root_properties: dict[str, str]) -> dict[tuple[str, str, str], PomArtifact]:
    root = ET.fromstring(content)
    properties = dict(root_properties)
    properties.update(read_properties(root))
    artifacts: dict[tuple[str, str, str], PomArtifact] = {}

    for dependency in root.findall(".//m:dependency", POM_NS):
        artifact = artifact_from_element("dependency", dependency, properties)
        if artifact:
            artifacts[artifact.key] = artifact

    for plugin in root.findall(".//m:plugin", POM_NS):
        artifact = artifact_from_element("plugin", plugin, properties)
        if artifact:
            artifacts[artifact.key] = artifact

    return artifacts


def read_properties(root: ET.Element) -> dict[str, str]:
    properties: dict[str, str] = {}
    properties_element = root.find("m:properties", POM_NS)
    if properties_element is None:
        return properties

    for child in list(properties_element):
        properties[local_name(child.tag)] = text(child)
    return properties


def artifact_from_element(kind: str, element: ET.Element, properties: dict[str, str]) -> PomArtifact | None:
    group_id = text(element.find("m:groupId", POM_NS))
    artifact_id = text(element.find("m:artifactId", POM_NS))
    version_raw = text(element.find("m:version", POM_NS))

    if kind == "plugin" and not group_id:
        group_id = "org.apache.maven.plugins"

    if not group_id or not artifact_id or not version_raw:
        return None
    if group_id == "com.iohao.net":
        return None

    version = resolve_property(version_raw, properties)
    if not version:
        return None

    return PomArtifact(kind, group_id, artifact_id, version_raw, version)


def resolve_property(version: str, properties: dict[str, str]) -> str:
    match = re.fullmatch(r"\$\{([^}]+)}", version)
    if match:
        return properties.get(match.group(1), version)
    return version


def root_properties_at(root: Path, ref: str) -> dict[str, str]:
    return read_properties(ET.fromstring(git_show(root, ref, "pom.xml")))


def dependency_upgrades(root: Path, previous: str, current_ref: str) -> list[Upgrade]:
    old_root_properties = root_properties_at(root, previous)
    new_root_properties = root_properties_at(root, current_ref)
    upgrades: dict[str, Upgrade] = {}

    for path in pom_paths(root, previous, current_ref):
        try:
            old_pom = git_show(root, previous, path)
            new_pom = git_show(root, current_ref, path)
        except RuntimeError:
            continue

        old_artifacts = parse_pom(old_pom, old_root_properties)
        new_artifacts = parse_pom(new_pom, new_root_properties)
        for key, old_artifact in old_artifacts.items():
            new_artifact = new_artifacts.get(key)
            if not new_artifact:
                continue
            if old_artifact.version == new_artifact.version:
                continue

            name = friendly_name(new_artifact.artifact_id)
            upgrades[name] = Upgrade(name, old_artifact.version, new_artifact.version)

    return sorted(upgrades.values(), key=lambda item: item.name.lower())


def friendly_name(artifact_id: str) -> str:
    if artifact_id in FRIENDLY_NAMES:
        return FRIENDLY_NAMES[artifact_id]

    clean = artifact_id
    clean = re.sub(r"^(java|jakarta|org|com)-", "", clean)
    clean = clean.replace("-plugin", " Plugin")
    return " ".join(part.upper() if part in {"api", "io"} else part.capitalize() for part in clean.split("-"))


def commits_between(root: Path, previous: str, current_ref: str) -> list[Commit]:
    output = git(["log", "--reverse", "--format=%s%x1f%b%x1e", f"{previous}..{current_ref}"], cwd=root)
    commits: list[Commit] = []
    for record in output.split("\x1e"):
        record = record.strip()
        if not record:
            continue
        subject, separator, body = record.partition("\x1f")
        commits.append(Commit(subject.strip(), body.strip() if separator else ""))
    return commits


def classify_commit(commit: Commit) -> str:
    def category(text: str) -> str | None:
        if any(word in text for word in ("publisher", "aeron offer", "publication", "backpressure", "queue")):
            return "publisher"
        if any(word in text for word in ("external", "session", "tcp", "websocket", "udp", "client", "micro")):
            return "external"
        if any(word in text for word in ("communication", "routing", "cmdregion", "connection", "future", "server id")):
            return "routing"
        if any(
            word in text for word in ("docs", "doc", "source", "protobuf", "build", "dependency", "javadoc", "readme")
        ):
            return "docs"
        if any(word in text for word in ("flow", "action", "broadcast", "core-framework", "bar", "skeleton")):
            return "core"
        return None

    return category(commit.subject.lower()) or category(commit.body.lower()) or "other"


def clean_subject(subject: str) -> str:
    subject = re.sub(r"^:[^:]+:\s*", "", subject).strip()
    subject = re.sub(r"^(feat|fix|docs|doc|build|chore|test|refactor|perf|ci)(?:\([^)]+\))?!?:\s*", "", subject)
    subject = subject.split(" - ", 1)[0]
    subject = subject.strip(" .")

    if not subject:
        return ""
    if re.fullmatch(r"\d+(?:\.\d+)*", subject):
        return ""
    if subject.lower() in {"doc", "docs(package-info)"}:
        return ""

    replacements = [
        ("add ", "Added "),
        ("allow ", "Allowed "),
        ("adjust ", "Adjusted "),
        ("clarify ", "Clarified "),
        ("close ", "Closed "),
        ("cover ", "Covered "),
        ("enhance ", "Enhanced "),
        ("enable ", "Enabled "),
        ("ensure ", "Ensured "),
        ("guard ", "Guarded "),
        ("handle ", "Handled "),
        ("harden ", "Hardened "),
        ("ignore ", "Ignored "),
        ("implement ", "Implemented "),
        ("improve ", "Improved "),
        ("introduce ", "Introduced "),
        ("optimize ", "Optimized "),
        ("register ", "Registered "),
        ("reject ", "Rejected "),
        ("remove ", "Removed "),
        ("replace ", "Replaced "),
        ("retry ", "Retried "),
        ("skip ", "Skipped "),
        ("update ", "Updated "),
        ("upgrade ", "Upgraded "),
        ("use ", "Used "),
    ]

    lowered = subject.lower()
    for prefix, replacement in replacements:
        if lowered.startswith(prefix):
            subject = replacement + subject[len(prefix):]
            break
    else:
        subject = subject[:1].upper() + subject[1:]

    return subject + "."


def commit_notes(commit: Commit) -> list[str]:
    notes: list[str] = []
    summary = clean_subject(commit.subject)
    if summary:
        notes.append(summary)

    for line in commit.body.splitlines():
        line = line.strip()
        if not line or line.lower() in {"changes:", "notes:"}:
            continue
        if line.startswith(("-", "*")):
            line = line[1:].strip()
        note = clean_subject(line)
        if note:
            notes.append(note)

    return notes


def grouped_commit_notes(commits: list[Commit]) -> dict[str, list[str]]:
    groups: dict[str, list[str]] = {key: [] for key in GROUP_TITLES}
    seen: set[str] = set()

    for commit in commits:
        group = classify_commit(commit)
        for note in commit_notes(commit):
            if not note or note == "." or note in seen:
                continue
            seen.add(note)
            groups[group].append(note)

    return {key: values for key, values in groups.items() if values}


def overview(version: str, groups: dict[str, list[str]]) -> str:
    preferred = [
        GROUP_TITLES[key].lower() for key in ("publisher", "routing", "external", "core", "docs") if key in groups
    ]
    if not preferred:
        preferred = ["framework reliability", "documentation", "dependency updates"]
    focus = ", ".join(preferred[:3])
    return f"Ionet {version} focuses on {focus}."


def render_notes(version: str, previous: str, commits: list[Commit], upgrades: list[Upgrade]) -> str:
    groups = grouped_commit_notes(commits)
    lines: list[str] = [overview(version, groups), ""]

    for index, (key, notes) in enumerate(groups.items(), start=1):
        lines.append(f"{index}. {GROUP_TITLES[key]}:")
        for note in notes:
            lines.append(f"  - {note}")
        lines.append("")

    if upgrades:
        lines.append("Dependency upgrades:")
        for upgrade in upgrades:
            lines.append(f"- {upgrade.name}:{upgrade.old} --> {upgrade.new}")
        lines.append("")

    lines.append(f"**Full Changelog**: https://github.com/iohao/ionet/compare/{previous}...{version}")
    lines.append("")
    return "\n".join(lines)


def publish_release(root: Path, repo: str, version: str, notes: str) -> None:
    with tempfile.NamedTemporaryFile("w", encoding="utf-8", suffix=".md", delete=False) as handle:
        handle.write(notes)
        notes_path = handle.name

    try:
        gh(
            [
                "release",
                "create",
                version,
                "--repo",
                repo,
                "--title",
                version,
                "--notes-file",
                notes_path,
                "--latest",
            ],
            cwd=root,
        )
    finally:
        os.unlink(notes_path)

    gh(["release", "view", version, "--repo", repo], cwd=root)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Publish the current ionet GitHub release")
    parser.add_argument("--dry-run", action="store_true", help="generate notes without creating tags or releases")
    parser.add_argument("--version", help="version/tag to release; defaults to root pom.xml version")
    parser.add_argument("--previous-tag", help="previous version tag; defaults to latest numeric tag before version")
    parser.add_argument("--repo", default=REPO, help=f"GitHub repository, defaults to {REPO}")
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    root = repo_root(Path.cwd())
    version = args.version or read_current_version(root)

    try:
        assert_remote(root, args.repo)
        git(["fetch", REMOTE, "--tags"], cwd=root)

        if not args.dry_run:
            assert_publishable(root, version, args.repo)

        ensure_tag(root, version, dry_run=args.dry_run)

        current_ref = version if tag_exists(root, version) else "HEAD"
        previous = args.previous_tag or previous_tag(root, version)
        commits = commits_between(root, previous, current_ref)
        upgrades = dependency_upgrades(root, previous, current_ref)
        notes = render_notes(version, previous, commits, upgrades)

        if args.dry_run:
            release_exists = bool(gh(["release", "view", version, "--repo", args.repo], cwd=root, check=False))
            if release_exists:
                print(f"[dry-run] GitHub release {version} already exists.")
            print(notes)
            return 0

        publish_release(root, args.repo, version, notes)
        print(f"Published https://github.com/{args.repo}/releases/tag/{version}")
        return 0
    except RuntimeError as exc:
        print(f"error: {exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
