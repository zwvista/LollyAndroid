package com.zwstudio.lolly.views.misc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(vm : LoginViewModel = koinViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        if (vm.isBusy) {
//            CircularProgressIndicator()
//        } else {
        Text("Login Screen", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = vm.username.observeAsState().value!!,
            onValueChange = { v -> vm.username.value = v },
            label = {
                Text("USER")
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = vm.password.observeAsState().value!!,
            onValueChange = { v -> vm.password.value = v },
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
        Button(
            onClick = {
                GlobalUserViewModel.save(context, "kk")
            }
        ) {
            Text("LOGIN")
        }
//        }
    }
}
