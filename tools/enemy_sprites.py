"""Pixel art for the Onion and Potato enemies (32x32, same style as the carrot/broccoli)."""
from PIL import Image
import os, sys

OUT = sys.argv[1] if len(sys.argv) > 1 else '/tmp/art/out'
os.makedirs(OUT, exist_ok=True)

T = (0, 0, 0, 0)
K = (20, 16, 20, 255)          # outline
WHITE = (240, 240, 240, 255)
RED = (210, 30, 30, 255)
TEAR = (120, 190, 235, 255)
GAS = (190, 225, 150, 255)
GAS2 = (160, 205, 120, 255)
DUST = (200, 185, 150, 255)
STREAK = (250, 250, 250, 255)
LEG = (110, 78, 45, 255)


class C:
    def __init__(self):
        self.g = [[T] * 32 for _ in range(32)]

    def set(self, x, y, c):
        if 0 <= x < 32 and 0 <= y < 32:
            self.g[y][x] = c

    def pts(self, pts, c):
        for x, y in pts:
            self.set(x, y, c)

    def ellipse(self, cx, cy, rx, ry, c):
        for y in range(32):
            for x in range(32):
                if ((x + .5 - cx) / rx) ** 2 + ((y + .5 - cy) / ry) ** 2 <= 1:
                    self.g[y][x] = c

    def recolor_in_ellipse(self, cx, cy, rx, ry, frm, to):
        for y in range(32):
            for x in range(32):
                if self.g[y][x] == frm and ((x + .5 - cx) / rx) ** 2 + ((y + .5 - cy) / ry) ** 2 <= 1:
                    self.g[y][x] = to

    def outline(self, skip=()):
        o = [r[:] for r in self.g]
        for y in range(32):
            for x in range(32):
                if self.g[y][x] != T:
                    continue
                for dx, dy in ((0, 1), (1, 0), (0, -1), (-1, 0)):
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < 32 and 0 <= ny < 32 and self.g[ny][nx] not in (T, K) and self.g[ny][nx] not in skip:
                        o[y][x] = K
                        break
        self.g = o

    def mirror(self):
        m = C()
        m.g = [list(reversed(r)) for r in self.g]
        return m

    def save(self, name):
        im = Image.new('RGBA', (32, 32))
        im.putdata([c for r in self.g for c in r])
        im.save(os.path.join(OUT, name + '.png'))
        return im


# ---------------------------------------------------------------- ONION
ON = (150, 80, 150, 255)
ON_D = (118, 55, 122, 255)
ON_L = (192, 125, 192, 255)
SPROUT = (95, 155, 60, 255)
SPROUT_D = (65, 115, 40, 255)


def onion_body(c, cx=16, cy=18, rx=8, ry=8, view='down', lean=0):
    c.ellipse(cx, cy, rx, ry, ON)
    # layer lines
    for x in (cx - 4, cx, cx + 4):
        for y in range(cy - ry + 3, cy + ry - 2):
            if c.g[y][x] == ON:
                c.set(x, y, ON_D)
    hx = cx - 3 if view != 'up' else cx + 3
    c.recolor_in_ellipse(hx, cy - 4, 3, 2.5, ON, ON_L)
    c.recolor_in_ellipse(hx, cy - 4, 3, 2.5, ON_D, ON_L)
    # sprout
    top = cy - ry
    s = [(16 + lean, top), (16 + lean, top - 1), (15 + lean, top - 2), (16 + lean, top - 2),
         (16 + lean, top - 3), (17 + lean, top - 3), (17 + lean, top - 4), (14 + lean, top - 3)]
    c.pts(s, SPROUT)
    c.pts([(16 + lean, top), (15 + lean, top - 2)], SPROUT_D)


def legs(c, frame, y, xl=13, xr=18, color=LEG):
    if frame == 1:
        c.pts([(xl, y), (xl, y + 1), (xr, y), (xr, y + 1), (xl - 1, y + 1), (xr + 1, y + 1)], color)
    else:
        c.pts([(xl, y), (xl, y + 1), (xr, y - 1), (xr, y), (xl - 1, y + 1), (xr + 1, y)], color)


def onion_face_down(c, angry=True, cy=18):
    c.pts([(12, cy - 1), (13, cy - 1), (18, cy - 1), (19, cy - 1)], WHITE)
    c.pts([(13, cy - 1), (18, cy - 1)], RED)
    c.pts([(11, cy - 3), (12, cy - 2), (20, cy - 3), (19, cy - 2)], K)
    c.pts([(14, cy + 3), (15, cy + 3), (16, cy + 3), (17, cy + 3)], K)


def onion_walk(view, frame):
    c = C()
    if view in ('down', 'up'):
        legs(c, frame, 26)
    else:
        legs(c, frame, 26, 14, 18)
    onion_body(c, view=view, lean=(-1 if view == 'left' else 0))
    c.outline()
    if view == 'down':
        onion_face_down(c)
        c.pts([(10, 19), (10, 20)] if frame == 1 else [(10, 20), (10, 21)], TEAR)
        c.pts([(21, 20), (21, 21)] if frame == 1 else [(21, 19), (21, 20)], TEAR)
    elif view == 'left':
        c.pts([(10, 17), (11, 17)], WHITE)
        c.set(10, 17, RED)
        c.pts([(9, 15), (10, 16), (11, 16)], K)
        c.pts([(9, 21), (10, 21), (11, 21)], K)
        c.pts([(12, 19), (12, 20)] if frame == 1 else [(12, 20), (12, 21)], TEAR)
    # up: back view, no face
    return c


def onion_attack(view, frame):
    c = C()
    legs(c, 1, 26) if view in ('down', 'up') else legs(c, 1, 26, 14, 18)
    if frame == 1:
        # inflate, eyes squeezed
        onion_body(c, cy=17, rx=9, ry=9, view=view, lean=(-1 if view == 'left' else 0))
        c.outline()
        if view == 'down':
            c.pts([(11, 15), (12, 16), (13, 17), (12, 18), (11, 19)], K)       # >
            c.pts([(20, 15), (19, 16), (18, 17), (19, 18), (20, 19)], K)       # <
            c.pts([(14, 21), (15, 21), (16, 21), (17, 21)], K)
            c.pts([(8, 14), (7, 13), (23, 14), (24, 13), (9, 12), (22, 12)], TEAR)
        elif view == 'left':
            c.pts([(9, 15), (10, 16), (11, 17), (10, 18), (9, 19)], K)
            c.pts([(8, 21), (9, 21)], K)
            c.pts([(6, 13), (5, 12), (7, 11)], TEAR)
        else:
            c.pts([(8, 12), (7, 11), (24, 12), (25, 11)], TEAR)
    else:
        onion_body(c, view=view, lean=(-1 if view == 'left' else 0))
        c.outline()
        if view == 'down':
            onion_face_down(c)
            c.pts([(14, 21), (15, 21), (16, 21), (17, 21), (14, 22), (17, 22), (14, 23), (15, 23), (16, 23), (17, 23)], K)
            c.pts([(15, 22), (16, 22)], (90, 20, 40, 255))
            puff = [(x, y) for x in range(11, 21) for y in range(27, 32) if (x + y) % 3 != 0]
            c.pts(puff, GAS)
            c.pts([(13, 29), (17, 30), (19, 28)], GAS2)
        elif view == 'left':
            c.pts([(10, 17), (11, 17)], WHITE)
            c.set(10, 17, RED)
            c.pts([(9, 15), (10, 16), (11, 16)], K)
            c.pts([(8, 20), (9, 20), (8, 21), (9, 22)], K)
            puff = [(x, y) for x in range(0, 7) for y in range(14, 25) if (x + y) % 3 != 0]
            c.pts(puff, GAS)
            c.pts([(2, 16), (4, 20), (1, 22)], GAS2)
        else:  # up: gas rises over the head
            puff = [(x, y) for x in range(9, 23) for y in range(0, 5) if (x + y) % 3 != 0]
            c.pts(puff, GAS)
            c.pts([(11, 2), (15, 1), (20, 3)], GAS2)
    return c


# ---------------------------------------------------------------- POTATO
PO = (170, 120, 70, 255)
PO_D = (128, 85, 45, 255)
PO_L = (205, 160, 105, 255)
TOOTH = (235, 230, 210, 255)


def potato_body(c, cx=16, cy=19, rx=10, ry=8, view='down'):
    c.ellipse(cx, cy, rx, ry, PO)
    if view in ('down', 'up'):
        c.ellipse(cx - 9, cy - 1, 2, 3, PO)
        c.ellipse(cx + 9, cy + 1, 2, 3, PO)
    else:
        c.ellipse(cx - 1, cy + 1, 2.5, 3, PO)
    hx = cx - 3 if view != 'up' else cx + 3
    c.recolor_in_ellipse(hx, cy - 4, 4, 2.5, PO, PO_L)
    for (dx, dy) in ((-6, 4), (5, 5), (7, -3), (-1, 6), (-5, -4)):
        c.set(cx + dx, cy + dy, PO_D)


def potato_face_down(c, cy=19, angry=1, open_mouth=False):
    ey = cy - 2
    c.pts([(12, ey), (13, ey), (19, ey), (20, ey)], WHITE)
    c.pts([(13, ey), (19, ey)], RED)
    c.pts([(11, ey - 2), (12, ey - 2), (13, ey - 1), (21, ey - 2), (20, ey - 2), (19, ey - 1)], K)
    if angry > 1:
        c.pts([(10, ey - 3), (22, ey - 3)], K)
    if open_mouth:
        c.pts([(14, cy + 2), (15, cy + 2), (16, cy + 2), (17, cy + 2), (18, cy + 2),
               (14, cy + 4), (15, cy + 4), (16, cy + 4), (17, cy + 4), (18, cy + 4), (13, cy + 3), (19, cy + 3)], K)
        c.pts([(14, cy + 3), (16, cy + 3), (18, cy + 3)], TOOTH)
        c.pts([(15, cy + 3), (17, cy + 3)], (90, 20, 40, 255))
    else:
        c.pts([(14, cy + 3), (15, cy + 2), (16, cy + 2), (17, cy + 2), (18, cy + 3)], K)


def potato_walk(view, frame):
    c = C()
    if view in ('down', 'up'):
        legs(c, frame, 27, 12, 19)
    else:
        legs(c, frame, 27, 13, 18)
    potato_body(c, view=view)
    c.outline()
    if view == 'down':
        potato_face_down(c)
    elif view == 'left':
        c.pts([(8, 17), (9, 17)], WHITE)
        c.set(8, 17, RED)
        c.pts([(7, 15), (8, 15), (9, 16)], K)
        c.pts([(6, 21), (7, 20), (8, 20)], K)
    return c


def potato_attack(view, frame):
    c = C()
    if frame == 1:
        # wind-up: squashed low, very angry, dust behind
        legs(c, 1, 27, 12, 19) if view in ('down', 'up') else legs(c, 1, 27, 13, 18)
        potato_body(c, cy=21, rx=11, ry=7, view=view)
        c.outline()
        if view == 'down':
            potato_face_down(c, cy=21, angry=2)
            c.pts([(4, 27), (5, 28), (27, 27), (26, 28), (3, 29), (28, 29)], DUST)
        elif view == 'left':
            c.pts([(7, 19), (8, 19)], WHITE)
            c.set(7, 19, RED)
            c.pts([(6, 17), (7, 17), (8, 18), (5, 16)], K)
            c.pts([(5, 23), (6, 22), (7, 22)], K)
            c.pts([(27, 26), (28, 27), (29, 28), (26, 28), (29, 25)], DUST)
        else:
            c.pts([(4, 27), (5, 28), (27, 27), (26, 28), (3, 29), (28, 29)], DUST)
    else:
        # charging: leaning in, mouth open, speed streaks behind
        if view in ('down', 'up'):
            legs(c, 2, 27, 12, 19)
            potato_body(c, cy=20 if view == 'down' else 18, view=view)
            c.outline()
            if view == 'down':
                potato_face_down(c, cy=20, angry=2, open_mouth=True)
                for x in (9, 14, 18, 23):
                    c.pts([(x, 1), (x, 2), (x, 3), (x, 4)], STREAK)
            else:
                for x in (9, 14, 18, 23):
                    c.pts([(x, 27), (x, 28), (x, 29), (x, 30)], STREAK)
        else:
            legs(c, 2, 27, 12, 17)
            potato_body(c, cx=14, view=view)
            c.outline()
            c.pts([(6, 17), (7, 17)], WHITE)
            c.set(6, 17, RED)
            c.pts([(5, 15), (6, 15), (7, 16), (4, 14)], K)
            c.pts([(4, 20), (5, 20), (6, 20), (4, 22), (5, 22), (6, 22), (3, 21)], K)
            c.pts([(4, 21), (6, 21)], TOOTH)
            c.set(5, 21, (90, 20, 40, 255))
            for y in (13, 18, 23):
                c.pts([(26, y), (27, y), (28, y), (29, y), (30, y)], STREAK)
    return c


def build(prefix, walk, attack):
    names = {}
    for frame in (1, 2):
        d = walk('down', frame); u = walk('up', frame); l = walk('left', frame); r = l.mirror()
        names[f'{prefix}Down{frame}'] = d; names[f'{prefix}Up{frame}'] = u
        names[f'{prefix}Left{frame}'] = l; names[f'{prefix}Right{frame}'] = r
        d = attack('down', frame); u = attack('up', frame); l = attack('left', frame); r = l.mirror()
        names[f'{prefix}_AttackDown{frame}'] = d; names[f'{prefix}_AttackUp{frame}'] = u
        names[f'{prefix}_AttackLeft{frame}'] = l; names[f'{prefix}_AttackRight{frame}'] = r
    return {k: v.save(k) for k, v in names.items()}


onion = build('Onion', onion_walk, onion_attack)
potato = build('Potato', potato_walk, potato_attack)

# Contact sheet for review
order = ['Down1', 'Down2', 'Up1', 'Up2', 'Left1', 'Left2', 'Right1', 'Right2',
         '_AttackDown1', '_AttackDown2', '_AttackUp1', '_AttackUp2', '_AttackLeft1', '_AttackLeft2', '_AttackRight1', '_AttackRight2']
sheet = Image.new('RGBA', (16 * 36, 2 * 36), (80, 100, 80, 255))
for row, (p, imgs) in enumerate((('Onion', onion), ('Potato', potato))):
    for i, o in enumerate(order):
        im = imgs[p + o]
        sheet.paste(im, (i * 36 + 2, row * 36 + 2), im)
sheet.resize((sheet.width * 4, sheet.height * 4), Image.NEAREST).save(os.path.join(OUT, '..', 'sheet.png'))
print('done', len(onion) + len(potato), 'frames')
