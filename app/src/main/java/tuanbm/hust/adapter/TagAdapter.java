package tuanbm.hust.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tuanbm.hust.R;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    Context mContext;
    List<String> mTags;

    public TagAdapter(Context context, List<String> tags){
        this.mContext = context;
        this.mTags = tags;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tvTag);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View tagView = inflater.inflate(R.layout.item_tag, parent, false);
        return new ViewHolder(tagView);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, int position) {
        String tag = mTags.get(position);
        holder.tvTag.setText(tag);
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public void updateData(List<String> newData){
        mTags.clear();
        mTags.addAll(newData);
        this.notifyDataSetChanged();
    }
}
