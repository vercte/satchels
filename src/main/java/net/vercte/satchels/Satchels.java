package net.vercte.satchels;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.vercte.satchels.compat.SatchelsCompat;
import net.vercte.satchels.network.ModPackets;
import net.vercte.satchels.util.SatchelsDataGeneration;

@Mod(Satchels.ID)
public class Satchels {
    public static final String ID = "satchels";

    public Satchels(IEventBus bus, ModContainer container) {
        SatchelsCommonConfig.load(bus);

        ModItems.loadAndListen(bus);
        ModSounds.loadAndRegister(bus);
        ModAttachmentTypes.loadAndRegister(bus);

        NeoForge.EVENT_BUS.addListener(SatchelsEventHooks::playerJoin);
        NeoForge.EVENT_BUS.addListener(SatchelsEventHooks::playerClone);
        NeoForge.EVENT_BUS.addListener(SatchelsEventHooks::onContainerOpen);

        bus.addListener(ModPackets::registerPayloadHandlers);
        bus.addListener(SatchelsDataGeneration::gatherData);
        bus.addListener(SatchelsEventHooks::creativeTabBuild);

        bus.addListener(Satchels::initExtra);

        SatchelsCompat.initialize();

        container.registerConfig(ModConfig.Type.COMMON, SatchelsCommonConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void initExtra(final FMLCommonSetupEvent event) {
        CauldronInteraction.WATER.map().put(ModItems.SATCHEL.get(), CauldronInteraction.DYED_ITEM);
    }

    public static ResourceLocation at(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
