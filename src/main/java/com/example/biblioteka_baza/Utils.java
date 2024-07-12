package com.example.biblioteka_baza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static ObservableList<String> filterBooksList(String query, DatabaseConnection dbConnection) {
        List<String> filteredBooks = dbConnection.selectBooks().stream()
                .filter(book -> book.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        return FXCollections.observableArrayList(filteredBooks);
    }

    public static Button createStyledButton(String text, String iconName) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        ImageView icon = new ImageView(new Image(Utils.class.getResourceAsStream("/com/example/biblioteka_baza/icons/" + iconName)));
        icon.setFitWidth(16);
        icon.setFitHeight(16);
        button.setGraphic(icon);
        return button;
    }

    public static TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add("styled-text-field");
        return textField;
    }
}
