package com.github.spacemex;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.slf4j.Logger;

public class Helper {
    @ExpectPlatform
    public static Logger getLogger() {
        throw new AssertionError();
    }
}
