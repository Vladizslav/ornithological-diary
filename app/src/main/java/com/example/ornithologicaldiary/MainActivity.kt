package com.example.ornithologicaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ornithologicaldiary.ui.OrnithologicalDiaryApp
import com.example.ornithologicaldiary.ui.theme.OrnithologicalDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrnithologicalDiaryTheme {
                OrnithologicalDiaryApp()
            }
        }
    }
}
