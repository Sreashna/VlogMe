package sree.ddukk.vlogapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CameraRecordScreen(
    navController: NavController,
    viewModel: VlogViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var videoCapture by remember { mutableStateOf<VideoCapture<Recorder>?>(null) }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }
    var isRecording by remember { mutableStateOf(false) }

    // Setup Camera
    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build()
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        val capture = VideoCapture.withOutput(recorder)

        preview.setSurfaceProvider(previewView?.surfaceProvider)

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, capture)
            videoCapture = capture
        } catch (e: Exception) {
            Log.e("CameraX", "Binding failed", e)
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { ctx -> PreviewView(ctx).also { previewView = it } },
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val capture = videoCapture ?: return@Button

                if (!isRecording) {
                    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
                        .format(System.currentTimeMillis())
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/VlogMe")
                        }
                    }

                    val outputOptions = MediaStoreOutputOptions
                        .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                        .setContentValues(contentValues)
                        .build()

                    val pendingRecording = capture.output
                        .prepareRecording(context, outputOptions)
                        .apply {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                                == PackageManager.PERMISSION_GRANTED
                            ) {
                                withAudioEnabled()
                            }
                        }

                    activeRecording = pendingRecording.start(
                        ContextCompat.getMainExecutor(context)
                    ) { event ->
                        when (event) {
                            is VideoRecordEvent.Start -> isRecording = true
                            is VideoRecordEvent.Finalize -> {
                                isRecording = false
                                val uri = event.outputResults.outputUri
                                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                                navController.navigate("addDetails/${Uri.encode(uri.toString())}")
                            }
                        }
                    }
                } else {
                    activeRecording?.stop()
                    activeRecording = null
                    isRecording = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }
    }
}


