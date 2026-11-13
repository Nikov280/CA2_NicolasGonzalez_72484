package com.example.CA2_NicolasGonzalez_72484

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// STATE HOISTING: Variables are declared higher up (e.g., in Activity/ViewModel)
// and passed into the composable to make sharing easier.
@Composable
fun MainScreenApp() {
    // This counter will reset on configuration changes (like screen rotation)
    var normalCounter by remember { mutableStateOf(0) }

    // This counter survives configuration changes
    var configSurvivingCounter by rememberSaveable { mutableStateOf(0) }

    // Passing variables to the stateless composable
    CounterComponent(
        counterOne = normalCounter,
        counterTwo = configSurvivingCounter,
        onIncrementCounters = {
            normalCounter++
            configSurvivingCounter++
        }
    )
}

// Stateless Composable receiving the variables as parameters
@Composable
fun CounterComponent(
    counterOne: Int,
    counterTwo: Int,
    onIncrementCounters: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Standard Counter: $counterOne")
        Text(text = "Surviving Counter: $counterTwo")

        Button(onClick = onIncrementCounters, modifier = Modifier.padding(top = 8.dp)) {
            Text("Increment Both Counters")
        }

        Button(onClick = { performBaaSLogin() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Login to BaaS")
        }
    }
}

// BaaS implementation using Firebase as platfomr
fun performBaaSLogin() {
    try {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("BaaS Logcat: Successfully logged in.")
                } else {
                    println("BaaS Logcat: Login failed - ${task.exception?.message}")
                }
            }
    } catch (e: Exception) {
        println("BaaS Error: Firebase is not initialized or configured. Details: ${e.message}")
    }
}

// Overrides the message property to emphasize the custom functionality
open class WordTooLongException(message: String) : Exception(message) {
    override val message: String?
        get() = "CUSTOM ERROR: ${super.message}"
}

fun runExceptionTests() {
    // EXCEPTIONS 1: Precondition Functions
    val isLightOn = false
    val userInputNumber = -5

    // 1. Using check() on a Boolean variable
    try {
        check(isLightOn) { "System Fault: The light is not turned on." }
    } catch (e: IllegalStateException) {
        // Exception is caught and handled in this specific catch block
        println("Exception 1 caught (check): ${e.message}")
    }

    // 2. Using require() for a variable (ensuring it's not negative)
    try {
        require(userInputNumber >= 0) { "Input Error: Number cannot be negative." }
    } catch (e: IllegalArgumentException) {
        // Exception is caught and handled in this specific catch block
        println("Exception 1 caught (require): ${e.message}")
    }

    // EXCEPTIONS 2: Provoking the Custom Exception
    val testWord = "Supercalifragilistic"

    try {
        // Check if length is greater than 10 characters
        if (testWord.length > 10) {
            throw WordTooLongException("The word '$testWord' has too many characters.")
        }
    } catch (e: WordTooLongException) {
        println("Exception 2 caught (Custom): ${e.message}")
    }
}