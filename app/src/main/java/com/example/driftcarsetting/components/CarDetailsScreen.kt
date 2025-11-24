package com.example.driftcarsetting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.driftcarsetting.data.CarModel
import com.example.driftcarsetting.data.CarRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailsScreen(
    carModel: CarModel,
    onBackClick: () -> Unit,
    repository: CarRepository
) {
    var isEditing by rememberSaveable { mutableStateOf(carModel.id == 0L) }
    var mirrorValues by rememberSaveable { mutableStateOf(false) }
    var editableCarModel by remember { mutableStateOf(carModel) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Car") },
            text = { Text("Are you sure you want to delete ${editableCarModel.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            repository.deleteCar(editableCarModel)
                            onBackClick()
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(editableCarModel.name) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .clickable(onClick = onBackClick)
                            .padding(16.dp)
                    )
                },
                actions = {
                    Switch(
                        checked = mirrorValues,
                        onCheckedChange = { mirrorValues = it },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .clickable {
                                if (isEditing) {
                                    scope.launch {
                                        repository.updateCar(editableCarModel)
                                    }
                                }
                                isEditing = !isEditing
                            }
                            .padding(16.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                Text("Front Settings", style = MaterialTheme.typography.headlineSmall)
            }
            item {
                EditableDetailItem(
                    label = "Camber Degree Left",
                    value = editableCarModel.frontCamberDegreeLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontCamberDegreeLeft = it,
                                frontCamberDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(frontCamberDegreeLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Degree Right",
                    value = editableCarModel.frontCamberDegreeRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontCamberDegreeLeft = it,
                                frontCamberDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(frontCamberDegreeRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Length Left",
                    value = editableCarModel.frontCamberLengthLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontCamberLengthLeft = it,
                                frontCamberLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(frontCamberLengthLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Length Right",
                    value = editableCarModel.frontCamberLengthRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontCamberLengthLeft = it,
                                frontCamberLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(frontCamberLengthRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Degree Left",
                    value = editableCarModel.frontTowDegreeLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontTowDegreeLeft = it,
                                frontTowDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(frontTowDegreeLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Degree Right",
                    value = editableCarModel.frontTowDegreeRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontTowDegreeLeft = it,
                                frontTowDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(frontTowDegreeRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Length Left",
                    value = editableCarModel.frontTowLengthLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontTowLengthLeft = it,
                                frontTowLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(frontTowLengthLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Length Right",
                    value = editableCarModel.frontTowLengthRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                frontTowLengthLeft = it,
                                frontTowLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(frontTowLengthRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Name",
                    value = editableCarModel.frontShockName,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(frontShockName = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Length",
                    value = editableCarModel.frontShockLength,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(frontShockLength = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Preload",
                    value = editableCarModel.frontShockPreload,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(frontShockPreload = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Rim Offset",
                    value = editableCarModel.frontRimOffset,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(frontRimOffset = it) }
                )
            }

            item {
                Text(
                    "Rear Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Degree Left",
                    value = editableCarModel.rearCamberDegreeLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearCamberDegreeLeft = it,
                                rearCamberDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(rearCamberDegreeLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Degree Right",
                    value = editableCarModel.rearCamberDegreeRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearCamberDegreeLeft = it,
                                rearCamberDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(rearCamberDegreeRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Length Left",
                    value = editableCarModel.rearCamberLengthLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearCamberLengthLeft = it,
                                rearCamberLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(rearCamberLengthLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Camber Length Right",
                    value = editableCarModel.rearCamberLengthRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearCamberLengthLeft = it,
                                rearCamberLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(rearCamberLengthRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Degree Left",
                    value = editableCarModel.rearTowDegreeLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearTowDegreeLeft = it,
                                rearTowDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(rearTowDegreeLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Degree Right",
                    value = editableCarModel.rearTowDegreeRight,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearTowDegreeLeft = it,
                                rearTowDegreeRight = it
                            )
                        } else {
                            editableCarModel.copy(rearTowDegreeRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Length Left",
                    value = editableCarModel.rearTowLengthLeft,
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearTowLengthLeft = it,
                                rearTowLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(rearTowLengthLeft = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Tow Length Right",
                    value = editableCarModel.rearTowLengthRight.toString(),
                    isEditing = isEditing,
                    onValueChange = {
                        editableCarModel = if (mirrorValues) {
                            editableCarModel.copy(
                                rearTowLengthLeft = it,
                                rearTowLengthRight = it
                            )
                        } else {
                            editableCarModel.copy(rearTowLengthRight = it)
                        }
                    }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Name",
                    value = editableCarModel.rearShockName,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(rearShockName = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Length",
                    value = editableCarModel.rearShockLength,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(rearShockLength = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Shock Preload",
                    value = editableCarModel.rearShockPreload,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(rearShockPreload = it) }
                )
            }
            item {
                EditableDetailItem(
                    label = "Rim Offset",
                    value = editableCarModel.rearRimOffset,
                    isEditing = isEditing,
                    onValueChange = { editableCarModel = editableCarModel.copy(rearRimOffset = it) }
                )
            }

            item {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text("Delete Car")
                }
            }
        }
    }
}