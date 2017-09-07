package service.impl;

import model.Book;
import service.BookService;

import java.io.FileNotFoundException;


public class BookServiceImpl implements BookService {

    @Override
    public Book findBookByListFileName(int id, String filename, String author, String title) throws FileNotFoundException {
        return new Book(id,filename, author, title, FileWorker.read(filename));
    }
}
