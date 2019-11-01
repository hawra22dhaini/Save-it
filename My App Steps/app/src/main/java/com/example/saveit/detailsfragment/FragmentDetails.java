package com.example.saveit.detailsfragment;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveit.R;
import com.example.saveit.homefragment.HomeFragment;
import com.example.saveit.offline.SaveitDatabase;
import com.squareup.picasso.Picasso;
import com.uttampanchasara.pdfgenerator.CreatePdf;

public class FragmentDetails extends Fragment implements Contract.view {

    int id;
    Contract.presenter presenter;
    TextView tvTitle,tvLink,tvText;
    ImageView image;
    FloatingActionButton cardfav;

    //this is where we inflate the design mn layouts w mnlze2a bel fragment deyman hayde jemle mnle2e metla lama na3mel dialog, w fragments
    // mnjib design w mnhutu
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details,container,false);
    }


    //hayde l function bten3amal b3d li fu2, hek bas nelzu2 l design bisir fina na3mel find view by id, ma fina abel....
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //initialization
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        tvTitle=view.findViewById(R.id.tvTitle);
        tvLink=view.findViewById(R.id.tvLInk);
        tvText=view.findViewById(R.id.tvText);
        image =view.findViewById(R.id.imArticle);
        ImageView imdelete=view.findViewById(R.id.imdelete);
        ImageView imsave=view.findViewById(R.id.imsave);
        ImageView imshare=view.findViewById(R.id.imshare);
        ImageView imback=view.findViewById(R.id.imback);
         cardfav=view.findViewById(R.id.fabfav);
        cardfav.setOnClickListener(v->{
            presenter.favorite();
        });
        ImageView imtranslate=view.findViewById(R.id.imtranslate);
        //


        //3am njeeb l text size mn sharedpref iza msh mawjud ( case awal mara) ma bikun msayav shi bihutelna 14 l text size l defValue
        tvText.setTextSize(sharedPreferences.getFloat("fontsize",14));
        ///

        //3am njeeb l text font mn sharedpref iza msh mawjude ( case awal mara) ma bikun msayav shi bihutelna normal font li heye defValue
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/"+sharedPreferences.getString("fontname","normalFont.ttf"));
        tvText.setTypeface(typeface);
        //

        //lama nekbus back badna nerja3 na3mel inflate lal home fragment w mnhuta bel R.id.content, l R.id.content heye bel activity li2an
        //kel fragment bado hosting activity
        imback.setOnClickListener(view12 -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content,new HomeFragment()).commit();
        });
        //

        //
        presenter = new Presenter(this);
        //
        //delete mn2elo lal presenter delete w mndaher l user mn l article
        imdelete.setOnClickListener(view1 -> {
            presenter.delete();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content,new HomeFragment()).commit();

        });
        //

        imsave.setOnClickListener(view13 -> {
        presenter.savetopdf();
        });

        //mn l internet, lshare article code
        imshare.setOnClickListener(view1 -> {
            Intent i=new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            //hun mnhut shu badna na3mel share , in our case "tvLink.getText().toString()" 3am njeeb l text juwa l textview li btu3rud link
            //3a sheshe w mn3mlo share
            i.putExtra(android.content.Intent.EXTRA_SUBJECT,tvLink.getText().toString());
            i.putExtra(android.content.Intent.EXTRA_TEXT, tvLink.getText().toString());
            startActivity(Intent.createChooser(i,"Share via"));
        });

        //awal ma kabasna 3a article... hawalna mn fragment home la hun w ba3atlna id l article li kabas 3laya w hal fragment byerja3 byeb3ata
        //lal presneter krml yjib l details w yhutun krml hay shaghlet l presenter
        presenter.getarticle(id);

        imtranslate.setOnClickListener(v ->{
            presenter.translate();
        });

    }

    //hun krml bas user ykbus 3a article nkhale l home fragment teb3atlna id l kabas 3laya mn hal method
    public void setId(int id) {
        this.id = id;

    }
    //


    //bas tjeeb l article mn database yhutelna heye bel textview wel image whul...
    @Override
    public void setArticleDetails(String title, String url, String text,String mainUrl) {
        getActivity().runOnUiThread(() -> {
            tvTitle.setText(title);
            tvLink.setText(mainUrl);
            tvText.setText(text);
            Picasso.get().load(url).into(image);
        });

    }
    //

    //getdatabase la yesta5dema l presneter w le hun? krml l context , w allow main thread queries krml nee2dar nshaghel l database
    // 3al main thread krml android ma bikhalina na3mel db access 3al main thread bas hay btsir tesmahlna
    @Override
    public SaveitDatabase getDatabse() {
        return Room.databaseBuilder(getContext(),SaveitDatabase.class, "sve")
                .allowMainThreadQueries()
                .build();
    }

    //lal presenter iza bado ye3rod message b toast
    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }//


    //pdf generator li jebne mn github fina nshufu mn build.gradle(module app) b dependencies, huwe li bya3mlna l pdf
    @Override
    public CreatePdf getpdfgenerator() {
        return new CreatePdf(getContext());
    }


    //iza favorite yhuta l color ahmar iza la2 aswad....
    @Override
    public void togglefavColor(boolean fav) {
        if(fav){
            cardfav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#990000")));
        }else{
            cardfav.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
        }
    }
}
