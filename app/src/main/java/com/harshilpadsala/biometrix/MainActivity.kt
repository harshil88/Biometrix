package com.harshilpadsala.biometrix

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.harshilpadsala.biometrix.ui.theme.BiometricPromptUtils
import com.harshilpadsala.biometrix.ui.theme.BiometrixTheme
import com.harshilpadsala.biometrix.ui.theme.CryptographyManager
import com.harshilpadsala.biometrix.ui.theme.compose.BiometrixAuthEnableScreen
import com.harshilpadsala.biometrix.ui.theme.compose.Dummy
import com.harshilpadsala.biometrix.ui.theme.compose.SignUpScreen
import com.harshilpadsala.biometrix.ui.theme.compose.SplashScreen

const val SPLASH = "splash"
const val SIGNUP = "signUp"
const val HOMEPAGE = "homePage"
const val BIOMETRIX = "biometrix"


const val SHARED_PREFS_FILENAME = "biometric_prefs"
const val CIPHERTEXT_WRAPPER = "ciphertext_wrapper"

const val KEY_NAME = "khufiya"


class MainActivity : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiometrixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraphSetup(
                        context = applicationContext,
                        fragmentActivity = this
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraphSetup(context: Context,  fragmentActivity: FragmentActivity){
    val biometrixSetupImpl = BiometrixSetupImpl(
        context = context,
        fragmentActivity = fragmentActivity
    )
    val navController = rememberNavController()
    val currentBackStack = navController.currentBackStackEntryAsState()

    NavHost(navController = navController , startDestination = "SPLASH"){
        composable( route = SIGNUP ){ SignUpScreen()}
        composable( route = SPLASH ){ SplashScreen()}
        composable( route = BIOMETRIX ){ BiometrixAuthEnableScreen{
            biometrixSetupImpl.showBiometricForEncryption()
        } }

    }
}


class BiometrixSetupImpl(private val context: Context, private val fragmentActivity: FragmentActivity) {

    private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val cipherTextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            context,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER)


     fun checkIfBiometrixIsEnabled() =
        BiometricManager.from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG
                    or BiometricManager.Authenticators.BIOMETRIC_WEAK
                    or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )

    @RequiresApi(Build.VERSION_CODES.P)
     fun showBiometricForEncryption(){
        if(checkIfBiometrixIsEnabled() == BiometricManager.BIOMETRIC_SUCCESS){
            val cipher = cryptographyManager.getInitializedCipherForEncryption(KEY_NAME)
            val biometricPrompt = BiometricPromptUtils.createBiometricPrompt(
                 fragmentActivity,
                processSuccess = {
                    encryptAndStoreServerToken(it)
                }
            )
        }
    }

     fun encryptAndStoreServerToken(authenticationResult: BiometricPrompt.AuthenticationResult){
        authenticationResult.cryptoObject?.cipher?.apply {
            Dummy.FAKE_TOKEN.let {
                    token ->
                val encryptedServerTokenWrapper = cryptographyManager.encryptData(token, this)
                cryptographyManager.persistCiphertextWrapperToSharedPrefs(
                    encryptedServerTokenWrapper,
                    context,
                    SHARED_PREFS_FILENAME,
                    Context.MODE_PRIVATE,
                    CIPHERTEXT_WRAPPER
                )
            }
        }
    }

     fun showBiometricAuthForDecryption(){
        cipherTextWrapper?.let {
                textWrapper ->
            val cipher = cryptographyManager.getInitializedCipherForDecryption(KEY_NAME, textWrapper.initializationVector)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricPrompt =
                    BiometricPromptUtils.createBiometricPrompt(
                        fragmentActivity,
                        ::decryptSeverTokenFromStorage
                    )
            }
            val promptInfo = BiometricPromptUtils.createPromptInfo(fragmentActivity)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
     fun decryptSeverTokenFromStorage(authenticationResult: BiometricPrompt.AuthenticationResult){
        cipherTextWrapper?.let {
                textWrapper ->
            authenticationResult?.cryptoObject?.cipher?.let {
                val plainTextToken = cryptographyManager.decryptData(textWrapper.ciphertext , it)
                Dummy.FAKE_TOKEN = plainTextToken
            }
        }
    }
}






























