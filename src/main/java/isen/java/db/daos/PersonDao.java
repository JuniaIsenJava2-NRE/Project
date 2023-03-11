package isen.java.db.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import isen.java.db.DataSourceFactory;
import isen.java.db.entities.Person;

public class PersonDao {

    public List<Person> listPeople() {
        List<Person> people = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM person")) {
                    while (resultSet.next()) {
                        Person person = new Person();
                        person.setId(resultSet.getInt("idperson"));
                        person.setLastname(resultSet.getString("lastname"));
                        person.setFirstname(resultSet.getString("firstname"));
                        person.setNickname(resultSet.getString("nickname"));
                        person.setPhoneNumber(resultSet.getString("phone_number"));
                        person.setAddress(resultSet.getString("address"));
                        person.setEmailAddress(resultSet.getString("email_address"));
                        person.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                        people.add(person);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return people;
    }

    public Person addPerson(Person person) {
        Person addedPerson = new Person();

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO person (lastname, firstname, nickname, phone_number, address, email_address, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)) {

                // Set values for the prepared statement parameters
                statement.setString(1, person.getLastname());
                statement.setString(2, person.getFirstname());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setDate(7, Date.valueOf(person.getBirthDate()));

                // Execute the statement
                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Adding person failed, no rows affected.");
                }

                // Retrieve the generated id from the statement and set it to the addedPerson object
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        addedPerson.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Adding person failed, no id obtained.");
                    }
                }

                // Set the remaining values to the addedPerson object
                addedPerson.setLastname(person.getLastname());
                addedPerson.setFirstname(person.getFirstname());
                addedPerson.setNickname(person.getNickname());
                addedPerson.setPhoneNumber(person.getPhoneNumber());
                addedPerson.setAddress(person.getAddress());
                addedPerson.setEmailAddress(person.getEmailAddress());
                addedPerson.setBirthDate(person.getBirthDate());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return addedPerson;
    }

    public Boolean updatePerson(Person person) {
        Boolean success = false;

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE person SET lastname=?, firstname=?, nickname=?, phone_number=?, address=?, email_address=?, birth_date=? WHERE idperson=?")) {

                statement.setString(1, person.getLastname());
                statement.setString(2, person.getFirstname());
                statement.setString(3, person.getNickname());
                statement.setString(4, person.getPhoneNumber());
                statement.setString(5, person.getAddress());
                statement.setString(6, person.getEmailAddress());
                statement.setObject(7, Date.valueOf(person.getBirthDate()));
                statement.setLong(8, person.getId());

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    success = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }

    public Person deletePerson(Integer id) {
        Person deletedPerson = null;

        try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM person WHERE idperson = ? RETURNING idperson, lastname, firstname, nickname, phone_number, address, email_address, birth_date")) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        deletedPerson = new Person();
                        deletedPerson.setId(resultSet.getInt("idperson"));
                        deletedPerson.setLastname(resultSet.getString("lastname"));
                        deletedPerson.setFirstname(resultSet.getString("firstname"));
                        deletedPerson.setNickname(resultSet.getString("nickname"));
                        deletedPerson.setPhoneNumber(resultSet.getString("phone_number"));
                        deletedPerson.setAddress(resultSet.getString("address"));
                        deletedPerson.setEmailAddress(resultSet.getString("email_address"));
                        deletedPerson.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return deletedPerson;
    }
}
