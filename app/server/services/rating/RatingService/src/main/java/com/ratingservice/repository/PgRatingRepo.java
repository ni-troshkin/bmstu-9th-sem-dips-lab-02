package com.ratingservice.repository;

import com.ratingservice.entity.Rating;
import com.ratingservice.utils.ConnectionManager;
import com.ratingservice.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/**
 * Репозиторий используется для работы с таблицей places базы данных PostgreSQL
 * Для подключения используется драйвер JDBC
 */
@Repository
public class PgRatingRepo implements IRatingRepo {
    /**
     * Объект подключения к БД
     */
    private final Connection conn = ConnectionManager.open();

    /**
     * Получение рейтинга пользователя по его имени
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число от 0 до 100, равное рейтингу пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public int getRatingByUsername(String username) throws SQLException {
        int rating;

        String getRating = "SELECT stars FROM public.rating " +
                "WHERE username = ?";

        PreparedStatement getRatingQuery = conn.prepareStatement(getRating);
        getRatingQuery.setString(1, username);
        ResultSet rs = getRatingQuery.executeQuery();

        if (rs.next())
            return rs.getInt("stars");

        return 0;
    }

    /**
     * Изменение рейтинга пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param delta численное изменение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void updateRating(String username, int delta) throws SQLException {
        String ratingUpd = "UPDATE public.rating SET stars = " +
                "CASE WHEN stars + ? < 0 THEN 0" +
                "   WHEN stars + ? > 100 THEN 100" +
                "   ELSE stars + ? " +
                "WHERE username = ?";

        PreparedStatement ratingUpdate = conn.prepareStatement(ratingUpd);
        ratingUpdate.setInt(1, delta);
        ratingUpdate.setInt(2, delta);
        ratingUpdate.setInt(3, delta);
        ratingUpdate.setString(4, username);
        ratingUpdate.executeUpdate();
    }
}
