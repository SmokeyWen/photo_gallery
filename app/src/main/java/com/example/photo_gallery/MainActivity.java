package com.example.photo_gallery;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photo_gallery.Model.Photo;
import com.example.photo_gallery.Presenter.GalleryPresenter;
import com.example.photo_gallery.Model.PhotoRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GalleryPresenter.View, View.OnClickListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    String mCurrentPhotoPath;
    Bitmap mBitMap;
    private ArrayList<String> photos = null;
    private int index = 0;
    private Date filterStartTimestamp = null;
    private Date filterEndTimestamp = null;
    private String filterCaption = null;

    GalleryPresenter presenter = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new GalleryPresenter(this);
        Button searchBtn = (Button) findViewById(R.id.btnSearch);
        Button nxtBtn = (Button) findViewById(R.id.btnNext);
        Button prevBtn = (Button) findViewById(R.id.btnPrev);
        searchBtn.setOnClickListener(searchListener);
        nxtBtn.setOnClickListener(nextPhotoListener);
        prevBtn.setOnClickListener(prevPhotoListener);
    }

    public void onClick(View v) {
    }

    public void takePhoto(View v) {
        presenter.takePhoto();
    }

//    public void scrollPhotos(View v) {presenter.scrollPhotos(v);}

//    public void goSearch(View v) {
//        Intent intent = new Intent(this, SearchActivity.class);
//        startActivityForResult(intent, REQUEST_IMAGE_FILTER);
//    }

    //(View) findViewById(R.id.btnSearch)
    private View.OnClickListener searchListener = new View.OnClickListener() {
        public void onClick(View v) {
            presenter.search();
        }
    };

    private View.OnClickListener nextPhotoListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View v) {
            String caption = ((EditText) findViewById(R.id.etCaption)).getText().toString();
            presenter.handleNavigationInput("ScrollNext", caption);
        }
    };

    private View.OnClickListener prevPhotoListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View v) {
            String caption = ((EditText) findViewById(R.id.etCaption)).getText().toString();
            presenter.handleNavigationInput("ScrollPrev", caption);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onReturn(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_FILTER) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                try {
                    String from = (String) data.getStringExtra("STARTTIMESTAMP");
                    String to = (String) data.getStringExtra("ENDTIMESTAMP");
                    filterStartTimestamp = format.parse(from);
                    filterEndTimestamp = format.parse(to);
                } catch (Exception ex) {
                    filterStartTimestamp = null;
                    filterEndTimestamp = null;
                }
                filterCaption = (String) data.getStringExtra("KEYWORDS");
                presenter.filterAndDisplay(filterStartTimestamp, filterEndTimestamp, filterCaption);
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            filterStartTimestamp = new Date(Long.MIN_VALUE);
            filterEndTimestamp = new Date();
            filterCaption = "";
            presenter.filterAndDisplay(filterStartTimestamp, filterEndTimestamp, filterCaption);
        }
    }

    @Override
    public void displayPhoto(Bitmap photo, String caption, Date timestamp) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        et.setText("");
        tv.setText("");
        if (photo == null) {
            iv.setImageResource(R.mipmap.ic_launcher);
        } else {
            iv.setImageBitmap(photo);
            et.setText(caption);
            tv.setText(new SimpleDateFormat("yyyyMMdd_HHmmss").format(timestamp));
        }
    }
}
