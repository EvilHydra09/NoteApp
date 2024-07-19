package com.example.countryapp.presentation.testing

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyList(modifier: Modifier = Modifier) {


    LazyColumn(contentPadding = WindowInsets.systemBars.asPaddingValues()) {
        items(100) {
            Text(
                "Item $it",
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }


}

@Composable
fun MyGridList(modifier: Modifier = Modifier) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),contentPadding = WindowInsets.systemBars.asPaddingValues()) {
        items(100) {
            Text(
                "Item $it",
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }

    }
}