package com.example.luisguzmn.healthcare40.Helo;

public class CountrySpinnerItem {
    private String code = "" ;
    private String name = "";

    public CountrySpinnerItem(String name, String code) {
        // TODO Auto-generated constructor stub
        this.name = name ;
        this.code = code ;
    }

    public String getCode() {
        return code;
    }



    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name;
    }
}

