package org.helmo.planclap_admin.presentations;

import java.util.List;

public interface MovieView {
    void showMovies(List<MovieViewModel> movies, String date, int heures, int minutes);
}

