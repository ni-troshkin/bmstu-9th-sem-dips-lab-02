package com.libraryservice.service;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.repository.ILibraryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Класс в слое сервисов, обращающийся к репозиторию рейтингов
 */
@Service
public class LibraryService {
    /**
     * Репозиторий, работающий с рейтингами
     */
    @Autowired
    private final ILibraryRepo repo;

    public LibraryService(ILibraryRepo repo) {
        this.repo = repo;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Library> getLibrariesByCity(String city) throws SQLException {
       return repo.getLibrariesByCity(city);
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param library_uid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Book> getBooksByLibrary(UUID library_uid) throws SQLException {
        return repo.getBooksByLibrary(library_uid);
    }

    /**
     * Взятие книги в библиотеке (уменьшение поля available_cnt)
     * @param library_uid UUID библиотеки, в которой хотим получить книгу
     * @param book_uid UUID книги, которую нужно взять в библиотеке
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void takeBook(UUID library_uid, UUID book_uid) throws SQLException, BookIsNotAvailable {
        repo.takeBook(library_uid, book_uid);
    }

    /**
     * Возврат книги в библиотеку (увеличение поля available_cnt)
     * @param library_uid UUID библиотеки, в которую хотим вернуть книгу
     * @param book_uid UUID книги, которую нужно вернуть в библиотеку
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void returnBook(UUID library_uid, UUID book_uid) throws SQLException {
        repo.returnBook(library_uid, book_uid);
    }
}
