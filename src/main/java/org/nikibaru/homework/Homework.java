package org.nikibaru.homework;

import org.example.lesson3.WorkJDBC;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class Homework {
    private static String URL = "jdbc:postgresql://localhost:5432/InstagrammDataBase";
    private static String USER = "postgres";
    private static String PASSWORD = "password";

    private static Statement statement;
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement(); )
        {
            System.out.println("SUCCESS!");

            statement.execute(readSqlFile("src/main/resources/homework2_scripts/create_tables.sql"));

            insertNewUser(statement, "Ivan", "Ivan007");
            insertNewUser(statement, "Peter", "Peter007");
            insertNewUser(statement, "Stan", "Stan007");
            insertNewPost(statement, "Ivans post", "1");
            insertNewPost(statement, "Peters post", "2");
            insertNewComment(statement, "Stans comment", "1","3");
            insertNewComment(statement, "Peters comment", "1","2");
            insertNewLike(statement, "2",null,"1");


            getStats(statement);
            getUserInfo(statement, "3");

        }catch (SQLException e){
            System.out.println("Connection failed!");
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public static String readSqlFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        String commands = new BufferedReader(new FileReader(file)).lines().collect(Collectors.joining());
        return commands;
    }
    public static void insertNewUser(Statement statement, String name, String password) throws SQLException {
        Formatter form = new Formatter();
        form.format("insert into \"user\" (name, password, created_at) values ('%s','%s','%s')", name, password, new Date());
        statement.execute(form.toString());
    }

    public static void insertNewPost(Statement statement, String text, String user_id) throws  SQLException {
        Formatter form = new Formatter();
        form.format("insert into post (text, created_at, user_id) values ('%s','%s',%s)", text, new Date(), user_id);
        statement.execute(form.toString());
    }

    public static void insertNewComment(Statement statement, String text,String post_id, String user_id) throws SQLException {
        Formatter form = new Formatter();
        form.format("insert into comment (text, post_id, user_id, created_at) values ('%s',%s,%s,'%s')", text, post_id, user_id, new Date());
        statement.execute(form.toString());
    }

    public static void insertNewLike(Statement statement, String user_id,String post_id, String comment_id) throws SQLException {
        Formatter form = new Formatter();
        form.format("insert into \"like\" (user_id, post_id, comment_id) values (%s,%s,%s)", user_id, post_id, comment_id);
        statement.execute(form.toString());
    }

    public static void getStats(Statement statement) throws SQLException, FileNotFoundException {
        String command = readSqlFile("src/main/resources/homework2_scripts/get_statistics.sql");
        statement.execute(command);
        ResultSet resultSet = statement.executeQuery("select * from stats");
        resultSet.next();
        Formatter form = new Formatter();
        form.format("Statistics:" +
                    "\n\tUsers: %s " +
                    "\n\tPosts: %s " +
                    "\n\tComments: %s " +
                    "\n\tLikes: %s",
                    resultSet.getString("user_count"),
                    resultSet.getString("post_count"),
                    resultSet.getString("comment_count"),
                    resultSet.getString("like_count"));
        System.out.println(form);
    }

    public static void getUserInfo(Statement statement, String user_id) throws FileNotFoundException, SQLException {
        Formatter form = new Formatter();
        String command = readSqlFile("src/main/resources/homework2_scripts/get_user_info.sql");
        command = String.valueOf(form.format(command,user_id));
        ResultSet resultSet = statement.executeQuery(command);
        if(!resultSet.next()){
            System.out.println("Пользователь не найден");
            throw new FileNotFoundException("Oops!");
        }
        form = new Formatter();
        form.format("User Info:" +
                        "\n\tName: %s " +
                        "\n\tUserCreatedAt %s " +
                        "\n\tFirstPostCreatedAt: %s " +
                        "\n\tComments: %s",
                resultSet.getString("name"),
                resultSet.getString("UserCreatedAt"),
                resultSet.getString("FirstPost"),
                resultSet.getString("comments"));
        System.out.println(form);
    }
}
