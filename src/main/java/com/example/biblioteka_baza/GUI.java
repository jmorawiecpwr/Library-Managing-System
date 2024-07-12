package com.example.biblioteka_baza;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.stream.Collectors;

public class GUI extends Application {
    private DatabaseConnection dbConnection;
    private ObservableList<String> booksList;
    private TextField searchField;
    private ComboBox<String> sortComboBox;
    private boolean inEditMode = false;
    private Button edytujButton;
    private Button zatwierdzButton;
    private Button odrzucButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rejestr biblioteczny");

        dbConnection = new DatabaseConnection();

        double iconSize = 20;

        Image dodajIcon = new Image(getClass().getResourceAsStream("/com/example/biblioteka_baza/icons/add.png"));
        ImageView dodajIconView = new ImageView(dodajIcon);
        dodajIconView.setFitWidth(iconSize);
        dodajIconView.setFitHeight(iconSize);
        Button dodajButton = new Button("Dodaj", dodajIconView);

        Image edytujIcon = new Image(getClass().getResourceAsStream("/com/example/biblioteka_baza/icons/edit.png"));
        ImageView edytujIconView = new ImageView(edytujIcon);
        edytujIconView.setFitWidth(iconSize);
        edytujIconView.setFitHeight(iconSize);
        edytujButton = new Button("Edytuj", edytujIconView);

        Image usunIcon = new Image(getClass().getResourceAsStream("/com/example/biblioteka_baza/icons/delete.png"));
        ImageView usunIconView = new ImageView(usunIcon);
        usunIconView.setFitWidth(iconSize);
        usunIconView.setFitHeight(iconSize);
        Button usunButton = new Button("Usuń", usunIconView);

        Image zatwierdzIcon = new Image(getClass().getResourceAsStream("/com/example/biblioteka_baza/icons/save.png"));
        ImageView zatwierdzIconView = new ImageView(zatwierdzIcon);
        zatwierdzIconView.setFitWidth(iconSize);
        zatwierdzIconView.setFitHeight(iconSize);
        zatwierdzButton = new Button("Zatwierdź zmiany", zatwierdzIconView);

        Image odrzucIcon = new Image(getClass().getResourceAsStream("/com/example/biblioteka_baza/icons/cancel.png"));
        ImageView odrzucIconView = new ImageView(odrzucIcon);
        odrzucIconView.setFitWidth(iconSize);
        odrzucIconView.setFitHeight(iconSize);
        odrzucButton = new Button("Odrzuć zmiany", odrzucIconView);


        TextField tytułField = new TextField();
        tytułField.setPromptText("Tytuł");

        TextField autorField = new TextField();
        autorField.setPromptText("Autor");

        booksList = FXCollections.observableArrayList();

        ListView<String> booksListView = new ListView<>(booksList);

        searchField = new TextField();
        searchField.setPromptText("Wyszukaj książki");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterBooksList(newValue));

        sortComboBox = new ComboBox<>();
        sortComboBox.setItems(FXCollections.observableArrayList("ID", "Tytuł", "Autor"));
        sortComboBox.getSelectionModel().selectFirst();
        sortComboBox.setOnAction(e -> refreshBooksList());

        zatwierdzButton = new Button("Zatwierdź zmiany");
        zatwierdzButton.setDisable(true);
        odrzucButton = new Button("Odrzuć zmiany");
        odrzucButton.setDisable(true);

        dodajButton.setOnAction(e -> {
            String tytuł = tytułField.getText();
            String autor = autorField.getText();
            if (!tytuł.isEmpty() && !autor.isEmpty()) {
                dbConnection.insertBook(tytuł, autor);
                tytułField.clear();
                autorField.clear();
                refreshBooksList();
            } else {
                showAlert("Błąd walidacji", "Tytuł i autor nie mogą być puste.");
            }
        });

        zatwierdzButton.setOnAction(e -> {
            // Pobieramy zaznaczoną książkę z listy
            String selectedBook = booksListView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                String[] parts = selectedBook.split("\t");
                int id = Integer.parseInt(parts[0]);
                String tytuł = tytułField.getText();
                String autor = autorField.getText();
                if (!tytuł.isEmpty() && !autor.isEmpty()) {
                    dbConnection.updateBook(id, tytuł, autor);
                    tytułField.clear();
                    autorField.clear();
                    // Przywracamy przycisk "Edytuj"
                    VBox mainLayout = (VBox) primaryStage.getScene().getRoot();
                    HBox buttonsBox = (HBox) mainLayout.getChildren().get(1);
                    buttonsBox.getChildren().add(edytujButton);
                    // Usuwamy przyciski "Zatwierdź zmiany" i "Odrzuć zmiany"
                    buttonsBox.getChildren().removeAll(zatwierdzButton, odrzucButton);
                    inEditMode = false;
                    refreshBooksList();
                } else {
                    showAlert("Błąd walidacji", "Tytuł i autor nie mogą być puste.");
                }
            }
        });

        odrzucButton.setOnAction(e -> {
            // Anulowanie edycji, czyli przywrócenie wartości wybranego elementu na liście
            String selectedBook = booksListView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                String[] parts = selectedBook.split("\t");
                tytułField.setText(parts[1]);
                autorField.setText(parts[2]);
                // Przywracamy przycisk "Edytuj"
                VBox mainLayout = (VBox) primaryStage.getScene().getRoot();
                HBox buttonsBox = (HBox) mainLayout.getChildren().get(1);
                buttonsBox.getChildren().add(edytujButton);
                // Usuwamy przyciski "Zatwierdź zmiany" i "Odrzuć zmiany"
                buttonsBox.getChildren().removeAll(zatwierdzButton, odrzucButton);
                inEditMode = true;
            }
        });

        usunButton.setOnAction(e -> {
            String selectedBook = booksListView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                String[] parts = selectedBook.split("\t");
                int id = Integer.parseInt(parts[0]);
                dbConnection.deleteBook(id);
                refreshBooksList();
            }
        });

        edytujButton.setOnAction(e -> {
            edytujButton.setDisable(true);
            zatwierdzButton.setDisable(false);
            odrzucButton.setDisable(false);
            // Włączamy tryb edycji
            inEditMode = true;

            // Usuwamy przycisk "Edytuj" z dolnego paska
            HBox buttonsBox = (HBox) ((VBox) primaryStage.getScene().getRoot()).getChildren().get(1);
            buttonsBox.getChildren().remove(edytujButton);
            // Dodajemy przyciski "Zatwierdź zmiany" i "Odrzuć zmiany" na dolny pasek
            buttonsBox.getChildren().addAll(zatwierdzButton, odrzucButton);
        });

        booksListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                edytujButton.setDisable(false);
                usunButton.setDisable(false);
                if (!inEditMode) {
                    String[] parts = newValue.split("\t");
                    tytułField.setText(parts[1]);
                    autorField.setText(parts[2]);
                }
            } else {
                edytujButton.setDisable(true);
                usunButton.setDisable(true);
            }
        });

        VBox inputBox = new VBox(10);
        inputBox.getChildren().addAll(
                new HBox(new Label("Tytuł: "), tytułField),
                new HBox(new Label("Autor: "), autorField),
                new HBox(dodajButton)
        );

        VBox searchAndSortBox = new VBox(10);
        searchAndSortBox.getChildren().addAll(searchField, sortComboBox);
        VBox root = new VBox(10);
        root.getChildren().addAll(
                inputBox,
                searchAndSortBox,
                new Label("Lista książek:"), booksListView
        );

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(edytujButton, usunButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(root, buttonsBox);

        Scene scene = new Scene(mainLayout, 600, 600);
        scene.getStylesheets().add(getClass().getResource("/com/example/biblioteka_baza/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();

        refreshBooksList();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (!tytułField.getText().isEmpty() && !autorField.getText().isEmpty()) {
                    dodajButton.fire();
                }
            }
        });
    }

    private void refreshBooksList() {
        List<String> books = dbConnection.selectBooks();
        String sortCriteria = sortComboBox.getValue();
        if (sortCriteria.equals("ID")) {
            booksList.setAll(books);
        } else if (sortCriteria.equals("Tytuł")) {
            booksList.setAll(books.stream()
                    .sorted((b1, b2) -> b1.split("\t")[1].compareToIgnoreCase(b2.split("\t")[1]))
                    .collect(Collectors.toList()));
        } else if (sortCriteria.equals("Autor")) {
            booksList.setAll(books.stream()
                    .sorted((b1, b2) -> b1.split("\t")[2].compareToIgnoreCase(b2.split("\t")[2]))
                    .collect(Collectors.toList()));
        }
        filterBooksList(searchField.getText());
    }

    private void filterBooksList(String query) {
        List<String> filteredBooks = dbConnection.selectBooks().stream()
                .filter(book -> book.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        booksList.setAll(filteredBooks);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
