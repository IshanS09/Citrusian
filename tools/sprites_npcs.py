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


def build(out):
    made = {}
    for f in (1, 2):
        made[f'AppleDown{f}'] = apple('down', f)
        made[f'AppleUp{f}'] = apple('up', f)
        made[f'AppleLeft{f}'] = apple('left', f)
        made[f'AppleRight{f}'] = made[f'AppleLeft{f}'].mirrored()
        made[f'lemon{f}'] = lemon(f)
    made['bigRock'] = big_rock()
    for name, cv in made.items():
        cv.save(os.path.join(out, name + '.png'))
    return made


if __name__ == '__main__':
    out = sys.argv[1]
    os.makedirs(out, exist_ok=True)
    m = build(out)
    sheet([m[k].image() for k in sorted(m)], os.path.join(out, '..', 'npc_sheet.png'), scale=8)
    print(len(m), 'frames')
