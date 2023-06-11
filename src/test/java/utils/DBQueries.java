package utils;

import Persons.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBQueries {
    private final static String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final static String USER_NAME = "postUser";
    private final static String USER_PASSWORD = "notUsualPassword";
    private final static String SELECTQUERY = "select * from person";
    private final static String INSERTQUERY = "insert into person values(?,?,?,?)";
    private final static String UPDATEQUERY = "update person set age=? where id=?";
    private final static String DELETEQUERY = "delete from person where id=?";

//    private static Connection connectionDB(){
//        try (Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)){
////            Statement sqlStatement = connection.createStatement();
//            return connection;
//        }catch (SQLException exception){
//            throw new RuntimeException("Check DB connection = " + URL);
//        }
//    }

    public static List<Person> selectFromDB(String query) {
        List<Person> personList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
//            Statement sqlStatement = connection.createStatement();
//            ResultSet resultSet = sqlStatement.executeQuery(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Person person = new Person(resultSet.getString("name"),
                        resultSet.getInt("age"), resultSet.getString("city"));
                personList.add(person);
            }
        }catch (SQLException exception){
            throw new RuntimeException("Check query request on data or/and type consistency = " + query);
        }
        return personList;
    }

    public static List<Person> insertToDB(String query, String name, int age, String city) {
        List<Person> personList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
//            Statement sqlStatement = connection.createStatement();
//            ResultSet resultSet = sqlStatement.executeQuery(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getLastIndex()+1);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, city);

            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new RuntimeException("Check query request on data or/and type consistency = " + query);
        }
        return personList;
    }

    public static List<Person> updateLastRowToDB(String query, int age) {
        List<Person> personList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
//            Statement sqlStatement = connection.createStatement();
//            ResultSet resultSet = sqlStatement.executeQuery(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, age);
            preparedStatement.setInt(2, getLastIndex());

            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new RuntimeException("Check query request on data or/and type consistency = " + query);
        }
        return personList;
    }

    public static List<Person> deleteLastRowToDB(String query) {
        List<Person> personList = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
//            Statement sqlStatement = connection.createStatement();
//            ResultSet resultSet = sqlStatement.executeQuery(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, getLastIndex());

            preparedStatement.executeUpdate();
        }catch (SQLException exception){
            throw new RuntimeException("Check query request on data or/and type consistency = " + query);
        }
        return personList;
    }

    public static int getLastIndex (){
        try(Connection connection = DriverManager.getConnection(URL, USER_NAME, USER_PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECTQUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            return count;
        }catch (SQLException exception){
            throw new RuntimeException("Check query request on data or/and type consistency = " + SELECTQUERY);
        }
    }




    public static void main(String[] args) {
        List<Person> listBefore = selectFromDB(SELECTQUERY);
        insertToDB(INSERTQUERY, "TestName1", 10, "TestCity1");
        updateLastRowToDB(UPDATEQUERY, 666);
        deleteLastRowToDB(DELETEQUERY);
        List<Person> listAfter = selectFromDB(SELECTQUERY);
    }
}
