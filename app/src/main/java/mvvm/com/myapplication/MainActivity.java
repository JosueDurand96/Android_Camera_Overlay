package mvvm.com.myapplication;




import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Binder;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;


import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import mvvm.com.myapplication.Adapter.ViewPagerAdapter;
import mvvm.com.myapplication.Interface.BrushFragmentListener;
import mvvm.com.myapplication.Interface.EditImageFragmentListener;
import mvvm.com.myapplication.Interface.EmojiFragmentListener;
import mvvm.com.myapplication.Interface.FiltersListFragmentListener;
import mvvm.com.myapplication.Utils.BitmapUtils;



public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener,EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener {
    public static final String pictureName= "josue.jpg";
    public static final int PERMISSION_PICK_IMAGE=1000;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    CardView  btn_filters_list,btn_edit,btn_brush,btn_emoji;


    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    int  brightnessFinal= 0;
    float saturationFinal= 1.0f;
    float constraintFinal = 1.0f;


    static {
       System.loadLibrary("NativeImageProcessor");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TE AMO BEBE");

        //view
        photoEditorView=(PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor= new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();


        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator);

        btn_edit=(CardView)findViewById(R.id.btn_edit);
        btn_filters_list=(CardView)findViewById(R.id.btn_filters_list);
        btn_brush=(CardView) findViewById(R.id.btn_brush);
        btn_emoji=(CardView)findViewById(R.id.btn_emoji);

        btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltersListFragment filtersListFragment = FiltersListFragment.getInstance();
                filtersListFragment.setListener(MainActivity.this);
                filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });


        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable brush mode
                photoEditor.setBrushDrawingMode(true);


                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });


        loadImage();


    }

    private void loadImage() {
        originalBitmap= BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager());
        filtersListFragment= new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment= new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment,"FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal=brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal=saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstrantChanged(float constrant) {
        constraintFinal=constrant;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constrant));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constraintFinal));
        finalBitmap=myFilter.processFilter(bitmap);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    private void resetControl() {
        if(editImageFragment !=null)
            editImageFragment.resetControls();
        brightnessFinal=0;
        saturationFinal=1.0f;
        constraintFinal=1.0f;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id==R.id.action_open)
        {
            openImageFromGallery();
            return true;
        }
        if (id==R.id.action_save)
        {
            saveImageGallery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImageGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                        {
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    try {

                                        photoEditorView.getSource().setImageBitmap(saveBitmap);

                                        final String path = BitmapUtils.insertImage(getContentResolver(),
                                                saveBitmap,
                                                System.currentTimeMillis()+"_profile.jpg"
                                                ,null);
                                        if (!TextUtils.isEmpty(path))
                                        {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                    "Imagen guardado en la galeria",
                                                    Snackbar.LENGTH_LONG)
                                                    .setAction("ABRIR", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            openImage(path);
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                        else {
                                            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                    "No se pudo guardar la imagen",
                                                    Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                        else {
                            Toast.makeText(MainActivity.this, "No tiene permiso", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery() {
     Dexter.withActivity(this)
             .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                     Manifest.permission.WRITE_EXTERNAL_STORAGE)
             .withListener(new MultiplePermissionsListener(){
                 @Override
                 public void onPermissionsChecked(MultiplePermissionsReport report) {
                     if (report.areAllPermissionsGranted())
                     {
                         Intent intent =new Intent(Intent.ACTION_PICK);
                         intent.setType("image/*");
                         startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                     }
                     else {
                         Toast.makeText(MainActivity.this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                     }
                 }

                 @Override
                 public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                     token.continuePermissionRequest();
                 }
             }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK &&requestCode==PERMISSION_PICK_IMAGE)
        {
            Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,data.getData(),800,800);

            //clear bitmap memory
            originalBitmap.recycle();
            finalBitmap.recycle();
            filteredBitmap.recycle();

            originalBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            finalBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
            photoEditorView.getSource().setImageBitmap(originalBitmap);
            bitmap.recycle();

            //Render selected img thumbnail
            filtersListFragment.displayThumbnail(originalBitmap);

        }
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setBrushColor(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }
}
