package com.example.photo_gallery;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity; import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle; import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View; import android.widget.EditText;
import android.widget.ImageView; import android.widget.TextView; import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat; import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    String mCurrentPhotoPath;
    Bitmap mBitMap;
    private ArrayList<String> photos = null;
    private int index = 0;
    private Date filterStartTimestamp = null;
    private Date filterEndTimestamp = null;
    private String filterCaption = null;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filterStartTimestamp = new Date(Long.MIN_VALUE);
        filterEndTimestamp = new Date();
        filterCaption = "";
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
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<String> findPhotos() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();

        if (fList != null) {
            Stream<File> fileStream = Arrays.stream(fList);
            Stream<File> findFileStream = fileStream.filter(f -> ((filterStartTimestamp == null && filterEndTimestamp == null) || (f.lastModified() >= filterStartTimestamp.getTime() && f.lastModified() <= filterEndTimestamp.getTime())) && (filterCaption == "" || filterCaption == null || f.getPath().contains(filterCaption)));
            findFileStream.forEach(f -> photos.add(f.getPath()));
        }
        return photos;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scrollPhotos(View v) {
        try {
            updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
            if (index > (photos.size() - 1)) // with an active caption filter, changing the last image's caption
                index = photos.size() - 1;   // causes errors. this will rectify it.
            if (photos.size() == 0) { // if we remove the only image that existed, we ought to
                displayPhoto(""); // display nothing.
                return;
            }
        } catch (IndexOutOfBoundsException e) {
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
            String[] attr = path.split("#");
            String timeStamp = attr[2].split("\\.")[0];
            for (int i = 0; i < attr.length; i++)
            et.setText(attr[1]);
            tv.setText(timeStamp);
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "#caption#" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("#");
        if (attr.length >= 3) {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goShare(View view) {
        photos = findPhotos();
        if (photos.size() == 0) {
        } else {
            sharePicturesIntent(1, mBitMap);
        }
    }

    private void sharePicturesIntent(Integer request_code, Bitmap bit) {
        String filePath = MediaStore.Images.Media.insertImage(getContentResolver(), bit, "Image to Share", null);
        Uri uri = Uri.parse(filePath);

        Intent sendPhotoIntent = new Intent(Intent.ACTION_SEND);
        sendPhotoIntent.setAction(Intent.ACTION_GET_CONTENT);
        sendPhotoIntent.setType("image/jpg");
        sendPhotoIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(sendPhotoIntent.createChooser(sendPhotoIntent, "Share Photo"));
    }
}