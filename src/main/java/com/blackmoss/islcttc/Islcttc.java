package com.blackmoss.islcttc;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Islcttc.MODID)
public class Islcttc {
    public static final String MODID = "islcttc";

    public Islcttc(ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(new IslcttcEvent());

        modContainer.registerConfig(ModConfig.Type.COMMON, IslcttcConfig.COMMON.build());
    }
}
