package exomind.online.usersproject.presentation.users.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exomind.online.usersproject.R
import exomind.online.usersproject.ui.theme.UsersProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddUserDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onAdd: (name: String, email: String, gender: String) -> Unit,
) {
    if (!visible) return

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("male", "female")

    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isNameValid = name.isNotBlank()
    val isGenderValid = gender.isNotBlank()
    val canConfirm = isNameValid && isEmailValid && isGenderValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_new_user)) },
        text = {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_name)) },
                    singleLine = true,
                    isError = !isNameValid && name.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                if (!isNameValid && name.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.error_name_required),
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.label_email)) },
                    singleLine = true,
                    isError = !isEmailValid && email.isNotBlank(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
                if (!isEmailValid && email.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.error_email_invalid),
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = gender,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.label_gender)) },
                        readOnly = true,
                        isError = !isGenderValid && gender.isNotBlank(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            errorTrailingIconColor = Color.Red
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    gender = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                if (!isGenderValid && gender.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.error_gender_required),
                        color = Color.Red,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = canConfirm,
                onClick = {
                    onAdd(name.trim(), email.trim(), gender)
                    name = ""
                    email = ""
                    gender = ""
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.button_add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun AddUserDialogPreview() {
    UsersProjectTheme {
        AddUserDialog(
            visible = true,
            onDismiss = { },
            onAdd = { _, _, _ -> }
        )
    }
}
