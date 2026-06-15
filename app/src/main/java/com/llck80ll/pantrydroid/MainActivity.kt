package com.llck80ll.pantrydroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.llck80ll.pantrydroid.ui.PantryApp
import com.llck80ll.pantrydroid.ui.theme.PantryDroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantryDroidTheme {
                PantryApp(viewModel())
            }
        }
    }
}

