package ru.myagkiy.springmvc.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class PersonDAO {
    public static int PERSON_COUNT;
    private static Connection connection;
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

        static {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/first_db", "postgres", "1029384756");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    public Person show(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Select * FROM Person p where p.id = ?");
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            Person person = new Person();
            rs.next();
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            person.setAge(rs.getInt("age"));
            person.setEmail(rs.getString("email"));

            return person;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPerson(Person person) {
        person.setId(++PERSON_COUNT);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Person VALUES(?,?,?,?)");
            preparedStatement.setInt(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Запрос через Statment, отрабатывает дольше, неудобный и можно совершить sql инъекцию
//        try {
//            Statement statement = connection.createStatement();
//            String SQL = "INSERT INTO Person VALUES("+ person.getId() + ",'" + person.getName() +"','" + person.getAge() + "','" + person.getEmail() + "')";
//            statement.executeUpdate(SQL);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

    }

    public void editPerson(Person personUpdate, int id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE Person set name = ?, age = ?, email = ? where id = ?");
            preparedStatement.setString(1, personUpdate.getName());
            preparedStatement.setInt(2, personUpdate.getAge());
            preparedStatement.setString(3, personUpdate.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePerson(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE From PERSON where id = ?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
