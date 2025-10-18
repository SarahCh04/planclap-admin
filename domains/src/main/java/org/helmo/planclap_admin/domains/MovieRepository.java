package org.helmo.planclap_admin.domains;

import java.util.List;

public interface MovieRepository {
    List<Movie> loadMovies();
}
