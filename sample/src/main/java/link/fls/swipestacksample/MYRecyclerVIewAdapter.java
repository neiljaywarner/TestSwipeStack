package link.fls.swipestacksample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by neil.warner on 5/10/17.
 */

public class MYRecyclerVIewAdapter extends RecyclerView.Adapter<MYRecyclerVIewAdapter.PromoViewHolder> {

    List<String> mData;

    public MYRecyclerVIewAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public PromoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PromoViewHolder.inflateFrom(parent);
    }

    @Override
    public void onBindViewHolder(PromoViewHolder holder, int position) {
        String s = mData.get(position);
        Log.e("NJW","s=" + s);
        holder.text.setText(s);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        public PromoViewHolder(View itemView)  {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.textViewCard);

        }

        public static PromoViewHolder inflateFrom(ViewGroup parent) {
            return new PromoViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card, parent, false)
            );
        }
    }
}
