# IntelliJ Groovy 2.5 compilation issue

There seems to be a bug in IntelliJ's Groovy compiler that fails when using Groovy 2.5, but succeeds when using Groovy 2.4. Here's how to reproduce it:

1. Import the project in IntelliJ 2018.2.3 by selecting `build.gradle` and choosing **Open as Project...**.
2. The Groovy version in `build.gradle` should be set to `2.4.15`.
3. Disable **Delegate build/run actions to Gradle** in **Build, Execution, Deployment > Build Tools > Gradle > Runner** (running via Gradle works fine, this is a problem with IntelliJ's build)
4. Choose a JDK 9 or 10. OpenJDK works just as well as Oracle. I tested this with OpenJDK 9.0.4, 10.0.2 and Oracle JDK 9.0.1 and 9.0.4 with similar results.
5. Running `App` in the `src/main/groovy` source folder, it should print this:

    ```text
    WARNING: An illegal reflective access operation has occurred
    WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass (file:/Users/lptr/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy/2.4.15/74b7e0b99526c569e3a59cb84dbcc6204d601ee6/groovy-2.4.15.jar) to method java.lang.Object.finalize()
    WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass
    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
    WARNING: All illegal access operations will be denied in a future release
    Hello from Groovy 2.4.15, I can see com.sun.jdi.VMDisconnectedException!
    ```

6. Running it from the command-line has the same effect:

    ```console
    $ ./gradlew -q run
    WARNING: An illegal reflective access operation has occurred
    WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass (file:/Users/lptr/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy/2.4.15/74b7e0b99526c569e3a59cb84dbcc6204d601ee6/groovy-2.4.15.jar) to method java.lang.Object.finalize()
    WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass
    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
    WARNING: All illegal access operations will be denied in a future release
    Hello from Groovy 2.4.15, I can see com.sun.jdi.VMDisconnectedException!
    ```

7. Now change the Groovy version in `build.gradle` from `2.4.15` to `2.5.2`. (The bug can be reproduced with Groovy `2.5.1` and `2.5.0` as well.)
8. Make sure the changes are imported and the import is finished.
9. Try running `App` again, or rebuild the project from **Build > Reuild Project**.
10. You'll see a failure like this in the **Messages > Build** view:

    ```text
    Information:Builder "Groovy compiler" requested rebuild of module chunk "intellij-groovyc-bug_main"
    Information:Module "intellij-groovyc-bug_main" was fully rebuilt due to project configuration/dependencies changes
    Information:2018. 09. 11. 10:48 - Compilation completed with 1 error and 0 warnings in 3 s 85 ms
    /Users/lptr/Workspace/gradle/bugs/intellij-groovyc-bug/src/main/groovy/org/example/jdi/App.groovy
    Error:(3, 1) Groovyc: unable to resolve class com.sun.jdi.VMDisconnectedException
    ```

11. Try running it via the command line and it works fine with Groovy 2.5.2:

    ```console
    $ ./gradlew -q run
    WARNING: An illegal reflective access operation has occurred
    WARNING: Illegal reflective access by org.codehaus.groovy.vmplugin.v7.Java7$1 (file:/Users/lptr/.gradle/caches/modules-2/files-2.1/org.codehaus.groovy/groovy/2.5.2/ff667436cfd98cc2c7e58631883c75d23d52261b/groovy-2.5.2.jar) to constructor java.lang.invoke.MethodHandles$Lookup(java.lang.Class,int)
    WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.vmplugin.v7.Java7$1
    WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
    WARNING: All illegal access operations will be denied in a future release
    Hello from Groovy 2.5.2, I can see com.sun.jdi.VMDisconnectedException!
    ```
