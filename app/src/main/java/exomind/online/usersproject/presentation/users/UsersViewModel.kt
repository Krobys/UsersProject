package exomind.online.usersproject.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exomind.online.usersproject.R
import exomind.online.usersproject.domain.AddUserUseCase
import exomind.online.usersproject.domain.DeleteUserUseCase
import exomind.online.usersproject.domain.GetUsersUseCase
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.presentation.users.models.UIEffect
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.presentation.users.models.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<UIEffect>()
    val effects = _effects.asSharedFlow()

    init {
        getUsers()
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.AddUser -> {
                addUser(
                    name = event.name,
                    email = event.email,
                    gender = event.gender
                )
            }

            is UIEvent.DeleteUser -> {
                deleteUser(event.userId)
            }

            is UIEvent.Retry -> {
                getUsers()
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            getUsersUseCase.invoke()
                .catch { error ->
                    _state.value = UIState.Error(error.message ?: "Something went wrong")
                }
                .collect { users ->
                    _state.value = UIState.Success(users)
                }
        }
    }

    private fun addUser(name: String, email: String, gender: String) {
        viewModelScope.launch {
            runCatching {
                val addUser = AddUser(
                    name = name,
                    email = email,
                    gender = gender
                )
                addUserUseCase(addUser)
            }.onSuccess {
                getUsers()
                _effects.emit(UIEffect.ShowToast(R.string.user_added_message))
            }.onFailure {
                _effects.emit(UIEffect.ShowToast(R.string.user_error_message))
            }
        }
    }

    private fun deleteUser(userId: Int) {
        viewModelScope.launch {
            runCatching {
                deleteUserUseCase(userId)
            }.onSuccess {
                getUsers()
                _effects.emit(UIEffect.ShowToast(R.string.user_deleted_message))
            }.onFailure {
                _effects.emit(UIEffect.ShowToast(R.string.delete_user_error))
            }
        }
    }
}
