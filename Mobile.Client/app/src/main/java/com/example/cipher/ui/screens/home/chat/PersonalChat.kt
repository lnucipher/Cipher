package com.example.cipher.ui.screens.home.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cipher.R
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.domain.models.message.Message
import com.example.cipher.domain.models.user.LocalUser
import com.example.cipher.ui.common.theme.CipherTheme
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.screens.home.chat.composable.ChatBox
import com.example.cipher.ui.screens.home.chat.composable.MessageItem
import com.example.cipher.ui.screens.home.chat.composable.PersonalChatTopAppBar
import java.sql.Timestamp

@Composable
fun PersonalChat(
    navController: NavHostController,
    localUser: LocalUser,
    chatCoUser: User
) {
    Scaffold (
        modifier = Modifier.imePadding(),
        topBar = {
            PersonalChatTopAppBar(
                navController = navController,
                chatCoUser = chatCoUser
            )
        },
        bottomBar = {
            ChatBox()
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colors.secondaryBackground)
                .padding(innerPadding)
        ) {
            val currentTime = System.currentTimeMillis()
            val messages: List<Message> =
                listOf(
                    Message("1", "user1", "user2", "Hello", Timestamp(currentTime - 100000)),
                    Message("2", "user2", "user1", "I'm good, thanks! And you?", Timestamp(currentTime - 90000)),
                    Message("3", "user1", "user2", "Just working on some stuff.", Timestamp(currentTime - 80000)),
                    Message("4", "user2", "user1", "Sounds interesting! What kind of stuff?", Timestamp(currentTime - 70000)),
                    Message("5", "user1", "user2", "Just some coding projects.", Timestamp(currentTime - 60000)),
                    Message("6", "user2", "user1", "Nice! I love coding too.", Timestamp(currentTime - 50000)),
                    Message("7", "user1", "user2", "What languages do you work with?", Timestamp(currentTime - 40000)),
                    Message("8", "user2", "user1", "Mainly Kotlin and Java.", Timestamp(currentTime - 30000)),
                    Message("9", "user1", "user2", "Awesome! I'm focusing on Android development.", Timestamp(currentTime - 20000)),
                    Message("10", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
                    Message("11", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
                    Message("12", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000)),
                    Message("13", "user2", "user1", "That's great! Let's work on a project together sometime.That's great! Let's work on a project together sometime", Timestamp(currentTime - 10000))
                )
//                emptyList<Message>()

            Spacer(modifier = Modifier.height(12.dp))

            if (messages.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    reverseLayout = true
                ) {
                    items(messages.reversed(), key = { message -> message.id }) { message ->
                        Spacer(modifier = Modifier.height(16.dp))

                        val isLocalUserMessage = message.senderId == localUser.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = if (isLocalUserMessage) Arrangement.End else Arrangement.Start
                        ) {
                            if (isLocalUserMessage) {
                                Spacer(modifier = Modifier.weight(1.0f))
                            }

                            MessageItem(
                                modifier = Modifier
                                    .weight(5f, false),
                                message = message,
                                isLocalUser = isLocalUserMessage
                            )

                            if (!isLocalUserMessage) {
                                Spacer(modifier = Modifier.weight(1.0f))
                            }
                        }
                    }
                }
            } else {
                EmptyChatState()
            }
        }
    }
}

@Composable
fun EmptyChatState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(0.75f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.no_messages_yet_dark_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.759f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "There is nothing here yet..",
                style = typography.toolbar,
                color = colors.primaryText
            )
        }
    }
}

@Preview
@Composable
fun PersonalChatPrew() {
    CipherTheme (darkTheme = true) {
        PersonalChat(chatCoUser =  User(
            username = "max123",
            name = "Max",
            birthDate = "1990-05-15",
            bio = "Loves coding and hiking.",
            status = Status.ONLINE,
            lastSeen = Timestamp(1234567890),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "user2"
        ), navController = rememberNavController(), localUser = LocalUser(
            username = "max123",
            name = "Max",
            birthDate = "1990-05-15",
            bio = "Loves coding and hiking.",
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "user1"
        ))
    }
}