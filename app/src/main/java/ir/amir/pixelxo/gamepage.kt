package ir.amir.pixelxo

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ir.amir.pixelxo.ui.theme.GreenO
import ir.amir.pixelxo.ui.theme.PurpleBackground
import ir.amir.pixelxo.ui.theme.RedX
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun gamepage(
    modifier: Modifier = Modifier,
    playerone: String,
    playertwo: String,
    navController: NavController
) {
    val googleSansRegular = FontFamily(Font(R.font.googlesans_reg))
    val googleSansBold = FontFamily(Font(R.font.googlesans_bold))
    val context = LocalContext.current

    var player1Score by remember { mutableIntStateOf(0) }
    var player2Score by remember { mutableIntStateOf(0) }
    var currentRound by remember { mutableIntStateOf(1) }

    var isPlayer1Turn by remember { mutableStateOf(true) }
    val board = remember { mutableStateListOf("", "", "", "", "", "", "", "", "") }

    var isRoundOver by remember { mutableStateOf(false) }
    var roundWinner by remember { mutableStateOf<String?>(null) }
    var winningCombo by remember { mutableStateOf<List<Int>?>(null) }
    var showEndDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    fun checkWinningCombo(b: List<String>): List<Int>? {
        val winCombos = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        for (combo in winCombos) {
            val cellA = b[combo[0]]
            val cellB = b[combo[1]]
            val cellC = b[combo[2]]
            if (cellA.isNotEmpty() && cellA == cellB && cellB == cellC) {
                return combo
            }
        }
        return null
    }

    fun nextRound() {
        if (currentRound >= 5) {
            val sdf = SimpleDateFormat("HH:mm — yyyy/MM/dd", Locale.getDefault())
            val currentDateTime = sdf.format(Date())
            ScoreManager.saveMatchRecord(
                context = context,
                record = MatchRecord(
                    player1Name = playerone,
                    player2Name = playertwo,
                    player1Score = player1Score,
                    player2Score = player2Score,
                    dateTime = currentDateTime
                )
            )
            showEndDialog = true
        } else {
            currentRound++
            for (i in 0 until 9) {
                board[i] = ""
            }
            isRoundOver = false
            roundWinner = null
            winningCombo = null
            isPlayer1Turn = (currentRound % 2 != 0)
        }
    }

    fun restartGame() {
        player1Score = 0
        player2Score = 0
        currentRound = 1
        for (i in 0 until 9) {
            board[i] = ""
        }
        isRoundOver = false
        roundWinner = null
        winningCombo = null
        showEndDialog = false
        isPlayer1Turn = true
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color(0xFFF2F3F9)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showExitDialog = true }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "←",
                            color = PurpleBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "PixelXO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A202C),
                        fontFamily = googleSansBold
                    )

                    Spacer(modifier = Modifier.width(32.dp))
                }

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val p1Active = isPlayer1Turn && !isRoundOver
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (p1Active) Color(0xFFE0F3F1) else Color.Transparent,
                            border = if (p1Active) BorderStroke(1.5.dp, GreenO) else null,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "O",
                                    color = GreenO,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = playerone,
                                    color = Color(0xFF2D3748),
                                    fontSize = 14.sp,
                                    fontFamily = googleSansRegular
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = player1Score.toString(),
                                    color = GreenO,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                            }
                        }

                        Text(
                            text = "—",
                            color = Color(0xFFA0AEC0),
                            fontSize = 20.sp,
                            fontFamily = googleSansRegular
                        )

                        val p2Active = !isPlayer1Turn && !isRoundOver
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (p2Active) Color(0xFFFFEBEE) else Color.Transparent,
                            border = if (p2Active) BorderStroke(1.5.dp, RedX) else null,
                            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "X",
                                    color = RedX,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = playertwo,
                                    color = Color(0xFF2D3748),
                                    fontSize = 14.sp,
                                    fontFamily = googleSansRegular
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = player2Score.toString(),
                                    color = RedX,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Round $currentRound of 5",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A5568),
                    fontFamily = googleSansBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Turn: ",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        fontFamily = googleSansRegular
                    )
                    Text(
                        text = if (isPlayer1Turn) playerone else playertwo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPlayer1Turn) GreenO else RedX,
                        fontFamily = googleSansBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(3) { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                repeat(3) { col ->
                                    val index = row * 3 + col
                                    val cellValue = board[index]

                                    val isWinningCell = winningCombo?.contains(index) == true

                                    val cellBg = when {
                                        cellValue == "O" -> Color(0xFFFFCA28)
                                        cellValue == "X" -> RedX
                                        else -> Color(0xFFF4F5FB)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = cellBg,
                                        border = if (isWinningCell) BorderStroke(3.dp, Color(0xFF4A43C6)) else null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clickable {
                                                if (cellValue.isEmpty() && !isRoundOver && !showEndDialog) {
                                                    board[index] = if (isPlayer1Turn) "O" else "X"
                                                    val combo = checkWinningCombo(board)
                                                    if (combo != null) {
                                                        isRoundOver = true
                                                        winningCombo = combo
                                                        val winner = board[combo[0]]
                                                        roundWinner = winner
                                                        if (winner == "O") {
                                                            player1Score++
                                                        } else {
                                                            player2Score++
                                                        }
                                                    } else if (board.none { it.isEmpty() }) {
                                                        isRoundOver = true
                                                        roundWinner = null
                                                    } else {
                                                        isPlayer1Turn = !isPlayer1Turn
                                                    }
                                                }
                                            }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            if (cellValue.isNotEmpty()) {
                                                Text(
                                                    text = cellValue,
                                                    fontSize = 40.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    fontFamily = googleSansBold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(
                    visible = isRoundOver && !showEndDialog,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut()
                ) {
                    val bannerText = when (roundWinner) {
                        "O" -> "🎉 $playerone won this round!"
                        "X" -> "🎉 $playertwo won this round!"
                        else -> "🤝 Round tied!"
                    }

                    Button(
                        onClick = { nextRound() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleBackground),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = bannerText,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = googleSansBold
                        )
                    }
                }
            }
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = "Exit Game",
                        fontWeight = FontWeight.Bold,
                        fontFamily = googleSansBold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to exit the game? Match progress will be lost.",
                        fontFamily = googleSansRegular
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitDialog = false
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
                    ) {
                        Text("Exit", fontFamily = googleSansBold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showExitDialog = false }) {
                        Text("Resume", fontFamily = googleSansRegular)
                    }
                }
            )
        }

        if (showEndDialog) {
            Dialog(onDismissRequest = { }) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🏆 Game Over 🏆",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A202C),
                                fontFamily = googleSansBold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "$playerone $player1Score — $player2Score $playertwo",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3748),
                                fontFamily = googleSansBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            val matchWinnerText = when {
                                player1Score > player2Score -> "🎉 $playerone Won!"
                                player2Score > player1Score -> "🎉 $playertwo Won!"
                                else -> "🤝 Game Tied!"
                            }

                            Text(
                                text = matchWinnerText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A5568),
                                fontFamily = googleSansBold
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        showEndDialog = false
                                        navController.navigate("scoretable")
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp),
                                    border = BorderStroke(1.dp, PurpleBackground),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleBackground),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Scoreboard",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = googleSansBold
                                    )
                                }

                                Button(
                                    onClick = { restartGame() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleBackground),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Play Again",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = googleSansBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
