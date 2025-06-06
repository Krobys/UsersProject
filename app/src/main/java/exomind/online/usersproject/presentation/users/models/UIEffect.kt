package exomind.online.usersproject.presentation.users.models

sealed interface UIEffect {
    data class ShowToast(val stringResource: Int): UIEffect
}