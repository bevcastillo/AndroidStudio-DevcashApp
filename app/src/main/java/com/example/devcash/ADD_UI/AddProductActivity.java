package com.example.devcash.ADD_UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.devcash.R;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView prodphoto;
    TextView takephoto, choosephoto;

    private static final int PICK_IMAGE = 100;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prodphoto = (ImageView) findViewById(R.id.prod_photo);

        takephoto = (TextView) findViewById(R.id.txt_prodtakephoto);
        choosephoto = (TextView) findViewById(R.id.txt_prodchoosephoto);

        //adding listeners to the textviews
        takephoto.setOnClickListener(this);
        choosephoto.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate SAVE menu
        getMenuInflater().inflate(R.menu.savemenu, menu);
        return true;


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Unsaved changes");
        builder.setMessage("Are you sure you want to leave without saving changes?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //menu item click handling
        //if back button is clicked
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }else if(id == R.id.action_save){ //if SAVE is clicked
            Toast.makeText(this, "Product Successfully added.", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.txt_prodchoosephoto:
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
                break;
            case R.id.txt_prodtakephoto:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, 0);
                break;
        }
    }


    //handles opening the camera and gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            prodphoto.setImageURI(imageUri);
        }else{
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            prodphoto.setImageBitmap(bitmap);
        }

    }
}
