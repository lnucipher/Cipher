package com.example.cipher.ui.screens.home.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.navigation.ChatNavScreens
import com.example.cipher.ui.common.theme.CipherTheme
import com.example.cipher.ui.screens.home.chat.composable.EmptyChatState
import com.example.cipher.ui.screens.home.chats.composable.ChatsItem
import com.example.cipher.ui.screens.home.chats.composable.SearchField
import java.sql.Timestamp

@Composable
fun ChatsScreen(
    navController: NavHostController,
    viewModel: ChatsViewModel = hiltViewModel()
) {

    val users = listOf(
        User(
            username = "max123",
            name = "Max",
            birthDate = "1990-05-15",
            bio = "Loves coding and hiking.",
            status = Status.ONLINE,
            lastSeen = Timestamp(1234567890),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "1"
        ),
        User(
            username = "julia89",
            name = "Julia",
            birthDate = "1989-11-23",
            bio = "Music enthusiast and photographer.",
            status = Status.OFFLINE,
            lastSeen = Timestamp(1234567800),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "2"
        ),
        User(
            username = "john_doe",
            name = "John",
            birthDate = "1985-03-10",
            bio = "Coffee lover and tech geek.",
            status = Status.ONLINE,
            lastSeen = Timestamp(1234567810),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "3"
        ),
        User(
            username = "anna_smith",
            name = "Anna",
            birthDate = "1992-07-30",
            bio = "Travel blogger and nature explorer.",
            status = Status.ONLINE,
            lastSeen = Timestamp(1234567820),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "4"
        ),
        User(
            username = "sarah_lee",
            name = "Sarah",
            birthDate = "1995-12-14",
            bio = "Digital artist and gamer.",
            status = Status.OFFLINE,
            lastSeen = Timestamp(1234567830),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "5"
        ),
        User(
            username = "david_w",
            name = "David",
            birthDate = "1988-09-22",
            bio = "Fitness trainer and chef.",
            status = Status.OFFLINE,
            lastSeen = Timestamp(1234567840),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "6"
        ),
        User(
            username = "david_w",
            name = "David",
            birthDate = "1988-09-22",
            bio = "Fitness trainer and chef.",
            status = Status.OFFLINE,
            lastSeen = Timestamp(1234567840),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "7"
        )
    )

    val localUser = LocalUser(
        username = "max123",
        name = "Max",
        birthDate = "1990-05-15",
        bio = "Loves coding and hiking.",
        avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
        id = "user1"
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchField(
            modifier = Modifier
        ) {

        }

        Spacer(modifier = Modifier.height(16.dp))

        if (users.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(users, key = { user -> user.id }) { user ->
                    ChatsItem(
                        user = user,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    ChatNavScreens.PersonalChatScreen(
                                        user = user,
                                        localUser = localUser
                                    )
                                )
                            }
                    )
                }
            }
        }  else {
            Box (
                modifier = Modifier.fillMaxSize(0.85f)
            ) {
                EmptyChatState()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ChatsScree() {
    CipherTheme {
        ChatsScreen(navController = rememberNavController())
    }
}
