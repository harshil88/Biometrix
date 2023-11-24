package com.harshilpadsala.biometrix.ui.theme.compose

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harshilpadsala.biometrix.ui.theme.CryptographyManager

const val SHARED_PREFS = "SHARED_PREFS"
const val IS_SHARED_PREFS_ENABLED = "IS_SHARED_PREFS_ENABLED"





@Composable
fun BiometrixAuthEnableScreen(onClick : () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedButton(onClick = { }) {
            Text(text = "BIOMETRIC VERIFICATION ENABLED" )
        }
    }
}