package com.example.gints.captureimages;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import androidx.core.content.FileProvider;
import 	androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gints.ImageUtils.ImageUtils;
import com.example.gints.ImageUtils.TextUtils;
import com.example.gints.Result.ResultActivity;
import com.example.gints.database.connection;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int IMAGE_PICKER_SELECT = 1;


    //Objects
    //TensorFlowImageClassifier tensorFlowImageClass = new TensorFlowImageClassifier();
    Constants constants = new Constants();
    ImageUtils imgUtils = new ImageUtils();
    connection cn = new connection(this);
    TextUtils  textUtil = new TextUtils();


    String currentPhotoPath;
    private Executor executor = Executors.newSingleThreadExecutor();
    private List <Classifier.Recognition> results = null;

    //Components
    private ImageView imageView;
    private TextView resultView;
    private Bitmap bitmap;
    private TextView logout;
    Bitmap bitmapImage;
    private InputStream imageStream;
    Matrix matrix;
    Bitmap newImage;
    Bitmap croppedBitmap;

    private Classifier classifier;
    private BitmapFactory.Options boptions;

    //private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize components
        Button captureImagesBtn = (Button) findViewById(R.id.captureImagesBtn);
        logout = findViewById(R.id.logoutTextView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent openLoginActivity = new Intent(MainActivity.this, com.example.gints.captureimages.loginActivity.class);
                startActivity(openLoginActivity);

            }
        });

        //Disable the button if the user has no camera
        if (!hasCamera())
            captureImagesBtn.setEnabled(false);

        initTensorFlowAndLoadModel();
      if (cn.numberOfRows() <= 0) {
          cn.insert_leaf("Ozols", "Augi", "Segsēkļi", "Parastais ozols (latīņu: Quercus robur) ir dižskābaržu dzimtas ozolu ģints koku suga, kas ir vienīgā šīs ģints suga, kura Latvijā aug savvaļā.Ir arī selekcionētas vairākas dekoratīvas apstādījumu šķirnes. Parastais ozols izaug liels koks ar vērtīgu koksni, augļi — rieksti, kurus sauc arī par zīlēm jeb ozolzīlēm. Zīles senāk cilvēki izmantoja pārtikā (gatavoja ozolzīļu kafiju), kā arī tās ir nozīmīgs barības avots dažādiem dzīvniekiem. Latvijā ir izdīgušas un turpina augt 1200 gadu vecas ozolzīles. Lielākais koksnes blīvums starp Eiropas ozoliem ir Latvijā.Tādēļ daudzu Eiropas viduslaiku mākslinieku gleznām kā pamats ir izmantots tieši ozolkoks no Latvijas.", "OZOLS");
          cn.insert_leaf("Smiltsērkšķis", "Augi", "Segsēkļi", "Pabērzu smiltsērkšķis (Hippophaë rhamnoides L.) ir eleagnu dzimtas divmāju krūmu vai koku augu suga. Šī suga ir sastopama Eirāzijas mērenajā joslā. Tas ir vērtīgs ārstniecības un krāšņumaugs. Pabērzu smiltsērkšķis bija minēts arī seno grieķu zinātnieku un rakstnieku darbos.", "SMILSUERKSKIS");
          cn.insert_leaf("Vītols", "Augi", "Segsēkļi", "Vītoli (Salix) ir vītolu dzimtas ģints. Šajā ģintī ietilpst ne tikai vītolu sugas, bet arī kārkli. Vairums sugu aug ziemeļu puslodes mērenajā joslā. Tikai neliels sugu skaits aug tropu un arktiskajā joslā. Vītolu ģints sugas bieži aug upju vai ezeru krastos.", "VITOLS");
          cn.insert_leaf("Mežrozīte", "Augi", "Segsēkļi", "Vidēji liels (ga 1.5-4 m) rožu dzimtas krūms. Pieder rožu grupai, kam kauslapas ar sānu piedevām, bet lapas pilnīgi kailas vai apmatots tikai kāts un dzīslojums. Krūms stāvs, zari un jaunie dzinumi stāvi, izlocīti vai lokveida. Miza brūna, uz jaunajiem dzinumiem zaļgana. Dzeloņi uz stumbra spēcīgi, slaidi, mazliet noliekti. Pielapes šauras. Lapas nepāra plūksnaini saliktas no 5 vai 7 iegareni ovālām vai lancetiskām lapiņām (ga 2-4 cm, pl 1-2 cm). ", "MEZROZITE");
          cn.insert_leaf("Apse", "Augi", "Segsēkļi", "Apse ir koks, sasniedz līdz 40 m augstumu un 1 m stumbra diametru. Mūža ilgums 100-180 gadi, reti vairāk. Miza gluda, pelēka, vēlāk (no ap 50 g. vecuma) tumšāka un plaisaina. Apsei ir spēcīga sakņu sistēma, kas sniedzas pat 20 — 30 m aiz vainaga robežām. Vainags skrajš. Zari trausli. Lapu virspuse ir zaļa, apakšpuse gaišāka", "APSE");
          cn.insert_leaf("Avene", "Augi", "Segsēkļi", "Meža avene (Rubus idaeus) ir vidēja lieluma, rožu dzimtas krūms. Stumbrs stāvs, apakšdaļā koksnains, zaro. Uz stumbra un zariem tievi dzeloņi. Pirmā gada dzinumi neziedoši, otrā gada ir ziedoši. Uz ziedošajiem dzinumiem lapas staraini saliktas no 3 lapiņām, uz neziedošajiem no piektās lapiņas iegareni olveidīgas, plātnes virspuse gandrīz kaila, apakšpuse pelēkbalta, tūbaina, gals smails, malas zobainas. Kauslapas paliek pie augļa, noziedot atliecas. Vainaglapas baltas, garākas nekā kauslapas. Auglis ir sārti sarkanā krāsā. Zied maijā, jūnijā. Augļi nogatavojas jūlija beigās, augustā.", "AVENE");
      }
        //Ja nepieciešams izdzēš datu bāzi
        // this.deleteDatabase("/data/user/0/com.example.gints.captureimages/databases/LeafDb.db");
        // Get the TextView by id

    }

    //Check if the user has a camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Launching the camera
    public void launchCamera(View view) {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dispatchTakePictureIntent();
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
       // currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && currentPhotoPath != null) {
            classifyPhoto(currentPhotoPath, null);
            currentPhotoPath = "";

        } else if (requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {

                try {
                    imageStream = getContentResolver().openInputStream(selectedMediaUri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                classifyPhoto("", selectedImage);
            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    //inttilizing model
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            constants.MODEL_FILE,
                            constants.LABEL_FILE,
                            constants.INPUT_SIZE,
                            constants.IMAGE_MEAN,
                            constants.IMAGE_STD,
                            constants.INPUT_NAME,
                            constants.OUTPUT_NAME);

                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });


    }

    public void classifyAndShowResult(Bitmap cropedBitmap) {

        results = classifier.recognizeImage(cropedBitmap);

        System.out.print("d");
        openResultActivity(results);
    }

    public void classifyPhoto(String currentPhotoPath,Bitmap bitmap) {


        if(currentPhotoPath != "") {
             bitmapImage = BitmapFactory.decodeFile(currentPhotoPath);
             matrix = imgUtils.getRotation(currentPhotoPath,bitmap);
             newImage = Bitmap.createBitmap(bitmapImage, 0, 0, bitmapImage.getWidth(), bitmapImage.getHeight(), matrix, true);
        }
        else{
           newImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix, true);
        }


          croppedBitmap = imgUtils.resizeImage(newImage, constants.INPUT_SIZE);
         classifyAndShowResult(croppedBitmap);

        }

    public void openResultActivity(List <Classifier.Recognition> results) {

        //results.toArray();
        //Reziltata konvertēsāana, lai būtu bez atstarpēm un visi lielie

        String result  =  textUtil.capitalString((textUtil.removeSapaces(results.get(0).getTitle())));
        float resultPercentage  =  results.get(0).getConfidence();
        Intent openResultActivity = new Intent(this, ResultActivity.class);
        openResultActivity.putExtra("imagePath", currentPhotoPath);
        openResultActivity.putExtra("resultList",result );
        openResultActivity.putExtra("resultPercentage",resultPercentage );
        openResultActivity.putExtra("BitmapImage", croppedBitmap);

        startActivity(openResultActivity);


    }


    public void openGalerry(View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/* video/*");
        startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);

    }



}

