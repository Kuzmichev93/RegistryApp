package app.utils;


import java.util.regex.Pattern;

public class Reg {
    //Обработка данных с помощью регулярных выражений
    public boolean checkValue(String regex,String value){
        boolean pat = Pattern.matches(regex,value);
        if(!pat){
            return true;
        }
        return false;
    }

}
