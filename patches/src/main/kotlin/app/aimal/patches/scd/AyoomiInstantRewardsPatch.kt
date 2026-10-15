package app.aimal.patches.scd

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
<<<<<<< HEAD
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

=======
import app.morphe.patcher.patch.bytecodePatch
import java.util.logging.Logger

>>>>>>> a789eb9f9d44d27c2243a75a5dae4a7be9b1c224
@Suppress("unused")
val ayoomiInstantRewardsPatch = bytecodePatch(
    name = "Ayoomi Instant Rewards",
    description = "Instantly grant ad rewards for games using Ayoomi SDK (Serpent Chain Defense, etc.) without watching ads or triggering AppLovin MAX 25-second timeouts.",
    default = true,
) {
<<<<<<< HEAD
    compatibleWith(SERPENT_CHAIN_DEFENSE)
=======
    category("Featured")

    compatiblePackages(
        "com.and.aym.scd" to setOf(),
    )
>>>>>>> a789eb9f9d44d27c2243a75a5dae4a7be9b1c224

    execute {
        val logger = Logger.getLogger(this::class.java.name)
        val targetClass = "Lcom/ayoomi/sdk/AyoomiApplication;"
        val mutableClass = mutableClassDefByOrNull(targetClass)

        if (mutableClass == null) {
            logger.info("Ayoomi Instant Rewards: target class $targetClass not found. Skipping.")
            return@execute
        }

        var patched = 0

        // 1. Force OriRewardIsReady()Z -> always true
        val readyMethod = mutableClass.methods.firstOrNull {
            it.name == "OriRewardIsReady" && it.returnType == "Z" && it.parameterTypes.isEmpty()
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
            logger.info("Ayoomi Instant Rewards: patched OriRewardIsReady() to return true")
            patched++
        }

        // 2. Hook ShowRewardVideoAd to immediately grant reward via callback
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
                if-eqz p2, :morphe_ayoomi_no_listener
                const-wide/high16 v0, 0x3ff0000000000000L
                invoke-interface {p2, v0, v1}, Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;->onReward(D)V
                const/4 v0, 0x0
                sput-object v0, Lcom/ayoomi/sdk/AyoomiApplication;->mRewardListener:Lcom/ayoomi/sdk/AyoomiApplication${'$'}OnRewardListener;
                return-void
                :morphe_ayoomi_no_listener
                """.trimIndent()
            )
            logger.info("Ayoomi Instant Rewards: patched ShowRewardVideoAd to instantly grant reward")
            patched++
        }

        if (patched > 0) {
            logger.info("Ayoomi Instant Rewards: successfully applied $patched patch(es)")
        } else {
            logger.warning("Ayoomi Instant Rewards: failed to match any target methods")
        }
    }
}
