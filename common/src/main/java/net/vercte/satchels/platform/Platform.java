package net.vercte.satchels.platform;

public enum Platform {
    FABRIC("Fabric"), NEOFORGE("NeoForge");

    private final String name;
    Platform(String name) { this.name = name; }

    @Override
    public String toString() {
        return "Platform{" + name + "}";
    }
}
