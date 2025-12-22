package net.vercte.satchels.client;

public class LerpHelper {
    public static float getProgress(long currentTime, long startTime, long endTime) {
        return Math.clamp((float) (currentTime - startTime) / (endTime - startTime), 0, 1);
    }
}
