package to.msn.wings.lenzeed.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import to.msn.wings.lenzeed.Model.Result;
import to.msn.wings.lenzeed.R;

public class GridViewAdapter extends BaseAdapter {

    class ViewHolder{
        ImageView exhibitimage;
        TextView exhibitname;
        TextView exhibitdetail;
    }

    private List<Result> resultList;
    private LayoutInflater inflater;
    private int layoutId;

    // コンストラクタ GridViewからデータを受け取る
    public GridViewAdapter(Context context, int layoutId, List<Result> results){
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
        resultList = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;
        if(convertView == null){
            // activity_cameraのGridViewにresult_list.xmlをinflateしてconvertviewとする
            convertView = inflater.inflate(layoutId, parent, false);
            // ViewHolder生成
            holder = new ViewHolder();

            holder.exhibitimage = convertView.findViewById(R.id.exhibit_image);
            holder.exhibitname = convertView.findViewById(R.id.exhibit_name);
            holder.exhibitdetail = convertView.findViewById(R.id.exhibit_detail);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        // imageの処理

        Result result = resultList.get(position);

        String Exhibit_link = result.getExhibitimage();

        holder.exhibitname.setText(result.getExhibitname());
        holder.exhibitdetail.setText(result.getExhibitdetail());
        Picasso.get().load(Exhibit_link).placeholder(android.R.drawable.ic_btn_speak_now).into(holder.exhibitimage);

        return convertView;
    }

    @Override
    public int getCount(){
        // List<String> imageListの全要素を返す
        return resultList.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

}
