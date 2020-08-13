package com.example.fish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private ResultDialog resultDialog;
    private NoResultDialog noResultDialog;
    private CustomDialog dialog;
    private CustomDialogFirst dialog_first;
    private CustomDialogSecond dialog_second;
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰


    private final static int PICK_IMAGE = 1;
    private final static int CAPTURE_IMAGE = 10;

    boolean result=false;
    float max=0;
    int maxIndex=0;
    float secondMax=0;
    int secondMaxIndex=0;
    float thirdMax=0;
    int thirdMaxIndex=0;

    String[] fishname={"고등어", "쏨뱅이", "벵어돔", "범돔","용치놀래기" ,"참돔", "줄전갱이" ,"감성돔","농어","까나리"};
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_result:
                        ResultDialog();
                        break;
                    case R.id.action_gallery:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, PICK_IMAGE);
                        break;
                    case R.id.action_camera:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAPTURE_IMAGE);
                        break;
                    case R.id.action_manual:
                        Dialog();
                        break;
                }
                return true;
            }
        });

    }

    public void ResultDialog(){
        if(result==false)
        {
            noResultDialog = new NoResultDialog(MainActivity.this,"물고기 사진을 넣어주세요",noResultLeftListener);
            noResultDialog.setCancelable(true);
            noResultDialog.getWindow().setGravity(Gravity.CENTER);
            noResultDialog.show();
        }
        else
        {
            resultDialog = new ResultDialog(MainActivity.this,fishname[maxIndex],max,fishname[secondMaxIndex],secondMax,fishname[thirdMaxIndex],thirdMax,resultLeftListener);
            resultDialog.setCancelable(true);
            resultDialog.getWindow().setGravity(Gravity.CENTER);
            resultDialog.show();
        }
    }
    public void Dialog(){
        dialog = new CustomDialog(MainActivity.this,
                "다나까 어플은 총 10가지의 \n물고기를 분류할 수 있습니다.\n\n현재 분류 가능한물고기\n\n● 고등어\n● 쏨뱅이\n● 벵어돔\n● 범돔\n● 용치놀래기\n● 참돔\n● 줄전갱이\n● 감성돔\n● 농어\n● 까나리", // 제목
                leftListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }
        public void DialogFirst(){
        dialog_first = new CustomDialogFirst(MainActivity.this,
                "1. 물고기의 옆면이 나오도록\n 사진을 찍어주세요", // 제목
                leftListenerFirst); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog_first.setCancelable(true);
        dialog_first.getWindow().setGravity(Gravity.CENTER);
        dialog_first.show();
    }
    public void DialogSecond(){
        dialog_second = new CustomDialogSecond(MainActivity.this,
                "2. 물고기가 화면 전체에 꽉차도록\n 사진을 찍어주세요\n(배경이 없을수록 좋습니다)", // 제목
                leftListenerSecond); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog_second.setCancelable(true);
        dialog_second.getWindow().setGravity(Gravity.CENTER);
        dialog_second.show();
    }
    //다이얼로그 클릭이벤트
    private View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
            DialogFirst();
        }
    };
    private View.OnClickListener leftListenerFirst = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_first.dismiss();
            DialogSecond();
        }
    };
    private View.OnClickListener leftListenerSecond = new View.OnClickListener() {
        public void onClick(View v) {
            dialog_second.dismiss();
        }
    };
    private View.OnClickListener resultLeftListener = new View.OnClickListener() {
        public void onClick(View v) {
           resultDialog.dismiss();
        }
    };
    private View.OnClickListener noResultLeftListener = new View.OnClickListener() {
        public void onClick(View v) {
            noResultDialog.dismiss();
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        Bitmap bmp;
        try {
            // 선택한 이미지에서 비트맵 생성
            if (requestCode == PICK_IMAGE) {
                InputStream stream = getContentResolver().openInputStream(data.getData());
                bmp = BitmapFactory.decodeStream(stream);
                stream.close();
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bmp);

            } else {
                ImageView imageView = findViewById(R.id.imageView);
                bmp = (Bitmap) data.getExtras().get("data");
                if (bmp != null) {
                    imageView.setImageBitmap(bmp);
                }

            }
            int cx = 150, cy = 150;
            Bitmap bitmap = Bitmap.createScaledBitmap(bmp, cx, cy, false);
            int[] pixels = new int[cx * cy];
            bitmap.getPixels(pixels, 0, cx, 0, 0, cx, cy);
            ByteBuffer input_img = getInputImage_2(pixels, cx, cy);

            GpuDelegate delegate = new GpuDelegate();
            Interpreter.Options options = (new Interpreter.Options()).addDelegate(delegate);
            Interpreter tf_lite = getTfliteInterpreter("gputes0517.tflite",options);

            float[][] pred = new float[1][CAPTURE_IMAGE];
            tf_lite.run(input_img, pred);
            delegate.close();

            max=0;
            maxIndex=0;
            secondMax=0;
            secondMaxIndex=0;
            thirdMax=0;
            thirdMaxIndex=0;

            for(int i=0;i<CAPTURE_IMAGE;i++)
            {
                pred[0][i]*=10000;
                pred[0][i]=(float)Math.floor(pred[0][i])/(float)100.0; // 소수 둘째 자리에서 버림
                if(max<=pred[0][i]) {
                    max = pred[0][i];
                    maxIndex=i;
                }
            }
            for(int i=0;i<CAPTURE_IMAGE;i++)
            {
                if(max>=pred[0][i]&&secondMax<=pred[0][i]&&maxIndex!=i) {
                    secondMax = pred[0][i];
                    secondMaxIndex=i;
                }
            }
            for(int i=0;i<CAPTURE_IMAGE;i++)
            {
                if(secondMax>=pred[0][i]&&thirdMax<=pred[0][i]&&maxIndex!=i&&secondMaxIndex!=i) {
                    thirdMax = pred[0][i];
                    thirdMaxIndex=i;
                }
            }

            result=true;
            TextView textView=findViewById(R.id.textView);
            String str=fishname[maxIndex]+"(으)로 "+ max+"%의 신뢰도로 예측하였습니다";
            //출력 텍스트 색상 변경
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 0, fishname[maxIndex].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 5+fishname[maxIndex].length(), fishname[maxIndex].length()+12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(ssb);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private Interpreter getTfliteInterpreter(String modelPath,Interpreter.Options options) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath),options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws
            IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }



    private ByteBuffer getInputImage_2(int[] pixels, int cx, int cy) {
        ByteBuffer input_img = ByteBuffer.allocateDirect(cx * cy * 3 * 4);
        input_img.order(ByteOrder.nativeOrder());

        for (int i = 0; i < cx * cy; i++) {
            int pixel = pixels[i];        // ARGB : ff4e2a2a

            input_img.putFloat(((pixel >> 0) & 0xff) / (float) 255);
            input_img.putFloat(((pixel >> 8) & 0xff) / (float) 255);
            input_img.putFloat(((pixel >> 16) & 0xff) / (float) 255);
        }
        return input_img;
    }
}

