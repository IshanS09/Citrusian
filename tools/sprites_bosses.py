"""Bosses: Malibroc (BossBroccoli, two phases) and Baron Rotjaw."""
import os, sys
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker, sheet

WHITE = rgb('f2f2f2')

# =============================================================== MALIBROC
HIDE = rgb('2c272b')          # charred body
FLO1 = rgb('353a33'); FLO1_B = rgb('59614f')   # phase 1: burnt florets
FLO2 = rgb('b8322a'); FLO2_B = rgb('ff7b54')   # phase 2: florets on fire
GLOW = rgb('ff4433'); GLOW_HOT = rgb('ffd0a8'); MOUTH = rgb('6e0f1c')


def malibroc(view, pose, frame, phase=1):
    """pose: walk, attack, idle."""
    c = Canvas(32)
    side = view == 'left'
    step = frame == 2
    flo, bump = (FLO1, FLO1_B) if phase == 1 else (FLO2, FLO2_B)

    # legs
    if side:
        if step and pose == 'walk':
            c.rect(13, 26, 18, 29, HIDE, 'leg')
        else:
            c.rect(11, 26, 14, 29, HIDE, 'leg'); c.rect(18, 26, 21, 29, HIDE, 'leg')
    else:
        dy = -1 if (step and pose == 'walk') else 0
        c.rect(10, 26, 14, 29 + dy, HIDE, 'leg'); c.rect(18, 26, 22, 29, HIDE, 'leg')

    # torso
    if side:
        c.poly([(9, 13), (23, 13), (21, 27), (11, 27)], HIDE, 'body')
    else:
        c.poly([(6, 13), (26, 13), (22.5, 27), (9.5, 27)], HIDE, 'body')

    # arms: (shoulder_x, shoulder_y, fist_x, fist_y) for each arm
    arms = []
    if pose in ('walk', 'idle'):
        sw = 1 if step else 0
        if side:
            arms = [(15, 15, 13 if not step else 18, 24)]
        else:
            arms = [(6, 15, 4, 23 + sw), (26, 15, 28, 24 - sw)]
    else:  # attack
        if view == 'down':
            arms = [(6, 14, 4, 6), (26, 14, 28, 6)] if frame == 1 else [(6, 15, 4, 23), (24, 16, 18, 29)]
        elif view == 'up':
            arms = [(6, 14, 4, 22), (26, 14, 28, 22)] if frame == 1 else [(8, 14, 9, 2), (24, 14, 23, 2)]
        else:
            arms = [(17, 15, 25, 6)] if frame == 1 else [(14, 16, 2, 17)]
    for sx, sy, fx, fy in arms:
        c.line(sx, sy, fx, fy, HIDE, 'arm', width=4)
        c.ellipse(fx + 1.5, fy + 1.5, 3, 3, HIDE, 'arm')
    c.shade('leg', HIDE, hi_amt=.12); c.shade('body', HIDE, hi_amt=.12); c.shade('arm', HIDE, hi_amt=.15)

    # florets on top
    bob = 1 if (pose == 'idle' and step) else 0
    cx = 16 if not side else 15
    blobs = [(-7, 4, 5), (-2, 1, 6), (4, 1, 6), (8, 5, 4.6), (0, 6, 5)] if not side else \
            [(-5, 4, 5), (0, 1, 6), (5, 3, 5), (1, 6, 5)]
    for dx, dy, r in blobs:
        c.ellipse(cx + dx, 6 + dy + bob, r, r * .9, flo, 'flo')
    c.shade('flo', flo, hi=bump)
    for dx, dy in ((-7, 3), (-3, 0), (2, -1), (6, 2), (9, 5), (-5, 7), (1, 5), (4, 8), (-1, 3)):
        x, y = cx + dx, 6 + dy + bob
        if c.inside(x, y) and c.tag[y][x] == 'flo':
            c.set(x, y, bump, 'flo')
    c.outline()

    # glowing face
    if view == 'down':
        rage = pose == 'attack'
        ey = 15
        for x0 in (10, 19):
            c.pts([(x0, ey), (x0 + 1, ey), (x0 + 2, ey)], GLOW)
            c.set(x0 + 1, ey, GLOW_HOT)
        c.pts([(9, ey - 1), (10, ey - 1), (22, ey - 1), (21, ey - 1)], K)
        mh = 3 if (rage or pose == 'walk') else 2
        c.ellipse(16, 20 + (1 if rage else 0), 4.5, mh, MOUTH)
        c.ellipse(16, 20.5 + (1 if rage else 0), 3, mh - 1.2, GLOW)
        for x in (13, 15, 17, 19):
            c.set(x, 18 + (1 if rage else 0), WHITE)
    elif side:
        c.pts([(10, 15), (11, 15), (12, 15)], GLOW); c.set(10, 15, GLOW_HOT)
        c.pts([(9, 14), (10, 14), (11, 14)], K)
        c.ellipse(11, 20, 2.5, 2.2, MOUTH); c.ellipse(11, 20.5, 1.5, 1.2, GLOW)
        c.set(10, 18, WHITE)
    return c


# =============================================================== BARON ROTJAW
HAT = rgb('2a2232'); GOLD = rgb('e8b931'); FACE = rgb('ee8a2a'); BEARD = rgb('3f8f3a')
COAT = rgb('a8323a'); PANTS = rgb('2c3e75'); BOOT = rgb('201a1e'); STEEL = rgb('cfd8dc'); BELT = rgb('5d3a22')


def cutlass(c, hx, hy, tx, ty):
    """Curved sword from the hand (hx,hy) to the tip (tx,ty)."""
    c.line(hx, hy, tx, ty, STEEL, 'blade', width=2)
    c.shade('blade', STEEL, hi=WHITE, lo=rgb('8fa1a8'))
    c.pts([(hx - 1, hy), (hx + 2, hy), (hx, hy + 1), (hx + 1, hy + 1)], GOLD, 'hilt')


def baron(view, pose, frame):
    """pose: walk, attack, slam."""
    c = Canvas(32)
    side = view == 'left'
    step = frame == 2 and pose == 'walk'

    # boots and legs
    if side:
        if step:
            c.rect(14, 22, 17, 26, PANTS, 'pants'); c.rect(13, 27, 17, 28, BOOT, 'boot')
        else:
            c.rect(12, 22, 14, 26, PANTS, 'pants'); c.rect(17, 22, 19, 26, PANTS, 'pants')
            c.rect(11, 27, 14, 28, BOOT, 'boot'); c.rect(16, 27, 19, 28, BOOT, 'boot')
    else:
        dy = -1 if step else 0
        c.rect(12, 22, 14, 26 + dy, PANTS, 'pants'); c.rect(17, 22, 19, 26, PANTS, 'pants')
        c.rect(11, 27 + dy, 14, 28 + dy, BOOT, 'boot'); c.rect(17, 27, 20, 28, BOOT, 'boot')

    # coat with tails
    if side:
        c.poly([(11, 13), (20, 13), (22, 23), (10, 23)], COAT, 'coat')
    else:
        c.poly([(9, 13), (22, 13), (23.5, 24), (7.5, 24)], COAT, 'coat')
    c.shade('pants', PANTS); c.shade('boot', BOOT, hi_amt=.2); c.shade('coat', COAT)
    bx0, bx1 = (9, 22) if not side else (11, 21)
    for x in range(bx0, bx1):
        if c.tag[19][x] == 'coat':
            c.set(x, 19, BELT, 'belt')
    if not side:
        c.pts([(15, 19), (16, 19)], GOLD)
        if view == 'down':
            c.pts([(15, 15), (15, 17), (15, 21)], GOLD)

    # head: a carrot face under a tricorn
    hx = 16 if not side else 15
    c.ellipse(hx, 11, 4.8 if not side else 4.2, 4.2, FACE, 'face'); c.shade('face', FACE)
    # beard of carrot leaves, hanging in points
    if view != 'up':
        rows = [(13, -4, 4), (14, -4, 4), (15, -3, 3), (16, -3, 3), (17, -2, 2), (18, -1, 1)]
        for y, x0, x1 in rows:
            for x in range(x0, x1 + 1):
                if side and x > 1:
                    continue
                if y >= 16 and (x % 2 == 0) and y == 17:
                    continue
                c.set(hx + x - (2 if side else 0), y, BEARD, 'beard')
        for x in (-3, -1, 1, 3):
            if not (side and x > 1):
                c.set(hx + x - (2 if side else 0), 19 if abs(x) == 1 else 18, BEARD, 'beard')
        c.shade('beard', BEARD)
    # tricorn
    c.poly([(hx - 9, 6), (hx + 9, 6), (hx + 6, 3), (hx, 0.5), (hx - 6, 3)], HAT, 'hat')
    c.shade('hat', HAT, hi_amt=.2)
    for x in range(hx - 8, hx + 9):
        if c.tag[6][x] == 'hat' or c.tag[5][x] == 'hat':
            c.set(x, 6, GOLD, 'trim')
    c.pts([(hx, 3), (hx - 1, 4), (hx + 1, 4)], WHITE, 'skull')

    # arms and the sword
    if pose == 'walk':
        sw = 1 if step else 0
        if side:
            c.rect(14 + sw * 2, 13, 16 + sw * 2, 18, COAT, 'sleeve'); c.rect(14 + sw * 2, 19, 15 + sw * 2, 19, FACE, 'hand')
            cutlass(c, 14 + sw * 2, 20, 9 + sw * 2, 25)
        else:
            c.rect(6, 13 + sw, 8, 18 + sw, COAT, 'sleeve'); c.rect(23, 13, 25, 18, COAT, 'sleeve')
            c.rect(6, 19 + sw, 8, 19 + sw, FACE, 'hand'); c.rect(23, 19, 25, 19, FACE, 'hand')
            if view == 'down':
                cutlass(c, 24, 20, 28, 27)
            else:
                cutlass(c, 7, 20, 3, 27)
    elif pose == 'attack':
        if view == 'down':
            if frame == 1:
                c.rect(23, 9, 25, 14, COAT, 'sleeve'); cutlass(c, 24, 8, 29, 1)
            else:
                c.rect(19, 18, 21, 21, COAT, 'sleeve'); cutlass(c, 20, 22, 17, 31)
        elif view == 'up':
            if frame == 1:
                c.rect(6, 13, 8, 18, COAT, 'sleeve'); cutlass(c, 7, 19, 3, 26)
            else:
                c.rect(20, 3, 22, 9, COAT, 'sleeve'); cutlass(c, 21, 2, 23, 0)
        else:
            if frame == 1:
                c.rect(19, 9, 21, 14, COAT, 'sleeve'); cutlass(c, 21, 8, 28, 3)
            else:
                c.rect(9, 15, 13, 17, COAT, 'sleeve'); cutlass(c, 8, 16, 0, 17)
    else:  # slam: both hands raise the sword overhead, then smash it into the ground
        if frame == 1:
            c.rect(8, 4, 10, 12, COAT, 'sleeve'); c.rect(21, 4, 23, 12, COAT, 'sleeve')
            cutlass(c, 15, 3, 15, 0); c.rect(10, 3, 21, 3, FACE, 'hand')
        else:
            c.rect(10, 15, 12, 20, COAT, 'sleeve'); c.rect(19, 15, 21, 20, COAT, 'sleeve')
            cutlass(c, 15, 21, 15, 30); c.rect(12, 21, 19, 21, FACE, 'hand')
            for x, y in ((9, 30), (10, 29), (21, 29), (22, 30), (7, 31), (24, 31), (12, 31), (19, 31)):
                c.set(x, y, rgb('c9b48a'), 'dust')
    c.shade('sleeve', COAT)
    c.outline()

    # face details
    if view == 'down':
        c.pts([(13, 10), (14, 10)], WHITE); c.set(14, 10, rgb('d62828'))
        c.rect(17, 9, 19, 11, K)                       # eyepatch
        c.line(13, 8, 20, 8, K)                        # strap
        c.pts([(12, 9), (13, 9)], K)
        c.pts([(15, 12), (16, 12)], darker(FACE, .35))  # nose
        if pose == 'slam' and frame == 2:
            c.pts([(15, 13), (16, 13)], K)
    elif side:
        c.pts([(11, 10), (12, 10)], WHITE); c.set(11, 10, rgb('d62828'))
        c.pts([(10, 9), (11, 9), (12, 9)], K)
        c.line(13, 8, 19, 9, K)                        # eyepatch strap round the back
        c.pts([(10, 12), (9, 12)], darker(FACE, .35))
    return c


def build(out):
    made = {}
    for f in (1, 2):
        for pose, suffix in (('walk', ''), ('attack', '_Attack')):
            names = {'down': 'Down', 'up': 'Up', 'left': 'Left'}
            for view, nm in names.items():
                made[f'BossBroccoli{suffix}{nm}{f}'] = malibroc(view, pose, f, 1)
                p2 = f'BossBroccoli_phase2_{"Attack" + nm if pose == "attack" else nm.lower()}{f}'
                made[p2] = malibroc(view, pose, f, 2)
                made[f'BaronRotjaw{suffix}{nm}{f}'] = baron(view, pose, f)
            made[f'BossBroccoli{suffix}Right{f}'] = made[f'BossBroccoli{suffix}Left{f}'].mirrored()
            rn = f'BossBroccoli_phase2_{"AttackRight" if pose == "attack" else "right"}{f}'
            ln = f'BossBroccoli_phase2_{"AttackLeft" if pose == "attack" else "left"}{f}'
            made[rn] = made[ln].mirrored()
            made[f'BaronRotjaw{suffix}Right{f}'] = made[f'BaronRotjaw{suffix}Left{f}'].mirrored()
        made[f'BossBroccoli_Idle{f}'] = malibroc('down', 'idle', f, 1)
        made[f'BaronRotjaw_Slam{f}'] = baron('down', 'slam', f)
    for name, cv in made.items():
        cv.save(os.path.join(out, name + '.png'))
    return made


if __name__ == '__main__':
    out = sys.argv[1]
    os.makedirs(out, exist_ok=True)
    m = build(out)
    sheet([m[k].image() for k in sorted(m)], os.path.join(out, '..', 'bosses_sheet.png'), scale=4)
    print(len(m), 'frames')
