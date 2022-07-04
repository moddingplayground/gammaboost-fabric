package net.moddingplayground.gammaboost.api.client.config;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.moddingplayground.frame.api.config.v0.Config;
import net.moddingplayground.frame.api.config.v0.option.BooleanOption;
import net.moddingplayground.frame.api.config.v0.option.IntOption;
import net.moddingplayground.frame.api.config.v0.option.Option;

import java.io.File;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;
import static net.moddingplayground.gammaboost.api.GammaBoost.*;

public class GammaBoostConfig extends Config {
    private static final String TOGGLE_KEY = "command." + MOD_ID + ":config.toggle";
    private static final String RELOAD_KEY = "command." + MOD_ID + ":config.reload";

    public static final GammaBoostConfig INSTANCE = new GammaBoostConfig(createFile("gammaboost")).load();

    public final BooleanOption enabled = mod("enabled", BooleanOption.of(true));

    public final BooleanOption underground_enabled = underground("enabled", BooleanOption.of(true));
    public final IntOption underground_minimumLightLevel = underground("minimum_light_level", IntOption.of(0, 0, 15));
    public final IntOption underground_base = underground("base", IntOption.ofMin(200, 0));
    public final IntOption underground_startY = underground("start_y", IntOption.ofFree(63));
    public final IntOption underground_endY = underground("end_y", IntOption.ofFree(32));

    protected GammaBoostConfig(File file) {
        super(file);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                literal(MOD_ID + ":config")
                    .then(
                        literal("toggle")
                            .executes(context -> {
                                boolean nu = !this.enabled.getValue();
                                this.enabled.setValue(nu);
                                this.save();
                                context.getSource().sendFeedback(Text.translatable(TOGGLE_KEY, nu));
                                return nu ? 1 : 0;
                            })
                    )
                    .then(
                        literal("reload")
                            .executes(context -> {
                                this.load();
                                context.getSource().sendFeedback(Text.translatable(RELOAD_KEY));
                                return 1;
                            })
                    )
            );
        });
    }

    protected  <T, O extends Option<T>> O mod(String id, O option) {
        return this.add(new Identifier(MOD_ID, id), option);
    }

    protected  <T, O extends Option<T>> O underground(String id, O option) {
        return this.add(new Identifier("underground", id), option);
    }
}
