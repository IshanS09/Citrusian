"""Tiny pixel-art toolkit used by the Citrusian sprite scripts.

Characters are built from filled shapes, then shaded (light from the top-left)
and outlined, which keeps every sprite in the same style.
"""
from PIL import Image

T = (0, 0, 0, 0)
K = (20, 16, 20, 255)  # outline


def rgb(h, a=255):
    h = h.lstrip('#')
    return (int(h[0:2], 16), int(h[2:4], 16), int(h[4:6], 16), a)


def mix(c, d, t):
    return tuple(int(c[i] + (d[i] - c[i]) * t) for i in range(3)) + (255,)


def lighter(c, t=0.25):
    return mix(c, (255, 255, 255, 255), t)


def darker(c, t=0.25):
    return mix(c, (0, 0, 0, 255), t)


class Canvas:
    def __init__(self, w, h=None):
        self.w = w
        self.h = h or w
        self.g = [[T] * self.w for _ in range(self.h)]
        self.tag = [[None] * self.w for _ in range(self.h)]  # which part each pixel belongs to

    # ---------------------------------------------------------------- basics
    def inside(self, x, y):
        return 0 <= x < self.w and 0 <= y < self.h

    def get(self, x, y):
        return self.g[y][x] if self.inside(x, y) else T

    def set(self, x, y, c, tag=None):
        if self.inside(x, y):
            self.g[y][x] = c
            self.tag[y][x] = tag

    def pts(self, pts, c, tag=None):
        for x, y in pts:
            self.set(x, y, c, tag)

    def rect(self, x0, y0, x1, y1, c, tag=None):
        for y in range(y0, y1 + 1):
            for x in range(x0, x1 + 1):
                self.set(x, y, c, tag)

    def ellipse(self, cx, cy, rx, ry, c, tag=None):
        for y in range(self.h):
            for x in range(self.w):
                if ((x + .5 - cx) / rx) ** 2 + ((y + .5 - cy) / ry) ** 2 <= 1:
                    self.set(x, y, c, tag)

    def poly(self, points, c, tag=None):
        n = len(points)
        for y in range(self.h):
            for x in range(self.w):
                px, py = x + .5, y + .5
                inside = False
                j = n - 1
                for i in range(n):
                    xi, yi = points[i]
                    xj, yj = points[j]
                    if (yi > py) != (yj > py) and px < (xj - xi) * (py - yi) / (yj - yi + 1e-9) + xi:
                        inside = not inside
                    j = i
                if inside:
                    self.set(x, y, c, tag)

    def line(self, x0, y0, x1, y1, c, tag=None, width=1):
        dx, dy = abs(x1 - x0), -abs(y1 - y0)
        sx, sy = (1 if x0 < x1 else -1), (1 if y0 < y1 else -1)
        err = dx + dy
        while True:
            for ox in range(width):
                for oy in range(width):
                    self.set(x0 + ox, y0 + oy, c, tag)
            if x0 == x1 and y0 == y1:
                break
            e2 = 2 * err
            if e2 >= dy:
                err += dy
                x0 += sx
            if e2 <= dx:
                err += dx
                y0 += sy

    # ---------------------------------------------------------------- shading
    def shade(self, tag, base, hi=None, lo=None, hi_amt=0.28, lo_amt=0.28):
        """Light edges facing up/left, dark edges facing down/right, for one part."""
        hi = hi or lighter(base, hi_amt)
        lo = lo or darker(base, lo_amt)
        out = [r[:] for r in self.g]
        for y in range(self.h):
            for x in range(self.w):
                if self.tag[y][x] != tag:
                    continue
                def other(dx, dy):
                    nx, ny = x + dx, y + dy
                    return not self.inside(nx, ny) or self.tag[ny][nx] != tag
                if other(1, 0) or other(0, 1):
                    out[y][x] = lo
                elif other(-1, 0) or other(0, -1):
                    out[y][x] = hi
        self.g = out

    def outline(self, color=K):
        out = [r[:] for r in self.g]
        for y in range(self.h):
            for x in range(self.w):
                if self.g[y][x] != T:
                    continue
                for dx, dy in ((0, 1), (1, 0), (0, -1), (-1, 0)):
                    nx, ny = x + dx, y + dy
                    if self.inside(nx, ny) and self.g[ny][nx] not in (T, color):
                        out[y][x] = color
                        break
        self.g = out

    # ---------------------------------------------------------------- output
    def mirrored(self):
        m = Canvas(self.w, self.h)
        m.g = [list(reversed(r)) for r in self.g]
        m.tag = [list(reversed(r)) for r in self.tag]
        return m

    def shifted(self, dx, dy):
        m = Canvas(self.w, self.h)
        for y in range(self.h):
            for x in range(self.w):
                if self.inside(x - dx, y - dy):
                    m.g[y][x] = self.g[y - dy][x - dx]
        return m

    def image(self):
        im = Image.new('RGBA', (self.w, self.h))
        im.putdata([c for r in self.g for c in r])
        return im

    def save(self, path):
        self.image().save(path)


def sheet(images, path, scale=4, cols=8, bg=(80, 100, 80, 255)):
    cell = max(max(i.size) for i in images) + 4
    rows = (len(images) + cols - 1) // cols
    s = Image.new('RGBA', (cols * cell, rows * cell), bg)
    for k, im in enumerate(images):
        s.paste(im, ((k % cols) * cell + 2, (k // cols) * cell + 2), im)
    s.resize((s.width * scale, s.height * scale), Image.NEAREST).save(path)
