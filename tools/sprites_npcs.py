"""NPCs: the old apple warrior (24x24), the lemon merchant (16x16) and the big rock (16x16)."""
import os, sys
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker, sheet

WHITE = rgb('f2f2f2')
APPLE = rgb('d93a32'); BEARD = rgb('c9c6c0'); STICK = rgb('7b4a2a'); LEAF = rgb('5cae4a'); STEM = rgb('5d3a22')
SCAR = rgb('7a1a1a')


def apple(view, frame):
    c = Canvas(24)
    side = view == 'left'
    step = frame == 2
    # little legs
    if side:
        if step:
            c.rect(11, 19, 12, 21, darker(APPLE, .35), 'leg')
        else:
            c.rect(9, 19, 10, 21, darker(APPLE, .35), 'leg'); c.rect(13, 19, 14, 21, darker(APPLE, .35), 'leg')
    else:
        c.rect(8, 19, 9, 21 - (1 if step else 0), darker(APPLE, .35), 'leg'); c.rect(14, 19, 15, 21, darker(APPLE, .35), 'leg')
    # the apple
    cx = 12 if not side else 13
    c.ellipse(cx - 2.5, 12, 6, 7, APPLE, 'body'); c.ellipse(cx + 2.5, 12, 6, 7, APPLE, 'body')
    c.shade('body', APPLE)
    c.set(cx, 5, darker(APPLE, .3), 'body')
    c.pts([(cx - 4, 8), (cx - 5, 9)], lighter(APPLE, .55), 'body')
    # stem and leaf
    c.pts([(cx, 4), (cx, 3), (cx + 1, 2)], STEM, 'stem')
    c.pts([(cx + 1, 4), (cx + 2, 3), (cx + 3, 3)], LEAF, 'leaf')
    # beard
    if view != 'up':
        bx = cx if not side else cx - 4
        width = [4, 5, 5, 4, 3, 2] if not side else [3, 4, 4, 3, 2, 1]
        for i, w in enumerate(width):
            y = 13 + i
            for x in range(bx - w, bx + w + 1):
                if side and x > bx + 2:
                    continue
                c.set(x, y, BEARD, 'beard')
        c.shade('beard', BEARD)
    # walking stick with a sprout on top
    sx = 20 if view == 'down' else (3 if view == 'up' else 2)
    bob = 1 if step else 0
    c.line(sx, 5 + bob, sx, 22, STICK, 'stick'); c.shade('stick', STICK)
    c.pts([(sx + 1, 4 + bob), (sx + 2, 3 + bob), (sx - 1, 4 + bob)], LEAF, 'sprout')
    c.outline()
    if view == 'down':
        c.pts([(8, 10), (9, 10)], WHITE); c.set(9, 10, K)          # good eye
        c.pts([(7, 8), (8, 8), (9, 8)], K)                         # bushy brow
        c.pts([(14, 9), (15, 10), (16, 11)], SCAR)                 # the scratched-out eye
        c.pts([(14, 10), (15, 10), (16, 10)], K)
        c.pts([(10, 14), (11, 14), (12, 14), (13, 14)], darker(BEARD, .35))   # mustache line
    elif side:
        c.pts([(7, 10), (8, 10)], WHITE); c.set(7, 10, K)
        c.pts([(6, 8), (7, 8), (8, 8)], K)
    return c


LEMON = rgb('f6d33c'); FEZ = rgb('6a3d9a'); TASSEL = rgb('e8b931')


def lemon(frame):
    c = Canvas(16)
    bob = 0 if frame == 1 else 1
    c.rect(6, 14, 6, 15, K); c.rect(9, 14, 9, 15, K)               # stick legs
    c.ellipse(8, 9 + bob, 4.6, 5.2, LEMON, 'body')
    c.pts([(8, 3 + bob), (8, 15 + bob - 1)], LEMON, 'body')         # lemon tips
    c.shade('body', LEMON)
    c.rect(5, 3 + bob, 10, 4 + bob, FEZ, 'fez'); c.rect(6, 2 + bob, 9, 2 + bob, FEZ, 'fez')
    c.shade('fez', FEZ)
    c.pts([(10, 2 + bob), (11, 3 + bob), (11, 4 + bob)], TASSEL, 'tassel')
    c.outline()
    ey = 7 + bob
    c.pts([(6, ey), (7, ey), (9, ey), (10, ey)], WHITE)
    c.pts([(7, ey) if frame == 1 else (6, ey), (10, ey) if frame == 1 else (9, ey)], K)   # shifty eyes
    c.pts([(6, ey + 3), (7, ey + 3), (8, ey + 3), (9, ey + 3)], darker(LEMON, .55))    # grin
    return c


ROCK = rgb('6e6a66'); MOSS = rgb('6f8f45')


def big_rock():
    c = Canvas(16)
    c.ellipse(8, 9, 7, 6.2, ROCK, 'rock'); c.shade('rock', ROCK, hi_amt=.25, lo_amt=.3)
    c.pts([(4, 6), (5, 5), (6, 5)], lighter(ROCK, .35), 'rock')
    c.pts([(9, 8), (10, 9), (10, 10), (11, 11)], darker(ROCK, .35), 'rock')
    c.pts([(5, 11), (6, 12)], darker(ROCK, .35), 'rock')
    c.pts([(7, 3), (8, 3), (9, 3), (8, 4), (12, 5)], MOSS, 'moss')
    c.outline()
    return c


# ---------------------------------------------------------------- Citrus Grove villagers (24x24)
KIWI = rgb('8a6a3a'); LIME = rgb('8bc34a'); PEACH = rgb('f7a072'); COCO = rgb('6b4a2e')
PEAR = rgb('c9d84a'); STRAW = rgb('e8c46a'); CHERRY = rgb('d62f45'); PLUM = rgb('7a4aa0'); APRON = rgb('f2ede0')


def villager(kind, view, frame):
    c = Canvas(24)
    side = view == 'left'
    step = frame == 2
    leg = darker({'pear': PEAR, 'cherry': CHERRY, 'plum': PLUM, 'kiwi': KIWI, 'lime': LIME,
                  'peach': PEACH, 'coconut': COCO}[kind], .4)
    if side:
        if step:
            c.rect(11, 19, 12, 21, leg, 'leg')
        else:
            c.rect(9, 19, 10, 21, leg, 'leg'); c.rect(13, 19, 14, 21, leg, 'leg')
    else:
        c.rect(8, 19, 9, 21 - (1 if step else 0), leg, 'leg'); c.rect(14, 19, 15, 21, leg, 'leg')
    cx = 12 if not side else 11

    if kind == 'pear':
        c.ellipse(cx, 14, 6.5, 5.6, PEAR, 'body'); c.ellipse(cx, 8.5, 4, 4, PEAR, 'body')
        c.shade('body', PEAR)
        # straw hat
        c.rect(cx - 6, 5, cx + 6, 5, STRAW, 'hat'); c.rect(cx - 3, 2, cx + 3, 4, STRAW, 'hat')
        c.shade('hat', STRAW)
        c.line(cx - 3, 4, cx + 3, 4, rgb('c0392b'), 'band')
        face_y = 10
    elif kind == 'cherry':
        c.ellipse(cx, 13, 6.4, 6.2, CHERRY, 'body'); c.shade('body', CHERRY)
        c.pts([(cx - 4, 8), (cx - 5, 7)], lighter(CHERRY, .55), 'body')
        # twin stems with a leaf
        c.line(cx, 7, cx - 2, 2, rgb('5d3a22'), 'stem'); c.line(cx, 7, cx + 3, 2, rgb('5d3a22'), 'stem')
        c.pts([(cx + 4, 1), (cx + 5, 1), (cx + 4, 2), (cx + 5, 0)], LEAF, 'leaf')
        c.ellipse(cx - 3, 2, 1.6, 1.6, CHERRY, 'twin')     # a tiny cherry buddy on the stem
        face_y = 12
    elif kind == 'kiwi':  # lighthouse keeper in a captain's cap
        c.ellipse(cx, 13, 6.6, 6.2, KIWI, 'body'); c.shade('body', KIWI)
        for x, y in ((cx - 4, 12), (cx + 3, 15), (cx - 1, 17), (cx + 4, 10), (cx - 3, 16)):
            c.set(x, y, darker(KIWI, .3), 'body')                 # fuzz
        c.rect(cx - 5, 5, cx + 5, 7, rgb('2c3e75'), 'cap'); c.rect(cx - 6, 7, cx + 3, 7, rgb('1c2850'), 'cap')
        c.shade('cap', rgb('2c3e75'))
        c.pts([(cx, 6)], rgb('e8b931'))
        face_y = 11
    elif kind == 'lime':  # fisher in a bucket hat, rod over the shoulder
        c.ellipse(cx, 13, 6.4, 6, LIME, 'body'); c.shade('body', LIME)
        for x, y in ((cx - 3, 15), (cx + 3, 12), (cx, 17)):
            c.set(x, y, lighter(LIME, .35), 'body')
        c.rect(cx - 5, 5, cx + 5, 7, rgb('c9b48a'), 'hat'); c.rect(cx - 3, 3, cx + 3, 5, rgb('c9b48a'), 'hat')
        c.shade('hat', rgb('c9b48a'))
        if view != 'up':
            rx = 20 if not side else 3
            c.line(rx, 3, rx, 21, rgb('7a4a28'), 'rod'); c.shade('rod', rgb('7a4a28'))
            c.line(rx + (1 if not side else -1), 3, rx + (2 if not side else -2), 8, rgb('dfe9f0'), 'line')
        face_y = 11
    elif kind == 'peach':  # market vendor with a green bandana
        c.ellipse(cx, 13, 6.6, 6.4, PEACH, 'body'); c.shade('body', PEACH)
        c.line(cx, 7, cx, 19, darker(PEACH, .12), 'body')         # the peach crease
        c.ellipse(cx, 6.5, 5, 2.2, rgb('3f8f3a'), 'bandana'); c.shade('bandana', rgb('3f8f3a'))
        c.pts([(cx + 2, 3), (cx + 3, 2), (cx + 4, 3)], LEAF, 'leaf')
        face_y = 11
    elif kind == 'coconut':  # sailor in a striped shirt
        c.ellipse(cx, 12, 6.4, 6, COCO, 'body'); c.shade('body', COCO)
        for x, y in ((cx - 3, 8), (cx + 2, 7), (cx + 4, 11), (cx - 5, 12)):
            c.set(x, y, darker(COCO, .3), 'body')                 # hairy shell
        for y in range(15, 19):
            for x in range(cx - 5, cx + 6):
                if c.tag[y][x] == 'body':
                    c.set(x, y, rgb('2e6fb0') if y % 2 else rgb('f2f2f2'), 'shirt')
        face_y = 10
    else:  # plum in an apron and headscarf
        c.ellipse(cx, 12.5, 6.6, 6.6, PLUM, 'body'); c.shade('body', PLUM)
        if view != 'up':
            c.rect(cx - 3 if not side else cx - 5, 14, cx + 3 if not side else cx - 1, 18, APRON, 'apron')
            c.shade('apron', APRON)
        c.ellipse(cx, 6.5, 5.4, 2.8, rgb('e0605a'), 'scarf'); c.shade('scarf', rgb('e0605a'))
        c.pts([(cx + 4, 8), (cx + 5, 9)], rgb('e0605a'), 'scarf')
        face_y = 11
    c.outline()
    if view == 'down':
        c.pts([(cx - 3, face_y), (cx + 2, face_y)], K)
        c.pts([(cx - 3, face_y - 1), (cx + 2, face_y - 1)], WHITE)
        c.pts([(cx - 1, face_y + 2), (cx, face_y + 2)], K)
        c.pts([(cx - 5, face_y + 1), (cx + 4, face_y + 1)], rgb('f47e8a'))
    elif side:
        c.pts([(cx - 4, face_y)], K); c.pts([(cx - 4, face_y - 1)], WHITE)
        c.pts([(cx - 6, face_y + 2), (cx - 5, face_y + 2)], K)
    return c


def build(out):
    made = {}
    for f in (1, 2):
        made[f'AppleDown{f}'] = apple('down', f)
        made[f'AppleUp{f}'] = apple('up', f)
        made[f'AppleLeft{f}'] = apple('left', f)
        made[f'AppleRight{f}'] = made[f'AppleLeft{f}'].mirrored()
        made[f'lemon{f}'] = lemon(f)
    made['bigRock'] = big_rock()
    for kind, nm in (('pear', 'Pear'), ('cherry', 'Cherry'), ('plum', 'Plum'), ('kiwi', 'Kiwi'),
                     ('lime', 'Lime'), ('peach', 'Peach'), ('coconut', 'Coconut')):
        for f in (1, 2):
            made[f'{nm}Down{f}'] = villager(kind, 'down', f)
            made[f'{nm}Up{f}'] = villager(kind, 'up', f)
            made[f'{nm}Left{f}'] = villager(kind, 'left', f)
            made[f'{nm}Right{f}'] = made[f'{nm}Left{f}'].mirrored()
    for name, cv in made.items():
        cv.save(os.path.join(out, name + '.png'))
    return made


if __name__ == '__main__':
    out = sys.argv[1]
    os.makedirs(out, exist_ok=True)
    m = build(out)
    sheet([m[k].image() for k in sorted(m)], os.path.join(out, '..', 'npc_sheet.png'), scale=8)
    print(len(m), 'frames')
