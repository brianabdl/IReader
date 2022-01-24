package ir.kazemcodes.infinity.feature_reader.presentation.reader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import ir.kazemcodes.infinity.core.data.network.utils.toast
import ir.kazemcodes.infinity.feature_reader.presentation.reader.ReaderScreenViewModel


@Composable
fun ChaptersSliderComposable(
    modifier: Modifier = Modifier,
    viewModel: ReaderScreenViewModel,
) {
    val context = LocalContext.current
    val currentIndex = viewModel.state.value.currentChapterIndex
    val chapters = viewModel.state.value.chapters
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = viewModel.state.value.chapters[currentIndex].title,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(modifier = modifier.weight(1f),
                onClick = {
                    if (currentIndex > 0) {
                        viewModel.updateChapterSliderIndex(currentIndex - 1)
                        viewModel.getChapter(chapters[currentIndex - 1 ])
                    } else context.toast("This is first chapter")
                }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Chapter")
            }
            Slider(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f),
                value = viewModel.state.value.currentChapterIndex.toFloat(),
                onValueChange = {
                    viewModel.updateChapterSliderIndex(it.toInt())
                },
                onValueChangeFinished = {
                    context.toast(chapters[viewModel.state.value.currentChapterIndex].title)
                    viewModel.updateChapterSliderIndex(currentIndex)
                    viewModel.getChapter(chapters[viewModel.state.value.currentChapterIndex])
                },
                valueRange = 0f..(chapters.size - 1).toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.primary,
                    activeTrackColor = MaterialTheme.colors.primary.copy(alpha = .6f),
                    inactiveTickColor = MaterialTheme.colors.onBackground.copy(alpha = .6f),
                    inactiveTrackColor = MaterialTheme.colors.onBackground.copy(alpha = .6f),
                    activeTickColor = MaterialTheme.colors.primary.copy(alpha = .6f)

                )
            )
            IconButton(modifier = modifier.weight(1f), onClick = {
                if (currentIndex < (chapters.size -1 )) {
                    viewModel.updateChapterSliderIndex(currentIndex + 1)
                    viewModel.getChapter(chapters[currentIndex + 1])
                } else {
                    context.toast("This is last chapter")
                }

            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Chapter")
            }
        }
    }
}