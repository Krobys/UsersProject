package exomind.online.usersproject.presentation.users.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import exomind.online.usersproject.R
import exomind.online.usersproject.ui.theme.UsersProjectTheme

@Composable
internal fun RemoveUserDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.remove_user_title)) },
        text = { Text(text = stringResource(R.string.remove_user_message)) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text(text = stringResource(R.string.remove_user_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.remove_user_cancel))
            }
        }
    )
}

@Preview
@Composable
private fun RemoveUserDialogPreview() {
    UsersProjectTheme {
        RemoveUserDialog(
            visible = true,
            onDismiss = { },
            onConfirm = { },
        )
    }
}
