package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.example.project.network.NetworkUtils.httpClient
import org.example.project.network.model.ApiResponse
import org.example.project.network.model.Hero

@Composable
@Preview
fun App() {
    MaterialTheme {
        var superHeroName by remember { mutableStateOf("") }
        var superHeroList by remember { mutableStateOf<List<Hero>>(emptyList()) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                TextField(value = superHeroName, onValueChange = {superHeroName = it})
                Button(onClick = {getSuperHeroList(superHeroName){superHeroList = it} }){
                    Text("Load")
                }
            }
            //List
            LazyColumn {
                items(superHeroList){ hero ->
                    Text(hero.name)
                }
            }
        }
    }
}

fun getSuperHeroList(superHeroName:String, onSuccesResponse: (List<Hero>) -> Unit){
    val url = "https://superheroapi.com/api/access-token/search/$superHeroName"
    if(superHeroName.isBlank()) return
    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url).body<ApiResponse>()
        onSuccesResponse(response.results)
    }
}