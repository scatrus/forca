package com.jogo.forca

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

// Lista de palavras
val wordList = listOf(
        "CASA", "BOLA", "MESA", "CADEIRA", "SOFA", "BONÉ", "ANEL", "CHAVE", "ROUPA", "RELÓGIO",
        "LIVRO", "TESOURA", "COPO", "XÍCARA", "GARFO", "FACA", "COLHER", "PRATO", "TRAVESSEIRO", "COBERTOR",
        "CANETA", "LÁPIS", "BORRACHA", "PAPEL", "CADERNO", "GELADEIRA", "FOGÃO", "CAMA", "CABIDE", "ESPONJA",
        "TOALHA", "CORTINA", "BOLSA", "CARRO", "MOTO", "BICICLETA", "VIOLÃO", "GUITARRA", "CELULAR", "LAPTOP",
        "TECLADO", "MOUSE", "SAPATO", "MEIA", "CALÇA", "SHORTS", "JAQUETA", "CAMISA", "SAIA", "VESTIDO",
        "BOLSA", "CARTEIRA", "LENÇO", "FIVELA", "ANEL", "BRINCO", "COLAR", "PULSEIRA", "CINTO", "CUECA",
        "BIQUÍNI", "SUNGA", "MANTO", "CÁLICE", "PANELA", "CUMBUCO", "CUIA", "CARTÃO", "FLORES", "ESPELHO",
        "TAPETE", "ESTANTE", "QUADRO", "MOLDURA", "VASSOURA", "RODO", "ESCORREDOR", "GARRAFA", "VASO", "MESA",
        "CADEIRA", "SOFA", "ARMÁRIO", "GAVETA", "PRATELEIRA", "FONE", "SPEAKER", "LIVRO", "REVISTA", "JORNAL",
        "TABLET", "CONECTOR", "CABO", "CACHORRO", "GATO", "PÁSSARO", "PEIXE", "TARTARUGA", "HAMSTER", "CACHIMBO",
        "CHARUTO", "ISQUEIRO", "VELA", "LANTERNA", "MOSQUITEIRO", "CAMPAINHA", "PORTA", "JANELA", "CORTINA", "CHUVEIRO",
        "TORNEIRA", "RÁDIO", "TV", "DVD", "CD", "MP3", "GPS", "ÓCULOS", "RELÓGIO", "BRINQUEDO", "BONECA",
        "CARRINHO", "QUEBRA-CABEÇA", "PISTOLA", "FACA", "ESPADA", "ARCO", "FLECHA", "LANÇA", "BOLA", "RAQUETE",
        "REDE", "CAMA", "BANHEIRA", "PIA", "ESPELHO", "PRATELEIRA", "ARMÁRIO", "QUADRO", "COFRE", "BAÚ",
        "TAMPA", "JARRA", "BULE", "COPO", "XÍCARA", "GARFO", "COLHER", "PRATO", "TRAVESSEIRO", "LENÇOL",
        "EDREDOM", "TRAVESSEIRO", "COBERTOR", "SAPATO", "TÊNIS", "SANDÁLIA", "SAPATILHA", "MEIA", "CHINELO", "TOALHA",
        "CORTINA", "LAVATÓRIO", "ESCOVA", "PASTA", "DENTAL", "FIO", "DENTAL", "FIO", "DENTAL", "ESCOVA",
        "DEPILADOR", "PINÇA", "MÁQUINA", "CORTAR", "CABELO", "SECADOR", "ESCOVA", "CREME", "HIDRATANTE", "XAMPU",
        "CONDICIONADOR", "SABONETE", "PENTE", "GRAMPO", "TIARA", "ANEL", "PULSEIRA", "COLAR", "BRINCO", "MAQUIAGEM",
        "BATOM", "SOMBRA", "RÍMEL", "LÁPIS", "BLUSH", "BASE", "ESMALTE", "LIXA", "PERFUME", "DESODORANTE"
)

// Função para selecionar uma palavra aleatória da lista


// Função para verificar se todas as letras da palavra foram adivinhadas
fun allLettersGuessed(word: String, guessedLetters: Set<Char>): Boolean {
    return word.all { it in guessedLetters }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun HangmanGame() {
    var word by remember { mutableStateOf(getRandomWord()) }
//    var guessedLetters by remember { mutableStateOf(mutableSetOf<Char>()) }
    var incorrectGuesses by remember { mutableStateOf(0) }
    var guessedLetters = remember { mutableStateOf(setOf<Char>()) }




    val alphabet = ('A'..'Z').toList()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {



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
                                if (!guessedLetters.value.contains(letter)) {
                                    val newGuessed = guessedLetters.value + setOf(letter)

                                    guessedLetters.value = newGuessed.toSet()
//                                    guessedLetters.add(letter)
                                    if (!word.contains(letter)) {
                                        incorrectGuesses++
                                    }
                                }

                                // Verificar se todas as letras foram adivinhadas
                                if (allLettersGuessed(word, guessedLetters.value)) {
                                    // Exibir uma mensagem indicando que o jogador ganhou
                                    Toast.makeText(
                                        context,
                                        "Parabéns! Você acertou! A palavra era $word.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    guessedLetters.value = emptySet()
                                    incorrectGuesses = 0
                                    word = getRandomWord()
                                }
                            },
                            enabled = !guessedLetters.value.contains(letter)
                        )
                    }
                }
            }
        }



        // Boneco da forca
        Hangman(incorrectGuesses = incorrectGuesses)

        // Mostrar a palavra com espaços para letras não reveladas
        Text(fontSize = 50.sp,
            text = buildString {
                word.forEach { char ->
                    if (guessedLetters.value.contains(char)) {
                        append("$char ")
                    } else {
                        append("_ ")
                    }
                }
            },
            fontWeight = FontWeight.Bold
        )

        // Exibir pop-up de fim de jogo se o jogador perdeu
        if (incorrectGuesses >= 6) {
            AlertDialog(
                onDismissRequest = { /* No-op */ },
                title = { Text(text = "Fim do jogo!") },
                text = { Text(text = "Você perdeu! A palavra era $word.") },
                confirmButton = {
                    Button(
                        onClick = {
                            guessedLetters.value = emptySet()
                            incorrectGuesses = 0
                            word = getRandomWord()
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
        Text(text = letter.toString(), color = Color.White)
    }
}

fun getRandomWord(): String {
    return wordList.random()
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
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp),
        contentAlignment = Alignment.Center,

    ) {
        Canvas(modifier = Modifier.size(150.dp)) {
            drawLine(
                color = darkColorScheme().primary,
                start = Offset(200f, 0f),
                end = Offset(200f, 280f),
                strokeWidth = 20f
            )
            drawLine(
                color = darkColorScheme().primary,
                start = Offset(190f, 0f),
                end = Offset(300f, 0f),
                strokeWidth = 20f
            )
            drawLine(
                color = darkColorScheme().primary,
                start = Offset(300f, -10f),
                end = Offset(300f, 40f),
                strokeWidth = 20f
            )

            visibleParts.forEach { part ->
                when (part) {
                    "O" -> drawCircle(
                        color = darkColorScheme().primary,
                        radius = 30f,
                        center = Offset(300f, 70f)
                    )
                    "|" -> drawLine(
                        color = darkColorScheme().primary,
                        start = Offset(300f, 100f),
                        end = Offset(300f, 220f),
                        strokeWidth = 20f
                    )
                    "/" -> drawLine(
                        color = darkColorScheme().primary,
                        start = Offset(300f, 100f),
                        end = Offset(220f, 130f),
                        strokeWidth = 16f
                    )
                    "\\" -> drawLine(
                        color = darkColorScheme().primary,
                        start = Offset(300f, 100f),
                        end = Offset(380f, 130f),
                        strokeWidth = 16f
                    )
                    "//" -> drawLine(
                        color = darkColorScheme().primary,
                        start = Offset(300f, 210f),
                        end = Offset(230f, 280f),
                        strokeWidth = 16f
                    )
                    "\\/" -> drawLine(
                        color = darkColorScheme().primary,
                        start = Offset(300f, 210f),
                        end = Offset(370f, 280f),
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
