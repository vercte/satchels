package net.vercte.satchels.client;

@FunctionalInterface
public interface LerpFunction {
    float lerp(float progress, float start, float end);
}
