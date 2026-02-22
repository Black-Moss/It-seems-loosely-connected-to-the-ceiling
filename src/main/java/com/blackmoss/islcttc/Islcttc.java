package com.blackmoss.islcttc;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Islcttc.MODID)
public class Islcttc {
    public static final String MODID = "islcttc";
    public static final Logger LOGGER = LogUtils.getLogger();


    public Islcttc(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new IslcttcEvent());

        modContainer.registerConfig(ModConfig.Type.COMMON, IslcttcConfig.COMMON.build());
    }
}
