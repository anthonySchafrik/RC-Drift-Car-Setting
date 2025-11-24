package com.example.driftcarsetting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.example.driftcarsetting.components.AddCarDialog
import com.example.driftcarsetting.components.CarDetailsScreen
import com.example.driftcarsetting.components.CarRow
import com.example.driftcarsetting.data.CarModel
import com.example.driftcarsetting.data.CarRepository
import kotlinx.coroutines.launch

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