package com.zwstudio.lolly.views.misc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    val coroutineScope = rememberCoroutineScope()
    val vm = LocalUserState.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (vm.isBusy) {
            CircularProgressIndicator()
        } else {
            Text("Login Screen", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = vm.user,
                onValueChange = { v -> vm.user = v },
                label = {
                    Text("USER")
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = vm.password,
                onValueChange = { v -> vm.password = v },
                label = {
                    Text("PASSWORD")
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
//            LoginButton(onClick = {
//                coroutineScope.launch {
//                    vm.signIn(email, password)
//                }
//            })
        }


    }
}
