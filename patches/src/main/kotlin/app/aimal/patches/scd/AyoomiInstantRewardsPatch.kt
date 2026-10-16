package app.aimal.patches.scd

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import java.util.logging.Logger

val SERPENT_CHAIN_DEFENSE = Compatibility(
    name = "Serpent Chain Defense",
    packageName = "com.and.aym.scd",
    apkFileType = ApkFileType.XAPK,
    appIconColor = 0xD4AF37,
    targets = listOf(
        AppTarget(version = null, isExperimental = true),
    ),
)

@Suppress("unused")
val ayoomiInstantRewardsPatch = bytecodePatch(
    name = "Ayoomi Instant Rewards",
    description = "Instantly grant ad rewards for games using Ayoomi SDK (Serpent Chain Defense, etc.) without watching ads or triggering AppLovin MAX 25-second timeouts.",
    default = true,
) {
    compatibleWith(SERPENT_CHAIN_DEFENSE)

    execute {
        val logger = Logger.getLogger(this::class.java.name)
        var patched = 0

        // ==========================================
        // Part 1: Patch AyoomiApplication
        // ==========================================
        val appClass = mutableClassDefByOrNull("Lcom/ayoomi/sdk/AyoomiApplication;")
        if (appClass != null) {
            // 1. Force RewardAdIsReady(String)Z -> always true
            val readyMethod = appClass.methods.firstOrNull {
                it.name == "RewardAdIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;")
            }
            if (readyMethod != null && readyMethod.implementation != null) {
                val target = appClass.methods.first { it == readyMethod }
                target.addInstructions(
                    0,
                    """
                    const/4 v0, 0x1
                    return v0
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiApplication.RewardAdIsReady(String) to return true")
                patched++
            }

            // 2. Force OriRewardIsReady()Z -> always true
            val oriReadyMethod = appClass.methods.firstOrNull {
                it.name == "OriRewardIsReady" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            }
            if (oriReadyMethod != null && oriReadyMethod.implementation != null) {
                val target = appClass.methods.first { it == oriReadyMethod }
                target.addInstructions(
                    0,
                    """
                    const/4 v0, 0x1
                    return v0
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiApplication.OriRewardIsReady() to return true")
                patched++
            }

            // 3. Hook ShowRewardVideoAd to immediately grant reward via callback
            val showMethod = appClass.methods.firstOrNull {
                it.name == "ShowRewardVideoAd" &&
                    it.parameterTypes == listOf(
                        "Ljava/lang/String;",
                        "Landroid/app/Activity;",
                        "Lcom/ayoomi/sdk/AyoomiApplication\$OnRewardListener;",
                    )
            }
            if (showMethod != null && showMethod.implementation != null) {
                val target = appClass.methods.first { it == showMethod }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p2
                    if-eqz v0, :morphe_ayoomi_skip_grant
                    const-wide/high16 v1, 0x3ff0000000000000L
                    invoke-interface {v0, v1, v2}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;->onReward(D)V
                    const/4 v0, 0x0
                    sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mRewardListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;
                    :morphe_ayoomi_skip_grant
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiApplication.ShowRewardVideoAd to instantly grant reward")
                patched++
            }

            // 4. Force InterstitialIsReady(String, Z)Z -> always true
            val intersReadyMethod = appClass.methods.firstOrNull {
                it.name == "InterstitialIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;", "Z")
            }
            if (intersReadyMethod != null && intersReadyMethod.implementation != null) {
                val target = appClass.methods.first { it == intersReadyMethod }
                target.addInstructions(
                    0,
                    """
                    const/4 v0, 0x1
                    return v0
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiApplication.InterstitialIsReady(String, Z) to return true")
                patched++
            }

            // 5. Hook ShowInterstitialAd to immediately call onClosed() callback
            val intersShowMethod = appClass.methods.firstOrNull {
                it.name == "ShowInterstitialAd" &&
                    it.parameterTypes == listOf(
                        "Ljava/lang/String;",
                        "Landroid/app/Activity;",
                        "Z",
                        "Lcom/ayoomi/sdk/AyoomiApplication\$OnInterstitialListener;",
                    )
            }
            if (intersShowMethod != null && intersShowMethod.implementation != null) {
                val target = appClass.methods.first { it == intersShowMethod }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p3
                    if-eqz v0, :morphe_ayoomi_skip_inters
                    invoke-interface {v0}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnInterstitialListener;->onClosed()V
                    const/4 v0, 0x0
                    sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mInterstitialListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnInterstitialListener;
                    :morphe_ayoomi_skip_inters
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiApplication.ShowInterstitialAd to instantly complete callback")
                patched++
            }
        } else {
            logger.warning("Ayoomi Instant Rewards: target class Lcom/ayoomi/sdk/AyoomiApplication; not found")
        }

        // ==========================================
        // Part 2: Patch AyoomiUnity (Direct Unity Callbacks)
        // ==========================================
        val unityClass = mutableClassDefByOrNull("Lcom/ayoomi/sdk/AyoomiUnity;")
        if (unityClass != null) {
            // 6. Force AyoomiUnity.RewardAdIsReady(String)Z -> always true
            val uReadyMethod = unityClass.methods.firstOrNull {
                it.name == "RewardAdIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;")
            }
            if (uReadyMethod != null && uReadyMethod.implementation != null) {
                val target = unityClass.methods.first { it == uReadyMethod }
                target.addInstructions(
                    0,
                    """
                    const/4 v0, 0x1
                    return v0
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.RewardAdIsReady(String) to return true")
                patched++
            }

            // 7. Force AyoomiUnity.InterstitialIsReady(String)Z and (String, Z)Z -> always true
            val uIntersReady1 = unityClass.methods.firstOrNull {
                it.name == "InterstitialIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;")
            }
            if (uIntersReady1 != null && uIntersReady1.implementation != null) {
                val target = unityClass.methods.first { it == uIntersReady1 }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.InterstitialIsReady(String) to return true")
                patched++
            }
            val uIntersReady2 = unityClass.methods.firstOrNull {
                it.name == "InterstitialIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;", "Z")
            }
            if (uIntersReady2 != null && uIntersReady2.implementation != null) {
                val target = unityClass.methods.first { it == uIntersReady2 }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.InterstitialIsReady(String, Z) to return true")
                patched++
            }

            // 8. Hook AyoomiUnity.ShowRewardVideoAd(String, OnRewardListener) -> immediately invoke listener.onReward()
            val uShowMethod1 = unityClass.methods.firstOrNull {
                it.name == "ShowRewardVideoAd" &&
                    it.parameterTypes == listOf("Ljava/lang/String;", "Lcom/ayoomi/sdk/AyoomiUnity\$OnRewardListener;")
            }
            if (uShowMethod1 != null && uShowMethod1.implementation != null) {
                val target = unityClass.methods.first { it == uShowMethod1 }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p1
                    if-eqz v0, :morphe_unity_skip_r1
                    invoke-interface {v0}, Lcom/ayoomi/sdk/AyoomiUnity${'$'}OnRewardListener;->onReward()V
                    :morphe_unity_skip_r1
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.ShowRewardVideoAd(OnRewardListener) to instantly call onReward()")
                patched++
            }

            // 9. Hook AyoomiUnity.ShowRewardVideoAd(String, OnRewardRevListener) -> immediately invoke listener.onReward(1.0)
            val uShowMethod2 = unityClass.methods.firstOrNull {
                it.name == "ShowRewardVideoAd" &&
                    it.parameterTypes == listOf("Ljava/lang/String;", "Lcom/ayoomi/sdk/AyoomiUnity\$OnRewardRevListener;")
            }
            if (uShowMethod2 != null && uShowMethod2.implementation != null) {
                val target = unityClass.methods.first { it == uShowMethod2 }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p1
                    if-eqz v0, :morphe_unity_skip_r2
                    const-wide/high16 v1, 0x3ff0000000000000L
                    invoke-interface {v0, v1, v2}, Lcom/ayoomi/sdk/AyoomiUnity${'$'}OnRewardRevListener;->onReward(D)V
                    :morphe_unity_skip_r2
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.ShowRewardVideoAd(OnRewardRevListener) to instantly call onReward(D)")
                patched++
            }

            // 10. Hook AyoomiUnity.ShowInterstitialAd(...) -> immediately invoke onClosed()
            val uIntersShow1 = unityClass.methods.firstOrNull {
                it.name == "ShowInterstitialAd" &&
                    it.parameterTypes == listOf("Ljava/lang/String;", "Lcom/ayoomi/sdk/AyoomiUnity\$OnInterstitialListener;")
            }
            if (uIntersShow1 != null && uIntersShow1.implementation != null) {
                val target = unityClass.methods.first { it == uIntersShow1 }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p1
                    if-eqz v0, :morphe_unity_skip_i1
                    invoke-interface {v0}, Lcom/ayoomi/sdk/AyoomiUnity${'$'}OnInterstitialListener;->onClosed()V
                    :morphe_unity_skip_i1
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.ShowInterstitialAd(1) to instantly call onClosed()")
                patched++
            }

            val uIntersShow2 = unityClass.methods.firstOrNull {
                it.name == "ShowInterstitialAd" &&
                    it.parameterTypes == listOf("Ljava/lang/String;", "Z", "Lcom/ayoomi/sdk/AyoomiUnity\$OnInterstitialListener;")
            }
            if (uIntersShow2 != null && uIntersShow2.implementation != null) {
                val target = unityClass.methods.first { it == uIntersShow2 }
                target.addInstructions(
                    0,
                    """
                    move-object/from16 v0, p2
                    if-eqz v0, :morphe_unity_skip_i2
                    invoke-interface {v0}, Lcom/ayoomi/sdk/AyoomiUnity${'$'}OnInterstitialListener;->onClosed()V
                    :morphe_unity_skip_i2
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AyoomiUnity.ShowInterstitialAd(2) to instantly call onClosed()")
                patched++
            }
        } else {
            logger.warning("Ayoomi Instant Rewards: target class Lcom/ayoomi/sdk/AyoomiUnity; not found")
        }

        // ==========================================
        // Part 3: Patch AdapterMAX (AppLovin MAX Fallback)
        // ==========================================
        val maxClass = mutableClassDefByOrNull("Lcom/ayoomi/sdk/adapter/AdapterMAX;")
        if (maxClass != null) {
            val maxReady = maxClass.methods.firstOrNull {
                it.name == "RewardIsReady" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            }
            if (maxReady != null && maxReady.implementation != null) {
                val target = maxClass.methods.first { it == maxReady }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: patched AdapterMAX.RewardIsReady() to return true")
                patched++
            }
            val maxIntersReady = maxClass.methods.firstOrNull {
                it.name == "InterstitialIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Z")
            }
            if (maxIntersReady != null && maxIntersReady.implementation != null) {
                val target = maxClass.methods.first { it == maxIntersReady }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: patched AdapterMAX.InterstitialIsReady(Z) to return true")
                patched++
            }

            // Hook AdapterMAX.ShowRewardVideo(String) -> immediately invoke AyoomiApplication.mRewardListener
            val maxShowReward = maxClass.methods.firstOrNull {
                it.name == "ShowRewardVideo" && it.returnType == "V" && it.parameterTypes == listOf("Ljava/lang/String;")
            }
            if (maxShowReward != null && maxShowReward.implementation != null) {
                val target = maxClass.methods.first { it == maxShowReward }
                target.addInstructions(
                    0,
                    """
                    sget-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mRewardListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;
                    if-eqz v0, :morphe_max_skip_reward
                    const-wide/high16 v1, 0x3ff0000000000000L
                    invoke-interface {v0, v1, v2}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;->onReward(D)V
                    const/4 v0, 0x0
                    sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mRewardListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;
                    :morphe_max_skip_reward
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AdapterMAX.ShowRewardVideo to instantly grant reward")
                patched++
            }

            // Hook AdapterMAX.ShowInterstitialVideo(String, Z) -> immediately invoke AyoomiApplication.mInterstitialListener
            val maxShowInters = maxClass.methods.firstOrNull {
                it.name == "ShowInterstitialVideo" && it.returnType == "V" && it.parameterTypes == listOf("Ljava/lang/String;", "Z")
            }
            if (maxShowInters != null && maxShowInters.implementation != null) {
                val target = maxClass.methods.first { it == maxShowInters }
                target.addInstructions(
                    0,
                    """
                    sget-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mInterstitialListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnInterstitialListener;
                    if-eqz v0, :morphe_max_skip_inters
                    invoke-interface {v0}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnInterstitialListener;->onClosed()V
                    const/4 v0, 0x0
                    sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mInterstitialListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnInterstitialListener;
                    :morphe_max_skip_inters
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: patched AdapterMAX.ShowInterstitialVideo to instantly complete callback")
                patched++
            }
        }

        // ==========================================
        // Part 4: Complete PairIP License Check & Crash Killer Bypass (for Non-Root / Galaxy S24)
        // ==========================================
        // 1. Neuter PairIP LicenseClient.checkLicense(Context) root trigger
        val licenseClientClass = mutableClassDefByOrNull("Lcom/pairip/licensecheck/LicenseClient;")
        if (licenseClientClass != null) {
            val checkLicenseMethod = licenseClientClass.methods.firstOrNull {
                it.name == "checkLicense" && it.returnType == "V" && it.parameterTypes == listOf("Landroid/content/Context;")
            }
            if (checkLicenseMethod != null && checkLicenseMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == checkLicenseMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseClient.checkLicense()")
                patched++
            }

            // 2. Neuter initializeLicenseCheck()
            val initCheckMethod = licenseClientClass.methods.firstOrNull {
                it.name == "initializeLicenseCheck" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (initCheckMethod != null && initCheckMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == initCheckMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseClient.initializeLicenseCheck()")
                patched++
            }

            // 3. Neuter scheduleAppShutdown()
            val shutdownMethod = licenseClientClass.methods.firstOrNull {
                it.name == "scheduleAppShutdown" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (shutdownMethod != null && shutdownMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == shutdownMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseClient.scheduleAppShutdown()")
                patched++
            }

            // 4. Force performLocalInstallerCheck()Z -> return true
            val localCheckMethod = licenseClientClass.methods.firstOrNull {
                it.name == "performLocalInstallerCheck" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            }
            if (localCheckMethod != null && localCheckMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == localCheckMethod }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: patched PairIP performLocalInstallerCheck to return true")
                patched++
            }

            // 5. Neuter handleError(LicenseCheckException)
            val handleErrorMethod = licenseClientClass.methods.firstOrNull {
                it.name == "handleError" && it.returnType == "V" && it.parameterTypes == listOf("Lcom/pairip/licensecheck/LicenseCheckException;")
            }
            if (handleErrorMethod != null && handleErrorMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == handleErrorMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseClient.handleError()")
                patched++
            }

            // 6. Neuter startErrorDialogActivity()
            val startErrorMethod = licenseClientClass.methods.firstOrNull {
                it.name == "startErrorDialogActivity" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (startErrorMethod != null && startErrorMethod.implementation != null) {
                val target = licenseClientClass.methods.first { it == startErrorMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseClient.startErrorDialogActivity()")
                patched++
            }
        }

        // 7. Neuter PairIP Application.attachBaseContext license check
        val pairipAppClass = mutableClassDefByOrNull("Lcom/pairip/application/Application;")
        if (pairipAppClass != null) {
            val attachMethod = pairipAppClass.methods.firstOrNull {
                it.name == "attachBaseContext" && it.returnType == "V" && it.parameterTypes == listOf("Landroid/content/Context;")
            }
            if (attachMethod != null && attachMethod.implementation != null) {
                val target = pairipAppClass.methods.first { it == attachMethod }
                target.addInstructions(
                    0,
                    """
                    invoke-super {p0, p1}, Lcom/pairip/application/Application;->attachBaseContext(Landroid/content/Context;)V
                    return-void
                    """.trimIndent()
                )
                logger.info("Ayoomi Instant Rewards: bypassed PairIP Application.attachBaseContext()")
                patched++
            }
        }

        // 8. Neuter PairIP LicenseContentProvider.onCreate()
        val providerClass = mutableClassDefByOrNull("Lcom/pairip/licensecheck/LicenseContentProvider;")
        if (providerClass != null) {
            val onCreateMethod = providerClass.methods.firstOrNull {
                it.name == "onCreate" && it.returnType == "Z" && it.parameterTypes.isEmpty()
            }
            if (onCreateMethod != null && onCreateMethod.implementation != null) {
                val target = providerClass.methods.first { it == onCreateMethod }
                target.addInstructions(0, "const/4 v0, 0x1\nreturn v0")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseContentProvider.onCreate()")
                patched++
            }
        }

        // 9. Neuter PairIP LicenseActivity.exitApp() and closeApp()
        val licenseActivityClass = mutableClassDefByOrNull("Lcom/pairip/licensecheck/LicenseActivity;")
        if (licenseActivityClass != null) {
            val exitAppMethod = licenseActivityClass.methods.firstOrNull {
                it.name == "exitApp" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (exitAppMethod != null && exitAppMethod.implementation != null) {
                val target = licenseActivityClass.methods.first { it == exitAppMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseActivity.exitApp()")
                patched++
            }
            val onStartMethod = licenseActivityClass.methods.firstOrNull {
                it.name == "onStart" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (onStartMethod != null && onStartMethod.implementation != null) {
                val target = licenseActivityClass.methods.first { it == onStartMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered PairIP LicenseActivity.onStart()")
                patched++
            }
        }

        // ==========================================
        // Part 7: Suppress Application.Quit in UnityPlayer
        // ==========================================
        val unityPlayerClass = mutableClassDefByOrNull("Lcom/unity3d/player/UnityPlayer;")
        if (unityPlayerClass != null) {
            val killMethod = unityPlayerClass.methods.firstOrNull {
                it.name == "kill" && it.returnType == "V" && it.parameterTypes.isEmpty()
            }
            if (killMethod != null && killMethod.implementation != null) {
                val target = unityPlayerClass.methods.first { it == killMethod }
                target.addInstructions(0, "return-void")
                logger.info("Ayoomi Instant Rewards: neutered UnityPlayer.kill()")
                patched++
            }
        }

        if (patched > 0) {
            logger.info("Ayoomi Instant Rewards: successfully applied $patched patch(es)")
        } else {
            logger.warning("Ayoomi Instant Rewards: failed to match any target methods")
        }
    }
}
