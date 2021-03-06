package com.example.marco;

/**
 * Created by Walber Rodrigues on 10/11/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

import base.Local;

public class CreateViagemAdapter extends RecyclerView.Adapter<CreateViagemAdapter.ContactViewHolder> implements Serializable{

    private ArrayList<Local> contactList;

    public CreateViagemAdapter(ArrayList<Local> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {

        Local ci = contactList.get(i);

        contactViewHolder.vNome.setText(ci.getName());
        contactViewHolder.vDesc.setText(ci.getDescription());
        contactViewHolder.vCategorias.setText(ci.getType());

        // Formatando para exibir Schedule como horário
        float time = Float.valueOf(ci.getHorario());
        int hours = (int) time;
        int minutes = (int) (60 * (time - hours));
        contactViewHolder.vSchedule.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes) + " -");

        // Formatando para exibir timeSpend como tempo
        double duration = Double.valueOf(ci.getTimespend());
        hours = (int) duration;
        minutes = (int) (60 * (duration - hours));
        String str_duration = "Tempo Estimado: ";
        if (hours > 0) {
            str_duration += (String.valueOf(hours) + "h");
        }
        if (minutes > 0) {
            str_duration += (String.valueOf(minutes) + "m");
        }
        contactViewHolder.vTimeSpend.setText(str_duration);



        // TODO: Trocar para o general_category
        String cat = ci.getGeneral_category();
        switch (cat) {
            case "food":
                contactViewHolder.vHead.setBackgroundColor(Color.parseColor("#f9524c"));
                break;
            case "outdoor":
                contactViewHolder.vHead.setBackgroundColor(Color.parseColor("#658e74"));
                break;
            case "shop":
                contactViewHolder.vHead.setBackgroundColor(Color.parseColor("#7d5bb2"));
                break;
            case "indoor":
                contactViewHolder.vHead.setBackgroundColor(Color.parseColor("#ffb14d"));
                break;

            default:
                contactViewHolder.vHead.setBackgroundColor(Color.parseColor("#000000"));
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(ci.getImage());

        final long tamanhoMax = 500 * 1024;
        storageRef.getBytes(tamanhoMax).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                contactViewHolder.vImagem.setImageBitmap(bm);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

//        contactViewHolder.vNome.setText(ci.nome + " " + ci.desc);
    }


    public ArrayList<Local> getContactList() {
        return contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected ImageView vImagem;
        protected TextView vNome;
        protected TextView vDesc;
        protected TextView vSchedule;
        protected TextView vCategorias;
        protected TextView vTimeSpend;
        protected LinearLayout vHead;

        public ContactViewHolder(View v) {
            super(v);
            vImagem = (ImageView) v.findViewById(R.id.imagem);
            vNome =  (TextView) v.findViewById(R.id.txtNome);
            vDesc = (TextView)  v.findViewById(R.id.txtDesc);
            vSchedule = (TextView)  v.findViewById(R.id.txtSchedule);
            vCategorias = (TextView) v.findViewById(R.id.txtCategoria);
            vTimeSpend = (TextView) v.findViewById(R.id.txtTimeSpend);
            vHead = (LinearLayout) v.findViewById(R.id.head);
        }
    }
}