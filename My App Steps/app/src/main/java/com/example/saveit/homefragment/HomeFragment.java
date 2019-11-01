package com.example.saveit.homefragment;


import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saveit.R;
import com.example.saveit.SaveHead;
import com.example.saveit.detailsfragment.FragmentDetails;
import com.example.saveit.homefragment.chipsAdp.adapterChips;
import com.example.saveit.homefragment.desArticles.adapterNews;
import com.example.saveit.homefragment.slidercards.SliderAdapter;
import com.example.saveit.models.Category;
import com.example.saveit.offline.SaveitDatabase;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements Contract.view {
    Contract.presenter presenter;
    adapterNews adp;
    adapterChips adpChips ;
    ImageView menubuttom, addarticle;
    CardView cardmenu;
    ImageView savehead;
    ImageView addcat;
    TextView tvNoFav,tvNoSaved;
    ImageView settings;
    ImageView about;
    CardSliderLayoutManager layoutManager;
    int currentPosition;
    SharedPreferences sharedPreferences;


    //method to put the design in fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    //end

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences= getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        cardmenu=view.findViewById(R.id.cardmenu);
        menubuttom=view.findViewById(R.id.immenu);
        tvNoFav = view.findViewById(R.id.tvNoFav);
        tvNoSaved = view.findViewById(R.id.tvNoSaved);
        addarticle=view.findViewById(R.id.imaddarticle);
        savehead=view.findViewById(R.id.imhead);
        addcat=view.findViewById(R.id.imcat);
        settings=view.findViewById(R.id.imsettings);
        about=view.findViewById(R.id.imabout);
        presenter=new Presenter(this);

        initRecyclerView(view);


        ////

        savehead.setOnClickListener(v -> {
            getActivity().startService(new Intent(getActivity(), SaveHead.class));
        });

        menubuttom.setOnClickListener(v->{
            if(cardmenu.getVisibility() == View.VISIBLE){
                cardmenu.setVisibility(View.GONE);
            }else{
                cardmenu.setVisibility(View.VISIBLE);
            }
        });


        ///

        ////
        addarticle.setOnClickListener(view1 ->{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            //mnlzu2 design hun la ne2dar na3mel find view by id , check 349S
            View root =LayoutInflater.from(getContext()).inflate(R.layout.dialogue_add_article,null);
            alertDialog.setView(root);

            //349S
            EditText editText = root.findViewById(R.id.edturl);
            Spinner spinner=root.findViewById(R.id.spinner);
            //

            //mnjib l cats mn database w mnhutuun b spinner
            List<String>cats=new ArrayList<>();
            for (Category cat:getcategories()){
                cats.add(cat.name);
            }
            ArrayAdapter<String>adapter=new ArrayAdapter<>(getContext(),R.layout.spinner_layout,R.id.text,cats);
            spinner.setAdapter(adapter);
            //


            //bas ykbus 3a add yjib l article mn link w ysayva offline
            alertDialog.setPositiveButton("Add", (dialog, which) -> presenter.getArticleFromLinkAndSaveItOffline(editText.getText().toString(),getcategories().get(spinner.getSelectedItemPosition()).name));
            //

            //show l dialog
            alertDialog.show();
        });

        //method to show about dialog
        about.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            View root =LayoutInflater.from(getContext()).inflate(R.layout.dialogue_about,null);
            alertDialog.setView(root);
            alertDialog.show();
        });
        //end

        //method to settings dialog
        settings.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            View root =LayoutInflater.from(getContext()).inflate(R.layout.dialogue_setting,null);
            EditText fontsize=root.findViewById(R.id.edtFontSize);
            Spinner font= root.findViewById(R.id.spinner);
            fontsize.setText(""+sharedPreferences.getFloat("fontsize",14));

            String[] files= new String[0];
            try {
                files = getActivity().getAssets().list("fonts");
                ArrayAdapter<String>adapter=new ArrayAdapter<>(getContext(),R.layout.spinner_layout,R.id.text,files);
                font.setAdapter(adapter);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] finalFiles = files;
            alertDialog.setPositiveButton("update", (dialogInterface, i) -> {

               sharedPreferences.edit().putFloat("fontsize", Float.parseFloat(fontsize.getText().toString())).apply();

                sharedPreferences.edit().putString("fontname", finalFiles[font.getSelectedItemPosition()]).apply();
            });

            alertDialog.setView(root);
            alertDialog.show();
        });
        //end

        addcat.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            View root =LayoutInflater.from(getContext()).inflate(R.layout.dialogue_addcat,null);
            EditText catname=root.findViewById(R.id.editText);
            alertDialog.setPositiveButton("Add Category", (dialog, which) -> {
                for(Category cat: getDatabse().getcategoriesDAO().getAll()){
                    if(cat.name.equals(catname.getText().toString().toUpperCase())){
                        Toast.makeText(getContext(),"Can't add duplicate categories",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                getDatabse().getcategoriesDAO().insert(new Category(catname.getText().toString().toUpperCase()));
                adpChips.notifyDataSetChanged();
                Toast.makeText(getContext(),"Category Added",Toast.LENGTH_SHORT).show();
            });
            alertDialog.setView(root);
            alertDialog.show();
        });




        ///
        RecyclerView RecArticles=view.findViewById(R.id.RecArticle);
        RecyclerView RecChips=view.findViewById(R.id.RecChips);

        LinearLayoutManager llm= new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

         adp= new adapterNews();
        adp.presenter=presenter;
        RecArticles.setLayoutManager(llm);
        RecArticles.setAdapter(adp);

        LinearLayoutManager llm1= new LinearLayoutManager(getContext());
        llm1.setOrientation(LinearLayoutManager.HORIZONTAL);

        adpChips= new adapterChips();
        adpChips.presenter=presenter;
        RecChips.setLayoutManager(llm1);
        RecChips.setAdapter(adpChips);

    }

    // we added methods here

    //method to get and build database
    @Override
    public SaveitDatabase getDatabse() {
        return Room.databaseBuilder(getContext(),SaveitDatabase.class, "sve")
                .allowMainThreadQueries()
                .build();
    }
    //end

    //method to show toast from other thread or main thread
    @Override
    public void showMessage(String text) {
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getContext(),text, Toast.LENGTH_LONG).show();
        });
    }
    //end

    //method to refresh recycler views from other thread or main thread
    @Override
    public void refreshnews() {
        getActivity().runOnUiThread(() -> {
            adp.notifyDataSetChanged();
            adpChips.notifyDataSetChanged();

        });
    }
    //end

    //method tthat navigates us to read article fragment, we have to set id so presenter of FragmentDetails knows what article to get from db and display
    @Override
    public void gotodetails(int id) {
        FragmentDetails fragment = new FragmentDetails();
        fragment.setId(id);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();

    }
    //end

    //method to show You have no favorite articles
    @Override
    public void showNoFav() {
        tvNoFav.setVisibility(View.VISIBLE);
    }
    //end

    //method to show You have no saved articles
    @Override
    public void showNoSaved() {
        tvNoSaved.setVisibility(View.VISIBLE);
    }
    //end

    //method to show You have no saved articles
    @Override
    public List<Category> getcategories() {
        return getDatabse().getcategoriesDAO().getAll();

    }
    //end

    //method for slider from internet
    public void updateCardInformation(int pos) {
        int[] animH = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int[] animV = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }
        currentPosition = pos;
    }
    //end

    //method to initialized recycler view of slider
    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recSlider);
        RecyclerView.Adapter sliderAdapter = new SliderAdapter(presenter);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull  RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });
        layoutManager = new CardSliderLayoutManager(50,800,24);
        recyclerView.setLayoutManager(layoutManager);
        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }
    //end

    //method for slider from internet
    private void onActiveCardChange() {
        final int pos = layoutManager.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }
        updateCardInformation(pos);
    }
    //end

}
