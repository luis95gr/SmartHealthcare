package com.example.luisguzmn.healthcare40.Helo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static List<CountrySpinnerItem> getCountries(Context context) {
        List<CountrySpinnerItem> countrySpinnerItems = new ArrayList<CountrySpinnerItem>();
        try {
            InputStream in = context.getAssets().open("PrefissIntern.txt");
            BufferedReader re = new BufferedReader(new InputStreamReader(in));
            String temp;
            while ((temp = re.readLine()) != null) {
                String[] tempArr = temp.split(";");
                if (tempArr.length == 2) {
                    countrySpinnerItems.add(new CountrySpinnerItem(tempArr[0]
                            .trim(), tempArr[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countrySpinnerItems;
    }
}

