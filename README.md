# RZVanish

A configurable vanish plugin for Minecraft servers running Paper 1.20.1 or compatible versions.

## Features

- `/vanish` command with aliases `v` and `rzvanish`
- Configurable messages and fake join/quit messages
- Configurable blocked actions while vanished
- Optional PlaceholderAPI integration
- Vanish priority permissions
- Public API events for vanish and unvanish actions

## Requirements

- Java 17
- Paper API for Minecraft 1.20.1
- Maven
- Optional: PlaceholderAPI on the server for placeholder integration

## Build

Open this project in IntelliJ IDEA as a Maven project, or build it with Maven:

```bash
mvn clean package
```

The plugin JAR is generated in `target/` after a successful build.

## Installation

1. Build the project.
2. Copy the generated `RZVanish-1.0.0.jar` into your server's `plugins` directory.
3. Start or restart the Paper server.
4. Configure `plugins/RZVanish/config.yml` as needed.

## Command

- `/vanish` — toggle vanish (subject to permissions)
- `/vanish reload` — reload configuration
- Other subcommands depend on the permissions and command implementation.

## Permissions

The permission nodes are declared in `src/main/resources/plugin.yml`. By default, most administrative permissions are granted to server operators.

## Configuration

Edit `src/main/resources/config.yml` for the default configuration. The server generates its active copy at `plugins/RZVanish/config.yml`.

## Notes

- PlaceholderAPI is an optional soft dependency.
- Generated build output and IDE-specific files are intentionally excluded from this source archive.
