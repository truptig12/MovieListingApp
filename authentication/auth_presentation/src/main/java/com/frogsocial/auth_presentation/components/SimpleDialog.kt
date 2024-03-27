package com.frogsocial.auth_presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SimpleDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {onDismiss()  }) {
                    Text("Okay", color = Color.Black, style = MaterialTheme.typography.body1)
                }
            }
        },
        title = { Text(text = "Error!", color = Color.Black, style = MaterialTheme.typography.h6)},
        text = { Text(message, color = Color.Black, style = MaterialTheme.typography.body1) },
        modifier = Modifier.width(IntrinsicSize.Max)
    )
}

