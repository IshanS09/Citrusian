"""Grow the world from 100x100 to 140x140 and build three new regions on the main map.

Everything is added to the east and south, so every existing coordinate in the
code (teleports, spawns, chests, the dry-tree maze) stays exactly where it is.
The other maps are padded with their own edge tiles.

New on the main map:
  Zest Harbor   (east)       fishing village: cobble roads, cottages, market, well, pier, boats
  Citrus Grove  (southeast)  orange orchard around a healing spring
  Rotting Marsh (south)      swamp with ponds, mud paths, slime, a treasure chest, enemies

Run from the repo root on the ORIGINAL 100x100 maps: python3 tools/expand_world.py
It prints the coordinates the Java code uses (spring, chest, enemy spawns).
"""
import math, random
from collections import deque

OLD, N = 100, 140
MAPS = ['worldV5', 'interior3', 'dungeonFIN', 'dungeonL2', 'BRJmap', 'interiorPK3']

# tile ids (tile/tileManager.java)
FLOOR_WALKABLE = None
WATER, WATER2, BRIDGE, TREE, GRASS, FLOWER, FLOWER2, ROCKS, SHORE = 9, 10, 2, 3, 4, 6, 12, 7, 13
SAND, VILLAGE, TILLED, SPRING, BOARDWALK, PIER, BOAT_ = 1, 11, 27, 22, 23, 40, 70
SWAMP_TREE, MUD, SLIME, SWAMP_GRASS = 56, 57, 63, 64
COBBLE, ORANGE_TREE, FENCE, STALL, WELL, BOAT, SWAMP_WATER, LILY, REEDS, COTTAGE = range(65, 75)

COLLIDE = {3, 5, 7, 9, 10, 11, 13, 14, 15, 16, 17, 18, 22, 25, 28, 29, 31, 32, 33, 34, 35, 36, 37, 41,
           42, 43, 44, 49, 50, 51, 56, 60, 62, 66, 67, 68, 69, 70, 71, 72, 74}


def load(name):
    return [[int(v) for v in line.replace(',', ' ').split()] for line in open(f'src/res/maps/{name}.txt') if line.strip()]


def save(name, rows):
    with open(f'src/res/maps/{name}.txt', 'w') as f:
        f.write(''.join(','.join(str(v) for v in r) + ',\n' for r in rows) + '\n')


def pad(rows):
    """Extend each row with its last tile, then repeat the (extended) last row."""
    assert len(rows) == OLD and all(len(r) == OLD for r in rows), 'run this on the original 100x100 maps'
    out = [r + [r[-1]] * (N - OLD) for r in rows]
    out += [out[-1][:] for _ in range(N - OLD)]
    return out


# ------------------------------------------------------------------ pad the other maps
for name in MAPS[1:]:
    save(name, pad(load(name)))

# ------------------------------------------------------------------ main map
orig = load('worldV5')
g = pad(orig)
rnd = random.Random(7)
for y in range(N):
    for x in range(N):
        if (x >= OLD or y >= OLD) and g[y][x] in (WATER, WATER2):
            g[y][x] = WATER2 if rnd.random() < .05 else WATER

# keep 3 tiles of open water around the original island
orig_land = {(x, y) for y in range(OLD) for x in range(OLD) if orig[y][x] not in (WATER, WATER2)}
protected = set()
for x, y in orig_land:
    for dy in range(-4, 5):
        for dx in range(-4, 5):
            protected.add((x + dx, y + dy))


def blob(ellipses, wobble=1.6, seed=0):
    """Union of ellipses with a wobbly edge."""
    cells = set()
    for (cx, cy, rx, ry) in ellipses:
        for y in range(int(cy - ry - 3), int(cy + ry + 4)):
            for x in range(int(cx - rx - 3), int(cx + rx + 4)):
                a = math.atan2(y - cy, x - cx)
                r = 1 + (math.sin(a * 5 + seed) * .06 + math.sin(a * 9 + seed * 2) * .04) * wobble
                if ((x - cx) / (rx * r)) ** 2 + ((y - cy) / (ry * r)) ** 2 <= 1:
                    if 1 <= x < N - 1 and 1 <= y < N - 1 and (x, y) not in protected:
                        cells.add((x, y))
    return cells


harbor = blob([(117, 40, 13, 13), (118, 58, 15, 13), (115, 76, 12, 12), (115, 91, 6, 7)], seed=1)
grove = blob([(112, 110, 17, 11), (124, 123, 11, 10), (101, 121, 10, 9)], seed=2)
marsh = blob([(25, 112, 13, 9), (42, 118, 16, 12), (60, 112, 11, 9), (34, 129, 14, 6)], seed=3)
land = harbor | grove | marsh

for x, y in land:
    g[y][x] = GRASS
# shoreline: land that touches water becomes the cliff edge used around the island
for x, y in list(land):
    if any(g[y + dy][x + dx] in (WATER, WATER2) for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1))):
        g[y][x] = SHORE
interior = {(x, y) for (x, y) in land if g[y][x] != SHORE}


def setc(x, y, t, only=None):
    if (x, y) in interior and (only is None or g[y][x] in only):
        g[y][x] = t


def h(x, y, s=0):
    return ((x * 73856093) ^ (y * 19349663) ^ (s * 83492791)) % 1000 / 1000


# ------------------------------------------------------------------ Zest Harbor
for x, y in interior & harbor:
    r = h(x, y, 1)
    g[y][x] = FLOWER if r < .05 else FLOWER2 if r < .08 else GRASS
roads = set()
for x in range(98, 135):
    roads |= {(x, 56), (x, 57)}                       # main street west to east
for y in range(30, 92):
    roads |= {(116, y), (117, y)}                     # north-south street
for x in range(110, 124):                             # town square
    for y in range(52, 62):
        roads.add((x, y))
for x, y in roads:
    setc(x, y, COBBLE)
setc(116, 56, WELL); setc(117, 56, WELL)
for x in (111, 113, 120, 122):
    setc(x, 53, STALL); setc(x, 60, STALL)
setc(116, 51, VILLAGE); setc(117, 51, VILLAGE)       # town hall
for x, y in ((108, 54), (105, 54), (102, 54), (108, 59), (105, 59), (126, 54), (129, 54), (126, 59), (129, 59),
             (114, 44), (114, 40), (119, 44), (119, 40), (114, 68), (119, 68), (114, 72), (119, 72), (114, 80), (119, 80)):
    setc(x, y, COTTAGE)
for x in range(104, 112):                             # fenced vegetable patch
    for y in range(36, 44):
        edge = x in (104, 111) or y in (36, 43)
        setc(x, y, FENCE if edge and not (x == 111 and y in (39, 40)) else TILLED)
setc(118, 29, VILLAGE)                                # lighthouse at the north tip
for y in range(34, 90, 4):                            # houses lining the north-south street
    if not 50 <= y <= 63:
        setc(114, y, COTTAGE); setc(119, y, COTTAGE)
for x in range(100, 134, 4):                          # and the main street
    if not 109 <= x <= 124:
        setc(x, 54, COTTAGE); setc(x, 59, COTTAGE)
# woods and boulders in the open fields, kept clear of streets and houses
near_town = set()
for x, y in roads:
    for dy in range(-3, 4):
        for dx in range(-3, 4):
            near_town.add((x + dx, y + dy))
for x, y in interior & harbor:
    if (x, y) in near_town or g[y][x] not in (GRASS, FLOWER, FLOWER2):
        continue
    r = h(x, y, 7)
    if r < .09:
        g[y][x] = TREE
    elif r < .11:
        g[y][x] = ROCKS
# pier out of the east side of town, with boats
y0 = 56
x = 124
while x < N - 1 and (x, y0) not in interior:
    x += 1
pier_start = None
for xx in range(124, N - 2):
    if g[y0][xx] in (WATER, WATER2, SHORE):
        pier_start = xx
        break
if pier_start:
    for xx in range(pier_start, min(pier_start + 7, N - 1)):
        g[56][xx] = PIER; g[57][xx] = PIER
    for xx in (pier_start + 2, pier_start + 5):
        if xx < N - 1:
            g[55][xx] = BOAT; g[58][xx] = BOAT

# ------------------------------------------------------------------ Citrus Grove
for x, y in interior & grove:
    r = h(x, y, 2)
    g[y][x] = FLOWER2 if r < .07 else FLOWER if r < .12 else GRASS
spring = (109, 112)   # pond sits just west of the path
grove_paths = set()
for y in range(95, 128):
    grove_paths |= {(112, y), (113, y)}               # down from the harbor neck to the spring and beyond
for x in range(86, 131):
    grove_paths |= {(x, 116), (x, 117)}               # west to the marsh bridge, east across the orchard
for x, y in grove_paths:
    setc(x, y, SAND)
for x in range(106, 112):                             # the spring itself, painted after the paths
    for y in range(110, 115):
        if ((x - 108.5) / 3) ** 2 + ((y - 112) / 2.6) ** 2 <= 1:
            setc(x, y, SPRING)
for x, y in interior & grove:
    if g[y][x] == GRASS or g[y][x] in (FLOWER, FLOWER2):
        if x % 3 == 0 and y % 3 == 1 and abs(x - 109) + abs(y - 112) > 6:
            g[y][x] = ORANGE_TREE
spring_event = (112, 115)                             # walk here (just south of the pond) to heal
# path over the neck from the harbor's main street down into the grove
neck = [(116, y) for y in range(88, 96)] + [(117, y) for y in range(88, 96)] + [(x, 95) for x in range(112, 118)] + [(x, 96) for x in range(112, 118)]
for x, y in neck:
    if g[y][x] not in (WATER, WATER2):
        g[y][x] = SAND

# ------------------------------------------------------------------ Rotting Marsh
for x, y in interior & marsh:
    r = h(x, y, 3)
    g[y][x] = REEDS if r < .06 else SWAMP_GRASS
ponds = [(24, 110, 4, 2.5), (36, 124, 5, 3), (52, 120, 4, 3), (60, 108, 3, 2.2), (30, 116, 2.5, 2)]
for cx, cy, rx, ry in ponds:
    for y in range(int(cy - ry) - 1, int(cy + ry) + 2):
        for x in range(int(cx - rx) - 1, int(cx + rx) + 2):
            if ((x - cx) / rx) ** 2 + ((y - cy) / ry) ** 2 <= 1:
                setc(x, y, LILY if h(x, y, 4) < .2 else SWAMP_WATER)
for x, y in interior & marsh:
    if g[y][x] == SWAMP_GRASS and h(x, y, 5) < .07:
        g[y][x] = SWAMP_TREE
marsh_paths = set()
for y in range(98, 121):
    marsh_paths |= {(41, y), (42, y)}                 # from the south bridge down into the marsh
for x in range(20, 76):
    marsh_paths |= {(x, 116), (x, 117)}               # across the marsh to the grove bridge
for y in range(116, 128):
    marsh_paths |= {(28, y), (29, y)}                 # down to the treasure
for x, y in marsh_paths:
    setc(x, y, SLIME if h(x, y, 6) < .06 else MUD)
chest = (28, 127)
setc(28, 127, MUD); setc(29, 127, MUD)

# ------------------------------------------------------------------ bridges
# east: from the island's east coast (rows 55-56) to Zest Harbor
for y in (55, 56):
    x = 85
    while (x, y) not in interior:
        g[y][x] = BOARDWALK
        x += 1
# south: from the beach (columns 41-42) down to the marsh
for x in (41, 42):
    y = 88
    while (x, y) not in interior:
        g[y][x] = BRIDGE
        y += 1
# marsh to grove (rows 116-117): planks from the marsh's last land tile to the grove's first
for y in (116, 117):
    west = max(x for (x, yy) in interior & marsh if yy == y)
    east = min(x for (x, yy) in interior & grove if yy == y)
    for xx in range(west + 1, east):
        g[y][xx] = BOARDWALK

# ------------------------------------------------------------------ checks
for (x, y) in [(21, 45), (58, 85), (63, 33), (77, 16), (81, 42), (22, 45), (57, 85), (77, 17), (81, 43), (64, 61)]:
    assert g[y][x] == orig[y][x], f'fixed spot {(x, y)} changed'
changed_old = [(x, y) for y in range(OLD) for x in range(OLD) if g[y][x] != orig[y][x]]
assert all((x, y) in {(85, 55), (85, 56), (41, 88), (42, 88)} or orig[y][x] in (WATER, WATER2) for x, y in changed_old), \
    [p for p in changed_old if orig[p[1]][p[0]] not in (WATER, WATER2)]
maze = [(x, y) for (x, y) in changed_old if 44 <= x <= 67 and 76 <= y <= 93]
assert not maze, f'touched the tree maze area: {maze}'


def reach(start):
    seen = {start}
    q = deque([start])
    while q:
        x, y = q.popleft()
        for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            n = (x + dx, y + dy)
            if 0 <= n[0] < N and 0 <= n[1] < N and n not in seen and g[n[1]][n[0]] not in COLLIDE:
                seen.add(n)
                q.append(n)
    return seen


r = reach((64, 61))
orig_reach = set()
seen = {(64, 61)}; q = deque([(64, 61)])
while q:
    x, y = q.popleft()
    for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
        n = (x + dx, y + dy)
        if 0 <= n[0] < OLD and 0 <= n[1] < OLD and n not in seen and orig[n[1]][n[0]] not in COLLIDE:
            seen.add(n); q.append(n)
assert seen <= r, 'something on the old island became unreachable'


def open_spot(cx, cy, size=2):
    """Nearest walkable spot with room for a 2x2-tile monster."""
    best = None
    for y in range(cy - 6, cy + 7):
        for x in range(cx - 6, cx + 7):
            if all((x + i, y + j) in r for i in range(size) for j in range(size)):
                d = abs(x - cx) + abs(y - cy)
                if best is None or d < best[0]:
                    best = (d, x, y)
    return best[1], best[2]


for name, p in (('spring event', spring_event), ('marsh chest', chest), ('harbor square', (115, 58)), ('lighthouse path', (117, 31))):
    assert p in r, f'{name} {p} not reachable'
# each bridge must work on its own: marsh reachable from the south beach,
# and grove reachable from the marsh, without any other route
def reach_without(start, blocked):
    saved = {p: g[p[1]][p[0]] for p in blocked}
    for (x, y) in blocked:
        g[y][x] = WATER
    out = reach(start)
    for (x, y), t in saved.items():
        g[y][x] = t
    return out
east_bridge = {(x, y) for y in (55, 56) for x in range(85, 105) if g[y][x] == BOARDWALK}
grove_bridge = {(x, y) for y in (116, 117) for x in range(60, 100) if g[y][x] == BOARDWALK}
assert chest in reach_without((41, 87), east_bridge | grove_bridge), 'south bridge does not reach the marsh'
assert spring_event in reach_without((41, 87), east_bridge), 'marsh-grove bridge is broken'
# the grove must also be reachable from the harbor without going through the marsh
g_saved = {(x, y): g[y][x] for x in range(70, 90) for y in (116, 117)}
for (x, y) in g_saved:
    g[y][x] = WATER
assert spring_event in reach((115, 58)), 'harbor and grove are not connected'
for (x, y), t in g_saved.items():
    g[y][x] = t
onions = [open_spot(22, 114), open_spot(33, 110), open_spot(48, 113)]
villagers = [open_spot(104, 113, 1), open_spot(119, 112, 1), open_spot(116, 121, 1)]
potatoes = [open_spot(55, 124), open_spot(40, 128)]
save('worldV5', g)
print('world is now', N, 'x', N)
print('healing spring event:', spring_event)
print('marsh chest:', chest)
print('marsh onions:', onions)
print('marsh potatoes:', potatoes)
print('grove villagers:', villagers)
print('reachable tiles from the start:', len(seen), '->', len(r))
