package mvvm.com.myapplication;




import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.support.design.widget.CoordinatorLayout;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


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


import mvvm.com.myapplication.Adapter.ViewPagerAdapter;
import mvvm.com.myapplication.Interface.EditImageFragmentListener;
import mvvm.com.myapplication.Interface.FiltersListFragmentListener;
import mvvm.com.myapplication.Utils.BitmapUtils;



public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener,EditImageFragmentListener {
    public static final String pictureName= "josue.jpg";
    public static final int PERMISSION_PICK_IMAGE=1000;

    ImageView img_preview;
    TabLayout tabLayout;
    ViewPager viewPager;
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
        getSupportActionBar().setTitle("JOSUE EFECTO");

        //view
        img_preview=(ImageView)findViewById(R.id.image_preview);
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator);

        loadImage();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void loadImage() {
        originalBitmap= BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        img_preview.setImageBitmap(originalBitmap);
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
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal=saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstrantChanged(float constrant) {
        constraintFinal=constrant;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constrant));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

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
        img_preview.setImageBitmap(filter.processFilter(filteredBitmap));
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
                            try {
                                final String path = BitmapUtils.insertImage(getContentResolver(),
                                        finalBitmap,
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
                        else {
                            Toast.makeText(MainActivity.this, "No tiene permiso", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                });
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
             });
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
            img_preview.setImageBitmap(originalBitmap);
            bitmap.recycle();

            //Render selected img thumbnail
            filtersListFragment.displayThumbnail(originalBitmap);

        }
    }
}
