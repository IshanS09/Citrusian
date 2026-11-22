"""Re-tile the dungeon (dungeonFIN), Malibroc's lair (dungeonL2) and the Pea King's
throne room (interiorPK3) with their own themed tiles.

Walls get a front face when floor is below them, floors get variants and a
shadow under walls. Every tile keeps its original walk/block setting, so the
rock puzzle, events and routes behave exactly as before. The Pea King's room
also gets 4 pillars and 4 slime puddles on spots checked to be off the paths.
Run from the repo root: python3 tools/area_maps.py
"""
WALL, FLOOR, STAIRS_DOWN, STAIRS_UP = 5, 19, 20, 21
(D_TOP, D_FACE, D_TORCH, D_FLOOR, D_MOSS, D_CRACK, D_SHADOW,
 M_TOP, M_FACE, M_EMBER_WALL, M_FLOOR, M_EMBER, M_ASH, M_SHADOW,
 SWAMP_TREE, MUD, ROYAL, ROYAL_MOSS, ROT_WALL, CARPET, PILLAR, SLIME, SWAMP_GRASS) = range(42, 65)


def load(path):
    return [[int(v) for v in line.replace(',', ' ').split()] for line in open(path) if line.strip()]


def save(path, rows):
    # same format as the originals: trailing comma on every line, blank line at the end
    with open(path, 'w') as f:
        f.write(''.join(','.join(str(v) for v in r) + ',\n' for r in rows) + '\n')


def h(x, y):
    return (x * 73856093 ^ y * 19349663) % 100


def dungeon_style(rows, top, face, torch, floor, variants, shadow, torch_every=11):
    src = [r[:] for r in rows]
    is_floor = lambda x, y: 0 <= y < len(src) and src[y][x] in (FLOOR, STAIRS_DOWN, STAIRS_UP)
    for y, r in enumerate(src):
        for x, t in enumerate(r):
            if t == WALL:
                if is_floor(x, y + 1):
                    rows[y][x] = torch if (x * 7 + y * 13) % torch_every == 0 else face
                else:
                    rows[y][x] = top
            elif t == FLOOR:
                if y > 0 and src[y - 1][x] == WALL:
                    rows[y][x] = shadow
                else:
                    v = h(x, y)
                    rows[y][x] = floor
                    acc = 0
                    for tile, pct in variants:
                        acc += pct
                        if v < acc:
                            rows[y][x] = tile
                            break
    return rows


# --- the dungeon with the rock puzzle
p = 'src/res/maps/dungeonFIN.txt'
save(p, dungeon_style(load(p), D_TOP, D_FACE, D_TORCH, D_FLOOR, [(D_MOSS, 10), (D_CRACK, 8)], D_SHADOW))

# --- Malibroc's lair
p = 'src/res/maps/dungeonL2.txt'
save(p, dungeon_style(load(p), M_TOP, M_FACE, M_EMBER_WALL, M_FLOOR, [(M_EMBER, 9), (M_ASH, 8)], M_SHADOW, torch_every=7))

# --- the Pea King's rotten throne room
p = 'src/res/maps/interiorPK3.txt'
rows = load(p)
swap = {3: SWAMP_TREE, 19: MUD, 7: ROT_WALL, 23: CARPET, 4: SWAMP_GRASS,
        5: ROT_WALL, 2: ROYAL_MOSS}   # stray stone/plank bits: same walk/block, matching look
for y, r in enumerate(rows):
    for x, t in enumerate(r):
        if t in swap:
            r[x] = swap[t]
        elif t == 26:
            r[x] = ROYAL_MOSS if h(x, y) < 15 else ROYAL
for x, y in ((42, 29), (60, 30), (44, 32), (58, 32)):
    assert rows[y][x] in (ROYAL, ROYAL_MOSS), (x, y)
    rows[y][x] = PILLAR
for x, y in ((47, 31), (55, 31), (46, 34), (56, 34)):
    assert rows[y][x] in (ROYAL, ROYAL_MOSS), (x, y)
    rows[y][x] = SLIME
save(p, rows)
print('re-tiled dungeonFIN, dungeonL2, interiorPK3')
