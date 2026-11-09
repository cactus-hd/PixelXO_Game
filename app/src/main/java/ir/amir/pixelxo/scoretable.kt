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
}

@Composable
fun ScoretableScreen(navController: NavController, modifier: Modifier = Modifier) {
    val vazirmatnRegular = FontFamily(Font(R.font.vazirmatn_regular))
    val vazirmatnBold = FontFamily(Font(R.font.vazirmatn_bold))
    val context = LocalContext.current

    val history = remember { mutableStateListOf<MatchRecord>() }

    LaunchedEffect(Unit) {
        history.clear()
        history.addAll(ScoreManager.getMatchHistory(context))
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                    Text(
                        text = "جدول امتیازات",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A202C),
                        fontFamily = vazirmatnBold
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { navController.popBackStack() }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "➔",
                            color = PurpleBackground,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (history.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "هنوز بازی‌ای انجام نشده",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF718096),
                            fontFamily = vazirmatnRegular
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(history) { index, item ->
                            val isLatest = index == 0

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
                                        Text(
                                            text = item.player1Name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2D3748),
                                            fontFamily = vazirmatnBold
                                        )

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
                                                fontFamily = vazirmatnBold
                                            )
                                        }

                                        Text(
                                            text = item.player2Name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2D3748),
                                            fontFamily = vazirmatnBold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = item.dateTime,
                                        fontSize = 12.sp,
                                        color = Color(0xFFA0AEC0),
                                        fontFamily = vazirmatnRegular
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
