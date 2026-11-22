"""The Lone Orange (player, 32x32) and projectiles (16x16): fire seed, fire skull, rock."""
import os, sys, math
sys.path.insert(0, os.path.dirname(__file__))
from pixel import Canvas, K, T, rgb, lighter, darker, sheet

WHITE = rgb('f7f7f7')
ORANGE = rgb('f39a1e'); PEEL = rgb('d97c12'); LEAF = rgb('5cae4a'); STEM = rgb('6d4c2f')
BOOT = rgb('5b3a24'); SCARF = rgb('2f7d6b')
STEEL = rgb('d4dde2'); STEEL_D = rgb('8fa1a8'); GOLD = rgb('e8b931'); HANDLE = rgb('8a5a36'); IRON = rgb('7c8088')
SHIELD = rgb('a0683c'); RIM = rgb('b9c2c8')


def body(c, view, step):
    side = view == 'left'
    # boots
    if side:
        if step:
            c.rect(14, 21, 17, 23, BOOT, 'boot')
        else:
            c.rect(11, 21, 13, 23, BOOT, 'boot'); c.rect(18, 21, 20, 23, BOOT, 'boot')
    else:
        d = 1 if step else 0
        c.rect(11, 21, 13, 23 - d, BOOT, 'boot'); c.rect(18, 21, 20, 23, BOOT, 'boot')
    # the orange
    c.ellipse(16, 13, 7.2, 7, ORANGE, 'body')
    c.shade('boot', BOOT, hi_amt=.2)
    c.shade('body', ORANGE, hi=lighter(ORANGE, .3), lo=PEEL)
    for x, y in ((12, 15), (19, 11), (17, 17), (14, 9), (20, 16)):
        if c.tag[y][x] == 'body':
            c.set(x, y, PEEL, 'body')                      # peel dimples
    c.pts([(12, 9), (13, 8)], lighter(ORANGE, .55), 'body')  # shine
    # scarf
    for x in range(9, 24):
        for y in (17, 18):
            if c.tag[y][x] == 'body':
                c.set(x, y, SCARF if y == 17 else darker(SCARF, .2), 'scarf')
    if view == 'up':
        c.pts([(15, 19), (15, 20), (16, 20), (17, 19), (17, 21)], SCARF, 'scarf')   # knot tails
    if side:
        c.pts([(22, 18), (23, 18), (24, 19), (23, 19)], SCARF, 'scarf')
    # stem and leaf
    c.pts([(16, 6), (16, 5)], STEM, 'stem')
    c.pts([(17, 5), (18, 4), (19, 4), (18, 5)], LEAF, 'leaf')


def arms(c, view, step, raise_right=None):
    """Little arms. raise_right: (x, y) for the weapon hand, or None for a normal swing."""
    side = view == 'left'
    if side:
        hx, hy = raise_right if raise_right else ((14, 17) if not step else (18, 17))
        c.ellipse(hx + .5, hy + .5, 1.6, 1.6, ORANGE, 'arm')
    else:
        sw = 1 if step else 0
        left = (8, 15 + sw)
        right = raise_right if raise_right else (23, 15 - sw + 1)
        for hx, hy in (left, right):
            c.ellipse(hx + .5, hy + .5, 1.6, 1.6, ORANGE, 'arm')
    c.shade('arm', ORANGE, lo=PEEL)


def face(c, view, mood='happy'):
    if view == 'down':
        for ex in (13, 18):
            c.rect(ex, 11, ex + 1, 13, K)
            c.set(ex, 11, WHITE)
        if mood == 'happy':
            c.pts([(14, 15), (15, 16), (16, 16), (17, 15)], K)
        else:  # focused, mid-swing
            c.pts([(14, 15), (15, 15), (16, 15), (17, 15)], K)
            c.pts([(12, 10), (13, 10), (19, 10), (20, 10)], K)
        c.pts([(11, 14), (21, 14)], rgb('f47e5a'))          # rosy cheeks
    elif view == 'left':
        c.rect(11, 11, 12, 13, K); c.set(11, 11, WHITE)
        c.pts([(10, 15), (11, 16), (12, 16)], K)
        c.set(10, 14, rgb('f47e5a'))


# ---------------------------------------------------------------- weapons
def draw_weapon(c, kind, hx, hy, ang, length=10):
    """Weapon held at (hx, hy) pointing at angle ang (degrees, 0 = right, 90 = down)."""
    dx, dy = math.cos(math.radians(ang)), math.sin(math.radians(ang))
    px, py = -dy, dx                                      # perpendicular
    tx, ty = round(hx + dx * length), round(hy + dy * length)
    if kind == 'sword':
        gx, gy = round(hx + dx * 2), round(hy + dy * 2)
        c.line(hx, hy, gx, gy, HANDLE, 'w_handle')
        c.line(gx, gy, tx, ty, STEEL, 'w_blade', width=2 if abs(dx) < .3 or abs(dy) < .3 else 1)
        c.line(round(gx + px * 2), round(gy + py * 2), round(gx - px * 2), round(gy - py * 2), GOLD, 'w_guard')
        c.shade('w_blade', STEEL, hi=WHITE, lo=STEEL_D)
    elif kind == 'axe':
        c.line(hx, hy, tx, ty, HANDLE, 'w_handle', width=1)
        bx, by = tx - dx * 2, ty - dy * 2
        c.poly([(bx + px * .5, by + py * .5), (bx + px * 4.5 - dx * 1.5, by + py * 4.5 - dy * 1.5),
                (bx + px * 4.5 + dx * 3, by + py * 4.5 + dy * 3), (bx + dx * 2.5, by + dy * 2.5)], IRON, 'w_head')
        c.shade('w_head', IRON, hi=lighter(IRON, .4))
    else:  # pickaxe: curved head across the end of the handle
        c.line(hx, hy, tx, ty, HANDLE, 'w_handle', width=1)
        for t in range(-5, 6):
            bend = abs(t) * .35
            x = round(tx + px * t - dx * bend)
            y = round(ty + py * t - dy * bend)
            c.set(x, y, IRON, 'w_head')
        c.shade('w_head', IRON, hi=lighter(IRON, .4))
    c.shade('w_handle', HANDLE)


# Hand position and weapon angle per (view, frame) for attacks
ATTACK = {
    ('down', 1): ((23, 13), -80), ('down', 2): ((15, 21), 95),
    ('up', 1): ((23, 17), 70), ('up', 2): ((17, 11), -88),
    ('left', 1): ((19, 9), -60), ('left', 2): ((9, 15), 180),
}


def player(view, pose, frame, weapon='sword'):
    c = Canvas(32)
    step = frame == 2 and pose == 'walk'
    if pose == 'attack':
        (hx, hy), ang = ATTACK[(view, frame)]
        behind = view == 'up' and frame == 1
        if behind:
            draw_weapon(c, weapon, hx, hy, ang)
        body(c, view, False)
        arms(c, view, False, raise_right=(hx, hy))
        if not behind:
            draw_weapon(c, weapon, hx, hy, ang)
        c.outline()
        face(c, view, mood='focused')
        return c
    body(c, view, step)
    if pose == 'guard':
        # round wooden shield held toward the enemy
        if view == 'down':
            sx, sy, rx, ry = 16, 16, 6, 6
        elif view == 'up':
            sx, sy, rx, ry = 16, 5, 6.5, 3
        else:
            sx, sy, rx, ry = 8, 14, 3, 7
        c.ellipse(sx, sy, rx, ry, RIM, 'rim')
        c.ellipse(sx, sy, rx - 1.2, ry - 1.2, SHIELD, 'shield')
        c.shade('shield', SHIELD); c.shade('rim', RIM, hi=WHITE)
        c.set(int(sx), int(sy), GOLD, 'boss')
        c.outline()
        if view == 'left':
            face(c, view, mood='focused')
        elif view == 'up':
            pass
        else:
            for ex in (13, 18):                   # eyes peek over the shield
                c.set(ex, 9, K); c.set(ex + 1, 9, K)
        return c
    arms(c, view, step)
    c.outline()
    face(c, view)
    return c


# ---------------------------------------------------------------- projectiles
FLAME = [rgb('fff3b0'), rgb('ffc93c'), rgb('ff8a1f'), rgb('e8432a')]


def flame_trail(c, cx, cy, ang, frame, core):
    """Fireball pointing at angle ang, with the flame trailing behind it."""
    dx, dy = math.cos(math.radians(ang)), math.sin(math.radians(ang))
    wob = 1 if frame == 2 else 0
    for i, col in enumerate(reversed(FLAME)):
        r = 4.6 - i * .9
        back = 2.2 - i * .5
        for k in range(4):
            c.ellipse(cx - dx * (k * 1.4 + back) + (wob if k % 2 else 0) * -dy,
                      cy - dy * (k * 1.4 + back) + (wob if k % 2 else 0) * dx, max(r - k * 1.1, .8), max(r - k * 1.1, .8), col, 'fire')
    c.ellipse(cx + dx * .5, cy + dy * .5, 2.6, 2.6, core, 'core')


def fire_seed(ang, frame):
    c = Canvas(16)
    flame_trail(c, 8, 8, ang, frame, rgb('6b4423'))
    c.shade('core', rgb('6b4423'))
    c.outline()
    return c


def fire_skull(ang, frame):
    c = Canvas(16)
    flame_trail(c, 8, 8, ang, frame, rgb('e9e4d0'))
    c.ellipse(8.5, 8.5, 3.4, 3.2, rgb('e9e4d0'), 'core')
    c.shade('core', rgb('e9e4d0'))
    c.outline()
    c.pts([(7, 8), (10, 8)], K); c.pts([(7, 7), (10, 7)], rgb('a01818'))
    c.pts([(8, 10), (9, 10)], K)
    return c


def rock():
    c = Canvas(16)
    c.ellipse(8, 9, 3.6, 3.2, rgb('7d7a76'), 'rock'); c.shade('rock', rgb('7d7a76'))
    c.set(7, 8, lighter(rgb('7d7a76'), .4))
    c.outline()
    return c


def build(out_player, out_proj):
    os.makedirs(out_player, exist_ok=True)
    os.makedirs(out_proj, exist_ok=True)
    made = {}
    names = {'down': 'Down', 'up': 'Up', 'left': 'Left'}
    for f in (1, 2):
        for view, nm in names.items():
            made[f'Orange{nm}{f}'] = player(view, 'walk', f)
            for weapon, pre in (('sword', 'Attack'), ('axe', 'AttackAxe'), ('pickaxe', 'AttackPic')):
                made[f'{pre}{nm}{f}'] = player(view, 'attack', f, weapon)
        made[f'OrangeRight{f}'] = made[f'OrangeLeft{f}'].mirrored()
        for pre in ('Attack', 'AttackAxe', 'AttackPic'):
            made[f'{pre}Right{f}'] = made[f'{pre}Left{f}'].mirrored()
    for view, nm in names.items():
        made[f'guard{nm}'] = player(view, 'guard', 1)
    made['guardRight'] = made['guardLeft'].mirrored()
    for name, cv in made.items():
        cv.save(os.path.join(out_player, name + '.png'))

    proj = {}
    angles = {'Down': 90, 'Up': -90, 'Left': 180, 'Right': 0}
    for nm, ang in angles.items():
        for f in (1, 2):
            proj[f'Seed{nm}{f}'] = fire_seed(ang, f)
            proj[f'Skull{nm}{f}'] = fire_skull(ang, f)
    proj['rock'] = rock()
    for name, cv in proj.items():
        cv.save(os.path.join(out_proj, name + '.png'))
    made.update({'proj_' + k: v for k, v in proj.items()})
    return made


if __name__ == '__main__':
    m = build(sys.argv[1], sys.argv[2])
    keys = sorted(k for k in m if not k.startswith('proj_')) + sorted(k for k in m if k.startswith('proj_'))
    sheet([m[k].image() for k in keys], os.path.join(sys.argv[1], '..', 'player_sheet.png'), scale=4)
    print(len(m), 'frames')
