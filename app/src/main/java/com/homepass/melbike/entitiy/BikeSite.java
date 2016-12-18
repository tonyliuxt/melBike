package com.homepass.melbike.entitiy;

/**
 * Created by Xintian on 2016/12/7.
 */

public class BikeSite {
    private String featurename; //:"Southern Cross Station - Spencer St - City",
    private Integer id; //:"29",
    private Integer nbbikes; // :"12",
    private Integer nbemptydoc; //:"14",
    private Integer terminalname; //:"60034",
    private String uploaddate; //:"2016-12-08T16:00:06.000"
    private Coordinates coordinates;

    public String getFeaturename() {
        return featurename;
    }

    public void setFeaturename(String featurename) {
        this.featurename = featurename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNbbikes() {
        return nbbikes;
    }

    public void setNbbikes(Integer nbbikes) {
        this.nbbikes = nbbikes;
    }

    public Integer getNbemptydoc() {
        return nbemptydoc;
    }

    public void setNbemptydoc(Integer nbemptydoc) {
        this.nbemptydoc = nbemptydoc;
    }

    public Integer getTerminalname() {
        return terminalname;
    }

    public void setTerminalname(Integer terminalname) {
        this.terminalname = terminalname;
    }

    public String getUploaddate() {
        return uploaddate;
    }

    public void setUploaddate(String uploaddate) {
        this.uploaddate = uploaddate;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

}
