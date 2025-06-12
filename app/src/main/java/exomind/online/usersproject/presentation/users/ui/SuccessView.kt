package exomind.online.usersproject.presentation.users.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import exomind.online.usersproject.R
import exomind.online.usersproject.domain.models.User
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.ui.theme.UsersProjectTheme

@Composable
internal fun SuccessView(
    users: List<User>,
    addUser: (UIEvent.AddUser) -> Unit,
    deleteUser: ((id: Int) -> Unit),
) {
    var addUserDialog by remember { mutableStateOf(false) }

    AddUserDialog(
        visible = addUserDialog,
        onDismiss = { addUserDialog = false },
        onAdd = { name, email, gender ->
            addUser(UIEvent.AddUser(name = name, email = email, gender = gender))
        }
    )

    var removeDialogVisible by remember { mutableStateOf(false) }

    var userIdToRemove by remember { mutableStateOf<Int?>(null) }

    RemoveUserDialog(
        visible = removeDialogVisible,
        onDismiss = { removeDialogVisible = false; userIdToRemove = null },
        onConfirm = {
            userIdToRemove?.let { deleteUser(it) }
        }
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(onClick = { addUserDialog = true }) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.add_user)
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (users.isEmpty()) {
                    item {
                        EmptyPlaceholder()
                    }
                }
                items(users) { user ->
                    UserItem(
                        user = user,
                        onLongClick = {
                            userIdToRemove = user.id
                            removeDialogVisible = true
                        }
                    )
                }
            }
        }
    )
}


@Composable
private fun EmptyPlaceholder() {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.empty_placeholder_users)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UserItem(
    user: User,
    onLongClick: (() -> Unit),
) {
    Card(
        modifier = Modifier.combinedClickable(
            onClick = {},
            onLongClick = onLongClick
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = user.name
            )
            Text(
                text = user.email
            )
            Text(
                text = user.gender
            )
        }
    }
}

@Preview
@Composable
private fun SuccessViewPreview() {
    UsersProjectTheme {
        SuccessView(
            users = List(100) { n ->
                User(
                    id = 1,
                    name = "user $n",
                    email = "email$n@test.com",
                    gender = "male"
                )
            },
            addUser = { },
            deleteUser = { }
        )
    }
}
