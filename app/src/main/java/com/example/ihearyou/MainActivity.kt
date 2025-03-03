package com.example.ihearyou

import android.os.Bundle
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.main)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        startListening() // Start listening automatically
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                    val spokenText = matches.firstOrNull()?.lowercase()

                    when (spokenText) {
                        "red" -> changeBackgroundColor(android.R.color.holo_red_dark, "Here is the red screen")
                        "blue" -> changeBackgroundColor(android.R.color.holo_blue_dark, "Here is the blue screen")
                    }
                }
                startListening() // Restart listening
            }

            override fun onError(error: Int) {
                Toast.makeText(this@MainActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                startListening() // Restart listening on error
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }

    private fun changeBackgroundColor(color: Int, message: String) {
        mainLayout.setBackgroundColor(ContextCompat.getColor(this, color))
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }
}
