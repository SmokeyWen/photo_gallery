package com.example.photo_gallery;
import androidx.appcompat.app.AppCompatActivity; import androidx.core.content.FileProvider;
import android.content.Intent; import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.net.Uri; import android.os.Bundle; import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View; import android.widget.EditText;
import android.widget.ImageView; import android.widget.TextView; import java.io.File;
import com.example.photo_gallery.Views.FramedImageView;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat; import java.util.ArrayList;
import java.util.Date;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.speech.RecognizerIntent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, SensorEventListener {
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_MIN_VELOCITY = 100;
    private GestureDetector gestures;
    private float zRef = Float.MIN_VALUE;
    private SensorManager sensorManager;
    private Sensor sensor;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FILTER = 2;
    static final int SPEECH_REQUEST = 4;
    String mCurrentPhotoPath;
    private ArrayList<String> photos = null;
    private int index = 0;
    private Date filterStartTimestamp = null;
    private Date filterEndTimestamp = null;
    private String filterCaption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");
        FramedImageView iv = (FramedImageView) findViewById(R.id.fiv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        iv.setContent(bitmap);
        filterStartTimestamp = new Date(Long.MIN_VALUE);
        filterEndTimestamp = new Date();
        filterCaption = "";
        photos = findPhotos();
        if (photos.size() == 0) {
            displayPhoto(null, null);
        } else {
            displayPhoto(photos.get(index), null);
        }
        gestures = new GestureDetector(getBaseContext(), this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor, 100);
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
        Log.i("startDateFilter", filterStartTimestamp == null ? "" : filterStartTimestamp.toString());
        Log.i("endDateFilter", filterEndTimestamp == null ? "" : filterEndTimestamp.toString());
        Log.i("findPhotos", "49");
        Log.i("findPhotos", Environment.getExternalStorageDirectory().toString());
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photo_gallery/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
//null in fList
//        Log.i("findPhotos", fList.toString());
        if (fList != null) {
            Log.i("findPhotos", "in if");
            for (File f : fList) {
//                Log.i("findPhotos", f.toString());
//                Log.i("findPhotos", f.getPath());
//                Log.i("startDateFilter", filterStartTimestamp == null ? "" : filterStartTimestamp.toString());
//                Log.i("endDateFilter", filterEndTimestamp == null ? "" : filterEndTimestamp.toString());
                if (((filterStartTimestamp == null && filterEndTimestamp == null) || (f.lastModified() >= filterStartTimestamp.getTime() && f.lastModified() <= filterEndTimestamp.getTime())) && (filterCaption == "" || filterCaption == null || f.getPath().contains(filterCaption))) {
                    Log.i("startDateFilter", filterStartTimestamp == null ? "" : filterStartTimestamp.toString());
                    Log.i("endDateFilter", filterEndTimestamp == null ? "" : filterEndTimestamp.toString());
                    photos.add(f.getPath());
                    Log.i("keywords", filterCaption);
//                    Log.i("photo-path", f.getPath());
                    Log.i("photo-timestamp", new Date(f.lastModified()).toString());
                    Log.i("findPhotos", "for loop if");
                }
            }
        }
        Log.i("findPhotos", "61");
        return photos;
    }

    public void goLeft() {
        index -= 1;
        if(index < 0) {
            index = photos.size() - 1;
        }
        Animation animationXML = AnimationUtils.loadAnimation(this, R.anim.slideleft);
        Animation animation = AnimationUtils.makeInAnimation(this, true);
        animation.setDuration(500);
        displayPhoto(photos.get(index), "left");
    }

    public void goRight() {
        index += 1;
        if(index >= photos.size()) {
            index = 0;
        }
        displayPhoto(photos.get(index), "right");
    }

    public void scrollPhotos(View v) {
        Log.i("scrollPhotos", "66");
        try {
            updatePhoto(photos.get(index), ((EditText) findViewById(R.id.etCaption)).getText().toString());
            if (index > (photos.size() - 1)) // with an active caption filter, changing the last image's caption
                index = photos.size() - 1;   // causes errors. this will rectify it.
            if (photos.size() == 0) { // if we remove the only image that existed, we ought to
                displayPhoto("", null); // display nothing.
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
                displayPhoto(photos.get(index), "left");
                break;
            case R.id.btnNext:
                if (index < (photos.size() - 1)) {
                    index++;
                }
                displayPhoto(photos.get(index), "right");
                break;
            default:
                break;
        }
    }
    private void displayPhoto(String path, String direction) {
        Log.i("displayPhoto", "86");
        FramedImageView iv = (FramedImageView) findViewById(R.id.fiv);
        TextView tv = (TextView) findViewById(R.id.tvTimestamp);
        EditText et = (EditText) findViewById(R.id.etCaption);
        if (path == null || path =="") {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
            iv.setContent(bitmap);
            et.setText("");
            tv.setText("");
        } else if (path != null && direction == null){
            iv.setContent(BitmapFactory.decodeFile(path));
//            String[] attr = path.split("_");
            String[] attr = path.split("#");
            String timeStamp = attr[2].split("\\.")[0];
//            Log.i("REALTIMESTAMP", otherAttr[1].split("\\.")[0]);
            for (int i = 0; i < attr.length; i++)
                Log.i("attr " + i, attr[i]);
//            Log.i("attrs", attr);
            et.setText(attr[1]);
            tv.setText(timeStamp);
        } else if (path != null && direction.equals("left")){
            iv.setContent(BitmapFactory.decodeFile(path));
//            String[] attr = path.split("_");
            String[] attr = path.split("#");
            String timeStamp = attr[2].split("\\.")[0];
//            Log.i("REALTIMESTAMP", otherAttr[1].split("\\.")[0]);
            Animation animationXML = AnimationUtils.loadAnimation(this, R.anim.slideleft);
            Animation animation = AnimationUtils.makeInAnimation(this, true);
            animation.setDuration(500);
            iv.startAnimation(animation);
            for (int i = 0; i < attr.length; i++)
                Log.i("attr " + i, attr[i]);
//            Log.i("attrs", attr);
            et.setText(attr[1]);
            tv.setText(timeStamp);
        } else if (path != null && direction.equals("right")){
            iv.setContent(BitmapFactory.decodeFile(path));
//            String[] attr = path.split("_");
            String[] attr = path.split("#");
            String timeStamp = attr[2].split("\\.")[0];
//            Log.i("REALTIMESTAMP", otherAttr[1].split("\\.")[0]);
            Animation animationXML = AnimationUtils.loadAnimation(this, R.anim.slideright);
            Animation animation = AnimationUtils.makeInAnimation(this, false);
            animation.setDuration(500);
            iv.startAnimation(animation);
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


    public void speakPressed(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        startActivityForResult(intent,4);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult", "115");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST) {
            try {
                ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String word = speech.get(0);
                if (word.contains("right")) {
                    goRight();
                }
                else if (word.contains("left")) {
                    goLeft();
                }
            }
            catch (Exception e) { }
        }
        if (requestCode == REQUEST_IMAGE_FILTER) {
            if (resultCode == RESULT_OK) {
                DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
//                Date filterStartTimestamp , filterEndTimestamp;
                try {
                    String from = (String) data.getStringExtra("filterStartTimestamp");
                    String to = (String) data.getStringExtra("filterEndTimestamp");
//                    Log.i("from", from);
//                    Log.i("to", to);
                    filterStartTimestamp = format.parse(from);
                    filterEndTimestamp = format.parse(to);
                } catch (Exception ex) {
                    Log.i("Exception?", ex.toString());
                    filterStartTimestamp = null;
                    filterEndTimestamp = null;
                }
                filterCaption = (String) data.getStringExtra("KEYWORDS");
                Log.i("intent", String.valueOf(data));
                Log.i("tag", filterCaption);
//                Log.i("from", filterStartTimestamp.toString());
//                Log.i("to", filterEndTimestamp.toString());
                index = 0;
                Log.i("finding photos", "...");
                photos = findPhotos();
                if (photos.size() == 0) {
                    Log.i("photo", "not found");
                    displayPhoto(null, null);
                } else {
                    Log.i("photo", "found photo");
                    displayPhoto(photos.get(index), null);
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("is this getting called?", "...");
            ImageView mImageView = (ImageView) findViewById(R.id.fiv);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            filterStartTimestamp = new Date(Long.MIN_VALUE);
            filterEndTimestamp = new Date();
            filterCaption = "";
            photos = findPhotos();
        }
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

    public void editSettings(View view) {
    }

    public void uploadPhoto(View view) {
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {return gestures.onTouchEvent(event);}
    @Override
    public boolean onDown(MotionEvent event) {return true;}
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //this.goRight();
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {
        //this.goLeft();
    }
    @Override
    public void onShowPress(MotionEvent e) {}
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                goLeft();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_MIN_VELOCITY) {
                goRight();
            }
        } catch (Exception e) { }
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            float z = event.values[2];
            if (zRef == Float.MIN_VALUE) {
                zRef = z;
                return;
            } else {
                float zChange = zRef - z;
                if (zChange > 0.1f) {
                    goRight();
                } else if (zChange < -0.1f) {
                    goLeft();
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}