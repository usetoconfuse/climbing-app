package com.example.climbing_app.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climbing_app.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest


@Composable
fun LoginScreen(
    navController: NavController,
    auth: FirebaseAuth
) {
    // If a user is logged in, go to all climbs screen
    if (auth.currentUser != null) {
        navController.navigate(
            route = AppScreens.Climbs.name
        ) {
            popUpTo(AppScreens.Login.name) {
                inclusive = true
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .padding(top = 48.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val context = LocalContext.current

            // Login details
            // Firebase authentication requires email and password
            var email by rememberSaveable { mutableStateOf("") }
            var userDisplayName by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            fun authenticateLogin() {
                // Display toast and do not upload if any input field is empty
                if (listOf(email, password).any { x -> x.isEmpty() }) {
                    Toast.makeText(
                        context,
                        "Email and password cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success")
                                navController.navigate(
                                    route = AppScreens.Climbs.name
                                ) {
                                    popUpTo(AppScreens.Login.name) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    context,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }

            fun addUser() {
                // Display toast and do not upload if any input field is empty
                if (listOf(userDisplayName, email, password).any { x -> x.isEmpty() }) {
                    Toast.makeText(
                        context,
                        "Username, email and password cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, store display name in firestore
                                Log.d(TAG, "createUserWithEmail:success")
                                val thisUser = auth.currentUser
                                val profileUpdates = userProfileChangeRequest {
                                    displayName = userDisplayName
                                }
                                thisUser!!.updateProfile(profileUpdates)
                                    .addOnSuccessListener {
                                        navController.navigate(
                                            route = AppScreens.Climbs.name
                                        ) {
                                            popUpTo(AppScreens.Login.name) {
                                                inclusive = true
                                            }
                                        }
                                    }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    context,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                }
            }

            Text(
                text = "Login / Register",
                fontSize = 32.sp
            )

            // Display name TextField
            OutlinedTextField(
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                value = userDisplayName,
                onValueChange = {
                    userDisplayName = it.take(15)
                }
            )

            // Email TextField
            OutlinedTextField(
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                value = email,
                onValueChange = {
                    email = it
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
                    password = it
                }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                // Upload button
                Button(
                    onClick = { authenticateLogin() }
                ) {
                    Text("LOGIN")
                }

                // New user
                Button(
                    onClick = { addUser() }
                ) {
                    Text("REGISTER")
                }
            }
        }
    }
}