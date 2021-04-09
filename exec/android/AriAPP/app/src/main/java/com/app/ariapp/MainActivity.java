package com.app.ariapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webview;

    private String url="http://j4a402.p.ssafy.io:8000";

    //녹음을 위한 사전작업
    MediaPlayer player;
    int position = 0;

    MediaRecorder recorder; //녹음하려면 필요
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //webView에 대한 셋팅
        webview = (WebView)findViewById(R.id.webView);

        //자바스크립트를 허용해주는 세팅
        webview.getSettings().setJavaScriptEnabled(true);

        //url주소를 가져오기
        webview.loadUrl(url);

        //웹을 더 쾌적하게 돌리기 위한 세팅
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClientClass());

        //서버와 통신하기 위한 Bridge => 앱이름 ARIAPP => 서버js파일에서 ARIAPP.함수이름 적으면 호출할수있다!
        webview.addJavascriptInterface(new WebBridge(),"ARIAPP");
    }

    private class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //안드로이드 OS 이전버전에서는 파라미터를 URL로 받을수있었지만
            //이제는 안전성 문제로 파라미터를 request로 받는다

            //그래서 request에서 url을 받아와서 uri로 저장하고
            Uri uri = request.getUrl();

            //string으로 url로 변환해준다
            String url = uri.toString();

            //받아온 url을 띄워주면 된다
            view.loadUrl(url);

            //true 일 때 : 한 번 로딩하면 끝
            //false 일 때 : 계속 로딩할수 있다
            return false;
        }
    }

    //웹뷰에서 이전버튼 누르면 그 전 페이지로 이동하고
    //더 이상 페이지가 없을 경우 앱 종료
    private long backBtnTime = 0;

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;
        if (webview.canGoBack()){
            webview.goBack();
        }else if (0 <= gapTime && 2000 >= gapTime){
            super.onBackPressed();
        }else{
            backBtnTime = curTime;
            Toast.makeText(this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }

    //서버 Bridge
    class WebBridge{
        @JavascriptInterface
        public void recording(String username){
            Intent intent = new Intent(getApplicationContext(),RecordActivity.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }

        @JavascriptInterface
        public void camera(String username) {
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    public boolean permissionCheck(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},1);
            return true;
        }else{
            return false;
        }
    }
}