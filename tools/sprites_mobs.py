"""Regular enemies: carrot bandit, broccoli brute, peas, skeleton pea, mini slime, Pea King."""
import os, sys
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker

WHITE = rgb('f2f2f2')
RED_EYE = rgb('d62828')


def angry_eyes(c, lx, rx, y, pupil_in=True):
    """Two eyes with a white, a red pupil and an angry brow."""
    c.pts([(lx, y), (lx + 1, y), (rx, y), (rx + 1, y)], WHITE)
    c.pts([(lx + 1, y), (rx, y)] if pupil_in else [(lx, y), (rx + 1, y)], RED_EYE)
    c.pts([(lx - 1, y - 2), (lx, y - 1), (lx + 1, y - 1), (rx + 2, y - 2), (rx + 1, y - 1), (rx, y - 1)], K)


# =============================================================== CARROT (Rotroot Bandit)
OR = rgb('f08a24'); LEAF = rgb('4caf50'); PANTS = rgb('5d4037'); BAND = rgb('c62828')


def carrot(view, frame):
    c = Canvas(32)
    side = view in ('left',)
    # legs (walk cycle)
    if not side:
        ly = 0 if frame == 1 else -1
        c.rect(13, 22, 14, 25 + ly, PANTS, 'pants'); c.rect(17, 22, 18, 25, PANTS, 'pants')
        c.rect(12, 26 + ly, 14, 26 + ly, darker(OR, .35), 'feet'); c.rect(17, 26, 19, 26, darker(OR, .35), 'feet')
    else:
        if frame == 1:
            c.rect(13, 22, 14, 25, PANTS, 'pants'); c.rect(17, 22, 18, 25, PANTS, 'pants')
            c.rect(12, 26, 14, 26, darker(OR, .35), 'feet'); c.rect(16, 26, 18, 26, darker(OR, .35), 'feet')
        else:
            c.rect(15, 22, 16, 25, PANTS, 'pants'); c.rect(14, 26, 16, 26, darker(OR, .35), 'feet')
    # body: a carrot that tapers toward the bottom
    if not side:
        c.ellipse(16, 12, 5.6, 4, OR, 'body')
        c.poly([(10.5, 12), (21.5, 12), (19.5, 22.5), (12.5, 22.5)], OR, 'body')
    else:
        c.ellipse(16, 12, 4.6, 4, OR, 'body')
        c.poly([(11.5, 12), (20.5, 12), (19, 22.5), (13.5, 22.5)], OR, 'body')
    # arms
    swing = 0 if frame == 1 else 1
    if not side:
        c.rect(9, 14 + swing, 10, 18 + swing, OR, 'arm'); c.rect(21, 15 - swing, 22, 19 - swing, OR, 'arm')
    c.shade('body', OR); c.shade('arm', OR); c.shade('pants', PANTS)
    # leafy top
    lean = -1 if side else 0
    for pts in ([(16, 8), (16, 7), (16, 6), (16, 5), (17, 4)], [(15, 8), (14, 7), (13, 6), (13, 5)], [(17, 8), (18, 7), (19, 6), (19, 5), (20, 5)]):
        c.pts([(x + lean, y) for x, y in pts], LEAF, 'leaf')
    c.pts([(16 + lean, 8), (15 + lean, 8)], darker(LEAF, .3), 'leaf')
    # bandana
    for y in range(14, 18):
        for x in range(32):
            if c.tag[y][x] == 'body' and (view != 'up' or y < 16):
                c.set(x, y, BAND, 'band')
    if view == 'down':
        c.pts([(15, 18), (16, 18), (16, 19)], BAND, 'band')
    if view == 'up':
        c.pts([(15, 16), (15, 17), (15, 18), (17, 16), (17, 17), (17, 18), (18, 19)], BAND, 'band')
    if side:
        c.pts([(21, 14), (22, 15), (23, 15), (22, 16)], BAND, 'band')
    c.shade('band', BAND)
    for x, y in ((12, 15), (15, 16), (18, 15), (20, 16), (13, 17), (17, 17)):
        if c.tag[y][x] == 'band' and c.g[y][x] != darker(BAND):
            c.set(x, y, lighter(BAND, .7), 'band')
    # carrot ridges
    for x, y in ((12, 19), (13, 19), (19, 20), (18, 20), (19, 12), (20, 12)):
        if c.tag[y][x] == 'body':
            c.set(x, y, darker(OR, .2), 'body')
    if side:  # front arm swings
        ax = 14 if frame == 1 else 17
        c.rect(ax, 15, ax + 1, 19, OR, 'farm'); c.shade('farm', OR)
    c.outline()
    if view == 'down':
        angry_eyes(c, 12, 18, 12)
    elif side:
        c.pts([(12, 12), (13, 12)], WHITE); c.set(12, 12, RED_EYE)
        c.pts([(11, 10), (12, 11), (13, 11)], K)
    return c


# =============================================================== BROCCOLI brute
FLO = rgb('2e7d32'); FLO_L = rgb('4caf50'); STALK = rgb('9ccc65'); WOOD = rgb('7b4a2a'); CLOTH = rgb('6d4c41')


def florets(c, cx, cy, side=False):
    blobs = [(-5, 2, 4), (0, 0, 4.6), (5, 2, 4), (-3, 5, 3.6), (3, 5, 3.6)] if not side else \
            [(-4, 2, 4), (1, 0, 4.6), (4, 3, 3.8), (-1, 5, 3.6)]
    for dx, dy, r in blobs:
        c.ellipse(cx + dx, cy + dy, r, r * .9, FLO, 'flo')
    c.shade('flo', FLO, hi=FLO_L)
    for dx, dy in ((-4, 1), (1, -1), (5, 2), (-1, 3), (3, 4), (-5, 4), (2, 1)):
        x, y = cx + dx, cy + dy
        if c.tag[y][x] == 'flo':
            c.set(x, y, FLO_L, 'flo')


def club(c, x0, y0, x1, y1):
    """Wooden club: a shaft from the hand (x0,y0) to a fat knobbly head at (x1,y1)."""
    c.line(x0, y0, x1, y1, WOOD, 'club', width=2)
    c.ellipse(x1 + 1, y1 + 1, 3, 3, WOOD, 'club')
    c.shade('club', WOOD)
    c.set(x1 + 1, y1, darker(WOOD, .35), 'club')
    c.set(x1, y1 + 2, darker(WOOD, .35), 'club')


def broccoli(view, kind, frame):
    """kind: 'walk' or 'attack'."""
    c = Canvas(32)
    side = view in ('left',)
    step = 0 if frame == 1 else -1
    # legs
    if not side:
        c.rect(12, 24, 14, 27 + (step if kind == 'walk' else 0), STALK, 'leg'); c.rect(17, 24, 19, 27, STALK, 'leg')
    else:
        if frame == 1 or kind == 'attack':
            c.rect(12, 24, 14, 27, STALK, 'leg'); c.rect(17, 24, 19, 27, STALK, 'leg')
        else:
            c.rect(14, 24, 17, 27, STALK, 'leg')
    # torso and arms
    if not side:
        c.poly([(9, 14), (23, 14), (20.5, 24.5), (11.5, 24.5)], STALK, 'body')
        c.ellipse(8.5, 18, 2.6, 4, STALK, 'arm'); c.ellipse(23.5, 18, 2.6, 4, STALK, 'arm')
    else:
        c.poly([(11, 14), (21, 14), (19.5, 24.5), (12.5, 24.5)], STALK, 'body')
    c.shade('leg', STALK); c.shade('body', STALK); c.shade('arm', STALK)
    c.rect(12 if not side else 13, 22, 19 if not side else 19, 24, CLOTH, 'cloth'); c.shade('cloth', CLOTH)
    florets(c, 16 if not side else 15, 7, side)
    # club placement
    if kind == 'walk':
        if view == 'down':
            club(c, 25, 20, 27, 26)
        elif view == 'up':
            club(c, 6, 20, 4, 26)
        else:
            club(c, 14, 19, 10, 24)
    else:
        if view == 'down':
            club(c, 24, 15, 25, 5) if frame == 1 else club(c, 22, 20, 18, 27)
        elif view == 'up':
            club(c, 8, 20, 6, 27) if frame == 1 else club(c, 18, 12, 20, 3)
        else:
            club(c, 20, 13, 26, 5) if frame == 1 else club(c, 12, 18, 2, 18)
    if side:
        hx = 14 if kind == 'walk' else (20 if frame == 1 else 12)
        hy = 18 if kind == 'walk' else (13 if frame == 1 else 17)
        c.ellipse(hx, hy, 2.4, 3.2, STALK, 'farm'); c.shade('farm', STALK)
    c.outline()
    if view == 'down':
        angry_eyes(c, 12, 18, 16)
        c.pts([(13, 19), (14, 19), (15, 19), (16, 19), (17, 19), (18, 19)], K)
        c.pts([(13, 20), (18, 20)], WHITE)  # tusks
    elif side:
        c.pts([(11, 16), (12, 16)], WHITE); c.set(11, 16, RED_EYE)
        c.pts([(10, 14), (11, 15), (12, 15)], K)
        c.pts([(10, 19), (11, 19), (12, 19)], K); c.set(10, 20, WHITE)
    return c


# =============================================================== PEA and SKELETON PEA (16x16)
PEA = rgb('7cb342'); STEM = rgb('6d4c41')


def pea(frame):
    c = Canvas(16)
    cy, ry = (9, 5) if frame == 1 else (9.6, 4.5)
    c.ellipse(8, cy, 5.2, ry, PEA, 'body'); c.shade('body', PEA)
    c.set(6, 7, lighter(PEA, .5), 'body')
    c.pts([(8, 4) if frame == 1 else (8, 5), (9, 3) if frame == 1 else (9, 4)], STEM, 'stem')
    c.outline()
    ey = 9 if frame == 1 else 10
    c.pts([(6, ey), (9, ey)], RED_EYE); c.pts([(5, ey - 1), (6, ey - 1), (9, ey - 1), (10, ey - 1)], K)
    c.pts([(7, ey + 2), (8, ey + 2)], K)
    return c


BONE = rgb('e9e4d0'); MOSS = rgb('7a9a50')


def skeleton_pea(frame):
    c = Canvas(16)
    c.ellipse(8, 8, 5.6, 5, BONE, 'skull'); c.shade('skull', BONE)
    jaw = 0 if frame == 1 else 1
    c.rect(5, 11 + jaw, 10, 12 + jaw, BONE, 'jaw'); c.shade('jaw', BONE)
    c.pts([(4, 9), (6, 5), (11, 10), (9, 4)], MOSS)
    c.pts([(8, 3), (9, 2), (10, 2)], MOSS, 'sprout')
    c.outline()
    c.rect(5, 7, 6, 8, K); c.rect(9, 7, 10, 8, K)
    c.set(6, 8, RED_EYE); c.set(9, 8, RED_EYE)
    c.set(8, 10, K)
    for x in (6, 8, 10):
        c.set(x, 11 + jaw, K)
    return c


# =============================================================== MINI SLIME and PEA KING (32x32)
ROT = rgb('3b3d42'); GOO = rgb('5cb85c')


def mini_slime(frame):
    c = Canvas(32)
    rx, ry, cy = (7.5, 6.5, 21) if frame == 1 else (8.5, 5.5, 22)
    c.ellipse(16, cy, rx, ry, ROT, 'shell'); c.shade('shell', ROT, hi_amt=.2)
    c.ellipse(16, cy + 1, rx - 2.5, ry - 3, GOO, 'face'); c.shade('face', GOO)
    c.pts([(16, int(cy - ry) - 1), (17, int(cy - ry) - 2)], STEM, 'stem')
    c.outline()
    ey = int(cy)
    c.pts([(13, ey), (14, ey), (18, ey), (19, ey)], RED_EYE)
    c.pts([(12, ey - 1), (13, ey - 1), (19, ey - 1), (20, ey - 1)], K)
    return c


KING = rgb('2f5d2f'); BELLY = rgb('4f7f3f'); IRON = rgb('4a4a52'); GEM = rgb('f2c43a')


def pea_king(frame, enraged=False):
    c = Canvas(32)
    bob = 0 if frame == 1 else 1
    # staff with a skull on top
    c.line(26, 9 + bob, 26, 30, WOOD, 'staff', width=2); c.shade('staff', WOOD)
    c.ellipse(27, 6 + bob, 3, 2.8, BONE, 'skull'); c.shade('skull', BONE)
    # body
    c.ellipse(14, 21 + bob, 10, 8.5, KING, 'body'); c.shade('body', KING, hi_amt=.2)
    c.ellipse(14, 24 + bob, 6, 4, BELLY, 'belly')
    # crown
    c.rect(6, 12 + bob, 22, 14 + bob, IRON, 'crown')
    for x in (7, 11, 15, 19, 22):
        c.poly([(x - 1.5, 12.5 + bob), (x + 1.5, 12.5 + bob), (x, 8 + bob)], IRON, 'crown')
    c.shade('crown', IRON)
    c.ellipse(26.5, 20 + bob, 2.5, 2.5, KING, 'hand'); c.shade('hand', KING)
    c.outline()
    gem = rgb('ff7a1a') if enraged else GEM
    for x in (9, 14, 19):
        c.set(x, 13 + bob, gem)
    eye = rgb('ffd23f') if enraged else RED_EYE
    ey = 18 + bob
    c.pts([(9, ey), (10, ey), (16, ey), (17, ey)], WHITE)
    c.pts([(10, ey), (16, ey)], eye)
    if enraged:
        c.pts([(9, ey), (17, ey)], eye)
    c.pts([(8, ey - 1), (9, ey - 2), (17, ey - 2), (18, ey - 1), (10, ey - 1), (16, ey - 1)], K)
    c.pts([(10, ey + 4), (11, ey + 5), (12, ey + 5), (13, ey + 5), (14, ey + 5), (15, ey + 5), (16, ey + 4)], K)
    c.pts([(11, ey + 4), (15, ey + 4)], WHITE)
    c.pts([(26, 6 + bob), (28, 6 + bob)], K)
    return c


def build(out):
    made = {}
    for f in (1, 2):
        made[f'CarrotDown{f}'] = carrot('down', f)
        made[f'CarrotUp{f}'] = carrot('up', f)
        made[f'CarrotLeft{f}'] = carrot('left', f)
        made[f'CarrotRight{f}'] = made[f'CarrotLeft{f}'].mirrored()
        for kind, pre in (('walk', 'broccoli'), ('attack', 'broccoliAttack')):
            made[f'{pre}Down{f}'] = broccoli('down', kind, f)
            made[f'{pre}Up{f}'] = broccoli('up', kind, f)
            made[f'{pre}Left{f}'] = broccoli('left', kind, f)
            made[f'{pre}Right{f}'] = made[f'{pre}Left{f}'].mirrored()
        made[f'peaDown{f}'] = pea(f)
        made[f'SkeletonPea{f}'] = skeleton_pea(f)
        made[f'MiniSlimeDown{f}'] = mini_slime(f)
        made[f'PeaKingDown{f}'] = pea_king(f)
    made['SplitImage2'] = pea_king(1, enraged=True)
    for name, cv in made.items():
        cv.save(os.path.join(out, name + '.png'))
    return made


if __name__ == '__main__':
    out = sys.argv[1]
    os.makedirs(out, exist_ok=True)
    m = build(out)
    from pixel import sheet
    order = sorted(m)
    sheet([m[k].image() for k in order], os.path.join(out, '..', 'mobs_sheet.png'), scale=4)
    print(len(m), 'frames')
