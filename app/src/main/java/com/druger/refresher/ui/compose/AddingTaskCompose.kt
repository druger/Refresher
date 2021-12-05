package com.druger.refresher.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.druger.refresher.R

object AddingTaskCompose {
    @Composable
    fun addTaskToolbar(navController: NavHostController, addTask: () -> Unit) {
        TopAppBar {
            Image(
                ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .weight(.1f)
                    .clickable { navController.navigateUp() }
            )

            Text(
                stringResource(R.string.add_task),
                modifier = Modifier.weight(.8f),
                textAlign = TextAlign.Center
            )

            Image(
                ImageVector.vectorResource(R.drawable.ic_done),
                contentDescription = null,
                modifier = Modifier
                    .weight(.1f)
                    .clickable { addTask() }
            )
        }
    }

    @Composable
    fun addTaskLayout(
        title: String,
        dateText: String,
        timeText: String,
        onValueChange: (String) -> Unit,
        onDateClick: () -> Unit,
        onTimeClick: () -> Unit
    ) {
        var descriptionText by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = title,
                onValueChange = { onValueChange(it) },
                label = { Text(stringResource(R.string.task_title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 22.sp)
            )

            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text(stringResource(R.string.text)) },
                modifier = Modifier.fillMaxWidth()
            )

            ClickableText(
                AnnotatedString(dateText),
                modifier = Modifier.padding(16.dp),
                onClick = { onDateClick() },
                style = TextStyle(
                    fontSize = 22.sp,
                    color = colorResource(R.color.secondary_text_light)
                )
            )

            ClickableText(
                AnnotatedString(timeText),
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onTimeClick() },
                style = TextStyle(
                    fontSize = 22.sp,
                    color = colorResource(R.color.secondary_text_light)
                )
            )
        }
    }
}