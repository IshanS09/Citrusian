"""Map tiles (16x16) and interactive tiles for Citrusian.

Ground tiles are built so they repeat without visible seams. Objects (trees,
rocks, furniture) are drawn on top of the matching ground with the same
outline-and-shading style as the characters.
"""
import os, sys, random
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker, mix, sheet

S = 16

GRASS = rgb('5aa64a'); GRASS_D = rgb('4a9440'); GRASS_L = rgb('72bd58'); BLADE = rgb('3c7f36')
SAND = rgb('dcbc80'); SAND_D = rgb('c9a56a'); SAND_L = rgb('ead1a0')
DIRT = rgb('9b6b42'); DIRT_D = rgb('83582f'); DIRT_L = rgb('b07e52')
PATH = rgb('8a6446'); PATH_D = rgb('6f4e36')
WATER = rgb('3c8fd4'); WATER_D = rgb('3279be'); WATER_L = rgb('7fbdf0'); FOAM = rgb('cfe8fb')
STONE = rgb('8b8f96'); STONE_D = rgb('6a6e75'); STONE_L = rgb('a9adb3'); MORTAR = rgb('55585e')
WOOD = rgb('9a6238'); WOOD_D = rgb('7a4a28'); WOOD_L = rgb('b47a4a')
LEAF = rgb('3f8f3c'); LEAF_D = rgb('2f7030'); LEAF_L = rgb('5cae4a')
TRUNK = rgb('6b4428')


def noisy(base, specks, seed):
    """Fill a tile with base color and scattered specks: [(color, chance), ...]."""
    rnd = random.Random(seed)
    c = Canvas(S)
    for y in range(S):
        for x in range(S):
            col = base
            r = rnd.random()
            acc = 0
            for sc, chance in specks:
                acc += chance
                if r < acc:
                    col = sc
                    break
            c.set(x, y, col, 'ground')
    return c


def grass(seed=1):
    c = noisy(GRASS, [(GRASS_D, .16), (GRASS_L, .06)], seed)
    for bx, by in ((3, 4), (11, 2), (7, 10), (13, 12), (1, 13)):
        c.pts([(bx, by), (bx + 1, by - 1), (bx + 2, by)], BLADE)
    return c


def flowers(kind):
    c = grass(seed=7 if kind == 1 else 11)
    if kind == 1:  # small scattered flowers
        for (x, y), col in zip(((3, 3), (11, 5), (6, 11), (13, 12), (2, 8)),
                               (rgb('f06292'), rgb('fff4f4'), rgb('64b5f6'), rgb('ffd54f'), rgb('f06292'))):
            c.pts([(x, y - 1), (x - 1, y), (x + 1, y), (x, y + 1)], col)
            c.set(x, y, rgb('ffe082') if col != rgb('ffd54f') else rgb('ff8f00'))
    else:  # a clump of yellow flowers
        for x, y in ((5, 5), (10, 7), (6, 11), (11, 12)):
            c.ellipse(x + .5, y + .5, 2.2, 2.2, rgb('ffd54f'), 'petal')
        c.shade('petal', rgb('ffd54f'))
        for x, y in ((5, 5), (10, 7), (6, 11), (11, 12)):
            c.set(x, y, rgb('f57c00'))
    return c


def tree():
    c = grass(seed=3)
    c.ellipse(8.5, 13.5, 5.5, 2, mix(GRASS_D, K, .25))          # shadow
    c.rect(7, 10, 9, 14, TRUNK, 'trunk'); c.shade('trunk', TRUNK)
    c.ellipse(8.5, 6.5, 6.8, 5.8, LEAF, 'leaf'); c.shade('leaf', LEAF, hi=LEAF_L, lo=LEAF_D)
    for x, y in ((5, 4), (9, 3), (11, 6), (6, 8), (8, 6)):
        c.set(x, y, LEAF_L, 'leaf')
    for x, y in ((10, 9), (4, 7), (12, 8)):
        c.set(x, y, LEAF_D, 'leaf')
    out = Canvas(S); out.g = [r[:] for r in c.g]
    # outline only the tree, not the grass
    top = Canvas(S)
    for y in range(S):
        for x in range(S):
            if c.tag[y][x] in ('leaf', 'trunk'):
                top.set(x, y, c.g[y][x], c.tag[y][x])
    top.outline()
    for y in range(S):
        for x in range(S):
            if top.g[y][x] == K:
                c.set(x, y, K)
    return c


def overlay(ground, obj):
    """Draw an outlined object canvas on top of a ground tile."""
    obj.outline()
    for y in range(S):
        for x in range(S):
            if obj.g[y][x] != T:
                ground.set(x, y, obj.g[y][x])
    return ground


def mountain():
    rock = Canvas(S)
    rock.poly([(1, 15.5), (5, 5), (8, 2), (11, 6), (15, 15.5)], STONE, 'rock')
    rock.shade('rock', STONE, hi=STONE_L, lo=STONE_D)
    rock.line(8, 3, 6, 9, STONE_L, 'rock'); rock.line(9, 6, 12, 13, STONE_D, 'rock')
    rock.pts([(7, 3), (8, 3), (9, 3)], rgb('eef3f6'), 'snow')
    g = grass(seed=5)
    return overlay(g, rock)


def sand():
    c = noisy(SAND, [(SAND_D, .14), (SAND_L, .08)], 21)
    c.pts([(4, 6), (12, 11)], rgb('b8945a'))
    return c


def path():
    c = noisy(PATH, [(PATH_D, .18), (lighter(PATH, .15), .08)], 31)
    for x, y in ((3, 4), (11, 9), (6, 13)):
        c.pts([(x, y), (x + 1, y)], STONE_L); c.set(x, y + 1, STONE_D)
    return c


def dirt():
    c = noisy(DIRT, [(DIRT_D, .12), (DIRT_L, .08)], 41)
    c.line(2, 3, 6, 6, DIRT_D); c.line(6, 6, 6, 9, DIRT_D)
    c.line(10, 11, 14, 13, DIRT_D); c.line(11, 2, 13, 4, DIRT_D)
    return c


def water(kind):
    c = noisy(WATER, [(WATER_D, .1)], 51 + kind)
    if kind == 0:
        for x, y in ((3, 4), (10, 10)):
            c.pts([(x, y), (x + 1, y), (x + 2, y)], WATER_L)
    elif kind == 1:
        for x, y in ((2, 3), (9, 6), (4, 11), (11, 13)):
            c.pts([(x, y), (x + 1, y), (x + 2, y), (x + 3, y)], WATER_L)
            c.set(x + 1, y, FOAM)
    else:  # sparkly shallows
        c = noisy(lighter(WATER, .15), [(WATER, .2), (WATER_L, .12)], 61)
        for x, y in ((3, 3), (12, 5), (7, 9), (2, 13), (13, 13)):
            c.pts([(x, y), (x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)], FOAM)
            c.set(x, y, rgb('ffffff'))
    return c


def bricks(base, dark, light, mortar, rows=4, seed=71):
    rnd = random.Random(seed)
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, mortar)
    h = S // rows
    for r in range(rows):
        off = 0 if r % 2 == 0 else 4
        y0 = r * h
        for bx in range(-8, S, 8):
            x0 = bx + off
            col = mix(base, light if rnd.random() < .5 else dark, .12)
            for y in range(y0, y0 + h - 1):
                for x in range(x0, x0 + 7):
                    if 0 <= x < S:
                        c.set(x, y, col)
            for x in range(x0, x0 + 7):
                if 0 <= x < S:
                    c.set(x, y0, mix(col, rgb('ffffff'), .1))
    return c


def cobbles(seed=81):
    rnd = random.Random(seed)
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, MORTAR)
    for cx, cy in ((3, 3), (10, 2), (14, 7), (6, 8), (2, 12), (10, 13), (15, 14), (0, 7)):
        col = mix(STONE, STONE_L if rnd.random() < .5 else STONE_D, .3)
        for y in range(cy - 2, cy + 3):
            for x in range(cx - 3, cx + 3):
                if ((x - cx + .5) / 3) ** 2 + ((y - cy) / 2.4) ** 2 <= 1:
                    c.set(x % S, y % S, col)
        c.set(cx % S, (cy - 1) % S, STONE_L)
    return c


def planks(base, dark, light, vertical=False, seed=91):
    rnd = random.Random(seed)
    c = Canvas(S)
    for i in range(4):
        col = mix(base, light if rnd.random() < .5 else dark, .2)
        for a in range(i * 4, i * 4 + 4):
            for b in range(S):
                x, y = (a, b) if vertical else (b, a)
                c.set(x, y, col)
        for b in range(S):
            x, y = (i * 4 + 3, b) if vertical else (b, i * 4 + 3)
            c.set(x, y, dark)
        seam = rnd.randint(2, 13)
        for a in range(i * 4, i * 4 + 3):
            x, y = (a, seam) if vertical else (seam, a)
            c.set(x, y, dark)
        nx, ny = ((i * 4 + 1, (seam + 5) % S) if vertical else ((seam + 5) % S, i * 4 + 1))
        c.set(nx, ny, darker(dark, .3))
    return c


def floor():
    return planks(rgb('8f5a36'), rgb('6e4226'), rgb('a86c43'), seed=101)


def boardwalk():
    return planks(WOOD, WOOD_D, WOOD_L, seed=111)


def bridge():
    c = planks(WOOD_L, WOOD_D, lighter(WOOD_L, .15), vertical=True, seed=121)
    return c


def wall():
    return bricks(rgb('7b7f86'), STONE_D, STONE_L, rgb('62656b'))


def house():
    g = grass(seed=131)
    h = Canvas(S)
    h.rect(3, 8, 12, 14, WOOD, 'wall'); h.shade('wall', WOOD, hi=WOOD_L, lo=WOOD_D)
    for y in (10, 12):
        for x in range(3, 13):
            h.set(x, y, WOOD_D, 'wall')
    h.poly([(1, 9), (8, 2.5), (15, 9)], rgb('b7463a'), 'roof'); h.shade('roof', rgb('b7463a'))
    h.rect(7, 11, 9, 14, rgb('4a2c18'), 'door')
    h.set(4, 10, rgb('ffe28a'), 'win'); h.set(11, 10, rgb('ffe28a'), 'win')
    return overlay(g, h)


def village():
    """Stone tower with a red-and-white flag (blocks movement)."""
    g = grass(seed=141)
    t = Canvas(S)
    t.rect(4, 6, 12, 15, STONE, 'tower'); t.shade('tower', STONE, hi=STONE_L, lo=STONE_D)
    for x in (4, 6, 8, 10, 12):
        t.rect(x, 4, x, 5, STONE, 'tower')
    t.rect(7, 11, 9, 15, rgb('3a2a1e'), 'door')
    t.line(8, 0, 8, 4, rgb('5d3a22'), 'pole')
    for i, (x, y) in enumerate(((9, 0), (10, 0), (11, 0), (9, 1), (10, 1), (11, 1), (9, 2), (10, 2))):
        t.set(x, y, rgb('e53935') if (x + y) % 2 == 0 else rgb('f5f5f5'), 'flag')
    return overlay(g, t)


def castle():
    """Mossy castle gate (walkable entrance)."""
    c = bricks(rgb('5f6a5a'), rgb('4b5547'), rgb('77836f'), rgb('3a4237'), seed=151)
    for x, y in ((2, 3), (3, 3), (12, 6), (13, 6), (5, 12), (14, 11)):
        c.set(x, y, rgb('6f9a45'))
    gate = Canvas(S)
    gate.ellipse(8, 7.5, 4.5, 4.5, rgb('2a1e18'), 'gate'); gate.rect(4, 8, 12, 15, rgb('2a1e18'), 'gate')
    for x in (5, 7, 9, 11):
        gate.line(x, 4, x, 15, rgb('5a5a62'), 'bars')
    return overlay(c, gate)


def pirate():
    """Skull and crossbones sign on dark wood."""
    c = planks(rgb('3b2b2a'), rgb('2b1e1d'), rgb('4b3634'), seed=161)
    s = Canvas(S)
    bone = rgb('ece6d6')
    s.line(3, 12, 12, 3, bone, 'bone'); s.line(3, 3, 12, 12, bone, 'bone')
    s.ellipse(8, 7, 3.6, 3.4, bone, 'skull'); s.rect(6, 9, 9, 11, bone, 'skull')
    overlay(c, s)
    c.pts([(6, 7), (9, 7)], K); c.pts([(6, 6), (9, 6)], K); c.set(7, 9, K); c.set(8, 9, K)
    return c


def flagpole():
    """Ship's mast seen from above: a round post on the deck (blocks movement)."""
    c = boardwalk()
    m = Canvas(S)
    m.ellipse(8, 8, 4.5, 4.5, rgb('6b4428'), 'mast'); m.shade('mast', rgb('6b4428'))
    m.ellipse(8, 8, 2, 2, rgb('8a5a36'), 'top')
    return overlay(c, m)


def furniture(kind):
    c = floor()
    f = Canvas(S)
    if kind == 'drawer':
        f.rect(2, 2, 13, 14, rgb('b07a42'), 'box'); f.shade('box', rgb('b07a42'))
        for y in (6, 10):
            f.line(3, y, 12, y, rgb('7a4f26'), 'box')
        for y in (4, 8, 12):
            f.set(7, y, rgb('e8b931'), 'knob'); f.set(8, y, rgb('e8b931'), 'knob')
    elif kind == 'table':
        f.ellipse(8, 7.5, 6.5, 5, rgb('c79a5e'), 'top'); f.shade('top', rgb('c79a5e'))
        f.rect(3, 11, 4, 14, rgb('7a4f26'), 'leg'); f.rect(11, 11, 12, 14, rgb('7a4f26'), 'leg')
        f.pts([(7, 6), (8, 6), (9, 7)], rgb('e9f2f9'), 'cup')
    elif kind == 'bookshelf':
        f.rect(1, 1, 14, 14, rgb('7a4f26'), 'shelf'); f.shade('shelf', rgb('7a4f26'))
        cols = [rgb('c0392b'), rgb('2e86c1'), rgb('27ae60'), rgb('f1c40f'), rgb('8e44ad'), rgb('e67e22')]
        for row, y0 in enumerate((3, 9)):
            x = 3
            i = row * 2
            while x < 13:
                w = 1 + (i % 2)
                f.rect(x, y0, x + w - 1, y0 + 3, cols[i % len(cols)], 'book')
                x += w + 1 if i % 3 == 0 else w
                i += 1
    elif kind == 'bed':
        f.rect(2, 1, 13, 14, rgb('8a5a36'), 'frame'); f.shade('frame', rgb('8a5a36'))
        f.rect(3, 6, 12, 13, rgb('c0504d'), 'blanket'); f.shade('blanket', rgb('c0504d'))
        f.rect(4, 2, 11, 4, rgb('f4f1ea'), 'pillow'); f.shade('pillow', rgb('f4f1ea'))
        f.line(3, 8, 12, 8, rgb('e57373'), 'blanket')
    return overlay(c, f)


def throne(kind):
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, rgb('6b2a2a'))
    c.rect(4, 0, 11, 15, rgb('b3262b'))                       # red carpet
    c.line(4, 0, 4, 15, rgb('e8b931')); c.line(11, 0, 11, 15, rgb('e8b931'))
    t = Canvas(S)
    gold = rgb('e0ae2e')
    if kind == 1:   # throne back
        t.rect(3, 1, 12, 13, gold, 'back'); t.shade('back', gold)
        t.rect(6, 3, 9, 11, rgb('b3262b'), 'cushion'); t.shade('cushion', rgb('b3262b'))
        t.pts([(3, 0), (7, 0), (8, 0), (12, 0)], gold, 'back')
        t.set(7, 2, rgb('5ec8e8')); t.set(8, 2, rgb('5ec8e8'))
    else:           # throne seat and arms
        t.rect(2, 2, 13, 11, gold, 'seat'); t.shade('seat', gold)
        t.rect(5, 2, 10, 9, rgb('b3262b'), 'cushion'); t.shade('cushion', rgb('b3262b'))
    return overlay(c, t)


def stairs(down):
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, rgb('1a1a1e'))
    for i in range(5):
        y = 1 + i * 3
        shade = mix(STONE_L, rgb('1a1a1e'), i / 5 if down else (4 - i) / 5)
        c.rect(1, y, 14, y + 1, shade)
        c.line(1, y + 2, 14, y + 2, darker(shade, .4))
    return c


def void():
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, rgb('050506'))
    return c


# ------------------------------------------------------------ interactive tiles
def dry_tree():
    """Cuttable tree (drawn on top of the ground, transparent background)."""
    c = Canvas(S)
    c.rect(7, 10, 9, 15, TRUNK, 'trunk'); c.shade('trunk', TRUNK)
    c.ellipse(8.5, 6.5, 6.5, 6, rgb('8cb04a'), 'leaf'); c.shade('leaf', rgb('8cb04a'))
    for x, y in ((5, 4), (10, 3), (12, 7), (7, 8)):
        c.set(x, y, rgb('a9c862'), 'leaf')
    c.pts([(6, 9), (10, 9)], rgb('5d4a2a'), 'leaf')
    c.outline()
    return c


def trunk():
    c = Canvas(S)
    c.ellipse(8, 13, 4, 2.5, TRUNK, 'stump'); c.shade('stump', TRUNK)
    c.ellipse(8, 12.5, 2.5, 1.4, rgb('c99a64'), 'ring')
    c.set(8, 12, rgb('a5784a'), 'ring')
    c.outline()
    return c


def destructible_wall():
    c = bricks(rgb('6e7268'), rgb('575b53'), rgb('878b80'), rgb('3e423b'), seed=171)
    c.line(4, 2, 7, 7, rgb('2b2d29')); c.line(7, 7, 6, 11, rgb('2b2d29')); c.line(7, 7, 11, 9, rgb('2b2d29'))
    for x, y in ((2, 12), (13, 3), (12, 13)):
        c.set(x, y, rgb('6f9a45'))
    return c


def metal_plate():
    c = Canvas(S)
    c.rect(0, 0, S - 1, S - 1, rgb('4a4d52'))
    c.rect(1, 1, 14, 14, rgb('7d828a'), 'plate'); c.shade('plate', rgb('7d828a'))
    for x, y in ((3, 3), (12, 3), (3, 12), (12, 12)):
        c.set(x, y, rgb('c9cdd3')); c.set(x + 1, y + 1, rgb('3a3c40'))
    return c


TILES = {
    'grass': grass, 'flower': lambda: flowers(1), 'flower2': lambda: flowers(2), 'tree': tree,
    'mountain': mountain, 'road00': sand, 'path': path, 'dirt': dirt, 'bridge': bridge,
    'boardwalk': boardwalk, 'water00': lambda: water(0), 'water01': lambda: water(1), 'water02': lambda: water(2),
    'floor': floor, 'wall': wall, 'house': house, 'village': village, 'castle': castle, 'pirate': pirate,
    'flagpole': flagpole, 'drawer': lambda: furniture('drawer'), 'table': lambda: furniture('table'),
    'bookshelf': lambda: furniture('bookshelf'), 'bed': lambda: furniture('bed'),
    'throne1': lambda: throne(1), 'throne2': lambda: throne(2),
    'stairsdown': lambda: stairs(True), 'stairsup': lambda: stairs(False), 'void': void,
}
INTERACTIVE = {'treedry': dry_tree, 'trunk': trunk, 'destructibleWall': destructible_wall, 'metalPlate': metal_plate}


def build(out_tiles, out_itiles):
    os.makedirs(out_tiles, exist_ok=True)
    os.makedirs(out_itiles, exist_ok=True)
    made = {}
    for name, fn in TILES.items():
        made[name] = fn()
        made[name].save(os.path.join(out_tiles, name + '.png'))
    for name, fn in INTERACTIVE.items():
        made['it_' + name] = fn()
        made['it_' + name].save(os.path.join(out_itiles, name + '.png'))
    return made


if __name__ == '__main__':
    m = build(sys.argv[1], sys.argv[2])
    sheet([m[k].image() for k in m], os.path.join(sys.argv[1], '..', 'tiles_sheet.png'), scale=6, cols=11, bg=(255, 0, 255, 255))
    print(len(m), 'tiles')
