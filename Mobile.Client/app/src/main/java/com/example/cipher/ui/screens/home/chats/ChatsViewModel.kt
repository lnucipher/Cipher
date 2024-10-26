package com.example.cipher.ui.screens.home.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.repository.contact.GetContactList
import com.example.cipher.domain.repository.user.LocalUserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    getContactList: GetContactList,
    private val userManager: LocalUserManager
): ViewModel() {

    val contactPagingDataFlow: Flow<PagingData<User>> = getContactList()
        .cachedIn(viewModelScope)

    private val _localUser: MutableStateFlow<LocalUser> = MutableStateFlow(
        LocalUser(
            id = "",
            username = "",
            name = "",
            birthDate = "",
            bio = "",
            avatarUrl = ""
        )
    )
    val localUser = _localUser.asStateFlow()

    init {
        viewModelScope.launch {
            _localUser.update { userManager.getUser() }
            _localUser.update { LocalUser(
                username = "max123",
                name = "Max",
                birthDate = "1990-05-15",
                bio = "Loves coding and hiking.",
                avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
                id = "user1"
            ) }
        }
    }

}