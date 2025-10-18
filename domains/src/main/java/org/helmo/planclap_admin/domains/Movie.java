package org.helmo.planclap_admin.domains;

public class Movie {
    private String title;
    private int duration; // minutes
    private int seances;

    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public int getSeances() { return seances; }

    public String getDurationHHMM() {
        int h = duration / 60;
        int m = duration % 60;
        return String.format("%d h %02d", h, m);
    }
}
