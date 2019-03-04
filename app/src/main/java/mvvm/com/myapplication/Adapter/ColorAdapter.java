package mvvm.com.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mvvm.com.myapplication.R;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = getColorList();
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

    private List<Integer>getColorList(){
        List<Integer>colorList=new ArrayList<>();
        colorList.add(Color.parseColor("#b26666"));

        colorList.add(Color.parseColor("#00bc6c"));
        colorList.add(Color.parseColor("#d8ccf4"));
        colorList.add(Color.parseColor("#81b7d3"));
        colorList.add(Color.parseColor("#95c3d0"));
        colorList.add(Color.parseColor("#00c29d"));
        colorList.add(Color.parseColor("#25857a"));
        colorList.add(Color.parseColor("#db4e76"));
        colorList.add(Color.parseColor("#fad022"));
        colorList.add(Color.parseColor("#0494d4"));
        colorList.add(Color.parseColor("#75facd"));
        colorList.add(Color.parseColor("#81c2c3"));
        colorList.add(Color.parseColor("#6f739d"));
        colorList.add(Color.parseColor("#9d6f73"));
        colorList.add(Color.parseColor("#87c2d1"));
        colorList.add(Color.parseColor("#c7e995"));
        colorList.add(Color.parseColor("#dc588f"));
        colorList.add(Color.parseColor("#02c6c0"));
        colorList.add(Color.parseColor("#f81f52"));
        colorList.add(Color.parseColor("#cf6f9e"));



        return colorList;
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
