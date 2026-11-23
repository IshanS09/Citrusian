"""Generates the rain loop and thunder clap used by the weather system.
Run from the repo root: python3 tools/weather_sounds.py"""
import wave
import numpy as np

RATE = 22050
rng = np.random.default_rng(7)

def lowpass(x, a):
    y = np.empty_like(x); acc = 0.0
    for i, v in enumerate(x):
        acc += a * (v - acc); y[i] = acc
    return y

def save(path, x):
    x = x / (np.max(np.abs(x)) + 1e-9) * 0.8
    with wave.open(path, "wb") as w:
        w.setnchannels(1); w.setsampwidth(2); w.setframerate(RATE)
        w.writeframes((x * 32767).astype(np.int16).tobytes())

# Rain: soft hiss plus random drop ticks, crossfaded so it loops cleanly
n = RATE * 6
hiss = lowpass(rng.normal(0, 1, n + RATE), 0.35)
hiss -= lowpass(hiss, 0.02)              # cut the rumble so it sounds like water, not wind
ticks = np.zeros(n + RATE)
for pos in rng.integers(0, n + RATE - 200, 2600):
    ticks[pos:pos + 120] += rng.uniform(0.2, 1.0) * np.exp(-np.arange(120) / 18) * rng.normal(0, 1, 120)
rain = hiss * 0.6 + lowpass(ticks, 0.5) * 0.5
fade = np.linspace(0, 1, RATE)
loop = rain[:n].copy()
loop[:RATE] = rain[:RATE] * fade + rain[n:n + RATE] * (1 - fade)
save("src/res/sound/rain.wav", loop)

# Thunder: a sharp crack that rolls into a long low rumble
n = int(RATE * 3.5)
t = np.arange(n) / RATE
crack = rng.normal(0, 1, n) * np.exp(-t * 18)
rumble = lowpass(lowpass(rng.normal(0, 1, n), 0.01), 0.03)
rumble *= (1 - np.exp(-t * 6)) * np.exp(-t * 0.9) * (1 + 0.5 * np.sin(t * 5.3))
save("src/res/sound/thunder.wav", crack * 0.35 + rumble / (np.max(np.abs(rumble)) + 1e-9))
print("wrote rain.wav and thunder.wav")
