# AGP Java resource optimization breaks JAR indexes on unit test classpaths

With `android.experimental.enableJavaResourceOptimizations` enabled (the default as of AGP 9.5.0-alpha09), local unit
tests fail with `jdk.internal.util.jar.InvalidJarIndexError: Invalid index` as soon as anything looks up a resource
under a package listed in a dependency's `META-INF/INDEX.LIST`. Robolectric does this for every test class in an
`org.*` package (it probes `org/robolectric.properties`), so every such test fails with `initializationError`.

## Steps

Requires JDK 17 or 21 (JDK 22 and later ignore `INDEX.LIST`, which hides the problem) and an Android SDK with
platform 37.2.

```
./gradlew :app:testDebugUnitTest
```

Expected: both tests pass. Actual: both fail with `InvalidJarIndexError`.

| AGP           | enableJavaResourceOptimizations | Result |
|---------------|---------------------------------|--------|
| 9.4.1         | false (default)                 | pass   |
| 9.4.1         | true                            | fail   |
| 9.5.0-alpha09 | true (default)                  | fail   |
| 9.5.0-alpha09 | false                           | pass   |

Workaround: set `android.experimental.enableJavaResourceOptimizations=false` in `gradle.properties`.

## Cause

`JavaResCompressionTransform` adds a resource-only copy of each dependency jar to the unit test runtime classpath.
For `objenesis-3.4.jar` the copy keeps `META-INF/INDEX.LIST`, which lists the `org` package, but drops all classes and
directory entries. When a lookup reaches that copy, `URLClassPath` follows the index back to the same jar, finds no
entry in the `org` package, and throws `InvalidJarIndexError`. Either dropping `META-INF/INDEX.LIST` from the copy or
keeping the directory entries would avoid it.

`android.packaging.resources.excludes += "META-INF/INDEX.LIST"` does not help: the transform only receives packaging
excludes for `ConsumableCreationConfig` components, and the unit test component is not one.
