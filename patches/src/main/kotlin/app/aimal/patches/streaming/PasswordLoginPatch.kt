package app.aimal.patches.streaming

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

private const val LOGIN_BRIDGE = "$EXTENSION_STREAMING/LoginBridge;"

/**
 * Bypasses Disney+ OneID's one-time passcode (OTP/Passcode) prompt and
 * forces the password login screen instead, allowing Google Password Manager
 * to auto-fill credentials without waiting for an email verification code.
 */
@Suppress("unused")
val passwordLoginPatch = bytecodePatch(
    name = "Direct password login",
    description = "Bypasses the one-time passcode (OTP) prompt and directly opens the password login screen on Disney+.",
    default = true,
) {
    compatibleWith(DISNEY_PLUS)

    extendWith("extensions/extension.mpe")

    execute {
        val adapterMethod = DisneyCheckResponseAdapterFingerprint.methodOrNull
            ?: throw PatchException(
                "Could not find Disney+ CheckQuery ResponseAdapter. This patch targets " +
                    "Disney+ (com.disney.disneyplus)."
            )

        val instructions = adapterMethod.implementation!!.instructions
        val returnIndex = instructions.indexOfLast { it.opcode == Opcode.RETURN_OBJECT }
        if (returnIndex == -1) {
            throw PatchException("Could not find RETURN_OBJECT in CheckQuery ResponseAdapter")
        }

        val returnInsn = instructions.elementAt(returnIndex) as OneRegisterInstruction
        val reg = returnInsn.registerA

        adapterMethod.addInstructions(
            returnIndex,
            """
                invoke-static { v$reg }, $LOGIN_BRIDGE->sanitizeCheckResult(Ljava/lang/Object;)Ljava/lang/Object;
                move-result-object v$reg
            """.trimIndent()
        )
    }
}
