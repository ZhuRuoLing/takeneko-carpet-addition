package icu.takeneko.tnca;

import icu.takeneko.tnca.hook.ClassPatcher;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.lang.instrument.UnmodifiableClassException;

public class Prelaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
