package mvvm.com.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import mvvm.com.myapplication.Adapter.ColorAdapter;
import mvvm.com.myapplication.Interface.BrushFragmentListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    SeekBar seekBar_brush_Size,seekBar_opacity_size;
    RecyclerView recycler_color;
    ToggleButton btn_brush_state;
    ColorAdapter colorAdapter;
    BrushFragmentListener listener;

    static BrushFragment instance;

    public static BrushFragment getInstance(){
        if (instance==null)
            instance=new BrushFragment();
        return instance;
    }

    public BrushFragment() {
        // Required empty public constructor
    }

    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview = inflater.inflate(R.layout.fragment_brush, container, false);
        seekBar_brush_Size=(SeekBar)itemview.findViewById(R.id.seekbar_brush_size);
        seekBar_opacity_size=(SeekBar)itemview.findViewById(R.id.seekbar_brush_opacity);
        btn_brush_state=(ToggleButton)itemview.findViewById(R.id.btn_brush_State);
        recycler_color=(RecyclerView)itemview.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        colorAdapter = new ColorAdapter(getContext(),getColorList(),this  );
        recycler_color.setAdapter(colorAdapter);

        //Evento
        seekBar_opacity_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                listener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_brush_Size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                listener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_brush_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                listener.onBrushStateChangedListener(isChecked);
            }
        });

       return itemview;
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

    @Override
    public void onColorSelected(int color) {
        listener.onBrushColorChangedListener(color);
    }
}
