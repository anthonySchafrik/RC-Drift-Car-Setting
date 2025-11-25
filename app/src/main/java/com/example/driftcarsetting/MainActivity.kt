package com.example.driftcarsetting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.driftcarsetting.data.CarDatabase
import com.example.driftcarsetting.data.CarRepository
import com.example.driftcarsetting.ui.theme.DriftCarSettingTheme

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
