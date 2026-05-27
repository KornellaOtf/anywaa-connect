package com.anywaa.connect.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

    val InterFont = FontFamily.Default
    val Typography = Typography(
        bodyLarge = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        titleLarge = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        ),
        labelSmall = TextStyle(
            fontFamily = InterFont,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
        )
    )