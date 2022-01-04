package com.dev_mobile.miniprojet_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter<Movie,myadapter.myviewholder>
{
    Context context;

    public myadapter(@NonNull FirebaseRecyclerOptions<Movie> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull Movie m)
    {



        holder.nom.setText(m.getMarque());
        holder.modele.setText(m.getModele());
        holder.prix.setText(m.getPrix().toString());
        Glide.with(holder.img.getContext()).load(m.getPimage()).into(holder.img);



        holder.edit.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {


                Intent j = new Intent(context, MovieUpdate.class);
                j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                j.putExtra("id",m.getId());
                j.putExtra("marque",m.getMarque());
                j.putExtra("modele",m.getModele());
                j.putExtra("prix",m.getPrix());
                j.putExtra("image",m.getPimage());

                context.startActivity(j);

            }
        }
        );

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("want delete?");
                builder.setMessage("sure?");

                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Movie")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);

    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView calendar;
        CircleImageView img;
        ImageView edit,delete;
        TextView nom,prix,modele,date;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(CircleImageView)itemView.findViewById(R.id.img1);
            nom=(TextView)itemView.findViewById(R.id.nom);
            modele=(TextView)itemView.findViewById(R.id.modele);
            prix=(TextView)itemView.findViewById(R.id.prix);
            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);
           // calendar=(ImageView)itemView.findViewById(R.id.calendar);
           // date = (TextView)itemView.findViewById(R.id.dater);



        }
    }
}