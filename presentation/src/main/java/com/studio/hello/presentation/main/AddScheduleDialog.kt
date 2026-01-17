package com.studio.hello.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.studio.hello.domain.model.Schedule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onConfirm: (Schedule) -> Unit
) {
    var content by remember { mutableStateOf(TextFieldValue("")) }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("새 일과 추가") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("무엇을 해야 하나요?") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) hour = it },
                        label = { Text("시 (0-23)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = minute,
                        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) minute = it },
                        label = { Text("분 (0-59)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val h = hour.toIntOrNull() ?: 0
                    val m = minute.toIntOrNull() ?: 0
                    val schedule = Schedule(
                        content = content.text,
                        alarmTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(h, m))
                    )
                    onConfirm(schedule)
                },
                enabled = content.text.isNotBlank() && hour.isNotBlank() && minute.isNotBlank()
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
