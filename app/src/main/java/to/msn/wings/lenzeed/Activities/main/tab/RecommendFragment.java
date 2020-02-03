package to.msn.wings.lenzeed.Activities.main.tab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import to.msn.wings.lenzeed.Data.SpotRecyclerViewAdapter;
import to.msn.wings.lenzeed.Model.Spot;
import to.msn.wings.lenzeed.R;
import to.msn.wings.lenzeed.Util.Constants;
import to.msn.wings.lenzeed.Util.Lang;
import to.msn.wings.lenzeed.Util.Prefs;
import to.msn.wings.lenzeed.Util.VolleyHelper;

public class RecommendFragment extends Fragment {


    public static RecommendFragment newInstance(){
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    private RecyclerView recyclerView;
    private List<Spot> spotList;
    private SpotRecyclerViewAdapter spotRecyclerViewAdapter;
    private VolleyHelper mInstance;
    private Lang lang_setting;
    private String lang;
    private String country;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        // アクティビティ取得（onAttachで取得しても良い）
        Activity activity = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        // Volleyシングルトンによるネットワーク接続
        mInstance = VolleyHelper.getInstance(getActivity().getApplicationContext());

        // 言語の取得
        lang_setting = new Lang(getContext());
        lang = lang_setting.getLang();
        country = lang_setting.getCountry();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        // set initial search word
        Prefs prefs = new Prefs(activity);
        prefs.setSearch(getString(R.string.search_ini));
        String search = prefs.getSearch();
        spotList = new ArrayList<>();

        spotList = getSpots(search);

        spotRecyclerViewAdapter = new SpotRecyclerViewAdapter(activity, spotList);
        recyclerView.setAdapter(spotRecyclerViewAdapter);
        spotRecyclerViewAdapter.notifyDataSetChanged();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }


    public List<Spot> getSpots(String searchTerm){
        spotList.clear();

        String URL = Constants.URL_SEARCH + lang + Constants.URL_COUNTRY + country + Constants.URL_AND + searchTerm;

        Log.d("URL", URL);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray spotsArray = response.getJSONArray("Search");

                    Log.d("response: ", spotsArray.toString());

                    for(int i = 0; i < spotsArray.length(); i++){
                        JSONObject spotObj = spotsArray.getJSONObject(i);
                        Spot spot = new Spot();
                        spot.setName(spotObj.getString("Title"));
                        spot.setMajorCategory(spotObj.getString("Major_category"));
                        spot.setThumbnail(spotObj.getString("Thumbnail"));
                        spot.setSpotpk(spotObj.getString("spotpk"));

                        Log.d("Spot:", spot.getName());
                        spotList.add(spot);
                    }

                    spotRecyclerViewAdapter.notifyDataSetChanged(); //Important

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());

            }
        });

        // リクエストの追加とネットワーク接続
        if (mInstance != null){
            // RequestQueueにJSONリクエストを追加
            mInstance.addToRequestQueue(jsonObjectRequest);
            // Queue開始
            // mInstance.getRequestQueue().start();
        }

        return spotList;
    }
}
