package exomind.online.usersproject.presentation.users.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import exomind.online.usersproject.R
import exomind.online.usersproject.ui.theme.UsersProjectTheme

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.add_new_user)) },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.label_email)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text(stringResource(R.string.label_gender)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAdd(name, email, gender)
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
