package mvvm.com.myapplication.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import mvvm.com.myapplication.R;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String>fontList;

    int row_selected = -1;

    public FontAdapter(Context context, FontAdapterClickListener listener, List<String> fontList) {
        this.context = context;
        this.listener = listener;
        fontList = loadFontList();
    }

    private List<String> loadFontList() {

        return null;

    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (row_selected==position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        Typeface typeface =Typeface.createFromAsset(context.getAssets(),new StringBuilder("fonts/*")
        .append(fontList.get(position)).toString());
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }


    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView txt_font_name,txt_font_demo;
        ImageView img_check;
        public FontViewHolder(View itemView) {
            super(itemView);
            txt_font_demo=(TextView)itemView.findViewById(R.id.txt_font_demo);
            txt_font_name=(TextView)itemView.findViewById(R.id.txt_font_name);

            img_check=(ImageView)itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  listener.onFontSelected(fontList.get(getAdapterPosition()));
                  row_selected=getAdapterPosition();
                }
            });
        }
    }

    public interface FontAdapterClickListener{
        public void onFontSelected(String fontName);
    }
}
