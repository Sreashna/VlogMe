package sree.ddukk.vlogapp

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import java.io.File

class VideoRecorderHelper(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private lateinit var outputFile: File

    fun prepareRecorder(): Boolean {
        return try {
            outputFile = File(
                context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                "vlog_${System.currentTimeMillis()}.mp4"
            )

            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoSize(640, 480)
                setOutputFile(outputFile.absolutePath)
                prepare()
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun startRecording() {
        try {
            recorder?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecording(): String {
        return try {
            recorder?.stop()
            outputFile.absolutePath
        } catch (e: Exception) {
            ""
        } finally {
            recorder?.release()
            recorder = null
        }
    }

    fun release() {
        recorder?.release()
        recorder = null
    }
}
