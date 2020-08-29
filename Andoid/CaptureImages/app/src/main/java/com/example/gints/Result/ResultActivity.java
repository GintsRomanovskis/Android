package com.example.gints.Result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gints.ImageUtils.ImageUtils;
import com.example.gints.captureimages.Classifier;
import com.example.gints.captureimages.R;
import com.example.gints.captureimages.cantRecognize;
import com.example.gints.database.connection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    String imagePath;

    //Objects
    ImageUtils imgUtils = new ImageUtils();
    connection cn = new connection(this);

   // private ImageView imageViewTaken;
    private ImageView imageViewOrginal;
    private TextView textViewNosaukums;
    private TextView textViewValsts;
    private TextView textViewTips;
    private TextView textViewApraksts;
    private BitmapFactory.Options boptions;
    private Matrix matrix;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

       // imageViewTaken = (ImageView) findViewById(R.id.imageViewTaken);
        imageViewOrginal = (ImageView) findViewById(R.id.imageViewOrginal);
        textViewNosaukums = (TextView) findViewById(R.id.textViewNosaukums);
        textViewValsts = (TextView) findViewById(R.id.textViewValsts);
        textViewTips = (TextView) findViewById(R.id.textViewTips);
        textViewApraksts = (TextView) findViewById(R.id.textViewApraksts);

        //Data from main activity
        Intent intent = getIntent();

        imagePath = intent.getStringExtra("imagePath");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        String resultList = getIntent().getStringExtra("resultList");
        float resultPercentage = getIntent().getFloatExtra("resultPercentage",0f);





           // setPic(imagePath, bitmap,imageViewTaken);
            setPic(imagePath, bitmap,imageViewOrginal);
            showResult(resultList,resultPercentage);


    }


    private void setPic(String currentPhotoPath,Bitmap bitmap,ImageView imageView) {


        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;


        if(bitmap == null && currentPhotoPath != null) {
          matrix = imgUtils.getRotation(currentPhotoPath,bitmap);

            BitmapFactory.decodeFile(currentPhotoPath, boptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;


            // Determine how much to scale down the image
            // int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            // bmOptions.inSampleSize = scaleFactor;
            Bitmap bitmapFromCamera = BitmapFactory.decodeFile(currentPhotoPath, boptions);
            imageView.setImageBitmap(Bitmap.createBitmap(bitmapFromCamera, 0, 0, bitmapFromCamera.getWidth(), bitmapFromCamera.getHeight(), matrix, true));

        }else {

           imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true));
        }
    }




    private void showResult(String result,float precentage) {
        ArrayList<String> array_list = new ArrayList<String>();
        array_list = cn.getAllDataByColumName("kods",result);

        if (precentage*100 > 50 ) {
            textViewNosaukums.setText(array_list.get(0));
            textViewValsts.setText(array_list.get(1));
            textViewTips.setText(array_list.get(2));
            textViewApraksts.setText(array_list.get(3));
        }
        else {
            Intent openCantRecognizeActivity = new Intent(this, cantRecognize.class);
            startActivity(openCantRecognizeActivity);
        }


    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public Bitmap getBitmapFromAssets(String fileName) throws IOException {
        AssetManager assetManager = getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }
}
