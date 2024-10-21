package com.example.cipher.ui.screens.home.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.screens.home.chat.composable.PersonalChatTopAppBar
import java.sql.Timestamp

@Composable
fun PersonalChat(
    navController: NavHostController,
    chatCoUser: User
) {
    Scaffold (
        topBar = {
            PersonalChatTopAppBar(
                navController = navController,
                chatCoUser = chatCoUser
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colors.secondaryBackground)
                .padding(innerPadding)
        ) {

        }
    }
}

@Preview
@Composable
fun PersonalChatPrew() {
    CipherTheme {
        PersonalChat(chatCoUser =  User(
            username = "max123",
            name = "Max",
            birthDate = "1990-05-15",
            bio = "Loves coding and hiking.",
            status = Status.ONLINE,
            lastSeen = Timestamp(1234567890),
            avatarUrl = "https://randomwordgenerator.com/img/picture-generator/55e6d0405754a809ea898279c02132761022dfe05a51774073267dd2_640.jpg",
            id = "1"
        ), navController = rememberNavController())
    }
}