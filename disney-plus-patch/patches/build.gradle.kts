group = "app.aimal"

patches {
    about {
        name = "Disney+ Patches"
        description = "Playback speed, aspect ratio and intro-skip patches for Disney+"
        source = "https://github.com/monesys/disney-plus-patch"
        author = "monesys"
        contact = "https://github.com/monesys/disney-plus-patch/issues"
        website = "https://github.com/monesys/disney-plus-patch"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Separate configuration so gson is available at runtime for the
// generatePatchesList task but never bundled into the patched app.
val patchListGeneratorClasspath = configurations.create("patchListGeneratorClasspath")

dependencies {
    compileOnly(libs.gson)
    patchListGeneratorClasspath(libs.gson)
}

tasks {
    // Called by .releaserc during a release to regenerate patches-list.json.
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath + patchListGeneratorClasspath
        mainClass.set("util.PatchListGeneratorKt")
    }

    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}
