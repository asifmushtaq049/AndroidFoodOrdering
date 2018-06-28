package com.uog.smartcafe.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uog.smartcafe.LoginOptionActivity;
import com.uog.smartcafe.MainActivity;
import com.uog.smartcafe.entities.LoginObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static final String PUBLIC_FOLDER = "https://ktutorials.com/restaurant/public/";
    public static final String PATH_TO_SERVER_LOGIN = PUBLIC_FOLDER + "signin";
    public static final String PATH_TO_SERVER_REGISTRATION = PUBLIC_FOLDER + "register";
    public static final String PATH_TO_RESTAURANT_HOME = PUBLIC_FOLDER + "mobilerestuarant";
    public static final String PATH_TO_MENU = PUBLIC_FOLDER + "mobilemenu";
    public static final String PATH_TO_ORDER_HISTORY = PUBLIC_FOLDER + "mobileorderhistory";
    public static final String PATH_TO_HOT_DEALS = PUBLIC_FOLDER + "mobilehotdeal";
    public static final String PATH_TO_EDIT_USER = PUBLIC_FOLDER + "mobileuseredit";
    public static final String PATH_TO_MENU_ITEM_BY_CATEGORY = PUBLIC_FOLDER + "mobilemenuitem";
    public static final String PATH_TO_PLACE_ORDER = PUBLIC_FOLDER + "placeorder";
    public static final String PATH_TO_ALL_ORDER = PUBLIC_FOLDER + "allorders";
    public static final String PATH_TO_SEND_COMPLAINT = PUBLIC_FOLDER + "placecomplaint";


    public static final String NEW_ACCOUNT = "Don't have an account yet?";
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;

    public static final String ID = "USER_ID";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";
    public static final String ADDRESS = "ADDRESS";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String CUSTOMER_TYPE = "CUSTOMER_TYPE";
    public static final String MENU_ID = "MENU_ID";

    public static final String USER_DATA = "USER_DATA";
    public static final String CART = "CART";
    public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    public static final String CREDIT_CARD = "CREDIT_CARD";
    public static final String RESTAURANT = "RESTAURANT";
    public static final String SHARED_PREF = "SHARED_PREFERENCE";

    public static final String CLIENT_ID = "";

    public static final int MINIMUM_LENGTH = 5;
    public static final int MINIMUM_PHONE = 11;


    public static void displayErrorMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(String email){
        if(email.contains("@uog.edu.pk"))
            return true;
        else if(email.contains("@UOG.EDU.PK"))
            return true;

        return false;
    }

    public static boolean isValidPhone(String phone){
        if(phone.startsWith("03"))
            return true;

        return false;
    }

    public static String dateFormatting(String dateInString){
        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date stringDate = null;
        try {
            stringDate = sdfDestination.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("dd MM, yyyy", Locale.ENGLISH).format(stringDate);
    }

    public static boolean isUserLoggedIn(Activity activity){
        Gson mGson = ((CustomApplication)activity.getApplication()).getGsonObject();
        String storedUser = ((CustomApplication)activity.getApplication()).getShared().getUserData();
        LoginObject userObject = mGson.fromJson(storedUser, LoginObject.class);
        return userObject != null;
    }
}
