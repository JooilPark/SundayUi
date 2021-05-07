package com.obelab.ui.ui.navstt;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.naver.speech.clientapi.SpeechRecognitionResult;
import com.obelab.ui.MainActivity;
import com.obelab.ui.R;
import com.obelab.ui.databinding.FragmentNaverSttBinding;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link naverSttFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * https://m.blog.naver.com/PostView.nhn?blogId=ndb796&logNo=221302622347&proxyReferer=https:%2F%2Fwww.google.com%2F
 */
public class naverSttFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentNaverSttBinding naverSttBinding;

    public naverSttFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment naverSttFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static naverSttFragment newInstance(String param1, String param2) {
        naverSttFragment fragment = new naverSttFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        naverSttBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_naver_stt, container, false);
        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(requireActivity(), handler, CLIENT_ID);
        naverSttBinding.upload.setOnClickListener(v -> {
            new Thread() {
                @Override
                public void run() {
                    AWSS3 S3 = new AWSS3();
                    String a = S3.upload(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest/upload.aac"));
                    Log.i(TAG , "RESULT : " + a);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            naverSttBinding.txtResult.setText(a);
                        }
                    });

                }
            }.start();

        });
        naverSttBinding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!naverRecognizer.getSpeechRecognizer().isRunning()) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    naverSttBinding.txtResult.setText("Connecting...");
                    naverSttBinding.btnStart.setText("멈춤");
                    naverRecognizer.recognize();
                } else {
                    Log.d(TAG, "stop and wait Final Result");
                    naverSttBinding.btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });
        return naverSttBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        naverRecognizer.getSpeechRecognizer().initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        mResult = "";
        naverSttBinding.txtResult.setText("");
        naverSttBinding.btnStart.setText("start");
        naverSttBinding.btnStart.setEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        naverRecognizer.getSpeechRecognizer().release();
    }

    public String makeSignature() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                    // new line
        String method = "GET";                    // method
        String url = "/photos/puppy.jpg?query1=&query2";    // url (include query string)
        String timestamp = System.currentTimeMillis() + "";            // current timestamp (epoch)
        String accessKey = "Vf67HGnHdfU341twUemg";            // access key id (from portal or Sub Account)
        String secretKey = "WuYZbtb1CmaSo5lc4LFfWIUUG73ZkLfzLKdpwzcU";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));


        return new String(Base64.encode(rawHmac, Base64.DEFAULT));
    }

    private static final String CLIENT_ID = "jspz3l9avq";
    // 1. "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    // 2. build.gradle (Module:app)에서 패키지명을 실제 개발자센터 애플리케이션 설정의 '안드로이드 앱 패키지 이름'으로 바꿔 주세요

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;
    private AudioWriterPCM writer;
    private String mResult;

    static class RecognitionHandler extends Handler {
        private final WeakReference<naverSttFragment> mActivity;

        RecognitionHandler(naverSttFragment activity) {
            mActivity = new WeakReference<naverSttFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            naverSttFragment activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                naverSttBinding.txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                naverSttBinding.txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                SpeechRecognitionResult speechRecognitionResult = (SpeechRecognitionResult) msg.obj;
                List<String> results = speechRecognitionResult.getResults();
                StringBuilder strBuf = new StringBuilder();
                for (String result : results) {
                    strBuf.append(result);
                    strBuf.append("\n");
                }
                mResult = strBuf.toString();
                naverSttBinding.txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                naverSttBinding.txtResult.setText(mResult);
                naverSttBinding.btnStart.setText("시작");
                naverSttBinding.btnStart.setEnabled(true);
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                naverSttBinding.btnStart.setText("시작");
                naverSttBinding.btnStart.setEnabled(true);
                break;
        }
    }


}