package com.example.photo_gallery;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private PhotoRepository photoRepository = null;

    GalleryPresenter presenter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new GalleryPresenter(this);
        Button searchBtn = (Button) findViewById(R.id.btnSearch);
        searchBtn.setOnClickListener(searchListener);
    }
    public void onClick( View v) {
        String caption = ((EditText) findViewById(R.id.etCaption)).getText().toString();
        switch (v.getId()) {
            case R.id.btnNext:
                presenter.handleNavigationInput("ScrollLeft", caption);
                break;
            case R.id.btnPrev:
                presenter.handleNavigationInput("ScrollRight", caption);
                break;
            default:
                break;
        }
    }
    public void takePhoto(View v) {
        presenter.takePhoto();
    }

    public void scrollPhotos(View v) {presenter.scrollPhotos();}

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onReturn(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_FILTER) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
                try {
                    String from = (String) data.getStringExtra("filterStartTimestamp");
                    String to = (String) data.getStringExtra("filterEndTimestamp");
                    filterStartTimestamp = format.parse(from);
                    filterEndTimestamp = format.parse(to);
                } catch (Exception ex) {
                    filterStartTimestamp = null;
                    filterEndTimestamp = null;
                }
                filterCaption = (String) data.getStringExtra("KEYWORDS");
                index = 0;
                photos = findPhotos();
                if (photos.size() == 0) {
                    displayPhoto(null);
                } else {
                    displayPhoto(photos.get(index));
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            filterStartTimestamp = new Date(Long.MIN_VALUE);
            filterEndTimestamp = new Date();
            filterCaption = "";
            photos = findPhotos();
        }
    }

    @Override
    public void displayPhoto(Bitmap photo, String caption, String timestamp) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        et.setText(caption);
        tv.setText(timestamp);
        if (photo == null)
            iv.setImageResource(R.mipmap.ic_launcher);
        else
            iv.setImageBitmap(photo);
    }
}
