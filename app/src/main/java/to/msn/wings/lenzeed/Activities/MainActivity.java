package to.msn.wings.lenzeed.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import to.msn.wings.lenzeed.Activities.main.bottom.HomeFragment;
import to.msn.wings.lenzeed.Activities.main.bottom.MapFragment;
import to.msn.wings.lenzeed.Activities.main.bottom.SettingsFragment;
import to.msn.wings.lenzeed.R;
import to.msn.wings.lenzeed.Util.Lang;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView navigation;
    private String lang;
    private SharedPreferences prefData;
    private SharedPreferences.Editor editor;
    private Lang lang_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 言語設定
        lang_setting = new Lang(this);
        lang_setting.setLang();


        // Bottomの生成
        // デフォルトのフラグメント
        loadFragment(HomeFragment.newInstance());
        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = HomeFragment.newInstance();
                break;

            case R.id.navigation_map:
                fragment = MapFragment.newInstance();
                break;

            case R.id.navigation_settings:
                fragment = SettingsFragment.newInstance();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment){
        // Bottom NavigationのFragmentを入れ替える処理
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    // Set language
    private void setLang() {
        Locale locale = Locale.getDefault();
        lang = locale.toString();

        String[] splitted = lang.split("[_-]");

        // ローカルの設定に保存
        prefData = getSharedPreferences("pref_data", MODE_PRIVATE);
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

}