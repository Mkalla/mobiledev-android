package hdm.csm.emergency;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Manuel on 25/04/16.
 */
public class DataManager {

    //Singleton to use same object in every activity
    private static DataManager instance;
    Context context;
    User user;
    String weatherType;
    String temperature;

    private DataManager(Context ctx) {
        this.context = ctx;
        this.user = User.getInstance(ctx);
    }

    public static DataManager getInstance(Context context) {

        //Check if there is a user instance
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public void saveReport(String fileName, Report report) {


        //add path to users shared pref
        user.addPath(fileName);
        user.saveInstance();

        //Save to share preds
        saveObjectToInternal(fileName, report);
    }


    public void saveObjectToInternal(String fileName, Object object) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(object);

            oos.close();
            fos.close();
            Log.i("DataManager", "Saving Success");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DataManager", "Saving Failed");
        }
    }

    public Object readObjectFromMemory(String fileName) {
        Object object = null;

        FileInputStream fis;
        try {
            fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();
            ois.close();
            fis.close();
            Log.i("DataManager", "Reading Success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DataManager", "Reading Failed");
        }

        return object;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}
