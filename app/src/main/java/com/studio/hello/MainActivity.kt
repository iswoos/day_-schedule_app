package com.studio.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.studio.hello.presentation.main.AddScheduleDialog
import com.studio.hello.presentation.main.MainScreen
import com.studio.hello.presentation.main.MainViewModel
import com.studio.hello.ui.theme.Day_scheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Day_scheduleTheme {
                var showAddDialog by remember { mutableStateOf(false) }

                MainScreen(
                    viewModel = viewModel,
                    onAddClick = { showAddDialog = true }
                )

                if (showAddDialog) {
                    AddScheduleDialog(
                        onDismiss = { showAddDialog = false },
                        onConfirm = { schedule ->
                            viewModel.addSchedule(schedule)
                            showAddDialog = false
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }
}