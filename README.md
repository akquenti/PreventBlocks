# PreventBlocks

![Created At](https://img.shields.io/badge/created_at-february_2026-brightgreen)
[![PreventBlocks license: GPLv3](https://img.shields.io/badge/license-GPLv3-orange)](LICENSE)
[![Paper](https://img.shields.io/badge/Paper-1.21.1-blue)](https://papermc.io)
[![Java](https://img.shields.io/badge/Java-21-red)](https://adoptium.net)

A lightweight Minecraft Paper plugin that prevents placement of specific blocks in configured worlds.

## Features

- Block specific blocks
- Configure which worlds the restriction applies to
- Blacklist or Whitelist mode
- Bypass permission for admins
- Admin notifications on block attempts

## Building

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/akquenti/PreventBlocks.git
   cd PreventBlocks
   ```

2. **Build with Maven**
   ```bash
   mvn clean package
   ```

3. **Find the compiled plugin**
   ```
   target/PreventBlocks-1.0.0.jar
   ```

Pre-built versions can also be found on the [Releases](https://github.com/akquenti/PreventBlocks/releases) page.

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/pb reload` | Reload configuration | `preventblocks.admin` |
| `/pb list` | Show list of blocked blocks | `preventblocks.admin` |
| `/pb info` | Show plugin information | `preventblocks.admin` |

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `preventblocks.bypass` | Allows placing blocked blocks | `op` |
| `preventblocks.admin` | Admin permissions (reload, list) | `op` |

## Support

- 📧 Create an [Issue](https://github.com/akquenti/PreventBlocks/issues)
- 💬 Contact via GitHub
