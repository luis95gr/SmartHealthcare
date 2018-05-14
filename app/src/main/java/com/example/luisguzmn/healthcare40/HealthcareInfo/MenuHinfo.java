package com.example.luisguzmn.healthcare40.HealthcareInfo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.luisguzmn.healthcare40.AboutUs;
import com.example.luisguzmn.healthcare40.GlobalVars;
import com.example.luisguzmn.healthcare40.MainScreen;
import com.example.luisguzmn.healthcare40.Profile;
import com.example.luisguzmn.healthcare40.R;
import com.example.luisguzmn.healthcare40.Registros;
import com.example.luisguzmn.healthcare40.Statistics;
import com.example.luisguzmn.healthcare40.configuracion;
import com.github.chrisbanes.photoview.PhotoView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class MenuHinfo extends AppCompatActivity {
    //VARIABLES
    TextView textTitleBp,textTitleHr,textTitleEcg,textTitleBr,textTitleSteps;
    TextView textTitleDiagHr,textTitleDiagBr,textTitleDiagSteps,textTitleDiagEcg,textTitleDiagBp;
    TextView textImageSignalHr,textImageDiagHr,textImageSignalEcg;
    LottieAnimationView animationHeart;
    HorizontalScrollView horizontalScrollViewHr,horizontalScrollViewBr,horizontalScrollViewEcg,horizontalScrollViewSteps,horizontalScrollViewBp;
    boolean booleanHr = true;
    boolean booleanBr,booleanBp,booleanEcg,booleanSteps = false;
    private AdapterViewFlipper AVF;
    Animation animationUp,animationDown;
    Dialog dialog;
    int[] images = {R.drawable.bp1_tra,R.drawable.bp2_tra};
    String[] names = {"bp1","bp2"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_hinfo);
        menu();
        Typeface font = Typeface.createFromAsset(getAssets(), "Jellee-Roman.ttf");
        //CAST
        //HR
        textTitleHr = (TextView)findViewById(R.id.textTitleHr);
        textTitleDiagHr = (TextView)findViewById(R.id.textTitleDiagHr);
        textImageSignalHr = (TextView)findViewById(R.id.textImageSignalHr);
        textImageDiagHr = (TextView)findViewById(R.id.textImageDiagHr);
        horizontalScrollViewHr = (HorizontalScrollView)findViewById(R.id.horizontalScrollHr);
        //BR
        textTitleBr = (TextView)findViewById(R.id.textTitleBr);
        textTitleDiagBr = (TextView)findViewById(R.id.textTitleDiagBr);
        horizontalScrollViewBr = (HorizontalScrollView)findViewById(R.id.horizontalScrollBr);
        // ECG
        textTitleEcg = (TextView)findViewById(R.id.textTitleEcg);
        textTitleDiagEcg = (TextView)findViewById(R.id.textTitleDiagEcg);
        textImageSignalEcg = (TextView)findViewById(R.id.textImageSignalEcg);
        horizontalScrollViewEcg = (HorizontalScrollView)findViewById(R.id.horizontalScrollEcg);
        //STEPS
        textTitleSteps = (TextView)findViewById(R.id.textTitleSteps);
        textTitleDiagSteps = (TextView)findViewById(R.id.textTitleDiagSteps);
        horizontalScrollViewSteps = (HorizontalScrollView)findViewById(R.id.horizontalScrollSteps);
        //BP
        textTitleBp = (TextView)findViewById(R.id.textTitleBp);
        textTitleDiagBp = (TextView)findViewById(R.id.textTitleDiagBp);
        horizontalScrollViewBp = (HorizontalScrollView)findViewById(R.id.horizontalScrollBp);
        //
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.move_down);
        //FONTS
        textTitleHr.setTypeface(font);
        textTitleDiagHr.setTypeface(font);
        textTitleBr.setTypeface(font);
        textTitleDiagBr.setTypeface(font);
        textTitleEcg.setTypeface(font);
        textTitleDiagEcg.setTypeface(font);
        textTitleSteps.setTypeface(font);
        textTitleDiagSteps.setTypeface(font);
        textTitleBp.setTypeface(font);
        textTitleDiagBp.setTypeface(font);
        //
        textImageSignalHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTable();
            }
        });
        textImageDiagHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogHeart();
            }
        });
        textImageSignalEcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEcg();
            }
        });



        AVF = (AdapterViewFlipper)findViewById(R.id.adapterBp);
        CustomAdapterImage customAdapterImage = new CustomAdapterImage(getApplicationContext(),names,images);
        AVF.setAdapter(customAdapterImage);
        AVF.setFlipInterval(2000);
        AVF.setAutoStart(true);
    }
    //HEART RATE SCROLL
    public void Hr(View view){
        if (!booleanHr){
            //
            horizontalScrollViewBr.setVisibility(View.INVISIBLE);
            booleanBr = false;
            //
            horizontalScrollViewEcg.setVisibility(View.INVISIBLE);
            booleanEcg = false;
            //
            horizontalScrollViewSteps.setVisibility(View.INVISIBLE);
            booleanSteps = false;
            //
            horizontalScrollViewBp.setVisibility(View.INVISIBLE);
            booleanBp = false;
            //VISIBLE
            horizontalScrollViewHr.setVisibility(View.VISIBLE);
            booleanHr = true;
        }

    }
    //BREATH RATE SCROLL
    public void Br(View view){
        if (!booleanBr){
            //
            horizontalScrollViewHr.setVisibility(View.INVISIBLE);
            booleanHr = false;
            //
            horizontalScrollViewEcg.setVisibility(View.INVISIBLE);
            booleanEcg = false;
            //
            horizontalScrollViewSteps.setVisibility(View.INVISIBLE);
            booleanSteps = false;
            //
            horizontalScrollViewBp.setVisibility(View.INVISIBLE);
            booleanBp = false;
            //VISIBLE
            horizontalScrollViewBr.setVisibility(View.VISIBLE);
            booleanBr = true;
        }
    }
    //ECG SCROLL
    public void Ecg(View view){
        if (!booleanEcg){
            //
            horizontalScrollViewBr.setVisibility(View.INVISIBLE);
            booleanBr = false;
            //
            horizontalScrollViewHr.setVisibility(View.INVISIBLE);
            booleanHr = false;
            //
            horizontalScrollViewSteps.setVisibility(View.INVISIBLE);
            booleanSteps = false;
            //
            horizontalScrollViewBp.setVisibility(View.INVISIBLE);
            booleanBp = false;
            //VISIBLE
            horizontalScrollViewEcg.setVisibility(View.VISIBLE);
            booleanEcg = true;
        }

    }
    //STEPS SCROLL
    public void Steps(View view){
        if (!booleanSteps){
            //
            horizontalScrollViewBr.setVisibility(View.INVISIBLE);
            booleanBr = false;
            //
            horizontalScrollViewEcg.setVisibility(View.INVISIBLE);
            booleanEcg = false;
            //
            horizontalScrollViewHr.setVisibility(View.INVISIBLE);
            booleanHr = false;
            //
            horizontalScrollViewBp.setVisibility(View.INVISIBLE);
            booleanBp = false;
            //VISIBLE
            horizontalScrollViewSteps.setVisibility(View.VISIBLE);
            booleanSteps = true;
        }

    }
    //BLOOD SCROLL
    public void Bp(View view){
        //
        horizontalScrollViewBr.setVisibility(View.INVISIBLE);
        booleanBr = false;
        //
        horizontalScrollViewEcg.setVisibility(View.INVISIBLE);
        booleanEcg = false;
        //
        horizontalScrollViewHr.setVisibility(View.INVISIBLE);
        booleanHr = false;
        //
        horizontalScrollViewSteps.setVisibility(View.INVISIBLE);
        booleanSteps = false;
        //VISIBLE
        horizontalScrollViewBp.setVisibility(View.VISIBLE);
        booleanBp = true;

    }












    private void showDialogHeart() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_heart);
        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void showDialogTable() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_table_hr);

        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void showDialogEcg() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_ecg);
        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }


    private void menu(){
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        //MENU
        //-----------------------------------------------
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarMain);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        GlobalVars g = (GlobalVars) getApplication();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this).withCompactStyle(true)
                .addProfiles(
                        new ProfileDrawerItem().withName(sp.getString("name","no name")).
                                withEmail(sp.getString("email","no email")).withIcon("http://meddata.sytes.net/newuser/profileImg/"
                                +sp.getString("imagen", "No Image")))
                .withSelectionListEnabledForSingleProfile(false).withHeaderBackground(R.drawable.header2)
                .build();

        //Image Menu
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);

                Picasso.with(imageView.getContext()).load("http://meddata.sytes.net/newuser/profileImg/"
                        + sp.getString("imagen", "No Image"))
                        .placeholder(placeholder).into(imageView);
            }
            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_main).withIcon(GoogleMaterial.Icon.gmd_home);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile).withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_records).withIcon(GoogleMaterial.Icon.gmd_assignment);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_statistics).withIcon(GoogleMaterial.Icon.gmd_insert_chart);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_healt).withIcon(GoogleMaterial.Icon.gmd_local_hospital);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.drawer_item_settinds).withIcon(GoogleMaterial.Icon.gmd_settings);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName(R.string.drawer_item_about).withIcon(GoogleMaterial.Icon.gmd_more);

        new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar).withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        new DividerDrawerItem(),
                        item6,
                        item7
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        if (position == 2) {
                            Intent intent = new Intent(MenuHinfo.this, Profile.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 3) {
                            Intent intent = new Intent(MenuHinfo.this, Registros.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 4){
                            Intent intent = new Intent(MenuHinfo.this, Statistics.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 1){
                            Intent intent = new Intent(MenuHinfo.this, MainScreen.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 7){
                            Intent intent = new Intent(MenuHinfo.this, configuracion.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        if (position == 8){
                            Intent intent = new Intent(MenuHinfo.this, AboutUs.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }


                        return false;
                    }
                })
                .build();

        //-------------------------------------------------------------------------------------------
        //MENU

    }
}
