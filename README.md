# 🍊 Citrusian

A 2D action-RPG built entirely in Java, developed by a team of 4 (2 juniors, 2 seniors).

Play as the **Lone Orange**, a resilient survivor on the Isle of Nectar. The Rot Root Clan — an army of earth-dwelling vegetables — has stolen the sacred Citrusian Helm, and the island's fruits are beginning to wither. Journey to the decaying heart of the island and take it back.

## Gameplay
- Explore an open world with dungeons, boss fights, and NPCs
- Collect weapons, shields, and power-ups (dash, invisibility, fire seeds, and more)
- Fight bosses including Boss Broccoli (2-phase), Baron Rotjaw, and the Slime King
- Charged attacks that change by weapon: swords lunge, axes knock enemies back harder, pickaxes hit a wider area
- A stamina bar for dashing, guarding and charged attacks
- Spend Crop Coins at the merchant on upgrades: more fire seeds, more stamina, easier parries, gear repair
- A bigger world: sail east to Zest Harbor, heal at the spring in the Citrus Grove, and brave the Rotting Marsh for its treasure
- Watch out for the Weeping Onion's tear gas and the Spud Brute's charge
- Veggies get tougher at night but drop double coins
- Weather: rain and storms slow your fire seeds, fog cuts how far you (and the veggies) can see, and the Rotting Marsh is always misty
- Side quests from the old apple, with a hidden Golden Seed and a sword you can't buy
- New Game+ after the credits: keep your level and gear, face tougher veggies
- Save/load system that remembers where you are, cutscenes, lighting engine, and pathfinding AI

## Tech
- Built in Java using custom game engine (no libraries)
- Tile-based rendering, collision detection, entity system
- Custom pixel art and original soundtrack

## Controls
| Key | Action |
|---|---|
| W A S D | Move (and move through menus) |
| Enter | Attack, talk, confirm, use selected item. Keep holding after a swing to charge, let go when the bar turns gold |
| Space | Guard with shield (time it right to parry). Uses stamina |
| F | Shoot fire seed |
| Shift | Dash (uses a Dash item and stamina) |
| C | Open or close the character and inventory screen |
| O | Save the game |
| Esc | Options |
| T | Toggle full screen |

## Build and Run
You need Java 17 or newer (JDK). From the repo root:

```bash
javac -d bin $(find src -name "*.java")
cp -r src/res bin/
java -cp bin main.Main
```

On Windows PowerShell, compile with `javac -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName`, then copy `src\res` into `bin\` and run the same `java` command.

The game writes `save.dat` to the folder you run it from.

To test a weather type, start the game with it: `java -Dcitrusian.weather=storm -cp bin main.Main` (also `rain`, `fog` or `clear`).

## Download
Play on itch.io: [ishans09.itch.io/citrusian](https://ishans09.itch.io/citrusian)
