package com.blackmoss.islcttc;

import net.neoforged.neoforge.common.ModConfigSpec;

public class IslcttcConfig {
    public static final ModConfigSpec.Builder COMMON = new ModConfigSpec.Builder();

    private static final String NAME_KEY = Islcttc.MODID + ".configuration.%s";

    // 垂直检测范围
    public static final ModConfigSpec.IntValue DETECTION_VERTICAL_RANGE = COMMON
            .translation(NAME_KEY.formatted("detectionVerticalRange"))
            .defineInRange("detectionVerticalRange", 5, 1, 384);

    // 最大伤害
    public static final ModConfigSpec.IntValue MAX_DAMAGE = COMMON
            .translation(NAME_KEY.formatted("maxDamage"))
            .defineInRange("maxDamage", 1, 1, Integer.MAX_VALUE);

    // 伤害倍数
    public static final ModConfigSpec.IntValue DAMAGE_MULTIPLIER = COMMON
            .translation(NAME_KEY.formatted("damageMultiplier"))
            .defineInRange("damageMultiplier", 1, 1, Integer.MAX_VALUE);
}
