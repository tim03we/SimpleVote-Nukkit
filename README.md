# SimpleVote
SimpleVote is a easy vote plugin for the [Minecraft Pocket Server Website](https://minecraftpocket-servers.com/).
# Commands
Command | Sub-Command | Permission | Alias
------- | ----------- | ---------- | ------
/vote | - | - | -

# Config
```yaml
---
# Your minecraftpocket-server.com SECRET KEY
secret-key: "YOUR_SECRET_KEY"

# Top Voter Floating Text
text:
  count: 10
  level: "world"
  x: 0.0
  y: 125.0
  z: 0.0

# Command Cooldown
cooldown:
  enable: true
  # In Seconds
  time: 60

# Commands if player voted
commands:
  - 'give "{player}" steak 64'
...
```

----------------

If problems arise, create an issue or write us on Discord.

| Discord |
| :---: |
[![Discord](https://img.shields.io/discord/639130989708181535.svg?style=flat-square&label=discord&colorB=7289da)](https://discord.gg/5tYC5dJ) |
