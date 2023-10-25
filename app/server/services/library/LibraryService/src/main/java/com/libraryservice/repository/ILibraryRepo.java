package com.libraryservice.repository;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Интерфейс репозитория используется для работы с базой данных, отвечающей за библиотеки и книги
 */
public interface ILibraryRepo {
    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Library> getLibrariesByCity(String city) throws SQLException;

    /**
     * Получение списка книг в выбранной библиотеке
     * @param library_uid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Book> getBooksByLibrary(UUID library_uid) throws SQLException;

    /**
     * Взятие книги в библиотеке (уменьшение поля available_cnt)
     * @param library_uid UUID библиотеки, в которой хотим получить книгу
     * @param book_uid UUID книги, которую нужно взять в библиотеке
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void takeBook(UUID library_uid, UUID book_uid) throws SQLException, BookIsNotAvailable;

    /**
     * Возврат книги в библиотеку (увеличение поля available_cnt)
     * @param library_uid UUID библиотеки, в которую хотим вернуть книгу
     * @param book_uid UUID книги, которую нужно вернуть в библиотеку
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void returnBook(UUID library_uid, UUID book_uid) throws SQLException;

    /**
     * Проверка доступности книги в библиотеке
     * @param library_uid UUID библиотеки, в которой ищем книгу
     * @param book_uid UUID нужной книги
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public boolean isAvailable(UUID library_uid, UUID book_uid) throws SQLException;
}
