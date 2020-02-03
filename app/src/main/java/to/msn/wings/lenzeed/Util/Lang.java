package to.msn.wings.lenzeed.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Lang {

    private Context mContext;
    private String lang;
    private String country;
    private SharedPreferences prefData;
    private SharedPreferences.Editor editor;

    public Lang(Context context) {
        mContext = context;
    }

    public void setLang(){
        Locale locale = Locale.getDefault();
        lang = locale.toString();

        String[] splitted = lang.split("[_-]");

        // ローカルの設定に保存
        prefData = mContext.getSharedPreferences("pref_data", MODE_PRIVATE);
        editor = prefData.edit();

        if (splitted.length == 1){
            editor.putString("lang", splitted[0]);
        }else{
            editor.putString("lang", splitted[0]);
            editor.putString("country", splitted[1]);
        }
        editor.apply();
        Log.d("language setting", "lang:" + splitted[0] + ",country:"+ splitted[1] +"saved");
    }

    public String getLang(){
        prefData = mContext.getSharedPreferences("pref_data", MODE_PRIVATE);
        lang = prefData.getString("lang", null);

        // 言語が設定されていない場合再設定する
        if(lang == null){
            setLang();
            lang = prefData.getString("lang", "none");
        }

        return lang;
    }

    public String getCountry(){
        prefData = mContext.getSharedPreferences("pref_data", MODE_PRIVATE);
        country = prefData.getString("country", null);

        // 言語が設定されていない場合再設定する
        if(country == null){
            setLang();
            country = prefData.getString("country", "none");
        }

        return country;
    }

}
