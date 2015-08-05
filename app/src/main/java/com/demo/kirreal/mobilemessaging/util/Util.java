package com.demo.kirreal.mobilemessaging.util;

/**
 * Created by kirreal on 05.08.2015.
 */
public class Util {
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;

    }
}
