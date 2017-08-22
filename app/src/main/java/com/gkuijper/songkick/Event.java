package com.gkuijper.songkick;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gabrielle on 06-08-17.
 */

public class Event implements Serializable{
    private String name, time, type, venue;
    private String startdate, enddate;
    private double price;
    private City city;
    private ArrayList<Performance> performances;
    private String eventID;
    private boolean isSaved;

    public Event(String name, String time, String type, String venue, String startdate, String enddate, ArrayList<Performance> performances, String eventid) {
        this.name = name;
        this.time = time;
        this.type = type;
        this.venue = venue;
        this.startdate = startdate;
        this.enddate = enddate;
        this.performances = performances;
        this.eventID = eventid;
    }


    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<Performance> getPerformances() {
        return performances;
    }

    public void setPerformances(ArrayList<Performance> performances) {
        this.performances = performances;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", venue='" + venue + '\'' +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", price=" + price +
                ", city=" + city +
                ", performances=" + performances +
                '}';
    }
}
