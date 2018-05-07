package com.example.luisguzmn.healthcare40.HealthcareInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.luisguzmn.healthcare40.R;
import com.github.chrisbanes.photoview.PhotoView;

public class CustomAdapterImage extends BaseAdapter{

    Context context;
    int[] images;
    String[] names;
    LayoutInflater inflater;

    public CustomAdapterImage(Context applicationContext, String[] names,int[] images){
        this.context = applicationContext;
        this.images = images;
        this.names = names;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.list_adapter,null);
        ImageView imageG = (ImageView) view.findViewById(R.id.imageBp);
        imageG.getLayoutParams().height = 420;
        imageG.getLayoutParams().width = 335;
        imageG.setImageResource(images[position]);
        return view;
    }
}
