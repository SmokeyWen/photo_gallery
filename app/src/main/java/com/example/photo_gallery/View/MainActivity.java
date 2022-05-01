package com.example.photo_gallery.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements GalleryPresenter.View, View.OnClickListener{
    GalleryPresenter presenter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new GalleryPresenter(this);
    }
    public void onClick( View v) {
        String caption = ((EditText) findViewById(R.id.etCaption)).getText().toString();
        switch (v.getId()) {
            case R.id.btnLeft:
                presenter.handleNavigationInput("ScrollLeft", caption);
                break;
            case R.id.btnRight:
                presenter.handleNavigationInput("ScrollRight", caption);
                break;
            default:
                break;
        }
    }
    public void takePhoto(View v) {
        presenter.takePhoto();
    }
    private View.OnClickListener searchListener = new View.OnClickListener() {
        public void onClick(View v) {
            presenter.search();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onReturn(requestCode, resultCode, data);
    }
    @Override
    public void displayPhoto(Bitmap photo, String timestamp, String caption, boolean isFirst, boolean isLast ) { }
    public void uploadPhoto(View v) {
    }
    public void editSettings(View v) {
    }
}
