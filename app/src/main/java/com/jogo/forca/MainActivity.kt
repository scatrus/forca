package com.jogo.forca

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jogo.forca.ui.theme.ForcaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForcaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HangmanGame()
                }
            }
        }
    }
}



@SuppressLint("MutableCollectionMutableState")
@Composable
fun HangmanGame() {
    var word by remember { mutableStateOf("BERNARDO") }
    var guessedLetters by remember { mutableStateOf(mutableSetOf<Char>()) }
    var incorrectGuesses by remember { mutableStateOf(0) }
    val alphabet = ('A'..'Z').toList()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Mostrar a palavra com espaços para letras não reveladas
        Text(fontSize = 50.sp,
            text = buildString {
                word.forEach { char ->
                    if (guessedLetters.contains(char)) {
                        append("$char ")
                    } else {
                        append("_ ")
                    }
                }
            },
            fontWeight = FontWeight.Bold
        )

        // Grid com letras do alfabeto
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(alphabet.chunked(6)) { row ->
                Row {
                    row.forEach { letter ->
                        AlphabetButton(
                            letter = letter,
                            onClick = {
                                if (!guessedLetters.contains(letter)) {
                                    guessedLetters.add(letter)
                                    if (!word.contains(letter)) {
                                        incorrectGuesses++
                                    }
                                }
                            },
                            enabled = !guessedLetters.contains(letter)
                        )
                    }
                }
            }
        }

        // Boneco da forca
        Hangman(incorrectGuesses = incorrectGuesses)

        // Exibir pop-up de fim de jogo se o jogador perdeu
        if (incorrectGuesses >= 6) {
            AlertDialog(
                onDismissRequest = { /* No-op */ },
                title = { Text(text = "Fim do jogo!") },
                text = { Text(text = "Você perdeu! A palavra era $word.") },
                confirmButton = {
                    Button(
                        onClick = {
                            guessedLetters.clear()
                            incorrectGuesses = 0
                        }
                    ) {
                        Text(text = "Novo Jogo")
                    }
                }
            )
        }
    }
}

@Composable
fun AlphabetButton(letter: Char, onClick: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(1.dp)
            .size(52.dp),
        enabled = enabled
    ) {
        Text(text = letter.toString(), color = Color.Black)
    }
}

@Composable
fun Hangman(incorrectGuesses: Int) {
    // Número de partes do corpo que serão mostradas baseadas no número de suposições incorretas
    val visibleParts = when (incorrectGuesses) {
        0 -> emptyList()
        1 -> listOf("O")
        2 -> listOf("O", "|")
        3 -> listOf("O", "|", "/")
        4 -> listOf("O", "|", "/", "\\")
        5 -> listOf("O", "|", "/", "\\", "//")
        6 -> listOf("O", "|", "/", "\\", "//", "\\/")

        else -> emptyList()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            drawLine(
                color = Color.Black,
                start = Offset(200f, 0f),
                end = Offset(200f, 220f),
                strokeWidth = 20f
            )
            drawLine(
                color = Color.Black,
                start = Offset(190f, 0f),
                end = Offset(300f, 0f),
                strokeWidth = 20f
            )
            drawLine(
                color = Color.Black,
                start = Offset(300f, -10f),
                end = Offset(300f, 40f),
                strokeWidth = 20f
            )

            visibleParts.forEach { part ->
                when (part) {
                    "O" -> drawCircle(
                        color = Color.Black,
                        radius = 30f,
                        center = Offset(300f, 70f)
                    )
                    "|" -> drawLine(
                        color = Color.Black,
                        start = Offset(300f, 100f),
                        end = Offset(300f, 220f),
                        strokeWidth = 20f
                    )
                    "/" -> drawLine(
                        color = Color.Black,
                        start = Offset(300f, 100f),
                        end = Offset(220f, 130f),
                        strokeWidth = 16f
                    )
                    "\\" -> drawLine(
                        color = Color.Black,
                        start = Offset(300f, 100f),
                        end = Offset(380f, 130f),
                        strokeWidth = 16f
                    )
                    "//" -> drawLine(
                        color = Color.Black,
                        start = Offset(300f, 210f),
                        end = Offset(230f, 330f),
                        strokeWidth = 16f
                    )
                    "\\/" -> drawLine(
                        color = Color.Black,
                        start = Offset(300f, 210f),
                        end = Offset(370f, 330f),
                        strokeWidth = 16f
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHangmanGame() {
    HangmanGame()
}
