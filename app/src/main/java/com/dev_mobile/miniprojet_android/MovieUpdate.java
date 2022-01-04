package com.dev_mobile.miniprojet_android;

import static java.lang.String.valueOf;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MovieUpdate extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener myDateSetListener;
    ImageView calendar;
    CircleImageView image;
    TextView date, titre, genre, prix, img;
    Button submit, browse;
    DatabaseReference reference;
    String id;
    Uri filepath;
    Uri uriimg, uriimg2;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);

        uriimg = Uri.parse(getIntent().getStringExtra("image"));
        uriimg2 = Uri.parse(getIntent().getStringExtra("image"));
        browse = (Button) findViewById(R.id.browse);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(MovieUpdate.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });


        id = getIntent().getStringExtra("id");

        reference = FirebaseDatabase.getInstance().getReference("movie");

        titre = findViewById(R.id.titre);
        titre.setText(getIntent().getStringExtra("titre"));
        genre = findViewById(R.id.genre);
        genre.setText(getIntent().getStringExtra("genre"));

        prix = findViewById(R.id.prix);
        prix.setText(valueOf(getIntent().getDoubleExtra("prix", 0.0)));


        img = findViewById(R.id.uimgurl);
        //img.setText(getIntent().getStringExtra("image"));

        image = findViewById(R.id.img);
        Picasso.get().load(getIntent().getStringExtra("image")).into(image);


        submit = findViewById(R.id.usubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                uploadtofirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadtofirebase() {


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("uploader image");
        dialog.show();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference uploader = storage.getReference("Image1" + new Random().nextInt(50));

        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                dialog.dismiss();
                                uriimg2 = uri;
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference root = db.getReference("movie");
                                Movie q = new Movie(id, titre.getText().toString(), genre.getText().toString(), Double.valueOf(prix.getText().toString()), uriimg2.toString());
                                reference.child(id).setValue(q);
                                Toast.makeText(getApplicationContext(), "sucess updated ", Toast.LENGTH_LONG).show();
                                Intent j = new Intent(getApplicationContext(), Accueil.class);
                                startActivity(j);


                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        float percent = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        dialog.setMessage("upload :" + (int) percent + " %");
                    }
                });


    }


}