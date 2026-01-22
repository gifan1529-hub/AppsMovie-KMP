package com.example.moviekmp.UI.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moviekmp.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun TopBar(
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical =30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(Res.drawable.propil),
            contentDescription = "Profile",
            modifier = Modifier
                .size(35.dp)
                .clickable(onClick = onProfileClick)
        )
        Image(
            painter = painterResource(Res.drawable.movie),
            contentDescription = "Movie",
            modifier = Modifier
                .size(45.dp)
        )
        Image(
            painter = painterResource(Res.drawable.search),
            contentDescription = "Search",
            modifier = Modifier
                .size(35.dp)
                .clickable(onClick = onSearchClick)
        )
    }
}