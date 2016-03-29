package hdm.csm.emergency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class User {
    private String title;
    private String forename;
    private String middlenames;
    private String surname;
    private String birthday;
    private String address1;
    private String address2;
    private String city;
    private String county;
    private String email;
    private String tel;
    private String mobile;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor prefsEditor;
    private static Gson gson = new Gson();


    //Singleton to use same object in every activity

    private static User instance;

    private User() {
    }

    public static User getInstance(Context context) {

        //Check if there is a user instance
        if (instance == null)
        {
            //If not: check if there is a user saved in shared pref
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            prefsEditor = sharedPreferences.edit();
            String json = sharedPreferences.getString("myUser", null);

            if (json != null) {
                instance = gson.fromJson(json, User.class);
            } else {
                //If not: create Instance
                instance = new User();
            }
        }
        return instance;
    }

    public void saveInstance(){
        String json = gson.toJson(instance);
        prefsEditor.putString("myUser", json);
        prefsEditor.commit();
    }

    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getMiddlenames() {
        return middlenames;
    }

    public void setMiddlenames(String middlenames) {
        this.middlenames = middlenames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
