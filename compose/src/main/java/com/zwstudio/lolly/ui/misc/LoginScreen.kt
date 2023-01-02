package com.zwstudio.lolly.ui.misc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(vm: LoginViewModel = getViewModel()) {
    val context = LocalContext.current
    var showAlert by remember { mutableStateOf(false) }
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
            label = { Text("USER") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = vm.password.observeAsState().value!!,
            onValueChange = { v -> vm.password.value = v },
            label = { Text("PASSWORD") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                vm.viewModelScope.launch {
                    vm.login(context)
//                    if (!GlobalUserViewModel.isLoggedIn)
//                        showAlert = true
                }
            }
        ) {
            Text("LOGIN")
        }
//        }
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(text = "Login") },
                text = { Text(text = "Wrong username or password!") },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("This is the Confirm Button")
                    }
                },
            )
        }
    }
}
