package com.example.driftcarsetting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.driftcarsetting.data.CarDatabase
import com.example.driftcarsetting.data.CarModel
import com.example.driftcarsetting.data.CarRepository
import com.example.driftcarsetting.ui.theme.DriftCarSettingTheme
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class MainActivity : ComponentActivity() {
    private lateinit var repository: CarRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = CarDatabase.getDatabase(applicationContext)
        repository = CarRepository(database.carDao())

        enableEdgeToEdge()
        setContent {
            DriftCarSettingTheme {
                DriftCarSettingApp(repository)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriftCarSettingApp(repository: CarRepository) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCarModel by remember { mutableStateOf<CarModel?>(null) }
    var showAddCarDialog by rememberSaveable { mutableStateOf(false) }
    val carModels by repository.allCars.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    val filteredCarModels = carModels.filter { carModel ->
        carModel.name.contains(searchQuery, ignoreCase = true)
    }

    if (showAddCarDialog) {
        AddCarDialog(
            onDismiss = { showAddCarDialog = false },
            onSave = { newCar ->
                scope.launch {
                    repository.insertCar(newCar)
                    selectedCarModel = newCar
                }
                showAddCarDialog = false
            }
        )
    }

    if (selectedCarModel != null) {
        CarDetailsScreen(
            carModel = selectedCarModel!!,
            onBackClick = { selectedCarModel = null },
            repository = repository
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.fillMaxWidth(0.7f),
                                placeholder = { Text("Search...") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                singleLine = true
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
                ) {
                    items(
                        items = filteredCarModels,
                        key = { carModel -> carModel.name }
                    ) { carModel ->
                        CarRow(
                            name = carModel.name,
                            onClick = { selectedCarModel = carModel }
                        )
                        HorizontalDivider()
                    }
                }

                Button(
                    onClick = { showAddCarDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text("Add Car")
                }
            }
        }
    }
}


@Composable
fun CarRow(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate to $name"
        )
    }
}

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

@Composable
fun EditableDetailItem(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    var textValue by remember(value, isEditing) {
        mutableStateOf(value)
    }
    val focusManager = LocalFocusManager.current
    val textFieldValueState = remember { mutableStateOf(TextFieldValue(textValue)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        if (isEditing) {
            OutlinedTextField(
                value = textFieldValueState.value,
                onValueChange = { newTextFieldValue ->
                    textFieldValueState.value = newTextFieldValue
                    textValue = newTextFieldValue.text
                    onValueChange(newTextFieldValue.text)
                },
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            textFieldValueState.value = textFieldValueState.value.copy(
                                selection = TextRange(0, textFieldValueState.value.text.length)
                            )
                        }
                    },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text("") }
            )
        } else {
            Text(value, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        }
    }
}




@Composable
fun AddCarDialog(
    onDismiss: () -> Unit,
    onSave: (CarModel) -> Unit
) {
    var carName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Car") },
        text = {
            OutlinedTextField(
                value = carName,
                onValueChange = { carName = it },
                label = { Text("Car Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (carName.isNotBlank()) {
                        val newCar = CarModel(
                            name = carName,
                            frontCamberDegreeLeft = "",
                            frontCamberDegreeRight = "",
                            frontCamberLengthLeft = "",
                            frontCamberLengthRight = "",
                            frontTowDegreeLeft = "",
                            frontTowDegreeRight = "",
                            frontTowLengthLeft = "",
                            frontTowLengthRight = "",
                            frontShockName = "",
                            frontShockLength = "",
                            frontShockPreload = "",
                            frontRimOffset = "",
                            rearCamberDegreeLeft = "",
                            rearCamberDegreeRight = "",
                            rearCamberLengthLeft = "",
                            rearCamberLengthRight = "",
                            rearTowDegreeLeft = "",
                            rearTowDegreeRight = "",
                            rearTowLengthLeft = "",
                            rearTowLengthRight = "",
                            rearShockName = "",
                            rearShockLength = "",
                            rearShockPreload = "",
                            rearRimOffset = ""
                        )
                        onSave(newCar)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}