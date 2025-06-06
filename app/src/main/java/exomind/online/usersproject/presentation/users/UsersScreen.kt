package exomind.online.usersproject.presentation.users

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import exomind.online.usersproject.presentation.users.models.UIEffect
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.presentation.users.models.UIState
import exomind.online.usersproject.presentation.users.ui.ErrorView
import exomind.online.usersproject.presentation.users.ui.LoadingView
import exomind.online.usersproject.presentation.users.ui.SuccessView

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
        is UIState.Error -> ErrorView(
            message = state.message,
            onRetry = {
                onEvent(UIEvent.Retry)
            }
        )

        is UIState.Success -> SuccessView(
            users = state.items,
            addUser = { addUser ->
                onEvent(addUser)
            },
            deleteUser = { id ->
                onEvent(UIEvent.DeleteUser(id))
            }
        )
    }
}
