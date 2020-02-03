package to.msn.wings.lenzeed.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import to.msn.wings.lenzeed.Data.GridViewAdapter;
import to.msn.wings.lenzeed.Inference.Classifier;
import to.msn.wings.lenzeed.Inference.TensorFlowImageClassifier;
import to.msn.wings.lenzeed.Model.Result;
import to.msn.wings.lenzeed.R;
import to.msn.wings.lenzeed.Util.Constants;
import to.msn.wings.lenzeed.Util.Lang;
import to.msn.wings.lenzeed.Util.VolleyHelper;

/**
 * Camera View Library
 * https://camerakit.io/docs?v=0.13.2#setup
 */


public class CameraActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    /**
     * モデルのパスを場所ごとに入れ替える検討
     */
    private static final String MODEL_PATH = "embedding_20200117_123222.tflite";
    /**
     * 量子化するかは検討
     */
    private static final boolean QUANT = false;
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private ImageButton btnDetectObject;
    private CameraView cameraView;
    private SharedPreferences prefData;
    private String email;
    private Lang lang_setting;
    private String lang;
    private String country;
    private String spotId;
    private VolleyHelper mInstance;

    private ProgressBar progressCircle;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout linearLayoutBSheet;
    private ToggleButton tbUpDown;
    private List<Result> resultList;
    private GridView gridView;
    private GridViewAdapter adapter;

    private int num_result;
    private TextView txtNumResult;
    // private ContentLoadingProgressBar progbar;

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Intentデータを取得
        spotId = String.valueOf(getIntent().getSerializableExtra("spotId"));

        // Volleyシングルトンによるネットワーク接続
        mInstance = VolleyHelper.getInstance(this);

        // 言語の設定
        lang_setting = new Lang(this);
        lang = lang_setting.getLang();
        country = lang_setting.getCountry();

        init();

        // 画面Viewの処理
        tbUpDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    tbUpDown.setChecked(true);
                }else if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    tbUpDown.setChecked(false);
                }
            }

            @Override
            public void onSlide(View view, float v) {

            }
        });




        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                // assetsからbitmapでサンプルを読み込む
                // Bitmap bitmap = getBitmapFromAsset("tanuki.jpg");

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                //imageViewResult.setImageBitmap(bitmap);
                //final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                final float[][] results = classifier.recognizeImage(bitmap);
                // resultsをAPIサーバーに送信（50次元のベクトルを返す）
                postClassification(results);

                // TODO: 処理が終わるまでProgress処理を入れる

                // responseをラベルと説明にする　レスポンスはJSON内で取得し処理する

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
                progressCircle.setVisibility(View.VISIBLE);
            }
        });

        initTensorFlowAndLoadModel();
    }


    @Override
    protected void onResume(){
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause(){
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }


    private void init() {
        this.linearLayoutBSheet = findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBSheet);
        this.tbUpDown = findViewById(R.id.toggleButton);
        //this.listView = findViewById(R.id.listView);
        this.txtNumResult = findViewById(R.id.txtNumResult);
        this.progressCircle = findViewById(R.id.progressBarResult);


        this.cameraView = findViewById(R.id.cameraView);
        // this.textViewResult.setMovementMethod(new ScrollingMovementMethod());
        //this.btnToggleCamera = findViewById(R.id.btnToggleCamera);
        this.btnDetectObject = findViewById(R.id.btnDetectObject);
    }


    // TensorFlowのモデル読み込み
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    classifier = TensorFlowImageClassifier.create(getAssets(), MODEL_PATH, INPUT_SIZE, QUANT);
                    makeButtonVisible();
                }catch (final Exception e){
                    throw new RuntimeException("Error initializing TensorFlow", e);
                }
            }
        });
    }

    private void makeButtonVisible(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }

    // MobileNetで推論処理した50次元ベクトルをサーバーに送信
    public void postClassification(float[][] result){
        JSONObject json;
        String URL = Constants.URL_CAMERA_RECOG;

        // TODO: Email画面登録時に戻す
        //email = prefData.getString("email", "");
        email = "nakano.yuta252@gmail.com";
        Log.d("result:", String.valueOf(result[0][1]));
        // JSONを生成
        json = new JSONObject();
        try {
            json.put("email", email);
            // TODO: 50次元のfloat配列をstringではなくfloatで送付できるように変更
            JSONArray result_list = new JSONArray();
            for (float number: result[0]){
                result_list.put(number);
            }
            json.put("result", result_list);
            json.put("spot_id", spotId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Json request", json.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // JSONパース処理
                try {
                    resultList = new ArrayList<>();
                    JSONArray exhibitsArray = response.getJSONArray("Result");

                    for(int i = 0; i < exhibitsArray.length(); i++){
                        JSONObject exhibitObj = exhibitsArray.getJSONObject(i);

                        // JSONの取得結果をResultに格納
                        Result result = new Result();
                        result.setExhibitid(exhibitObj.getString("exhibit_id"));
                        result.setExhibitname(exhibitObj.getString("exhibit_name"));
                        result.setExhibitdetail(exhibitObj.getString("exhibit_desc"));
                        result.setExhibitimage(exhibitObj.getString("exhibit_image"));
                        Log.d("exhibit id", exhibitObj.getString("exhibit_id"));
                        Log.d("exhibit name", exhibitObj.getString("exhibit_name"));
                        Log.d("exhibit description", exhibitObj.getString("exhibit_desc"));
                        Log.d("exhibit image", exhibitObj.getString("exhibit_image"));

                        resultList.add(result);
                    }

                    num_result = resultList.size();

                    txtNumResult.setText(String.valueOf(num_result));


                    // GridViewのインスタンス作成
                    GridView gridView = findViewById(R.id.gridView);
                    // BaseAdapterを継承したGridAdapterのインスタンスを生成
                    // 子要素のレイアウト result_item.xmlをactivity_camera.xmlにinflateする
                    GridViewAdapter adapter = new GridViewAdapter(CameraActivity.this, R.layout.result_items, resultList);
                    gridView.setAdapter(adapter);

                    // 検索結果の表示
                    progressCircle.setVisibility(View.GONE);
                    linearLayoutBSheet.setVisibility(View.VISIBLE);

                    gridView.setOnItemClickListener(CameraActivity.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // リクエストの追加とネットワーク接続
        if (mInstance != null){
            mInstance.addToRequestQueue(jsonObjectRequest);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /**
        Intent intent = new Intent(CameraActivity.this, ExhibitDetail.class);

        Result result = resultList.get(position);
        intent.putExtra("Exhibitid", result.getExhibitid());
        intent.putExtra("Exhibitname", result.getExhibitname());
        intent.putExtra("Exhibitdetail", result.getExhibitdetail());
        intent.putExtra("Exhibitimage", result.getExhibitimage());
        startActivity( intent );
         */
    }

    /*
    // ローカルの画像読み込み時
    private Bitmap getBitmapFromAsset(String strName)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
     */
}

