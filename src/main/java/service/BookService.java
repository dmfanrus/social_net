package service;

import model.Book;

import java.io.FileNotFoundException;

public interface BookService {
    Book findBookByListFileName(int id, String filename, String author, String title) throws FileNotFoundException;
}
