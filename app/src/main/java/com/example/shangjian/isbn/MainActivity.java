package com.example.shangjian.isbn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends Activity{
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView mImageView;
    private Button showMessageButton;
    private Button uploadImageButton;
    private ArrayList<String> blockOfTexts = new ArrayList<>();
    private String isbnCode10;
    private String isbnCode13;
    private TextView isbn10TextView;
    private TextView isbn13TextView;
    private SharedPreferences savedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = mFirebaseAnalytics.getInstance(this);
        savedPrefs = getSharedPreferences( "ISBNPrefs", MODE_PRIVATE );

        mImageView = findViewById(R.id.mImageView);
        showMessageButton = findViewById(R.id.showMessageButton);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        isbn10TextView = findViewById(R.id.isbn10TextView);
        isbn13TextView = findViewById(R.id.isbn13TextView);
        ButtonListener buttonListener = new ButtonListener();
        showMessageButton.setOnClickListener(buttonListener);
        uploadImageButton.setOnClickListener(buttonListener);
    }

    @Override
    public void onPause() {
        // Save the billAmountString and tipPercentage instance variables
        Editor prefsEditor = savedPrefs.edit();

        prefsEditor.commit();

        // Calling the parent onPause() must be done LAST
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void parseImageView(){
        Bitmap bm=((BitmapDrawable)mImageView.getDrawable()).getBitmap();
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bm);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

        Task<FirebaseVisionText> result = detector.detectInImage(image).addOnSuccessListener(new ImageParseSuccessListener());
    }

    class ImageParseSuccessListener implements OnSuccessListener<FirebaseVisionText> {
        @Override
        public void onSuccess(FirebaseVisionText firebaseVisionText) {
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                for (FirebaseVisionText.Line line : block.getLines()) {
                    Log.e("onPause() method", line.getText());
                    blockOfTexts.add(line.getText());
                }
            }
        }
    }

    private void selectImageIntent(){
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectImageIntent, "Select Picture"), 1);
    }
    @Override
    //after the camera app closes, and returns to this activity this method will be called.
    //data from the camera app will be passed in
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri extras = data.getData();
            mImageView.setImageURI(null);
            mImageView.setImageURI(extras);
            blockOfTexts.clear();
            parseImageView();
        }
    }
    class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            if (v.getId() == R.id.showMessageButton) {
                isbnCode10 = IsbnHelper.findISBN10(blockOfTexts);
                isbnCode13 = IsbnHelper.findISBN13(blockOfTexts);

                IsbnHelper.displayISBN10(isbnCode10,isbn10TextView);
                IsbnHelper.displayISBN13(isbnCode13,isbn13TextView);

                if (isbnCode10!= null && IsbnHelper.isIsbn10(isbnCode10)) {
                    startOnSearchIntent(isbnCode10);
                }
                else if (isbnCode13 != null && IsbnHelper.isIsbn13(isbnCode13)){
                    startOnSearchIntent(isbnCode13);
                }
            }
            else{
                selectImageIntent();
            }
        }
    }
    public void startOnSearchIntent(String code){
        Intent intent = new Intent(this, OnSearchActivity.class);
        intent.putExtra("IsbnCode", code);
        startActivity(intent);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


}
