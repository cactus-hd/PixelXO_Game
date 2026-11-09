package ir.amir.pixelxo


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.amir.pixelxo.ui.theme.GreenO
import ir.amir.pixelxo.ui.theme.PixelXOTheme
import ir.amir.pixelxo.ui.theme.PurpleBackground
import ir.amir.pixelxo.ui.theme.RedX


@Composable
fun Mainpage(modifier: Modifier = Modifier,navController: NavController) {

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = PurpleBackground
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

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "دوز",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "بازی دو نفره — ۵ دسته",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = FontFamily(Font(R.font.vazirmatn_regular))
                )

                Spacer(modifier = Modifier.height(40.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        var player1Name by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = player1Name,
                            onValueChange = { player1Name = it },
                            placeholder = {Text("نام بازیکن اول")},
                            trailingIcon = {
                                Text("O", color = GreenO, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor =  Color.Black,
                            )
                        )


                        var player2Name by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = player2Name,
                            onValueChange = { player2Name = it },
                            placeholder = {Text("نام بازیکن اول")},
                            trailingIcon = {
                                Text("X", color = RedX, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor =  Color.Black,
                            )
                        )


                        var showError by remember { mutableStateOf(false) }


                        val errorMessage: String? = when {
                            !showError -> null
                            player1Name.isBlank() -> "نام بازیکن اول را وارد کنید"
                            player2Name.isBlank() -> "نام بازیکن دوم را وارد کنید"
                            player1Name.trim() == player2Name.trim() -> "نام‌ها نباید یکسان باشند"
                            else -> null
                        }

                        AnimatedVisibility(
                            visible = errorMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Card(modifier= Modifier.fillMaxWidth().padding(4.dp)
                                .clip(RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFC9C9)
                                )
                            ){
                                Text(
                                    text = errorMessage ?: "",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF6868),
                                    fontSize = 14.sp,
                                    modifier= Modifier.padding(12.dp),
                                    fontFamily = FontFamily(Font(R.font.vazirmatn_regular))
                                )
                            }
                        }

                        Button(
                            onClick = {
                                showError = true
                                val p1 = player1Name.trim()
                                val p2 = player2Name.trim()

                                if (p1.isNotBlank() && p2.isNotBlank() && p1 != p2) {
                                    navController.navigate("gamepage/${player1Name}/${player2Name}")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleBackground),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "شروع بازی",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.vazirmatn_regular))
                            )
                        }


                        OutlinedButton(
                            onClick = {  },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            border = BorderStroke(1.dp, PurpleBackground),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleBackground),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("جدول امتیازات",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.vazirmatn_regular))
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PlayerBadge(symbol: String, color: Color) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = color,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

