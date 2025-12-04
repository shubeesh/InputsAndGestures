package com.example.inputsassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * Single-file starter for the "Inputs Only (No Gestures)" assignment.
 *
 * Contents:
 *  - MainActivity entry point
 *  - ContactFormScreen composable (UI)
 *  - Contact data class
 *  - Pure validation functions (TODOs)
 *
 * Students implement the TODOs to:
 *  - Add validation rules
 *  - Wire IME Next/Done and focus movement
 *  - Revalidate fields while correcting errors
 *  - Validate & submit (show Snackbar/Dialog), and Clear
 */

// -----------------------------
// Entry point
// -----------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    ContactFormScreen()
                }
            }
        }
    }
}

// -----------------------------
// UI
// -----------------------------
@Composable
fun ContactFormScreen(modifier: Modifier = Modifier) {
    // region --- State ---
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var agreed by rememberSaveable { mutableStateOf(false) }

    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }
    var termsError by rememberSaveable { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val focus = LocalFocusManager.current

    // Focus requesters for IME "Next" chain
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val phoneFocus = remember { FocusRequester() }
    // endregion --- State ---

    // region --- Helpers ---
    fun revalidateIfShowingError() {


        // TODO(Student): If a field currently has an error, re-run its validator and clear once valid.
        // For example:
        if (nameError != null) nameError = validateName(name)
        if (emailError != null) emailError = validateEmail(email)
        if (phoneError != null) phoneError = validatePhone(phone)
        if (termsError != null) termsError = validateTerms(agreed)
    }

    fun validateAll(): Boolean {
        // TODO(Student): Run all validators and set error states.
        // Example:
        nameError = validateName(name)
        emailError = validateEmail(email)
        phoneError = validatePhone(phone)
        termsError = validateTerms(agreed)
        // Return true only if all are valid (all errors == null).
        return listOf(nameError, emailError, phoneError, termsError).all { it == null }
    }

    fun clearAll() {
        name = ""; email = ""; phone = ""; agreed = false
        nameError = null; emailError = null; phoneError = null; termsError = null
    }

    fun submit() {
        // TODO(Student):
        if (!validateAll()) {

            nameFocus.requestFocus()
            emailFocus.requestFocus()
            phoneFocus.requestFocus()

        } else {
            focus.clearFocus();
        }



    }
    // endregion --- Helpers ---

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Name
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    // TODO(Student): If nameError is showing, revalidate just this field now.
                    nameError = validateName(name)
                    revalidateIfShowingError()
                },
                label = { Text("Name") },
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) Text(
                        nameError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        emailFocus.requestFocus()
                        // TODO(Student): Move focus to Email field using emailFocus.requestFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(nameFocus)
                    .semantics { contentDescription = "Name field" }
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    // TODO(Student): If emailError is showing, revalidate this field only.
                    emailError = validateEmail(email)
                    revalidateIfShowingError()
                },
                label = { Text("Email") },
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) Text(
                        emailError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        phoneFocus.requestFocus()
                        // TODO(Student): Move focus to Phone field using phoneFocus.requestFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocus)
                    .semantics { contentDescription = "Email field" }
            )

            // Phone
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    // TODO(Student): If phoneError is showing, revalidate this field only.
                    phoneError = validatePhone(phone)
                    revalidateIfShowingError()
                },
                label = { Text("Phone") },
                isError = phoneError != null,
                supportingText = {
                    if (phoneError != null) Text(
                        phoneError!!,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        submit()
                        // TODO(Student): Attempt submit() when user presses Done
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(phoneFocus)
                    .semantics { contentDescription = "Phone field" }
            )

            // Agree to Terms
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.semantics { contentDescription = "Agree to terms" }
            ) {
                Checkbox(
                    checked = agreed,
                    onCheckedChange = {
                        agreed = it
                        // TODO(Student): If termsError is showing, revalidate the terms.
                        termsError = validateTerms(agreed)
                        revalidateIfShowingError()
                    }
                )
                Text("I agree to the terms and conditions")
            }
            if (termsError != null) {
                Text(
                    text = termsError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        submit()
                        // TODO(Student): Call submit()
                    },
                    modifier = Modifier.semantics { contentDescription = "Submit" }
                ) { Text("Submit") }

                OutlinedButton(
                    onClick = {
                        clearAll()

                        // TODO(Student): Optionally show a light confirmation (e.g., Snackbar("Cleared"))
                    },
                    modifier = Modifier.semantics { contentDescription = "Clear" }
                ) { Text("Clear") }
            }

            // Optional: show a non-interactive summary (no gestures) after submit.
            // TODO(Student): Display a simple Text/Card with the last submitted contact, if you like.
        }
    }
}

// -----------------------------
// Data
// -----------------------------
data class Contact(
    val name: String,
    val email: String,
    val phone: String,
    val agreed: Boolean
)

// -----------------------------
// Validation (pure functions)
// -----------------------------

/**
 * Validators return null when valid, or a non-empty error message when invalid.
 * NOTE: These stubs currently return null so the app compiles; replace with real checks.
 */

fun validateName(name: String): String? {
    val trimmed = name.trim()
    return if (trimmed.length >= 2) null else "Name must be at least 2 characters"
}

fun validateEmail(email: String): String? {
    val at = email.indexOf('@')
    if (at <= 0) return "Email must contain '@' and a '.' after it"
    val dotAfter = email.indexOf('.', at + 1)
    return if (dotAfter > at) null else "Email must contain '@' and a '.' after it"
}

fun validatePhone(phone: String): String? {
    val digits = phone.filter(Char::isDigit)
    return if (digits.length in 10..15) null else "Phone number must contain 10 to 15 digits"
}

fun validateTerms(agreed: Boolean): String? {
    return if (agreed) null else "You must agree to the terms"
}