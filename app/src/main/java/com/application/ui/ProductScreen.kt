package com.application.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProductScreen() {
    Text("LISTADO DE PRODUCTOS")
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProductScreen()
}