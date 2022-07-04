package net.moddingplayground.gammaboost.impl;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.moddingplayground.frame.api.util.InitializationLogger;
import net.moddingplayground.gammaboost.api.GammaBoost;
import net.moddingplayground.gammaboost.api.client.config.GammaBoostConfig;

public final class GammaBoostClientImpl implements GammaBoost, ClientModInitializer {

    private final InitializationLogger initializer;

    public GammaBoostClientImpl() {
        this.initializer = new InitializationLogger(LOGGER, MOD_NAME, EnvType.CLIENT);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitializeClient() {
        this.initializer.start();
        Reflection.initialize(GammaBoostConfig.class);
        this.initializer.finish();
    }
}
