package com.frogsocial.movie_presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.frogsocial.movie_domain.model.Movie
import com.frogsocial.movie_presentation.components.ErrorMessage
import com.frogsocial.movie_presentation.components.ItemMovie
import com.frogsocial.movie_presentation.components.LoadingNextPageItem
import com.frogsocial.movie_presentation.components.PageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    navController: NavHostController
) {
    val moviePagingItems: LazyPagingItems<Movie> = viewModel.moviesState.collectAsLazyPagingItems()
    var searchText by rememberSaveable { mutableStateOf("") }


    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movies App",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .weight(1.0f),
                    textAlign = TextAlign.Center
                )

                Button(onClick = {
                 //   viewModel.logout()

                }) {
                    Text("Log Out")
                }
            }
        }
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                value = searchText,
                onValueChange = {
                    searchText = it
                    viewModel.onEvent(HomeEvent.Search(searchText)) },
                label = { Text("Search") },
                textStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor= Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Gray, // Change to your desired color
                    unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.2f ) // Op
                ),
            )
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(0xfff9f9f9))
            ) {
                item { Spacer(modifier = Modifier.padding(4.dp)) }
                items(moviePagingItems.itemCount) { index ->
                    ItemMovie(
                        itemEntity = moviePagingItems[index]!!,
                        onClick = {

                        }
                    )
                }
                moviePagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = moviePagingItems.loadState.refresh as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier.fillParentMaxSize(),
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }

                        loadState.append is LoadState.Loading -> {
                            item { LoadingNextPageItem(modifier = Modifier) }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = moviePagingItems.loadState.append as LoadState.Error
                            item {
                                ErrorMessage(
                                    modifier = Modifier,
                                    message = error.error.localizedMessage!!,
                                    onClickRetry = { retry() })
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.padding(4.dp)) }
            }
        }
    }
}