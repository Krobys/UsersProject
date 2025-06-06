package exomind.online.usersproject.presentation.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exomind.online.usersproject.domain.GetUsersUseCase
import exomind.online.usersproject.presentation.users.models.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state = _state.asStateFlow()

    init {
        getUsers()
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
}
