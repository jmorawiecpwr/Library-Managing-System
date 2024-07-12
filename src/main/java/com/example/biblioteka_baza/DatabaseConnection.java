package com.example.biblioteka_baza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    // Method to connect to the SQLite database
    public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:lib.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Połączenie z bazą danych zostało nawiązane.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Method to create a new table
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books (\n"
                + " id integer PRIMARY KEY,\n"
                + " title text NOT NULL,\n"
                + " author text NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela została utworzona.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to insert a new book
    public static void insertBook(String title, String author) {
        String sql = "INSERT INTO books(title, author) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.executeUpdate();
            System.out.println("Książka została dodana.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to select all books
    public static List<String> selectBooks() {
        String sql = "SELECT id, title, author FROM books";
        List<String> books = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(rs.getInt("id") + "\t" +
                        rs.getString("title") + "\t" +
                        rs.getString("author"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    // Method to update a book
    public static void updateBook(int id, String title, String author) {
        String sql = "UPDATE books SET title = ?, author = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Dane książki zostały zaktualizowane.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to delete a book
    public static void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Książka została usunięta.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
        createTable();
//        insertBook("1984", "George Orwell");
//        List<String> books = selectBooks();
//        for (String book : books) {
//            System.out.println(book);
//        }
//        updateBook(1, "1984", "George Orwell");
//        deleteBook(4);
    }
}