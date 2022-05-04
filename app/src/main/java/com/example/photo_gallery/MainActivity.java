package com.example.photo_gallery;
import androidx.appcompat.app.AppCompatActivity; import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri; import android.os.Bundle; import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View; import android.widget.EditText;
import android.widget.ImageView; import android.widget.TextView; import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat; import java.util.ArrayList;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    String mCurrentPhotoPath;
    Bitmap mBitMap;
    private ArrayList<String> photos = null;
    private int index = 0;
    Filter defaultFilter = null;
    Filter newFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");
        Filter filter = new Filter.FilterBuilder(new Date(Long.MIN_VALUE), new Date())
                .withCaption("")
                .build();
        defaultFilter = filter;
        photos = findPhotos();
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
    }
    public void takePhoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.photo_gallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Log.i("photo", "44");
            }
        }
    }
//    private ArrayList<String> findPhotos(Date filterStartTimestamp, Date filterEndTimestamp, String keywords) {
    private ArrayList<String> findPhotos() {
        Log.i("startDateFilter", defaultFilter.getFilterStartTimeStamp() == null ? "" : defaultFilter.getFilterStartTimeStamp().toString());
        Log.i("endDateFilter", defaultFilter.getFilterEndTimeStamp() == null ? "" : defaultFilter.getFilterEndTimeStamp().toString());
        Log.i("findPhotos", "49");
        Log.i("findPhotos", Environment.getExternalStorageDirectory().toString());
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
//null in fList
//        Log.i("findPhotos", fList.toString());
        if (newFilter != null) {
            if (fList != null) {
                for (File f : fList) {
                    if (((newFilter.getFilterStartTimeStamp() == null && newFilter.getFilterEndTimeStamp() == null) || (f.lastModified() >= newFilter.getFilterStartTimeStamp().getTime() && f.lastModified() <= newFilter.getFilterEndTimeStamp().getTime())) && (newFilter.getFilterCaption() == "" || newFilter.getFilterCaption() == null || f.getPath().contains(newFilter.getFilterCaption()))) {
                        Log.i("startDateFilter", newFilter.getFilterStartTimeStamp() == null ? "" : newFilter.getFilterStartTimeStamp().toString());
                        Log.i("endDateFilter", newFilter.getFilterEndTimeStamp() == null ? "" : newFilter.getFilterEndTimeStamp().toString());
                        photos.add(f.getPath());
                        Log.i("keywords", newFilter.getFilterCaption());
//                    Log.i("photo-path", f.getPath());
                        Log.i("photo-timestamp", new Date(f.lastModified()).toString());
                        Log.i("findPhotos", "for loop if");
                    }
                }
            }
        }
        if (newFilter == null && fList != null) {
            Log.i("findPhotos", "in if");
            for (File f : fList) {
//                Log.i("findPhotos", f.toString());
//                Log.i("findPhotos", f.getPath());
//                Log.i("startDateFilter", filterStartTimestamp == null ? "" : filterStartTimestamp.toString());
//                Log.i("endDateFilter", filterEndTimestamp == null ? "" : filterEndTimestamp.toString());
                if (((defaultFilter.getFilterStartTimeStamp() == null && defaultFilter.getFilterEndTimeStamp() == null) || (f.lastModified() >= defaultFilter.getFilterStartTimeStamp().getTime() && f.lastModified() <= defaultFilter.getFilterEndTimeStamp().getTime())) && (defaultFilter.getFilterCaption() == "" || defaultFilter.getFilterCaption() == null || f.getPath().contains(defaultFilter.getFilterCaption()))) {
                    Log.i("startDateFilter", defaultFilter.getFilterStartTimeStamp() == null ? "" : defaultFilter.getFilterStartTimeStamp().toString());
                    Log.i("endDateFilter", defaultFilter.getFilterEndTimeStamp() == null ? "" : defaultFilter.getFilterEndTimeStamp().toString());
                    photos.add(f.getPath());
                    Log.i("keywords", defaultFilter.getFilterCaption());
//                    Log.i("photo-path", f.getPath());
                    Log.i("photo-timestamp", new Date(f.lastModified()).toString());
                    Log.i("findPhotos", "for loop if");
                }
            }
        }
        Log.i("findPhotos", "61");
        return photos;
    }

    public void scrollPhotos(View v) {
        Log.i("scrollPhotos", "66");
        try {
            updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
            if (index > (photos.size() - 1)) // with an active caption filter, changing the last image's caption
                index = photos.size() - 1;   // causes errors. this will rectify it.
            if (photos.size() == 0) { // if we remove the only image that existed, we ought to
                displayPhoto(""); // display nothing.
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            Log.i("empty photos global", "...");
            return;
        }

        switch (v.getId()) {
            case R.id.btnPrev:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index++;
                }
                break;
            default:
                break;
        }

        displayPhoto(photos.get(index));
    }
    private void displayPhoto(String path) {
        Log.i("displayPhoto", "86");
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        if (path == null || path =="") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            mBitMap = BitmapFactory.decodeFile(path);
//            String[] attr = path.split("_");
            String[] attr = path.split("#");
            String timeStamp = attr[2].split("\\.")[0];
//            Log.i("REALTIMESTAMP", otherAttr[1].split("\\.")[0]);
            for (int i = 0; i < attr.length; i++)
                Log.i("attr " + i, attr[i]);
//            Log.i("attrs", attr);
            et.setText(attr[1]);
            tv.setText(timeStamp);
        }
    }
    private File createImageFile() throws IOException {
        Log.i("createImageFile", "102");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "#caption#" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("photo", mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String from = (String) data.getStringExtra("STARTTIMESTAMP");
        String to = (String) data.getStringExtra("ENDTIMESTAMP");
        String keyword = (String) data.getStringExtra("KEYWORDS");
        Log.i("onActivityResult", "115");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_FILTER) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
//                Date filterStartTimestamp , filterEndTimestamp;
                try {
//                    Log.i("from", from);
//                    Log.i("to", to);
                    Filter newFilterNoCaption = new Filter.FilterBuilder(format.parse(from), format.parse(to))
                            .withCaption(keyword)
                            .build();
                    newFilter = newFilterNoCaption;
                } catch (Exception ex) {
                    Log.i("Exception?", ex.toString());
                    Filter newFilterException = new Filter.FilterBuilder(null, null)
                            .withCaption("")
                            .build();
                    newFilter = newFilterException;
                }
                Log.i("intent", String.valueOf(data));
//                Log.i("tag", filterCaption);
//                Log.i("from", filterStartTimestamp.toString());
//                Log.i("to", filterEndTimestamp.toString());
                index = 0;
                Log.i("finding photos", "...");
                photos = findPhotos();
                if (photos.size() == 0) {
                    Log.i("photo", "not found");
                    displayPhoto(null);
                } else {
                    Log.i("photo", "found photo");
                    displayPhoto(photos.get(index));
                }
            }
        }
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Log.i("is this getting called?", "...");
//            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
//            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
//            Filter filter = new Filter.FilterBuilder(new Date(Long.MIN_VALUE), new Date())
//                    .withCaption("")
//                    .build();
//            defaultFilter = filter;
//            photos = findPhotos();
//        }
    }

    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("#");
        if (attr.length >= 3) {
            Log.i("new pathname:", attr[0] + "#" + caption + "#" + attr[2]);
            File to = new File(attr[0] + "#" + caption + "#" + attr[2]);
            File from = new File(path);
            from.renameTo(to);
            photos = findPhotos(); // update photo list with new names.
        }
    }

    public void goSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE_FILTER);
    }

    public void goShare(View view) {
        photos = findPhotos();
        if (photos.size() == 0) {
            Log.i("Error", "No photos");
        } else {
            sharePicturesIntent(1, mBitMap);
        }
    }

    private void sharePicturesIntent(Integer request_code, Bitmap bit) {
        String filePath = MediaStore.Images.Media.insertImage(getContentResolver(), bit, "Image to Share", null);
        Uri uri = Uri.parse(filePath);

        Intent sendPhotoIntent = new Intent(Intent.ACTION_SEND);
        sendPhotoIntent.setType("image/jpg");
        sendPhotoIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(sendPhotoIntent.createChooser(sendPhotoIntent, "Share Photo"));
    }
}