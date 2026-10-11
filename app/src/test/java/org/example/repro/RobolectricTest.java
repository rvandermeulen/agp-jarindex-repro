package org.example.repro;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 35)
public class RobolectricTest {
    @Test
    public void applicationIsAvailable() {
        assertNotNull(RuntimeEnvironment.getApplication());
    }
}
