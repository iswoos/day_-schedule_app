package com.studio.hello.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.studio.hello.domain.model.Schedule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScheduleDialog(
    editingSchedule: Schedule? = null,
    onDismiss: () -> Unit,
    onConfirm: (Schedule) -> Unit
) {
    var content by remember { mutableStateOf(TextFieldValue(editingSchedule?.content ?: "")) }
    var hour by remember { mutableStateOf(editingSchedule?.alarmTime?.hour?.toString() ?: "") }
    var minute by remember { mutableStateOf(editingSchedule?.alarmTime?.minute?.toString() ?: "") }

    val h = hour.toIntOrNull() ?: -1
    val m = minute.toIntOrNull() ?: -1
    val isValidTime = h in 0..23 && m in 0..59
    
    val targetTime = if (isValidTime) {
        LocalDateTime.of(LocalDate.now(), LocalTime.of(h, m))
    } else null
    
    val isPastTime = targetTime?.isBefore(LocalDateTime.now()) ?: false

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = if (editingSchedule == null) "새 일정 등록" else "일정 수정",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("어떤 일을 하시나요?") },
                    placeholder = { Text("운동하기, 책 읽기 등") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Column {
                    Text(
                        text = "시간 설정",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = hour,
                            onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) hour = it },
                            label = { Text("시") },
                            placeholder = { Text("00") },
                            modifier = Modifier.weight(1f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                        OutlinedTextField(
                            value = minute,
                            onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) minute = it },
                            label = { Text("분") },
                            placeholder = { Text("00") },
                            modifier = Modifier.weight(1f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )
                    }
                }
                
                if (isValidTime && isPastTime) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "현재 시간보다 이후의 시간을 입력해주세요.",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("취소", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = {
                            if (targetTime != null) {
                                val schedule = editingSchedule?.copy(
                                    content = content.text,
                                    alarmTime = targetTime
                                ) ?: Schedule(
                                    content = content.text,
                                    alarmTime = targetTime
                                )
                                onConfirm(schedule)
                            }
                        },
                        enabled = content.text.isNotBlank() && isValidTime && !isPastTime,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(if (editingSchedule == null) "일정 저장" else "일정 수정")
                    }
                }
            }
        }
    }
}
