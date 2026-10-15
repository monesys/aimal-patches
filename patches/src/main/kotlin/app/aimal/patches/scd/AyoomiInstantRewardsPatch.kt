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
        val targetClass = "Lcom/ayoomi/sdk/AyoomiApplication;"
        val mutableClass = mutableClassDefByOrNull(targetClass)

        if (mutableClass == null) {
            logger.info("Ayoomi Instant Rewards: target class $targetClass not found. Skipping.")
            return@execute
        }

        var patched = 0

        // 1. Force RewardAdIsReady(String)Z -> always true
        val readyMethod = mutableClass.methods.firstOrNull {
            it.name == "RewardAdIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;")
        }
        if (readyMethod != null && readyMethod.implementation != null) {
            val target = mutableClass.methods.first { it == readyMethod }
            target.addInstructions(
                0,
                """
                const/4 v0, 0x1
                return v0
                """.trimIndent()
            )
            logger.info("Ayoomi Instant Rewards: patched RewardAdIsReady(String) to return true")
            patched++
        }

        // 2. Force OriRewardIsReady()Z -> always true
        val oriReadyMethod = mutableClass.methods.firstOrNull {
            it.name == "OriRewardIsReady" && it.returnType == "Z" && it.parameterTypes.isEmpty()
        }
        if (oriReadyMethod != null && oriReadyMethod.implementation != null) {
            val target = mutableClass.methods.first { it == oriReadyMethod }
            target.addInstructions(
                0,
                """
                const/4 v0, 0x1
                return v0
                """.trimIndent()
            )
            logger.info("Ayoomi Instant Rewards: patched OriRewardIsReady() to return true")
            patched++
        }

        // 3. Hook ShowRewardVideoAd to immediately grant reward via callback
        val showMethod = mutableClass.methods.firstOrNull {
            it.name == "ShowRewardVideoAd" &&
                it.parameterTypes == listOf(
                    "Ljava/lang/String;",
                    "Landroid/app/Activity;",
                    "Lcom/ayoomi/sdk/AyoomiApplication\$OnRewardListener;",
                )
        }
        if (showMethod != null && showMethod.implementation != null) {
            val target = mutableClass.methods.first { it == showMethod }
            target.addInstructions(
                0,
                """
                if-nez p2, :morphe_ayoomi_skip_grant
                const-wide/high16 v0, 0x3ff0000000000000L
                invoke-interface {p2, v0, v1}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;->onReward(D)V
                const/4 v0, 0x0
                sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mRewardListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;
                :morphe_ayoomi_skip_grant
                return-void
                """.trimIndent()
            )
            logger.info("Ayoomi Instant Rewards: patched ShowRewardVideoAd to instantly grant reward")
            patched++
        }

        // 4. Hook InterstitialIsReady to return false (silently block popups)
        val intersReadyMethod = mutableClass.methods.firstOrNull {
            it.name == "InterstitialIsReady" && it.returnType == "Z" && it.parameterTypes == listOf("Ljava/lang/String;")
        }
        if (intersReadyMethod != null && intersReadyMethod.implementation != null) {
            val target = mutableClass.methods.first { it == intersReadyMethod }
            target.addInstructions(
                0,
                """
                const/4 v0, 0x0
                return v0
                """.trimIndent()
            )
            logger.info("Ayoomi Instant Rewards: patched InterstitialIsReady(String) to return false")
            patched++
        }

        if (patched > 0) {
            logger.info("Ayoomi Instant Rewards: successfully applied $patched patch(es)")
        } else {
            logger.warning("Ayoomi Instant Rewards: failed to match any target methods")
        }
    }
}
