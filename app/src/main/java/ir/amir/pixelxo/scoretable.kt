package ir.amir.pixelxo

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.navigation.NavController
import ir.amir.pixelxo.ui.theme.PurpleBackground
import org.json.JSONArray
import org.json.JSONObject

data class MatchRecord(
    val player1Name: String,
    val player2Name: String,
    val player1Score: Int,
    val player2Score: Int,
    val dateTime: String
)

object ScoreManager {
    private const val PREFS_NAME = "pixelxo_prefs"
    private const val KEY_HISTORY = "history"

    fun saveMatchRecord(context: Context, record: MatchRecord) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_HISTORY, "[]") ?: "[]"
        val jsonArray = JSONArray(jsonString)

        val obj = JSONObject().apply {
            put("player1Name", record.player1Name)
            put("player2Name", record.player2Name)
            put("player1Score", record.player1Score)
            put("player2Score", record.player2Score)
            put("dateTime", record.dateTime)
        }

        val newArray = JSONArray()
        newArray.put(obj)
        for (i in 0 until jsonArray.length()) {
            newArray.put(jsonArray.get(i))
        }

        prefs.edit().putString(KEY_HISTORY, newArray.toString()).apply()
    }

    fun getMatchHistory(context: Context): List<MatchRecord> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_HISTORY, "[]") ?: "[]"
        val list = mutableListOf<MatchRecord>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    MatchRecord(
                        player1Name = obj.optString("player1Name", ""),
                        player2Name = obj.optString("player2Name", ""),
                        player1Score = obj.optInt("player1Score", 0),
                        player2Score = obj.optInt("player2Score", 0),
                        dateTime = obj.optString("dateTime", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun clearHistory(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_HISTORY).apply()
    }
}

@Composable
fun ScoretableScreen(navController: NavController, modifier: Modifier = Modifier) {
    val googleSansRegular = FontFamily(Font(R.font.googlesans_reg))
    val googleSansBold = FontFamily(Font(R.font.googlesans_bold))
    val context = LocalContext.current

    val history = remember { mutableStateListOf<MatchRecord>() }
    var showClearDialog by remember { mutableStateOf(false) }

    fun refreshHistory() {
        history.clear()
        history.addAll(ScoreManager.getMatchHistory(context))
    }

    LaunchedEffect(Unit) {
        refreshHistory()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color(0xFFF2F3F9)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { navController.popBackStack() }
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Scoreboard",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A202C),
                            fontFamily = googleSansBold
                        )
                    }

                    if (history.isNotEmpty()) {
                        TextButton(onClick = { showClearDialog = true }) {
                            Text(
                                text = "Clear",
                                color = Color(0xFFE63946),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = googleSansBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No games played yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF718096),
                            fontFamily = googleSansRegular
                        )
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PurpleBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Games",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 13.sp,
                                    fontFamily = googleSansRegular
                                )
                                Text(
                                    text = history.size.toString(),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = googleSansBold
                                )
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(history) { index, item ->
                            val isLatest = index == 0
                            val p1Winner = item.player1Score > item.player2Score
                            val p2Winner = item.player2Score > item.player1Score

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                border = if (isLatest) BorderStroke(1.5.dp, Color(0xFFFFCA28)) else null,
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isLatest) Color(0xFFFFF7DB) else Color.White
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (p1Winner) {
                                                Text(text = "👑 ", fontSize = 16.sp)
                                            }
                                            Text(
                                                text = item.player1Name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2D3748),
                                                fontFamily = googleSansBold
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = PurpleBackground,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 20.dp, vertical = 8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${item.player1Score} - ${item.player2Score}",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = googleSansBold
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = item.player2Name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2D3748),
                                                fontFamily = googleSansBold
                                            )
                                            if (p2Winner) {
                                                Text(text = " 👑", fontSize = 16.sp)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = item.dateTime,
                                        fontSize = 12.sp,
                                        color = Color(0xFFA0AEC0),
                                        fontFamily = googleSansRegular
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = {
                    Text(
                        text = "Clear History",
                        fontWeight = FontWeight.Bold,
                        fontFamily = googleSansBold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete all match history?",
                        fontFamily = googleSansRegular
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            ScoreManager.clearHistory(context)
                            refreshHistory()
                            showClearDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
                    ) {
                        Text("Clear", fontFamily = googleSansBold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showClearDialog = false }) {
                        Text("Cancel", fontFamily = googleSansRegular)
                    }
                }
            )
        }
    }
}
