package app.aimal.patches.streaming

import app.morphe.patcher.Fingerprint

/**
 * Injection points, verified against
 * Disney+ 26.14.1+rc2-2026.08.20 (com.disney.disneyplus).
 */

private const val EXO_PLAYER_INTERFACE = "Landroidx/media3/exoplayer/ExoPlayer;"

/**
 * Disney+ hooks the main Activity instead: its Application class
 * (DominguezMobileApplication) inherits onCreate from an obfuscated base and
 * does not declare one of its own, so there is no method there to hook.
 *
 * `p0` is the Activity, which is a Context, and attachBaseContext has already
 * run by the time onCreate is entered - so the context is usable at index 0.
 */
internal object DisneyPlusMainActivityFingerprint : Fingerprint(
    definingClass = "Lcom/bamtechmedia/dominguez/main/MainActivity;",
    name = "onCreate",
    returnType = "V",
    parameters = listOf("Landroid/os/Bundle;"),
)

/**
 * Every constructor of every class implementing media3's ExoPlayer interface.
 *
 * What both preserve is the *interface* type, because media3's own ProGuard rules
 * keep it - so the interface is what this matches on.
 */
internal object ExoPlayerConstructorFingerprint : Fingerprint(
    name = "<init>",
    returnType = "V",
    custom = { _, classDef -> EXO_PLAYER_INTERFACE in classDef.interfaces },
)

/** Disney+ state object emitted when its native skip action becomes visible. */
internal object DisneySkipStateFingerprint : Fingerprint(
    returnType = "Ljava/lang/String;",
    strings = listOf("Show(label=", ", shouldRequestFocus=", ", skipPointMs=", ", isControlsVisible="),
)
