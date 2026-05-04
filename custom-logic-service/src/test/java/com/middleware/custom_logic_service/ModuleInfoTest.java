package com.middleware.custom_logic_service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ModuleInfoTest {

    @Test
    void moduleNameShouldNotBeBlank() {
        assertFalse(ModuleInfo.NAME.isBlank());
    }
}
