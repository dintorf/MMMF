package dintorf.mmmf;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by dintorf on 11/13/14.
 */
public class MainApplication extends Application {
    private static MainApplication instance = new MainApplication();

    public MainApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "T2Pi3dg49Na7727OvCMjl8aETNiBLSjY20W3eKqJ", "ODvALDTEkqKdBtElcuihd1oE8v48ygK0jJHRbC7d");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
