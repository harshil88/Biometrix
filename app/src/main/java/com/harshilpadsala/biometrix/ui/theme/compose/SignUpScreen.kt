package com.harshilpadsala.biometrix.ui.theme.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen() {

    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }


    Scaffold(topBar = {
        BiometrixAppBar()
    }) { paddingValues ->
        Box(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            Column {
                Text(text = userName.value)
                Text(text = password.value)
                SignUpBody(
                    userName = userName, password = password
                )
            }
        }
    }
}

fun generateRandomToken(): Int = Random.nextInt(10, 1000)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiometrixAppBar() {
    CenterAlignedTopAppBar(title = { Text(text = "Sign Up") })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpBody(userName: MutableState<String>, password: MutableState<String>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            label = { Text(text = "Enter your username") },
            value = userName.value,
            onValueChange = { value -> userName.value = value },
        )

        TextField(
            label = { Text(text = "Enter your password") },
            modifier = Modifier.padding(top = 24.dp),
            value = password.value,
            onValueChange = { value -> password.value = value },
        )

        ElevatedButton(modifier = Modifier.padding(top = 24.dp), onClick = {}) {
            Text(text = "SUBMIT")

            Log.i(
                "SignUpScreen",
                "The JWT Token received from internet is ${generateRandomToken()}"
            )

        }

    }
}