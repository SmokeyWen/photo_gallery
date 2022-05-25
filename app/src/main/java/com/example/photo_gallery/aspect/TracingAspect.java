package com.example.photo_gallery.aspect;

import android.util.Log;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import com.example.photo_gallery.Presenter.GalleryPresenter;
import com.example.photo_gallery.MainActivity;
import com.example.photo_gallery.Model.Photo;
import com.example.photo_gallery.Model.PhotoRepository;

/** Our old code base had many logs that would separately dictate the same message that photos have been
 * initiated. This aspect here encaspulates all the logs that are sent out by the different objects
 *  */
@Aspect
public class TracingAspect {

    //Pointcuts -- Method calls
    //MainActivity onCreate - Photos scanned here
    @Pointcut("call(void MainActivity.onCreate(Bundle))")
    void initialOnCreate() {}

    //GalleryPresenter construction - Photos also scanned here
    @Pointcut("call(GalleryPresenter GalleryPresenter(Activity)")
    void galleryPresenterCreation() {}

    //findPhotos() method found within GalleryPresenter - Photos scanned here at lowest level
    @Pointcut("call(ArrayList<Photo> *.findPhotos(IFilter)")
    void findPhotosTracing() {}

    //Advices
    //After app initiates
    @After("initialOnCreate()")
    public void appStarted() {
        Log.i("onCreate Trace","App started. Photo Repository populated");
    }

    //After GalleryPresenter initiates
    @After("galleryPresenterCreation()")
    public void galleryInitiated() {
        Log.i("galleryPresenter Trace","Gallery initiated. Gallery Presenter in system");
    }

    //After successful findPhotos() call
    @After("findPhotosTracing()")
    public void photosFound() {
        Log.i("findPhotos Trace","findPhotos() successfully invoked");
    }

}
