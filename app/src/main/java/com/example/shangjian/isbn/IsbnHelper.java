package com.example.shangjian.isbn;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.util.NumberUtils;

import java.util.ArrayList;

/*
This class contains public methods that are used to help update isbn widgets and functions
related to isbn.
 */
class IsbnHelper {

    //This method takes an isbn-10 code as a string. Returns whether or not it's a valid one.
    public static boolean isIsbn10(String number){
        int Sum = 0;
        if (number.length() != 10){
            return false;
        }
        if (number.charAt(9) == 'X'){
            Sum += 10;
        }

        for (int i = 10; i > 0; i--){
            Sum += Character.getNumericValue(number.charAt(Math.abs(i-10))) * i;
        }
        return Sum%11 == 0;
    }
    //This method takes an isbn-13 code as a string. Returns whether or not it's a valid one.
    public static boolean isIsbn13(String number){
        if (number.length() != 13){
            return false;
        }
        int Sum = Character.getNumericValue(number.charAt(0)) +
                Character.getNumericValue(number.charAt(2)) +
                Character.getNumericValue(number.charAt(4)) +
                Character.getNumericValue(number.charAt(6)) +
                Character.getNumericValue(number.charAt(8)) +
                Character.getNumericValue(number.charAt(10)) +
                Character.getNumericValue(number.charAt(12));
        int[] even = {Character.getNumericValue(number.charAt(1)),
                Character.getNumericValue(number.charAt(3)),
                Character.getNumericValue(number.charAt(5)),
                Character.getNumericValue(number.charAt(7)),
                Character.getNumericValue(number.charAt(9)),
                Character.getNumericValue(number.charAt(11))};

        for (int num:even){
            Sum += num*3;
        }

        return Sum%10 == 0;

    }
    /*
    This method takes an ArrayList of type string as an argument. It will iterate through the
    ArrayList and look for potential valid isbn-10 codes. It will return the first potential
    isbn-10 code found, in the form of a string. The string should contain 10 numbers. Returns
    null if nothing is found.
     */
    public static String findISBN10(ArrayList<String> blockOfTexts){
        String code;
        for (int i = 0; i < blockOfTexts.size(); i++) {
            if (blockOfTexts.get(i).toUpperCase().contains("ISBN") && blockOfTexts.get(i).replaceAll("\\s+","").substring(4).replaceAll("-","").length() == 10) {
                code =  blockOfTexts.get(i).replaceAll("\\s+","").substring(4).replaceAll("-","").replaceAll("O","0").replaceAll("b","6");
                if (NumberUtils.isNumeric(code)){
                    return code;
                }
                return null;
            }
            if (blockOfTexts.get(i).contains(":")){
                int indexOfColon = blockOfTexts.get(i).replaceAll("\\s+","").indexOf(':');
                Log.e("findISBN10 method","has ran "+ blockOfTexts.get(i));

                if (blockOfTexts.get(i).replaceAll("\\s+","").substring(indexOfColon+1).replaceAll("-","").length() == 10) {
                    code = blockOfTexts.get(i).replaceAll("\\s+","").substring(indexOfColon + 1).replaceAll("-","").replaceAll("b","6").replaceAll("O","0");
                    if (NumberUtils.isNumeric(code)){
                        return code;
                    }
                    return null;
                }
            }
        }
        return null;
    }
    /*
    This method takes an ArrayList of type string as an argument. It will iterate through the
    ArrayList and look for potential valid isbn-13 codes. It will return the first potential
    isbn-13 code found, in the form of a string. The string should contain 13 numbers. Returns
    null if nothing is found.
     */
    public static String findISBN13(ArrayList<String> blockOfTexts){
        String code;
        for (int i = 0; i < blockOfTexts.size(); i++) {
            if (blockOfTexts.get(i).toUpperCase().contains("ISBN") && blockOfTexts.get(i).replaceAll("\\s+","").substring(4).replaceAll("-","").length() == 13) {
                code = blockOfTexts.get(i).replaceAll("\\s+","").substring(4).replaceAll("-","").replaceAll("b","6").replaceAll("O","0");
                if (NumberUtils.isNumeric(code)){
                    return code;
                }
                return null;
            }
            if (blockOfTexts.get(i).contains(":")){
                int indexOfColon = blockOfTexts.get(i).replaceAll("\\s+","").indexOf(':');
                Log.e("findISBN13 method","has ran "+ blockOfTexts.get(i));

                if (blockOfTexts.get(i).replaceAll("\\s+","").substring(indexOfColon+1).replaceAll("-","").length() == 13) {
                    code = blockOfTexts.get(i).replaceAll("\\s+","").substring(indexOfColon + 1).replaceAll("-","").replaceAll("b","6").replaceAll("O","0");
                    if (NumberUtils.isNumeric(code)){
                        return code;
                    }
                    return null;
                }
            }
            if (!blockOfTexts.get(i).contains("ISBN") && blockOfTexts.get(i).replaceAll("\\s+","").length() == 13){
                code =  blockOfTexts.get(i).replaceAll("\\s+","").replaceAll("b","6");
                if (NumberUtils.isNumeric(code)){
                    return code;
                }
                return null;
            }
        }
        return null;
    }
    public static void displayISBN13(String isbn13, TextView isbn13TextView) {
        Log.e("displayISBN13 method", "METHOD HAS RAN " + isbn13);

        if (isbn13 != null) {
            isbn13TextView.setText(isbn13);
        } else {
            isbn13TextView.setText("ISBN-13 code not found.");
        }
    }

    public static void displayISBN10(String isbn10, TextView isbn10TextView) {
        Log.e("displayISBN10 method", "METHOD HAS RAN " + isbn10);

        if (isbn10 != null) {
            isbn10TextView.setText(isbn10);
        } else {
            isbn10TextView.setText("ISBN-10 code not found.");
        }
    }
    public static void setValid10Label(String isbnCode10, TextView valid10Label){
        if (isbnCode10 == null){
            return;
        }
        else if (IsbnHelper.isIsbn10(isbnCode10)){
            valid10Label.setText("Valid ISBN-10 : ");
            valid10Label.setTextColor(Color.rgb(0, 153, 0));
        }
        else{
            valid10Label.setText("Invalid ISBN-10");
            valid10Label.setTextColor(Color.RED);
        }
    }
    public static void setValid13Label(String isbnCode13 , TextView valid13Label){
        if (isbnCode13 == null){
            return;
        }
        else if (IsbnHelper.isIsbn13(isbnCode13)){
            valid13Label.setText("Valid ISBN-13 : ");
            valid13Label.setTextColor(Color.rgb(0, 153, 0));
        }
        else{
            valid13Label.setText("Invalid ISBN-13");
            valid13Label.setTextColor(Color.RED);
        }
    }

}
