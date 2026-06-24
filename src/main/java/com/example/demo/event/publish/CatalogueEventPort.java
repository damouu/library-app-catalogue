package com.example.demo.event.publish;

import com.example.demo.model.Chapter;
import com.example.demo.model.Series;


public interface CatalogueEventPort {

    void publishChapterCreated(Chapter chapter, int initial_copies_count);

    void publishSeriesCreated(Series series);
}
