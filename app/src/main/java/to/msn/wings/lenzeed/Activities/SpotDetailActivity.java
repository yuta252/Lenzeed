package to.msn.wings.lenzeed.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import to.msn.wings.lenzeed.Model.Spot;
import to.msn.wings.lenzeed.R;
import to.msn.wings.lenzeed.Util.Constants;
import to.msn.wings.lenzeed.Util.Lang;
import to.msn.wings.lenzeed.Util.VolleyHelper;

public class SpotDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout toolbarLayout;
    private Spot spot;
    private TextView spotTitle;
    private ImageView spotImage;
    private TextView spotCategory;
    private TextView information;
    private TextView address;
    private TextView telephone;
    private TextView entranceFee;
    private TextView businessHours;
    private TextView holiday;

    private VolleyHelper mInstance;
    private String spotId;

    private Lang lang_setting;
    private String lang;
    private String country;

    private Button camerabtn;
    private Button reviewbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Volleyシングルトンによるネットワーク接続
        mInstance = VolleyHelper.getInstance(this);

        // Intentの変数（spotId）取得
        spot = (Spot) getIntent().getSerializableExtra("spot");
        spotId = spot.getSpotpk();

        // 言語の設定
        lang_setting = new Lang(this);
        lang = lang_setting.getLang();
        country = lang_setting.getCountry();

        init();
        getSpotDetails(spotId);

        // カメラ押下
        camerabtn.setOnClickListener(this);

        // レビュー投稿
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Write review", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getSpotDetails(String id) {

        String URL = Constants.URL_DETAIL + lang + Constants.URL_COUNTRY + country + Constants.URL_AND_DETAIL + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    toolbarLayout.setTitle(response.getString("Title"));
                    // spotTitle.setText(response.getString("Title"));
                    //spotCategory.setText(response.getString("Category"));
                    information.setText("Information: " + response.getString("Information"));
                    address.setText("Address: " + response.getString("Address"));
                    telephone.setText("Telephone: " + response.getString("Telephone"));
                    entranceFee.setText("Entrance fee: " + response.getString("EntranceFee"));
                    businessHours.setText("Business hours: " + response.getString("BusinessHours"));
                    holiday.setText("Holiday: " + response.getString("Holiday"));

                    Picasso.get().load(response.getString("Thumbnail")).placeholder(android.R.drawable.ic_btn_speak_now).into(spotImage);


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.getMessage());
            }
        });

        // リクエストの追加とネットワーク接続
        if (mInstance != null){
            mInstance.addToRequestQueue(jsonObjectRequest);
        }
    }

    private void init() {
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //spotTitle = (TextView) findViewById(R.id.spotTitleId);
        spotImage = (ImageView) findViewById(R.id.spotImageId);
        spotCategory = (TextView) findViewById(R.id.spotCategoryId);
        information = (TextView) findViewById(R.id.informationId);
        address = (TextView) findViewById(R.id.addressId);
        telephone = (TextView) findViewById(R.id.telephoneId);
        entranceFee = (TextView) findViewById(R.id.entrancefeeId);
        businessHours = (TextView) findViewById(R.id.businesshoursId);
        holiday = (TextView) findViewById(R.id.holidayId);

        camerabtn = (Button) findViewById(R.id.cameraId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraId:

                Intent intentCamera = new Intent(this, CameraActivity.class);
                intentCamera.putExtra("spotId", spotId);
                startActivity(intentCamera);
                break;
        }
    }
}
