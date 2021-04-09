package com.app.ariapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordActivity extends Activity implements AutoPermissionsListener{
    private static Intent intent ;
    MediaRecorder recorder;
    MediaPlayer player;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //인텐트에서 받아온 유저이름을 변수에 저장
        Intent intent = getIntent();
        String name = intent.getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //xml 요소들 등록
        Button SendVoice = findViewById(R.id.SendVoice);
        ImageView Mic = findViewById(R.id.Mic);
        ImageView Stop = findViewById(R.id.Stop);
        ImageView ReMic = findViewById(R.id.ReMic);

        //Mic 클릭 시 event
        Mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(); //startRecording 함수 실행
                Mic.setVisibility(View.GONE);
                Stop.setVisibility(View.VISIBLE);
            }
        });

        //Stop 클릭 시 event
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording(); //stopRecording 함수 실행
                ReMic.setVisibility(View.VISIBLE);
                Stop.setVisibility(View.GONE);
                SendVoice.setVisibility(View.VISIBLE);
            }
        });

        //ReMic 클릭 시 event
        ReMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(); //startRecording 함수 실행
                ReMic.setVisibility(View.GONE);
                Stop.setVisibility(View.VISIBLE);
                SendVoice.setVisibility(View.GONE);
            }
        });

        //SendVoice 클릭 시 event
        SendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filename); //파일 생성
                goSend(file,name); //goSend 함수 실행
                finish();
            }
        });

        //녹음파일 저장될 루트경로 찾기
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

        //날짜 저장 패턴
        String pattern = "yyyyMMdd";
        int num = (int)(Math.random()*100);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        //음성파일명 지정
        filename = sdcard + File.separator + date+ "_"+num+".mp4";

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    //녹음시작
    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(filename);

        try {
            //녹음 하기 전에 prepare과정 필수
            recorder.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //녹음 시작
        recorder.start();
    }

    //녹음중지
    public void stopRecording() {
        if (recorder == null) {
            return;
        }

        recorder.stop();
        recorder.release();
        recorder = null;

        ContentValues values = new ContentValues(10);

        values.put(MediaStore.MediaColumns.TITLE, "Recorded");
        values.put(MediaStore.Audio.Media.ALBUM, "Audio Album");
        values.put(MediaStore.Audio.Media.ARTIST, "Mike");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, "Recorded Audio");
        values.put(MediaStore.Audio.Media.IS_RINGTONE, 1);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        values.put(MediaStore.MediaColumns.DATE_ADDED,
                System.currentTimeMillis()/1000);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4");
        values.put(MediaStore.Audio.Media.DATA, filename);

        //오디오 파일 uri저장
        Uri audioUri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        if (audioUri == null) {
            Log.d("SampleAudioRecorder", "Audio insert failed.");
            return;
        }
    }

    //음성파일 서버로 전송
    public static void goSend(File file, String name){
        //requestBody 생성 => 목소리 주인, 음성파일
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("speaker_name",name)
                .addFormDataPart("files",file.getName(),RequestBody.create(MultipartBody.FORM,file))
                .build();

        //post방식으로 음성파일 전송
        Request request = new Request.Builder()
                .url("http://j4a402.p.ssafy.io:8000/record/")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //e.printStackTrace();
                System.out.println("FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("TEST : ",response.body().string());
            }
        });
    }

    private void killMediaPlayer() {
        if (player != null) {
            try {
                player.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
    }
}