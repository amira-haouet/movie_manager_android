package com.dev_mobile.miniprojet_android;

import static java.lang.Double.valueOf;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener myDateSetListener;
    ImageView calendar;
    EditText t1;
    EditText t2,t3;
    TextView date;
    Button annuler,add_submit;
    CircleImageView img;
    Button browse, upload;
    Uri filepath;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar=(ImageView)findViewById(R.id.calendar);
        date = findViewById(R.id.dater);
        Calendar today=Calendar.getInstance();
        int moi=today.get(Calendar.MONTH)+1;
        date.setText(today.get(Calendar.DAY_OF_MONTH)+"/"+moi+"/"+today.get(Calendar.YEAR));
        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        img=(CircleImageView)findViewById(R.id.img);

        browse=(Button)findViewById(R.id.browse);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
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

        add_submit=findViewById(R.id.add_submit);
        add_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtofirebase();
            }
        });


        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        myDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }});
        myDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month +=1;
                String date2 = dayOfMonth + "/" + month + "/" + year;
                date.setText(date2);
            }
        };
        annuler=findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText("");
                t2.setText("");
                t3.setText("");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
/*
    public void process(View view){

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference root=db.getReference("quad");
        String _id = root .push().getKey();
        Quad q=new Quad(_id,t1.getText().toString(),valueOf(t2.getText().toString()),date.getText().toString());
        root.child(_id).setValue(q);
        Toast.makeText(getApplicationContext(),"Inseré avec succès" ,Toast.LENGTH_SHORT).show();

    }
*/

    private void uploadtofirebase()
    {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("Téléchargeur d'image");
        dialog.show();

        t1=findViewById(R.id.t1);
        t2=findViewById(R.id.t2);
        t3=findViewById(R.id.t3);
        date = findViewById(R.id.dater);
        //roll=(EditText)findViewById(R.id.t4);


        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference uploader=storage.getReference("Image1"+new Random().nextInt(50));


        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri){

                                dialog.dismiss();
                                FirebaseDatabase db=FirebaseDatabase.getInstance();
                                DatabaseReference root=db.getReference("quad");
                                String _id = root .push().getKey();
                                Quad q=new Quad(_id,t1.getText().toString(),t3.getText().toString(),valueOf(t2.getText().toString()),date.getText().toString(),uri.toString());
                                root.child(_id).setValue(q);



                                img.setImageResource(R.drawable.sss);
                                Toast.makeText(getApplicationContext(),"Quad ajouté avec succès",Toast.LENGTH_LONG).show();
                                Intent j = new Intent(getApplicationContext(),Accueil.class);
                                startActivity(j);
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Téléchargement :"+(int)percent+" %");
                    }
                });

    }



}
