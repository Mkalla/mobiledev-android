package hdm.csm.emergency;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Manuel on 25/04/16.
 */
public class Report implements Serializable {
    private Date date;
    private Location location;
    private User user;
    private String roadReportComment;
    private ArrayList<String> roadReportImageURIs = new ArrayList<String>();
    private String vehicleReportComment;
    private ArrayList<String> vehicleReportImageURIs = new ArrayList<String>();
    private String witnessReportComment;
    private ArrayList<String> witnessReportImageURIs = new ArrayList<String>();
    private String weatherType = "";
    private String temperature = "";

    public Report() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoadReportComment() {
        return roadReportComment;
    }

    public void setRoadReportComment(String roadReportComment) {
        this.roadReportComment = roadReportComment;
    }

    public ArrayList<String> getRoadReportImageURIs() {
        return roadReportImageURIs;
    }

    public void setRoadReportImageURIs(ArrayList<String> roadReportImageURIs) {
        this.roadReportImageURIs = roadReportImageURIs;
    }

    public String getVehicleReportComment() {
        return vehicleReportComment;
    }

    public void setVehicleReportComment(String vehicleReportComment) {
        this.vehicleReportComment = vehicleReportComment;
    }

    public ArrayList<String> getVehicleReportImageURIs() {
        return vehicleReportImageURIs;
    }

    public void setVehicleReportImageURIs(ArrayList<String> vehicleReportImageURIs) {
        this.vehicleReportImageURIs = vehicleReportImageURIs;
    }

    public String getWitnessReportComment() {
        return witnessReportComment;
    }

    public void setWitnessReportComment(String witnessReportComment) {
        this.witnessReportComment = witnessReportComment;
    }

    public ArrayList<String> getWitnessReportImageURIs() {
        return witnessReportImageURIs;
    }

    public void setWitnessReportImageURIs(ArrayList<String> witnessReportImageURIs) {
        this.witnessReportImageURIs = witnessReportImageURIs;
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
