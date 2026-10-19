package io.github.tedlivings.spendwise;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;


public class ModularityTests {

    private static final ApplicationModules MODULES = ApplicationModules.of(SpendwiseBackendApplication.class);

    @Test
    public void shouldRespectModuleBoundaries() {
        MODULES.verify();
    }
}
