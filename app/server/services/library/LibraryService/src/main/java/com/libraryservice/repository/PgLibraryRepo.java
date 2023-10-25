package com.libraryservice.repository;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.Condition;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Репозиторий используется для работы с таблицей places базы данных PostgreSQL
 * Для подключения используется драйвер JDBC
 */
@Repository
public class PgLibraryRepo implements ILibraryRepo {
    /**
     * Объект подключения к БД
     */
    private final Connection conn = ConnectionManager.open();

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public ArrayList<Library> getLibrariesByCity(String city) throws SQLException {
        ArrayList<Library> libs = new ArrayList<>();

        String getLibs = "SELECT id, library_uid, name, city, address " +
                "FROM public.library " +
                "WHERE city = ?";

        PreparedStatement librariesQuery = conn.prepareStatement(getLibs);
        librariesQuery.setString(1, city);
        ResultSet rs = librariesQuery.executeQuery();

        while (rs.next())
        {
            Library lib = new Library(rs.getInt("id"),
                                        rs.getObject("library_uid", java.util.UUID.class),
                                        rs.getString("name"), rs.getString("city"),
                                        rs.getString("address"));
            libs.add(lib);
        }

        return libs;
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param library_uid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public ArrayList<Book> getBooksByLibrary(UUID library_uid) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        String getBooks = "SELECT b.id, b.book_uid, b.name, author, genre, condition, lb.available_count " +
                "FROM public.books b JOIN public.library_books lb ON b.id = lb.book_id " +
                "JOIN public.library l ON lb.library_id = l.id " +
                "WHERE l.library_uid = ?";

        PreparedStatement booksQuery = conn.prepareStatement(getBooks);
        booksQuery.setObject(1, library_uid);
        ResultSet rs = booksQuery.executeQuery();

        while (rs.next())
        {
            Book b = new Book(rs.getInt("id"),
                    rs.getObject("book_uid", java.util.UUID.class),
                    rs.getString("name"), rs.getString("author"),
                    rs.getString("genre"), Condition.valueOf(rs.getString("condition")),
                    rs.getInt("available_count"));
            books.add(b);
        }

        return books;
    }

    /**
     * Взятие книги в библиотеке (уменьшение поля available_cnt)
     * @param library_uid UUID библиотеки, в которой хотим получить книгу
     * @param book_uid UUID книги, которую нужно взять в библиотеке
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void takeBook(UUID library_uid, UUID book_uid) throws SQLException, BookIsNotAvailable {
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        if (isAvailable(library_uid, book_uid))
            throw new BookIsNotAvailable("Нет свободной книги в выбранной библиотеке");

        String decCnt = "UPDATE public.library_books " +
                "SET available_count = available_count - 1 " +
                "WHERE (book_id, library_id) IN " +
                "(SELECT b.id, l.id  " +
                "FROM public.books b JOIN public.library_books lb ON b.id = lb.book_id " +
                "JOIN public.library l ON lb.library_id = l.id " +
                "WHERE l.library_uid = ? AND b.book_uid = ?)";

        PreparedStatement updAvailable = conn.prepareStatement(decCnt);
        updAvailable.setObject(1, library_uid);
        updAvailable.setObject(2, book_uid);
        updAvailable.executeUpdate();

        conn.commit();
        conn.setAutoCommit(true);
    }

    /**
     * Возврат книги в библиотеку (увеличение поля available_cnt)
     * @param library_uid UUID библиотеки, в которую хотим вернуть книгу
     * @param book_uid UUID книги, которую нужно вернуть в библиотеку
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void returnBook(UUID library_uid, UUID book_uid) throws SQLException {
        String incCnt = "UPDATE public.library_books " +
                "SET available_count = available_count + 1 " +
                "WHERE (book_id, library_id) IN " +
                "(SELECT b.id, l.id  " +
                "FROM public.books b JOIN public.library_books lb ON b.id = lb.book_id " +
                "JOIN public.library l ON lb.library_id = l.id " +
                "WHERE l.library_uid = ? AND b.book_uid = ?)";

        PreparedStatement updAvailable = conn.prepareStatement(incCnt);
        updAvailable.setObject(1, library_uid);
        updAvailable.setObject(2, book_uid);
        updAvailable.executeUpdate();
    }

    /**
     * Проверка доступности книги в библиотеке
     * @param library_uid UUID библиотеки, в которой ищем книгу
     * @param book_uid UUID нужной книги
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public boolean isAvailable(UUID library_uid, UUID book_uid) throws SQLException {
        String getBooks = "SELECT lb.available_count " +
                "FROM public.books b JOIN public.library_books lb ON b.id = lb.book_id " +
                "JOIN public.library l ON lb.library_id = l.id " +
                "WHERE l.library_uid = ? and b.book_uid = ?";

        PreparedStatement booksQuery = conn.prepareStatement(getBooks);
        booksQuery.setObject(1, library_uid);
        booksQuery.setObject(2, book_uid);
        ResultSet rs = booksQuery.executeQuery();

        int cnt = 0;

        if (rs.next())
            cnt = rs.getInt("available_count");

        return cnt > 0;
    }
}
