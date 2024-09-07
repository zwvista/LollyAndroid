package com.zwstudio.lolly.compose.ui.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen() {
    val vm = koinViewModel<LoginViewModel>()
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
            value = vm.username.collectAsState().value,
            onValueChange = { v -> vm.username.value = v },
            label = { Text(text = stringResource(id = R.string.label_username2)) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = vm.password.collectAsState().value,
            onValueChange = { v -> vm.password.value = v },
            label = { Text(text = stringResource(id = R.string.label_password2)) },
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
                    if (!GlobalUserViewModel.isLoggedIn)
                        showAlert = true
                }
            }
        ) {
            Text("LOGIN")
        }
//        }
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text(text = stringResource(id = R.string.login_string)) },
                text = { Text(text = stringResource(id = R.string.login_fail_message)) },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("OK")
                    }
                },
            )
        }
    }
}
