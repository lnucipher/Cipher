package com.example.cipher.ui.screens.home.settings.composable.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cipher.ui.common.theme.CipherTheme.colors
import com.example.cipher.ui.common.theme.CipherTheme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceSlider(
    currentValue: Int,
    maxValue: Int,
    minValue: Int,
    onValueChange: (Int) -> Unit
) {
    val normalizedValue = currentValue.toFloat() / maxValue.toFloat()
    val state = remember { SliderState(normalizedValue) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$minValue",
            color = colors.tintColor,
            style = typography.toolbar.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.weight(0.1f)
        )

        Slider(
            onValueChange = { newValue ->
                state.value = newValue
                onValueChange((newValue * maxValue).toInt())
            },
            modifier = Modifier.weight(1f),
            value = state.value,
            valueRange = 0f..1f,
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    thumbTrackGapSize = 0.dp,
                    colors = SliderDefaults.colors().copy(
                        thumbColor = colors.tintColor,
                        activeTrackColor = colors.tintColor,
                        activeTickColor = colors.tintColor,
                        inactiveTickColor = colors.errorColor,
                        inactiveTrackColor = colors.tintColor.copy(alpha = 0.4f)
                    )
                )
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color = colors.tintColor, shape = CircleShape)
                )
            }
        )

        Text(
            text = "$currentValue",
            color = colors.tintColor,
            style = typography.toolbar.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.weight(0.1f)
        )
    }
}
