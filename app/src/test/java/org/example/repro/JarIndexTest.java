package org.example.repro;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JarIndexTest {
    @Test
    public void missingResourceUnderIndexedPackageIsNull() {
        assertNull(getClass().getClassLoader().getResource("org/missing.properties"));
    }
}
