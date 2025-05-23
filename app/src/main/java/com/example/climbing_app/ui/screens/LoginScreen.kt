package com.example.climbing_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.climbing_app.AppScreens
import com.example.climbing_app.data.User
import com.example.climbing_app.ui.ClimbViewModel


@Composable
fun LoginScreen(climbViewModel: ClimbViewModel, navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            val context = LocalContext.current

            val userList by climbViewModel.allUsers.observeAsState(initial = emptyList())

            // Login details
            var username by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            fun authenticateLogin() {
                val user = userList.find{user ->
                    user.username == username && user.password == password
                }
                if (user?.userId == null) {
                    // Login failed
                    Toast.makeText(
                        context,
                        "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Log the user in
                    navController.navigate(route = AppScreens.Climbs.name+"/${user.userId}")
                }
            }

            fun addUser() {
                // Display toast and do not upload if any input field is empty
                if (listOf(username, password).any { x -> x.isEmpty() }) {
                    Toast.makeText(
                        context,
                        "Username and password cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    val newUser = User(
                        username = username,
                        password = password
                    )
                    climbViewModel.insertUser(newUser)
                    Toast.makeText(
                        context,
                        "Added user $username",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Username TextField
            OutlinedTextField(
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = username,
                onValueChange = {
                    username = it.take(15)
                }
            )

            // Password TextField
            OutlinedTextField(
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = password,
                onValueChange = {
                    password = it.take(15)
                }
            )

            // Upload button
            Button(
                modifier = Modifier.padding(top = 24.dp),
                onClick = { authenticateLogin() }
            ) {
                Text("LOGIN")
            }

            // New user
            Button(
                modifier = Modifier.padding(top = 24.dp),
                onClick = { addUser() }
            ) {
                Text("ADD USER")
            }
        }
    }
}