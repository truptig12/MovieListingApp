package com.frogsocial.movie_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frogsocial.movie_domain.model.Movie

@Composable
fun MovieItem(
    movie:Movie,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = movie.Title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(text = movie.Year, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = wordInfo.origin)

    }
}