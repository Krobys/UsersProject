package exomind.online.usersproject.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exomind.online.usersproject.R
import exomind.online.usersproject.domain.AddUserUseCase
import exomind.online.usersproject.domain.GetUsersUseCase
import exomind.online.usersproject.domain.models.AddUser
import exomind.online.usersproject.presentation.users.models.UIEffect
import exomind.online.usersproject.presentation.users.models.UIEvent
import exomind.online.usersproject.presentation.users.models.UIState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<UIEffect>()
    val effects = _effects.asSharedFlow()

    init {
        getUsers()
    }

    fun onEvent(event: UIEvent) {
        when(event) {
            is UIEvent.AddUser -> {
                addUser(
                    name = event.name,
                    email = event.email,
                    gender = event.gender
                )
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            runCatching {
                getUsersUseCase()
            }.onSuccess { users ->
                _state.value = UIState.Success(users)
            }.onFailure { error ->
                _state.value = UIState.Error(error.message ?: "Something went wrong")
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
}
