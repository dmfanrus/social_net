package model;

import lombok.Value;

/**
 * Created by Михаил on 05.01.2017.
 */
@Value
public class Book {
    int id;
    String name_file;
    String author;
    String title;
    String context;
}
