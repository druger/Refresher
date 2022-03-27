package com.druger.refresher.presentation.task

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.druger.refresher.R
import com.druger.refresher.data.db.entity.Task
import com.druger.refresher.domain.task.model.ModelTask
import com.druger.refresher.util.extensions.getFullDate

object TaskRowCompose {
    @Composable
    fun TaskRow(
        task: ModelTask,
        moveTaskToDone: (task: ModelTask) -> Unit,
        moveTaskToCurrent: (task: ModelTask) -> Unit
    ) {
        val isDone = task.status == Task.STATUS_DONE
        val isCurrent = task.status == Task.STATUS_CURRENT
        Row(
            modifier = Modifier
                .height(60f.dp)
                .padding(horizontal = 8f.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = isDone,
                onCheckedChange = {
                    if (isCurrent) moveTaskToDone(task)
                    else if (isDone) moveTaskToCurrent(task)
                },
                modifier = Modifier.weight(0.1f)
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(0.6f),
                color = colorResource(R.color.primary_text_light),
                fontSize = 16.sp
            )
            val reminderDate = task.reminderDate
            if (reminderDate != 0L) {
                Text(
                    text = reminderDate.getFullDate(),
                    modifier = Modifier.weight(0.3f),
                    color = colorResource(R.color.secondary_text_light),
                    fontSize = 14.sp
                )
            }
        }
    }
}