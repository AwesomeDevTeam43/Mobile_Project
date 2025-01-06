package com.example.mobile_project.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project.ui.theme.Blue01
import com.example.mobile_project.ui.theme.Mobile_ProjectTheme
import com.example.mobile_project.ui.theme.Orange01

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit = {},
    navController: NavController = rememberNavController(),
) {
    val viewModel: RegisterViewModel = viewModel()
    val state = viewModel.state.value
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(Orange01.value)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            text = "Social Shop",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(100.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))

                // Username TextField
                TextField(
                    value = state.username,
                    onValueChange = { viewModel.onUsernameChange(it) },
                    placeholder = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChange(it) },
                    placeholder = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { viewModel.onRegisterClick { onRegisterSuccess() } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(Orange01.value),
                        contentColor = Color.White
                    )
                ) {
                    Text("Register", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Link
                TextButton(
                    onClick = { navController.navigate("Login") }
                ) {
                    Text(
                        "Back to Login",
                        fontSize = 14.sp,
                        color = Color(Blue01.value),
                        textDecoration = TextDecoration.Underline
                    )
                }

                // Loader and Errors
                if (state.error != null) {
                    Text(
                        text = state.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (state.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterViewPreview() {
    Mobile_ProjectTheme {
        RegisterView()
    }
}