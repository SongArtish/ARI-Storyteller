package com.app.ariapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class CameraActivity extends Activity implements AutoPermissionsListener {

    private static Intent intent;
    ImageView imageView;
    String mCurrentPhotoPath;
    File file;
    String filename;
    String book_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //인텐트로 넘어온 유저이름을 string타입으로 저장 -> 후에 post로 보내줌
        Intent intent = getIntent();
        String name = intent.getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        //xml 요소들 등록
        ImageView camera = findViewById(R.id.Camera); //촬영 이미지
        Button finish = findViewById(R.id.Finish); //이미지 전송 버튼

        TextView shot = findViewById(R.id.bookshot); //촬영안내 메시지
        TextView reshot = findViewById(R.id.ReShot); //재촬영 안내 메시지

        EditText bookname = findViewById(R.id.BOOKNAME); //책 제목 입력 받는부분
        TextView input = findViewById(R.id.input); //책 제목 입력 안내
        Button confirm = findViewById(R.id.Enter); //책 제목 입력 끝

        //confirm 버튼 클릭 시 event
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                book_name = bookname.getText().toString(); //입력받은 텍스트를 변수에 저장
                if (!book_name.equals("")){
                    bookname.setVisibility(View.GONE);
                    input.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                    camera.setVisibility(View.VISIBLE);
                    shot.setVisibility(View.VISIBLE);
                }
            }
        });

        //camera 버튼 클릭 시 event
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera(); //startCamera 함수 실행
                shot.setVisibility(View.GONE);
                reshot.setVisibility(View.VISIBLE);
                finish.setVisibility(View.VISIBLE);
            }
        });

        //finish 버튼 클릭 시 event
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image = new File(filename); //image라는 파일 생성
                goSend(image,name,book_name); //goSend함수 실행(파일, 유저이름, 입력받은책이름)
            }
        });

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath(); //변수에 환경경로 저장
        String pattern = "yyyyMMdd"; //패턴등록
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern); //등록한 패턴에 맞게 날짜 정보 저장
        String date = simpleDateFormat.format(new Date());
        filename = sdcard+File.separator+date+".jpg"; //저장할 파일이름을 변수에 저장
        file = new File(filename); //파일 생성
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    public void startCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //인텐트를 처리할 카메라 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            //촬영한 사진을 저장할 파일생성
            File photoFile = null;

            try{
                String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
                String pattern = "yyyyMMdd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                filename = sdcard+File.separator+date+".jpg";
                File tempImage = new File(filename);

                //ACTION_VIEW 인텐트를 사용할 경로(임시파일의 경로)
                mCurrentPhotoPath = tempImage.getAbsolutePath();

                photoFile = tempImage;
            } catch (Exception e){

                //에러 로그
                Log.w(TAG,"파일생성에러!",e);
            }

            Uri photoURI = null;

            //파일이 정상적으로 생성되었다면 계속 진행
            if (photoFile!=null){

                //Uri가져오기
                photoURI = FileProvider.getUriForFile(this,getPackageName()+".fileprovider",photoFile);
                Log.d(TAG,photoURI.toString());
                
                //인텐트에 Uri담기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);

                //인텐트 실행
                startActivityForResult(takePictureIntent,10);

            }
        }
    }

    //서버에 방금찍은 사진 전송
    public void goSend(File image,String name,String book_name){
        //requestBody 생성 => Form에 책 제목, 유저 이름, 파일을 넣어서 build
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("myBooks",book_name)
                .addFormDataPart("user_name",name)
                .addFormDataPart("files",file.getName(),RequestBody.create(MultipartBody.FORM,file))
                .build();

        //post방식으로 이미지파일 전송
        Request request = new Request.Builder()
                .url("http://j4a402.p.ssafy.io:8000/ic/android/")
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
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101 && resultCode==Activity.RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=8;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            imageView.setImageBitmap(bitmap);
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
