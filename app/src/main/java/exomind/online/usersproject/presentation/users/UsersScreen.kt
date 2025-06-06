package exomind.online.usersproject.presentation.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import exomind.online.usersproject.domain.models.User
import exomind.online.usersproject.presentation.users.models.UIState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import exomind.online.usersproject.R

@Composable
fun UsersScreen(
    viewModel: UsersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    UsersUI(state)
}

@Composable
fun UsersUI(
    state: UIState,
) {
    when (state) {
        is UIState.Loading -> LoadingView()
        is UIState.Error -> ErrorView(state.message)
        is UIState.Success -> SuccessView(state.items)
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
private fun SuccessView(users: List<User>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
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
    Column(modifier = Modifier.fillMaxWidth()) {
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
