package com.sunday.ui.ui.stt

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sunday.ui.R
import com.sunday.ui.databinding.FragmentSttBinding
import java.util.ArrayList

class SttFragment : Fragment() {
    lateinit var binding : FragmentSttBinding
    lateinit var intent : Intent
    lateinit var sppech: SpeechRecognizer
    var isStop : Boolean = false
    var count :Int = 0
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stt, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //퍼이션 체크
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO), 1)
        }
       intent  = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
      //  intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 60*1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 60*1000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 60*1000);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, requireActivity().packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        binding.start.setOnClickListener {
            startREC();
            isStop = false
            count = 0;
        }

        binding.Stop.setOnClickListener {
            sppech.stopListening()
            isStop = true
        }
        sppech = SpeechRecognizer.createSpeechRecognizer(activity)
        sppech.setRecognitionListener(listener)

    }
    fun startREC(){
        sppech.startListening(intent)

    }
    private val listener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle) {
            binding.status.text = "음성인식 시작하다"
        }
        override fun onBeginningOfSpeech() {
            binding.status.text = "onBeginningOfSpeech"
        }
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray) {}
        override fun onEndOfSpeech() {
            binding.status.text = "onEndOfSpeech"
            count++
            Log.i("reault" , "[" +count+"]nEndOfSpeech")
        }
        override fun onError(error: Int) {

            var result : String = when(error){
                SpeechRecognizer.ERROR_AUDIO-> "ERROR_AUDIO"
                SpeechRecognizer.ERROR_CLIENT-> "ERROR_CLIENT"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS-> "ERROR_INSUFFICIENT_PERMISSIONS"
                SpeechRecognizer.ERROR_NETWORK-> "ERROR_NETWORK"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT-> "ERROR_NETWORK_TIMEOUT"
                SpeechRecognizer.ERROR_NO_MATCH-> {
                    if(!isStop){
                        sppech.startListening(intent)
                    }
                    "ERROR_NO_MATCH"}
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY-> "ERROR_RECOGNIZER_BUSY"
                SpeechRecognizer.ERROR_SERVER-> "ERROR_SERVER"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT-> "ERROR_SPEECH_TIMEOUT"
                else -> "알수 없는 에러"
            }
            Log.i("onError" , result)
            binding.status.text = result

        }
        override fun onResults(results: Bundle) {
            val matcsa : ArrayList<String>? = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for( text in matcsa!!){
                Log.i("reault" , text)
            }
            binding.sttresult.text = matcsa.toString()
            if(!isStop){
                sppech.startListening(intent)
            }

        }
        override fun onPartialResults(partialResults: Bundle) {
            val matcsa : ArrayList<String>? = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            matcsa?.let {
                for( text in matcsa!!){
                    Log.i("reault" , "onPartialResults["+text)
                }
            }
        }
        override fun onEvent(eventType: Int, params: Bundle) {}
    }

}