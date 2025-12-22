package net.vercte.satchels.client;

public enum LerpFunctions {
    LINEAR((t, s, e) -> (e - s)*t + s),
    EXPONENTIAL((t, s, e) -> {
        double multiplier = t == 1 ? 1 : 1 - Math.pow(2, -10 * t);
        return (e - s)*(float)multiplier + s;
    });

    private final LerpFunction function;

    LerpFunctions(LerpFunction function) {
        this.function = function;
    }

    public float lerp(float time, float start, float end) {
        return this.function.lerp(time, start, end);
    }
}
