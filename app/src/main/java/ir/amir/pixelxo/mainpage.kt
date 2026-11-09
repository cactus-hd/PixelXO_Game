package ir.amir.pixelxo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.amir.pixelxo.ui.theme.GreenO
import ir.amir.pixelxo.ui.theme.RedX

@Composable
fun Mainpage(modifier: Modifier = Modifier, navController: NavController) {
    val googleSansRegular = FontFamily(Font(R.font.googlesans_reg))
    val googleSansBold = FontFamily(Font(R.font.googlesans_bold))

    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val errorMessage: String? = when {
        !showError -> null
        player1Name.isBlank() -> "Enter Player 1 name"
        player2Name.isBlank() -> "Enter Player 2 name"
        player1Name.trim() == player2Name.trim() -> "Player names must be different"
        else -> null
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color(0xFF463FCD)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF5E54E8),
                                Color(0xFF463FCD),
                                Color(0xFF332CA0)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PlayerBadge(symbol = "O", color = GreenO)
                        PlayerBadge(symbol = "X", color = RedX)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "PixelXO",
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = googleSansBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "2 Player Mode — 5 Rounds",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontFamily = googleSansRegular
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            OutlinedTextField(
                                value = player1Name,
                                onValueChange = { player1Name = it },
                                placeholder = { Text("First Player Name", fontFamily = googleSansRegular) },
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (player1Name.isNotEmpty()) {
                                            Text(
                                                "✕",
                                                color = Color.Gray,
                                                modifier = Modifier
                                                    .clickable { player1Name = "" }
                                                    .padding(horizontal = 4.dp),
                                                fontSize = 14.sp
                                            )
                                        }
                                        Text(
                                            "O",
                                            color = GreenO,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            fontFamily = googleSansBold,
                                            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = GreenO
                                )
                            )

                            OutlinedTextField(
                                value = player2Name,
                                onValueChange = { player2Name = it },
                                placeholder = { Text("Second Player Name", fontFamily = googleSansRegular) },
                                trailingIcon = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (player2Name.isNotEmpty()) {
                                            Text(
                                                "✕",
                                                color = Color.Gray,
                                                modifier = Modifier
                                                    .clickable { player2Name = "" }
                                                    .padding(horizontal = 4.dp),
                                                fontSize = 14.sp
                                            )
                                        }
                                        Text(
                                            "X",
                                            color = RedX,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            fontFamily = googleSansBold,
                                            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = RedX
                                )
                            )

                            AnimatedVisibility(
                                visible = errorMessage != null,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFC9C9)
                                    )
                                ) {
                                    Text(
                                        text = errorMessage ?: "",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF4D4D),
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(12.dp),
                                        fontFamily = googleSansRegular
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    showError = true
                                    val p1 = player1Name.trim()
                                    val p2 = player2Name.trim()

                                    if (p1.isNotBlank() && p2.isNotBlank() && p1 != p2) {
                                        navController.navigate("gamepage/${player1Name.trim()}/${player2Name.trim()}")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF463FCD)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    "Start Game",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                            }

                            OutlinedButton(
                                onClick = { navController.navigate("scoretable") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                border = BorderStroke(1.5.dp, Color(0xFF463FCD)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF463FCD)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text(
                                    "Scoreboard",
                                    fontSize = 15.sp,
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

@Composable
fun PlayerBadge(symbol: String, color: Color) {
    Surface(
        shape = CircleShape,
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier.size(68.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = symbol,
                color = color,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
