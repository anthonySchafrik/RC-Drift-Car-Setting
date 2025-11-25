package com.example.driftcarsetting.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.driftcarsetting.data.CarModel

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
