package com.musigma.controllers.workspaces;

import com.musigma.controllers.WorkspaceController;
import com.musigma.models.Stock;
import com.musigma.models.exception.StockException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class StockController extends WorkspaceController {
    public static WorkspaceRegister REGISTER = new WorkspaceRegister(
            "Stock",
            "/com/musigma/images/icons/stock.png",
            "/com/musigma/views/stock-view.fxml"
    );

    @FXML
    TextField textFieldObjet, textFieldQuantite, textFieldPrix;

    @FXML
    Button ajouterButton;

    @FXML
    TableView<Stock> tableView;

    @FXML
    public void initialize() {
        handleTextField();
        addListener();
        ajouterButton.setOnAction(e -> {
            try {
                onAjouterPressed();
            } catch (StockException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    public void addListener(){
        textFieldObjet.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().equals("Objet") || newValue.trim().isEmpty() || newValue.trim().isBlank() || newValue.matches(".*[^a-zA-Z-\\s].*")) {
                textFieldObjet.setStyle("-fx-border-color: crimson;");
            } else {
                textFieldObjet.setStyle("-fx-border-color: transparent;");
            }
        });

        textFieldPrix.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty() || newValue.trim().isBlank() || !isNumeric(newValue.trim())) {
                textFieldPrix.setStyle("-fx-border-color: crimson;");
            } else {
                textFieldPrix.setStyle("-fx-border-color: transparent;");
            }
        });

        textFieldQuantite.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty() || newValue.trim().isBlank() || !isNumeric(newValue.trim())) {
                textFieldQuantite.setStyle("-fx-border-color: crimson;");
            } else {
                textFieldQuantite.setStyle("-fx-border-color: transparent;");
            }
        });
    }

    private void handleTextField() {
        textFieldObjet.setOnMouseClicked(e -> {
            if (textFieldObjet.getText().equals("Objet")) {
                textFieldObjet.setText("");
            }
        });

        textFieldObjet.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textFieldObjet.getText().isEmpty()) {
                textFieldObjet.setText("Objet");
            }
        });

        textFieldQuantite.setOnMouseClicked(e -> {
            if (textFieldQuantite.getText().equals("Quantité")) {
                textFieldQuantite.setText("");
            }
        });

        textFieldQuantite.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textFieldQuantite.getText().isEmpty()) {
                textFieldQuantite.setText("Quantité");
            }
        });

        textFieldPrix.setOnMouseClicked(e -> {
            if (textFieldPrix.getText().equals("Prix")) {
                textFieldPrix.setText("");
            }
        });

        textFieldPrix.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textFieldPrix.getText().isEmpty()) {
                textFieldPrix.setText("Prix");
            }
        });
    }

    private void onAjouterPressed() throws StockException {
        textFieldObjet.setStyle("-fx-border-color: transparent;");
        textFieldPrix.setStyle("-fx-border-color: transparent;");
        textFieldQuantite.setStyle("-fx-border-color: transparent;");

        if (textFieldObjet.getText().trim().equals("Objet") || textFieldObjet.getText().trim().isEmpty() || textFieldObjet.getText().trim().isBlank() || textFieldObjet.getText().matches(".*[^a-zA-Z-\\s].*")) {
            textFieldObjet.requestFocus();
            textFieldObjet.setStyle("-fx-border-color: crimson;");
        } else if (textFieldPrix.getText().trim().isEmpty() || textFieldPrix.getText().trim().isBlank() || !isNumeric(textFieldPrix.getText().trim())) {
            textFieldPrix.requestFocus();
            textFieldPrix.setStyle("-fx-border-color: crimson;");
        } else if (textFieldQuantite.getText().trim().isEmpty() || textFieldPrix.getText().trim().isBlank() || !isNumeric(textFieldQuantite.getText().trim())) {
            textFieldQuantite.requestFocus();
            textFieldQuantite.setStyle("-fx-border-color: crimson;");
        } else {
            Stock stock = new Stock(textFieldObjet.getText(), Integer.parseInt(textFieldQuantite.getText()), true, Double.parseDouble(textFieldPrix.getText()));
            textFieldObjet.setText("Objet");
            textFieldQuantite.setText("Quantité");
            textFieldPrix.setText("Prix");
            tableView.getItems().add(stock);
            System.out.println("Stock ajouté");
        }
    }


    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
