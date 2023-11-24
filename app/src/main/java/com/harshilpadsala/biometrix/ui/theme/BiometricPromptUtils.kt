package com.harshilpadsala.biometrix.ui.theme

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricPromptUtils {
    private const val TAG = "BiometricPromptUtils"
    @RequiresApi(Build.VERSION_CODES.P)
    fun createBiometricPrompt(
        activity: FragmentActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errCode, errString)
                    Log.d(TAG, "errCode is $errCode and errString is: $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d(TAG, "User biometric rejected." )
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d(TAG, "Authentication was successful")
                    processSuccess(result)
                }
            }
        } else {
            TODO("VERSION.SDK_INT < P")
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(activity : FragmentActivity): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle("TITLE")
            setSubtitle("SUBTITLE")
            setDescription("DESCRIPTION")
            setConfirmationRequired(false)
            setNegativeButtonText("PASSWORD")
        }.build()
}