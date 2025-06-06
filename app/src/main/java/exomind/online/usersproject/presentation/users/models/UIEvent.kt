package exomind.online.usersproject.presentation.users.models

sealed interface UIEvent {
    data class AddUser(val name: String, val email: String, val gender: String) : UIEvent
    data class DeleteUser(val userId: Int): UIEvent
    data object Retry: UIEvent
}