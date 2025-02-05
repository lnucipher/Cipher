package com.example.cipher.ui.screens.home.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.R
import com.example.cipher.domain.models.user.Status
import com.example.cipher.domain.models.user.User
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography
import com.example.cipher.ui.common.utils.LastSeenFormatter

@Composable
fun AccountBannerScreen(
    user: User
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.125f)
            .background(colors.tintColor)
            .padding(vertical = 12.dp, horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
//                    AsyncImage(
//                        model = "${NetworkKeys.IDENTITY_SERVER_BASE_URL}${user.avatarUrl}",
//                        contentDescription = null,
//                        imageLoader = imageLoader,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(50.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                    )
            Image(
                painter = painterResource(R.drawable.account_circle_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(65.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(colors.tintColor)
                    .border((0.5).dp, colors.tertiaryText, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = user.name,
                    color = colors.tertiaryText,
                    style = typography.toolbar.copy(fontSize = 20.sp)
                )
                Text(
                    text = if(user.status == Status.ONLINE) "online"
                    else LastSeenFormatter.getLastSeenMessage(user.lastSeen),
                    color = colors.tertiaryText,
                    style = typography.body.copy(fontSize = 12.sp)
                )
            }
        }
    }
}