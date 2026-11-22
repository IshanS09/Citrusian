"""Rebuild Baron Rotjaw's ship map (BRJmap.txt) with ship tiles.

Keeps Ishan's layout: every walkable tile stays walkable and every event spot
(arrival 50,63 / exit 50,64 / boss trigger 50,43 / iron door 50,44) is untouched.
Run from the repo root: python3 tools/ship_map.py
"""
PATH = 'src/res/maps/BRJmap.txt'

# tile ids (see tile/tileManager.java)
WATER = (9, 10); WALL = 5; BOOKSHELF = 16; DRAWER = 14; BED = 17; STAIRS = 20; PATHT = 19; DECK = 23
HULL, CANNON_L, CANNON_R, BARREL, CRATE, HAMMOCK, WHEEL, HATCH, GANGPLANK, PIER, FOAM = range(31, 42)

raw = open(PATH).read()
rows = [[int(v) for v in line.replace(',', ' ').split()] for line in raw.splitlines() if line.strip()]

KEEP = {(50, 63), (50, 64), (50, 43), (50, 44)}


def put(x, y, t, expect=DECK):
    assert (x, y) not in KEEP
    assert rows[y][x] == expect, f'({x},{y}) is {rows[y][x]}, expected {expect}'
    rows[y][x] = t


for y, r in enumerate(rows):
    for x, t in enumerate(r):
        if t == WALL:  # stone walls become the wooden hull
            r[x] = HULL
        elif t == BOOKSHELF:
            r[x] = BARREL if (x + y) % 3 else CRATE
        elif t == DRAWER:
            r[x] = CRATE
        elif t == BED:
            r[x] = HAMMOCK
        elif t == STAIRS:
            r[x] = HATCH
        elif t == PATHT:
            r[x] = PIER if y >= 62 else GANGPLANK

# Upper deck (the boss arena): cannons along both rails, the wheel at the bow,
# and some cargo tucked into the corners. The middle stays open for the fight.
for y in (35, 38, 41):
    put(42, y, CANNON_L)
    put(58, y, CANNON_R)
put(50, 33, WHEEL)
for x, y in ((43, 33), (57, 33)):
    put(x, y, BARREL)
for x, y in ((43, 42), (57, 42)):
    put(x, y, CRATE)

# Foam where the ocean touches the ship
solid_ship = {HULL, GANGPLANK, PIER, DECK, 24, 25}
foam = []
for y in range(len(rows)):
    for x in range(len(rows[0])):
        if rows[y][x] in WATER:
            for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nx, ny = x + dx, y + dy
                if 0 <= ny < len(rows) and 0 <= nx < len(rows[0]) and rows[ny][nx] in solid_ship:
                    foam.append((x, y))
                    break
for x, y in foam:
    rows[y][x] = FOAM

# Same format as the original: trailing comma on every line, blank line at the end
with open(PATH, 'w') as f:
    f.write(''.join(','.join(str(v) for v in r) + ',\n' for r in rows) + '\n')
print('ship map rebuilt:', len(foam), 'foam tiles')
