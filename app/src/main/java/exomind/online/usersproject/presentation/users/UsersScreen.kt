package exomind.online.usersproject.presentation.users

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import exomind.online.usersproject.R
import exomind.online.usersproject.domain.models.User
import exomind.online.usersproject.presentation.users.models.UIEffect
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.presentation.users.models.UIState

@Composable
fun UsersScreen(
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is UIEffect.ShowToast -> {
                    Toast.makeText(context, effect.stringResource, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    UsersUI(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun UsersUI(
    state: UIState,
    onEvent: ((UIEvent) -> Unit),
) {
    when (state) {
        is UIState.Loading -> LoadingView()
        is UIState.Error -> ErrorView(state.message)
        is UIState.Success -> SuccessView(
            users = state.items,
            addUser = onEvent
        )
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = message
        )
    }
}

@Composable
fun AddUserDialog(
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
        title = { Text(text = "Add New User") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
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
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun SuccessView(
    users: List<User>,
    addUser: (UIEvent.AddUser) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    AddUserDialog(
        visible = showDialog,
        onDismiss = { showDialog = false },
        onAdd = { name, email, gender ->
            addUser(UIEvent.AddUser(name = name, email = email, gender = gender))
        }
    )

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Add user"
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
                    UserItem(user)
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

@Composable
private fun UserItem(user: User) {
    Card {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
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
