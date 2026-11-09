package ir.amir.pixelxo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amir.pixelxo.ui.theme.GreenO
import ir.amir.pixelxo.ui.theme.RedX

@Composable
fun gamepage(modifier: Modifier = Modifier, playerone: String, playertwo: String) {
    val vazirmatnRegular = FontFamily(Font(R.font.vazirmatn_regular))
    val vazirmatnBold = FontFamily(Font(R.font.vazirmatn_bold))

    var isPlayer1Turn by remember { mutableStateOf(true) }
    val board = remember { mutableStateListOf("", "", "", "", "", "", "", "", "") }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                Spacer(modifier = Modifier.height(8.dp))


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
                            .padding(vertical = 16.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isPlayer1Turn) Color(0xFFE0F3F1) else Color.Transparent)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "O",
                                    color = GreenO,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = vazirmatnBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = playerone,
                                    color = Color(0xFF2D3748),
                                    fontSize = 14.sp,
                                    fontFamily = vazirmatnRegular
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "0",
                                    color = GreenO,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = vazirmatnBold
                                )
                            }
                        }


                        Text(
                            text = "—",
                            color = Color(0xFFA0AEC0),
                            fontSize = 20.sp,
                            fontFamily = vazirmatnRegular
                        )


                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (!isPlayer1Turn) Color(0xFFFFEBEE) else Color.Transparent)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "X",
                                    color = RedX,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = vazirmatnBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = playertwo,
                                    color = Color(0xFF2D3748),
                                    fontSize = 14.sp,
                                    fontFamily = vazirmatnRegular
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "0",
                                    color = RedX,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = vazirmatnBold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "دست 1 از 5",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4A5568),
                    fontFamily = vazirmatnBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "نوبت: ",
                        fontSize = 15.sp,
                        color = Color(0xFF4A5568),
                        fontFamily = vazirmatnRegular
                    )
                    Text(
                        text = if (isPlayer1Turn) playerone else playertwo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPlayer1Turn) GreenO else RedX,
                        fontFamily = vazirmatnBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


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

                                    val cellBg = when (cellValue) {
                                        "O" -> Color(0xFFFFCA28)
                                        "X" -> RedX
                                        else -> Color(0xFFF4F5FB)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(cellBg)
                                            .clickable {
                                                if (cellValue.isEmpty()) {
                                                    board[index] = if (isPlayer1Turn) "O" else "X"
                                                    isPlayer1Turn = !isPlayer1Turn
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (cellValue.isNotEmpty()) {
                                            Text(
                                                text = cellValue,
                                                fontSize = 40.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontFamily = vazirmatnBold
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
    }
}
