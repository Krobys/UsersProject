package exomind.online.usersproject.presentation.users.models

import exomind.online.usersproject.domain.models.User

sealed interface UIState {
    data object Loading: UIState
    data class Error(val message: String): UIState
    data class Success(val items: List<User>): UIState
}