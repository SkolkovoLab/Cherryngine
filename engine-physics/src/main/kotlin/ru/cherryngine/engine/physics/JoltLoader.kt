package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.Jolt
import com.github.stephengold.joltjni.JoltPhysicsObject
import electrostatic4j.snaploader.LibraryInfo
import electrostatic4j.snaploader.LoadingCriterion
import electrostatic4j.snaploader.NativeBinaryLoader
import electrostatic4j.snaploader.filesystem.DirectoryPath
import electrostatic4j.snaploader.platform.NativeDynamicLibrary
import electrostatic4j.snaploader.platform.util.PlatformPredicate
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories

// https://github.com/stephengold/jolt-jni-docs/blob/master/java-apps/src/main/java/com/github/stephengold/sportjolt/javaapp/sample/console/HelloJoltJni.java
object JoltLoader {
    private var inited = false
    fun checkAndInit() {
        if (inited) return
        inited = true

        val nativesDir = Path("natives").apply { createDirectories() }
        val info = LibraryInfo(null, "joltjni", DirectoryPath(nativesDir.absolutePathString()))
        val loader = NativeBinaryLoader(info)
        val libraries = arrayOf(
            // NativeDynamicLibrary("linux/aarch64/com/github/stephengold", PlatformPredicate.LINUX_ARM_64),
            // NativeDynamicLibrary("linux/armhf/com/github/stephengold", PlatformPredicate.LINUX_ARM_32),
            NativeDynamicLibrary("linux/x86-64/com/github/stephengold", PlatformPredicate.LINUX_X86_64),
            // NativeDynamicLibrary("osx/aarch64/com/github/stephengold", PlatformPredicate.MACOS_ARM_64),
            // NativeDynamicLibrary("osx/x86-64/com/github/stephengold", PlatformPredicate.MACOS_X86_64),
            NativeDynamicLibrary("windows/x86-64/com/github/stephengold", PlatformPredicate.WIN_X86_64)
        )
        loader.registerNativeLibraries(libraries).initPlatformLibrary()
        loader.loadLibrary(LoadingCriterion.CLEAN_EXTRACTION)

        // Jolt.setTraceAllocations(true); // to log Jolt-JNI heap allocations
        JoltPhysicsObject.startCleaner() // to reclaim native memory
        Jolt.registerDefaultAllocator() // tell Jolt Physics to use malloc/free
        Jolt.installDefaultAssertCallback()
        Jolt.installDefaultTraceCallback()
        Jolt.newFactory().also(::check)
        Jolt.registerTypes()
    }
}