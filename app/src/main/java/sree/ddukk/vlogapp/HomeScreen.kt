package sree.ddukk.vlogapp

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sree.ddukk.vlogapp.ui.theme.VlogAppTheme

@Composable
fun HomeScreen(
    viewModel: VlogViewModel,
    onRecordClick: () -> Unit,
    onVlogClick: (VlogEntry) -> Unit
) {
    val vlogList = viewModel.vlogList.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onRecordClick, containerColor = Color.White) {
                Text("🎥", color = Color.Black)
            }
        },
        containerColor = Color(0xFF121212) // Dark background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "🎬 Welcome to VlogMe",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Capture moments, share your vibe 🎤✨",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (vlogList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No vlogs recorded yet", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(vlogList) { vlog ->
                        VlogItem(
                            vlog = vlog,
                            onClick = { onVlogClick(vlog) },
                            onDeleteClick = { viewModel.deleteVlog(vlog) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun VlogItem(
    vlog: VlogEntry,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎞️ ${vlog.title}", style = MaterialTheme.typography.titleLarge, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))

            Text("😄 Mood: ${vlog.mood}", color = Color(0xFFBB86FC))
            Text("📅 Date: ${vlog.date}", color = Color.Gray)
            Text(
                text = "📁 ${vlog.videoPath.takeLast(25)}...",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("🗑️ Delete")
            }
        }
    }
}






