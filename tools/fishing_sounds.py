"""Generates the splash used when you cast or land a fish.
Run from the repo root: python3 tools/fishing_sounds.py"""
import wave
import numpy as np

RATE = 22050
rng = np.random.default_rng(11)

def lowpass(x, a):
    y = np.empty_like(x); acc = 0.0
    for i, v in enumerate(x):
        acc += a * (v - acc); y[i] = acc
    return y

def save(path, x):
    x = x / (np.max(np.abs(x)) + 1e-9) * 0.7
    with wave.open(path, "wb") as w:
        w.setnchannels(1); w.setsampwidth(2); w.setframerate(RATE)
        w.writeframes((x * 32767).astype(np.int16).tobytes())

# A "plop": a falling sine blip, then a short burst of filtered spray noise
n = int(RATE * 0.55)
t = np.arange(n) / RATE
plop = np.sin(2 * np.pi * (900 * t - 1400 * t ** 2)) * np.exp(-t * 28)
spray = lowpass(rng.normal(0, 1, n), 0.25) * np.exp(-((t - 0.06) ** 2) / 0.004) * 0.6
drops = np.zeros(n)
for pos in rng.integers(int(RATE * 0.08), n - 300, 14):
    drops[pos:pos + 200] += rng.uniform(0.1, 0.35) * np.sin(2 * np.pi * rng.uniform(1200, 2400) * np.arange(200) / RATE) * np.exp(-np.arange(200) / 40)
save("src/res/sound/splash.wav", plop + spray + drops)
print("wrote src/res/sound/splash.wav")
