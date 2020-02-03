package to.msn.wings.lenzeed.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import to.msn.wings.lenzeed.Activities.SpotDetailActivity;
import to.msn.wings.lenzeed.Model.Spot;
import to.msn.wings.lenzeed.R;

public class SpotRecyclerViewAdapter extends RecyclerView.Adapter<SpotRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Spot> spotList;

    public SpotRecyclerViewAdapter(Context context, List<Spot> spots) {
        this.context = context;
        spotList = spots;
    }

    @Override
    public SpotRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Spot spot = spotList.get(position);
        String thumbnailLink = spot.getThumbnail();

        holder.title.setText(spot.getName());
        holder.category.setText((spot.getMajorCategory()));
        Picasso.get().load(thumbnailLink).placeholder(android.R.drawable.ic_btn_speak_now).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return spotList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView thumbnail;
        TextView category;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.spotTitleId);
            thumbnail = (ImageView) itemView.findViewById(R.id.spotThumbnailId);
            category = (TextView) itemView.findViewById(R.id.spotCategoryId);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    Spot spot = spotList.get(getAdapterPosition());

                    Intent intent = new Intent(context, SpotDetailActivity.class);

                    intent.putExtra("spot", spot);

                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}

