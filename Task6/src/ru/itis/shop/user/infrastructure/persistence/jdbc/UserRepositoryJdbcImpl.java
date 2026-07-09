package ru.itis.shop.user.infrastructure.persistence.jdbc;

import ru.itis.shop.infrastructure.persistence.jdbc.RowMapper;
import ru.itis.shop.user.domain.User;
import ru.itis.shop.user.repository.UserRepository;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository {

    private final DataSource dataSource;

    private final RowMapper<User> userRowMapper = row -> new User(
            row.getInt("id"),
            row.getString("name"),
            row.getString("email"),
            row.getString("password"),
            row.getString("profileDescription")
    );

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(User user) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "insert into account(name, email, password, profileDescription) values (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getProfileDescription());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Не смогли добавить юзера");
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        try (Connection connection = dataSource.getConnection()) {
            String sql = "select * from account where email = ?";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(sql)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(userRowMapper.mapRow(resultSet));
                    }
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Integer id) {

        try (Connection connection = dataSource.getConnection()) {
            String sql = "select * from account where id = ?";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return Optional.of(userRowMapper.mapRow(resultSet));
                    }
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("select * from account")) {
                    while (resultSet.next()) {
                        users.add(userRowMapper.mapRow(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return users;
    }

    @Override
    public List<User> findAllByProfileDescription(String profileDescription) {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            String sql = "select * from account where profileDescription = ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, profileDescription);

                try (ResultSet resultSet = statement.executeQuery()) {

                    while (resultSet.next()) {
                        users.add(userRowMapper.mapRow(resultSet));
                    }

                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return users;
    }

    @Override
    public void update(User user) {

        try (Connection connection = dataSource.getConnection()) {

            String sql = "update account set profileDescription = ? where email = ?";

            try (PreparedStatement preparedStatement =
                         connection.prepareStatement(sql)) {

                preparedStatement.setString(1, user.getProfileDescription());
                preparedStatement.setString(2, user.getEmail());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows != 1) {
                    throw new SQLException("Can't update user");
                }

            }

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
