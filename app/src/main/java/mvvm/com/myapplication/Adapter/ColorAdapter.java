package mvvm.com.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mvvm.com.myapplication.R;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, List<Integer> colorList, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = colorList;
        this.listener = listener;
    }



    public class ColorViewHolder extends  RecyclerView.ViewHolder{
       public CardView color_section;

        public ColorViewHolder(View itemview){
            super(itemview);
            color_section=(CardView)itemview.findViewById(R.id.color_section);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }


    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {

        holder.color_section.setCardBackgroundColor(colorList.get(position));

    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }
}
