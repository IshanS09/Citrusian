"""Item and HUD icons (16x16, a couple at 15/17) for res/Objects."""
import os, sys
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker, sheet

WHITE = rgb('f7f7f7')
STEEL = rgb('cfd8dc'); STEEL_D = rgb('8fa1a8'); GOLD = rgb('e8b931'); GOLD_D = rgb('b8871c')
WOOD = rgb('9a6238'); WOOD_D = rgb('6e4226'); GRIP = rgb('6b3f22'); IRON = rgb('7c8088')
LEAF = rgb('5cae4a')


def done(c):
    c.outline()
    return c


# ---------------------------------------------------------------- weapons
def blade_icon(blade, blade_hi, blade_lo, guard, grip, pommel, curve=0):
    c = Canvas(16)
    for i in range(9):
        x, y = 5 + i, 10 - i
        off = 1 if (curve and 2 < i < 7) else 0
        c.set(x + off, y + off, blade, 'blade'); c.set(x + 1 + off, y + off, blade, 'blade')
    c.shade('blade', blade, hi=blade_hi, lo=blade_lo)
    c.pts([(2, 9), (3, 10), (4, 11), (5, 12), (6, 13)], guard, 'guard')
    c.pts([(3, 12), (2, 13)], grip, 'grip'); c.pts([(4, 12)], grip, 'grip')
    c.set(1, 14, pommel, 'pommel')
    return done(c)


def banana_blade():
    """A sword whose blade is a curved banana."""
    import math
    c = Canvas(16)
    yel = rgb('ffd54f')
    for i in range(40):
        t = i / 39
        x = 5 + 9 * t - 2.4 * math.sin(math.pi * t)
        y = 11 - 9 * t - 2.4 * math.sin(math.pi * t)
        c.set(round(x), round(y), yel, 'blade'); c.set(round(x) + 1, round(y), yel, 'blade')
    c.shade('blade', yel, hi=rgb('fff3b0'), lo=rgb('d9a520'))
    c.pts([(14, 2), (15, 1)], rgb('6d4c2f'), 'tip')
    c.pts([(2, 9), (3, 10), (4, 11), (5, 12), (6, 13)], rgb('6d4c2f'), 'guard')
    c.pts([(3, 12), (2, 13), (4, 12)], rgb('3f8f3a'), 'grip')
    c.set(1, 14, rgb('6d4c2f'), 'pommel')
    return done(c)


def axe_icon(head, head_hi, head_lo, handle=WOOD, extra=None):
    """Handle running bottom-left to top-right, crescent blade off its top-right side."""
    c = Canvas(16)
    c.poly([(9.5, 3.5), (12.5, 0.5), (15.5, 1.5), (15.8, 6), (14, 9.5), (12.5, 6.5)], head, 'head')
    c.shade('head', head, hi=head_hi, lo=head_lo)
    if extra:
        extra(c)
    c.line(2, 14, 12, 4, handle, 'handle'); c.line(3, 14, 12, 5, handle, 'handle')
    c.shade('handle', handle)
    return done(c)


def pick_icon(head, head_hi, head_lo, handle=WOOD, extra=None):
    c = Canvas(16)
    c.line(3, 13, 11, 5, handle, 'handle'); c.line(4, 13, 12, 5, handle, 'handle')
    c.shade('handle', handle)
    for x, y in ((5, 3), (6, 2), (7, 2), (8, 2), (9, 2), (10, 3), (11, 3), (12, 4), (13, 5), (13, 6), (14, 7), (14, 8),
                 (6, 3), (7, 3), (8, 3), (9, 3), (12, 5), (13, 7)):
        c.set(x, y, head, 'head')
    c.shade('head', head, hi=head_hi, lo=head_lo)
    if extra:
        extra(c)
    return done(c)


def watermelon_head(c):
    # green rind on the outer edge, seeds in the red flesh
    for y in range(16):
        for x in range(16):
            if c.tag[y][x] == 'head' and (x >= 15 or (x >= 14 and y >= 7)):
                c.set(x, y, rgb('3f8f3a'), 'rind')
    for x, y in ((13, 2), (14, 4), (13, 6)):
        if c.tag[y][x] == 'head':
            c.set(x, y, rgb('2b2b2b'), 'seed')


def pineapple_pick(c):
    for x, y in ((7, 2), (9, 2), (12, 4), (13, 6), (8, 3)):
        c.set(x, y, rgb('b8871c'), 'head')
    c.pts([(11, 4), (12, 3), (10, 4)], LEAF, 'leafy')


# ---------------------------------------------------------------- shields
def shield_icon(face, face_hi, face_lo, rim=STEEL, deco=None):
    c = Canvas(16)
    c.poly([(2, 2), (14, 2), (14, 8), (8, 15), (2, 8)], rim, 'rim')
    c.poly([(3.2, 3.2), (12.8, 3.2), (12.8, 8), (8, 13.4), (3.2, 8)], face, 'face')
    c.shade('rim', rim, hi=WHITE)
    c.shade('face', face, hi=face_hi, lo=face_lo)
    if deco:
        deco(c)
    return done(c)


def strawberry(c):
    for x, y in ((5, 5), (8, 5), (11, 5), (6, 8), (10, 8), (8, 10)):
        c.set(x, y, rgb('ffe28a'))
    c.pts([(5, 1), (6, 2), (7, 1), (8, 2), (9, 1), (10, 2), (11, 1)], LEAF, 'leaf')


def blueberry(c):
    c.pts([(7, 5), (8, 5), (9, 5), (8, 4), (8, 6)], darker(rgb('3b6fd1'), .45))


def grape(c):
    for x, y in ((6, 5), (9, 5), (12, 5), (7, 8), (10, 8), (8, 11)):
        c.ellipse(x, y, 1.3, 1.3, rgb('9b59d0'))
        c.set(x - 1, y - 1, rgb('c9a0f0'))
    c.pts([(8, 1), (9, 0)], rgb('6d4c2f'))


def dragonfruit(c):
    for x, y in ((4, 4), (8, 6), (12, 4), (6, 9), (10, 9)):
        c.pts([(x, y), (x + 1, y - 1)], rgb('7ccf5a'))


def plain_wood(c):
    c.line(8, 3, 8, 12, rgb('8a5a36'))
    c.set(8, 7, GOLD)


# ---------------------------------------------------------------- the helm (also shown big on the title screen)
def helmet():
    c = Canvas(16)
    c.ellipse(8, 7.5, 6.5, 6, GOLD, 'dome')
    c.rect(2, 8, 14, 13, GOLD, 'dome')
    c.shade('dome', GOLD, hi=rgb('fff1a8'), lo=GOLD_D)
    c.rect(5, 8, 11, 10, rgb('3a2a1e'), 'visor')
    c.rect(7, 10, 9, 13, rgb('3a2a1e'), 'visor')
    c.line(8, 1, 8, 6, rgb('f39a1e'), 'crest'); c.line(7, 2, 7, 5, rgb('f39a1e'), 'crest')
    c.shade('crest', rgb('f39a1e'))
    c.pts([(9, 1), (10, 0)], LEAF, 'leaf')
    c.pts([(4, 5), (5, 4)], WHITE)
    return done(c)


# ---------------------------------------------------------------- potions
def potion(liquid, extra=None):
    c = Canvas(16)
    c.ellipse(8, 10.5, 5, 4.6, liquid, 'liquid')
    c.rect(6, 3, 9, 6, rgb('dfe9f0'), 'neck')
    c.rect(6, 1, 9, 2, rgb('8a5a36'), 'cork')
    c.shade('liquid', liquid); c.shade('neck', rgb('dfe9f0')); c.shade('cork', rgb('8a5a36'))
    c.pts([(5, 9), (5, 10), (6, 8)], lighter(liquid, .6))
    if extra:
        extra(c)
    return done(c)


def flame_mark(c):
    c.pts([(8, 9), (7, 10), (8, 10), (9, 10), (7, 11), (8, 11), (9, 11), (8, 12)], rgb('ffd54f'))
    c.set(8, 11, rgb('fff3b0'))


# ---------------------------------------------------------------- keys and doors
def key_icon(metal, hi, lo):
    c = Canvas(16)
    c.ellipse(4.5, 5.5, 3.6, 3.6, metal, 'key')
    c.rect(6, 5, 14, 6, metal, 'key')
    c.rect(11, 7, 11, 9, metal, 'key'); c.rect(13, 7, 13, 8, metal, 'key')
    c.shade('key', metal, hi=hi, lo=lo)
    c.ellipse(4.5, 5.5, 1.3, 1.3, T)
    for y in range(16):
        for x in range(16):
            if c.g[y][x] == T:
                c.tag[y][x] = None
    return done(c)


def door_icon(wood, hi, lo, bands=None, studs=None):
    c = Canvas(16)
    c.ellipse(8, 6.5, 6.5, 6, wood, 'door')
    c.rect(1, 6, 14, 15, wood, 'door')
    for x in (4, 8, 12):
        for y in range(1, 16):
            if c.tag[y][x] == 'door':
                c.set(x, y, lo, 'door')
    c.shade('door', wood, hi=hi, lo=lo)
    if bands:
        for y in (5, 11):
            for x in range(16):
                if c.tag[y][x] == 'door':
                    c.set(x, y, bands, 'band')
    if studs:
        for x, y in ((3, 5), (13, 5), (3, 11), (13, 11)):
            c.set(x, y, studs)
    c.set(11, 9, GOLD); c.set(11, 10, darker(GOLD, .3))       # handle
    return done(c)


# ---------------------------------------------------------------- misc items
def chest(opened):
    c = Canvas(16)
    c.rect(1, 7, 14, 14, WOOD, 'box'); c.shade('box', WOOD, hi=lighter(WOOD, .2), lo=WOOD_D)
    if opened:
        c.rect(1, 2, 14, 5, darker(WOOD, .15), 'lid'); c.shade('lid', darker(WOOD, .15))
        c.rect(2, 7, 13, 8, rgb('2a1a10'), 'inside')
        c.pts([(5, 7), (6, 6), (9, 7), (10, 6)], GOLD)
    else:
        c.ellipse(8, 7, 7, 4, WOOD, 'lid'); c.rect(1, 6, 14, 7, WOOD, 'lid'); c.shade('lid', WOOD, hi=lighter(WOOD, .2), lo=WOOD_D)
        c.line(1, 7, 14, 7, GOLD_D, 'band')
    c.line(1, 11, 14, 11, GOLD_D, 'band')
    c.rect(7, 9, 8, 11, GOLD, 'lock')
    return done(c)


def coin():
    c = Canvas(16)
    c.ellipse(8, 8, 6, 6, GOLD, 'coin'); c.shade('coin', GOLD, hi=rgb('fff1a8'), lo=GOLD_D)
    c.ellipse(8, 8, 3.6, 3.6, darker(GOLD, .12))
    c.pts([(8, 5), (8, 6), (8, 7), (8, 8), (8, 9), (8, 10)], GOLD_D)          # leaf vein
    c.pts([(7, 7), (9, 7), (7, 9), (9, 9), (6, 8), (10, 8)], lighter(GOLD, .3))
    return done(c)


def lantern():
    c = Canvas(16)
    c.rect(4, 4, 11, 13, rgb('3a3a40'), 'frame')
    c.rect(5, 6, 10, 12, rgb('ffd36a'), 'glow')
    c.shade('frame', rgb('3a3a40'), hi_amt=.3); c.shade('glow', rgb('ffd36a'), hi=rgb('fff6c8'), lo=rgb('f0a23a'))
    c.pts([(7, 8), (8, 8), (7, 9), (8, 9), (7, 10), (8, 10)], rgb('fff6c8'))
    c.line(6, 2, 9, 2, rgb('3a3a40'), 'frame'); c.pts([(6, 3), (9, 3)], rgb('3a3a40'), 'frame')
    return done(c)


def tent():
    c = Canvas(16)
    c.poly([(0.5, 14.5), (8, 3), (15.5, 14.5)], rgb('4f8a3c'), 'tent'); c.shade('tent', rgb('4f8a3c'))
    c.poly([(6, 14.5), (8, 8), (10, 14.5)], rgb('23331c'), 'door')
    c.line(8, 1, 8, 3, rgb('6d4c2f'), 'pole'); c.pts([(9, 1), (10, 1), (9, 2)], rgb('e8b931'), 'flag')
    return done(c)


def dash():
    c = Canvas(16)
    for ox, col in ((0, rgb('5aa9e6')), (5, rgb('9fd2fa'))):
        c.poly([(2 + ox, 3), (5 + ox, 3), (10 + ox, 8), (5 + ox, 13), (2 + ox, 13), (7 + ox, 8)], col, 'chev%d' % ox)
        c.shade('chev%d' % ox, col)
    return done(c)


def speed():
    c = Canvas(16)
    c.poly([(9, 0.5), (3, 9), (7.5, 9), (5, 15.5), (13, 6), (8.5, 6), (11, 0.5)], rgb('ffd23f'), 'bolt')
    c.shade('bolt', rgb('ffd23f'), hi=rgb('fff3b0'), lo=rgb('e0a020'))
    return done(c)


def freeze():
    c = Canvas(15)
    ice = rgb('9fd8fb')
    c.line(7, 1, 7, 13, ice, 'f'); c.line(1, 7, 13, 7, ice, 'f'); c.line(2, 2, 12, 12, ice, 'f'); c.line(2, 12, 12, 2, ice, 'f')
    for x, y in ((6, 2), (8, 2), (6, 12), (8, 12), (2, 6), (2, 8), (12, 6), (12, 8)):
        c.set(x, y, ice, 'f')
    c.shade('f', ice, hi=WHITE, lo=rgb('5aa9e6'))
    c.set(7, 7, WHITE)
    return done(c)


def invis():
    c = Canvas(17)
    c.ellipse(8.5, 7.5, 6, 6.5, GOLD, 'frame'); c.shade('frame', GOLD, hi=rgb('fff1a8'), lo=GOLD_D)
    c.ellipse(8.5, 7.5, 4.3, 4.8, rgb('9fd2fa'), 'glass'); c.shade('glass', rgb('9fd2fa'), hi=WHITE, lo=rgb('5aa9e6'))
    c.pts([(6, 5), (7, 4), (6, 6)], WHITE)
    c.rect(7, 14, 10, 16, GOLD, 'handle'); c.shade('handle', GOLD, lo=GOLD_D)
    return done(c)


def heart(fill):
    """fill: 'full', 'half', 'blank'."""
    c = Canvas(16)
    red, empty = rgb('e53935'), rgb('5a4048')
    pts_shape = []
    tmp = Canvas(16)
    tmp.ellipse(5, 5.5, 3.8, 3.6, red); tmp.ellipse(11, 5.5, 3.8, 3.6, red)
    tmp.poly([(1.3, 6), (14.7, 6), (8, 14.5)], red)
    for y in range(16):
        for x in range(16):
            if tmp.g[y][x] != T:
                col = red if (fill == 'full' or (fill == 'half' and x < 8)) else empty
                c.set(x, y, col, 'red' if col == red else 'empty')
    c.shade('red', red, hi=rgb('ff8a80'), lo=rgb('b71c1c'))
    c.shade('empty', empty, hi_amt=.15)
    if fill != 'blank':
        c.pts([(4, 4), (5, 3)], WHITE)
    return done(c)


def regen():
    c = heart('full')
    c.pts([(8, 6), (8, 7), (8, 8), (8, 9), (8, 10), (6, 8), (7, 8), (9, 8), (10, 8)], rgb('b9f6a8'))
    c.pts([(13, 1), (13, 3), (12, 2), (14, 2)], rgb('b9f6a8'))
    return c


def seed_icon(full):
    """HUD fire seed: lit when you have it, grey when spent."""
    c = Canvas(16)
    if full:
        c.poly([(8, 0.5), (12.5, 7), (13, 11), (8, 15.5), (3, 11), (3.5, 7)], rgb('ff8a1f'), 'fire')
        c.shade('fire', rgb('ff8a1f'), hi=rgb('ffd54f'), lo=rgb('e8432a'))
        c.ellipse(8, 10.5, 3.3, 3.6, rgb('ffd54f'), 'inner')
        c.ellipse(8, 11, 1.8, 2.2, rgb('6b4423'), 'seed'); c.shade('seed', rgb('6b4423'))
    else:
        c.poly([(8, 0.5), (12.5, 7), (13, 11), (8, 15.5), (3, 11), (3.5, 7)], rgb('6a6670'), 'fire')
        c.shade('fire', rgb('6a6670'), hi_amt=.15)
        c.ellipse(8, 11, 1.8, 2.2, rgb('4a4650'), 'seed')
    return done(c)


def fruit_basket():
    c = Canvas(16)
    c.ellipse(5, 7, 2.5, 2.5, rgb('e53935'), 'a'); c.ellipse(10, 6, 2.6, 2.6, rgb('f39a1e'), 'o'); c.ellipse(8, 8, 2.2, 2.2, rgb('ffd23f'), 'l')
    for t, col in (('a', rgb('e53935')), ('o', rgb('f39a1e')), ('l', rgb('ffd23f'))):
        c.shade(t, col)
    c.poly([(1, 9), (15, 9), (13, 14), (3, 14)], rgb('b07a42'), 'basket'); c.shade('basket', rgb('b07a42'))
    for x in (4, 7, 10, 13):
        c.line(x, 10, x - 1, 13, rgb('8a5a36'), 'basket')
    return done(c)


# ---------------------------------------------------------------- fishing
def fishing_rod():
    """Bamboo rod running bottom-left to top-right, reel near the grip, line hanging off the tip."""
    c = Canvas(16)
    bamboo = rgb('c9a45a')
    c.line(2, 14, 13, 2, bamboo, 'rod'); c.line(3, 14, 13, 3, bamboo, 'rod')
    c.shade('rod', bamboo, hi=rgb('ecd59a'), lo=rgb('8f6f32'))
    c.pts([(5, 10), (8, 7), (11, 4)], rgb('8f6f32'), 'node')  # bamboo joints
    c.pts([(1, 15), (2, 15), (1, 14)], rgb('6b3f22'), 'grip')
    c.ellipse(5.5, 12.5, 1.8, 1.8, IRON, 'reel'); c.shade('reel', IRON)
    c.set(5, 12, STEEL)
    c.line(14, 2, 14, 10, rgb('e8e8e8'))
    c.pts([(14, 11), (13, 11)], rgb('e53935'), 'bob'); c.set(14, 12, WHITE, 'bob')
    return done(c)


def fish_icon(body, belly, fin, eye=K, spots=None, long=False):
    c = Canvas(16)
    if long:
        c.ellipse(8, 8, 6.8, 2.4, body, 'body')
        c.poly([(1.5, 8), (0, 5.5), (0, 10.5)], fin, 'tail')
    else:
        c.ellipse(8.5, 8, 5.5, 3.6, body, 'body')
        c.poly([(3.5, 8), (0.5, 4.5), (0.5, 11.5)], fin, 'tail')
        c.poly([(7, 4.6), (10, 2.5), (11, 5)], fin, 'fin')
    for y in range(16):
        for x in range(16):
            if c.tag[y][x] == 'body' and y >= 9:
                c.set(x, y, belly, 'belly')
    c.shade('body', body); c.shade('belly', belly, hi_amt=.1)
    c.shade('tail', fin); c.shade('fin', fin)
    if spots:
        spots(c)
    c.set(12 if not long else 13, 7, eye)
    return done(c)


def carp_spots(c):
    c.pts([(7, 6), (9, 7), (8, 9)], rgb('fff1a8'))


def eel_spots(c):
    c.pts([(4, 7), (7, 8), (10, 7)], rgb('2f4a2a'))


def old_boot():
    c = Canvas(16)
    leather = rgb('7a5232')
    c.rect(4, 2, 9, 10, leather, 'leg')
    c.poly([(4, 9), (14.5, 10), (15, 13.5), (4, 13.5)], leather, 'foot')
    c.shade('leg', leather); c.shade('foot', leather)
    c.rect(3, 14, 15, 14, rgb('3a2a1e'), 'sole')
    c.pts([(6, 4), (7, 6), (6, 8)], rgb('c8b48a'))  # laces
    c.pts([(12, 11), (10, 12)], rgb('5cae4a'))       # pond weed
    c.set(5, 1, rgb('5cae4a'))
    return done(c)


ITEMS = {
    'DefaultSword': lambda: blade_icon(STEEL, WHITE, STEEL_D, GOLD, GRIP, rgb('d62828')),
    'BananaBlade': lambda: banana_blade(),
    'Axe': lambda: axe_icon(IRON, lighter(IRON, .45), darker(IRON, .3)),
    'WatermelonSmasher': lambda: axe_icon(rgb('e84a5f'), rgb('ff8a9a'), rgb('3f8f3a'), extra=watermelon_head),
    'Picaxe': lambda: pick_icon(IRON, lighter(IRON, .45), darker(IRON, .3), handle=rgb('b5a03a')),
    'PP': lambda: pick_icon(rgb('f2c43a'), rgb('fff1a8'), rgb('b8871c'), extra=pineapple_pick),
    'DefaultShield': lambda: shield_icon(WOOD, lighter(WOOD, .2), WOOD_D, deco=plain_wood),
    'BlueShield': lambda: shield_icon(rgb('3b6fd1'), rgb('7ea6f0'), rgb('274d99'), deco=blueberry),
    'AppleShield': lambda: shield_icon(rgb('e53935'), rgb('ff8a80'), rgb('b71c1c'), deco=strawberry),
    'GrapeShield': lambda: shield_icon(rgb('6a3d9a'), rgb('9b6fd0'), rgb('4a2670'), rim=GOLD, deco=grape),
    'DragonfruitShield': lambda: shield_icon(rgb('e0408a'), rgb('ff8ac0'), rgb('a82a66'), deco=dragonfruit),
    'CitrusianHelmet': helmet,
    'Potion': lambda: potion(rgb('3b8fe0')),
    'firepotion': lambda: potion(rgb('e8432a'), extra=flame_mark),
    'key': lambda: key_icon(GOLD, rgb('fff1a8'), GOLD_D),
    'bronzeKey': lambda: key_icon(rgb('c98a4a'), rgb('e8b98a'), rgb('8a5a2a')),
    'silverKey': lambda: key_icon(rgb('c9d1d6'), WHITE, rgb('8a959b')),
    'Door': lambda: door_icon(rgb('9a6238'), rgb('b47a4a'), rgb('7a4a28'), bands=rgb('5a5a62')),
    'bronzeDoor': lambda: door_icon(rgb('b97a3e'), rgb('d89a5e'), rgb('8a5a2a'), bands=rgb('8a5a2a'), studs=rgb('e8b98a')),
    'goldDoor': lambda: door_icon(rgb('e0ae2e'), rgb('fff1a8'), rgb('b8871c'), studs=rgb('fff1a8')),
    'sliverDoor': lambda: door_icon(rgb('aeb7bd'), rgb('e2e8eb'), rgb('7c868c'), bands=rgb('7c868c')),
    'ironDoor': lambda: door_icon(rgb('5a5e66'), rgb('7c8088'), rgb('3a3d42'), bands=rgb('3a3d42'), studs=rgb('9aa0a8')),
    'chest': lambda: chest(False),
    'openedChest': lambda: chest(True),
    'coin': coin,
    'lantern': lantern,
    'tent': tent,
    'dash': dash,
    'speedboost': speed,
    'freeze': freeze,
    'invis': invis,
    'heart_full': lambda: heart('full'),
    'heart_half': lambda: heart('half'),
    'heart_blank': lambda: heart('blank'),
    'regeneration': regen,
    'Full': lambda: seed_icon(True),
    'Empty': lambda: seed_icon(False),
    'fruitBasket': fruit_basket,
    'fishingRod': fishing_rod,
    'minnow': lambda: fish_icon(rgb('8fb3c9'), rgb('dfe9ef'), rgb('6a8ea6')),
    'sunfish': lambda: fish_icon(rgb('f39a1e'), rgb('ffd54f'), rgb('3b8fe0')),
    'marshEel': lambda: fish_icon(rgb('5f7a3a'), rgb('a8b86a'), rgb('42572a'), spots=eel_spots, long=True),
    'goldenCarp': lambda: fish_icon(rgb('e8b931'), rgb('fff1a8'), rgb('e8432a'), spots=carp_spots),
    'oldBoot': old_boot,
}


def build(out):
    os.makedirs(out, exist_ok=True)
    made = {}
    for name, fn in ITEMS.items():
        made[name] = fn()
        made[name].save(os.path.join(out, name + '.png'))
    return made


if __name__ == '__main__':
    m = build(sys.argv[1])
    sheet([m[k].image() for k in sorted(m, key=str.lower)], os.path.join(sys.argv[1], '..', 'items_sheet.png'), scale=5, cols=10)
    print(len(m), 'icons')
