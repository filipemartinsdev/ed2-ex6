package com.ed2ex6.controller;

import com.ed2ex6.service.CryptographyService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class MainController {
    private CryptographyService cryptographyService = new CryptographyService();

    @FXML
    private Button btnCompararHash;

    @FXML
    private Button btnCriptografar;

    @FXML
    private Button btnDescriptografar;

    @FXML
    private Button btnGerarHash;

    @FXML
    private ComboBox<String> cbAlgoritmoCriptografar;

    @FXML
    private ComboBox<String> cbAlgoritmoGerarHash;

    @FXML
    private TextField inputChaveCriptografar;

    @FXML
    private TextArea inputCompararHash1;

    @FXML
    private TextArea inputCompararHash2;

    @FXML
    private TextArea inputGerarHash;

    @FXML
    private TextArea inputTextoCriptografar;

    @FXML
    private TextArea outputCriptografia;

    @FXML
    private TextArea outputGerarHash;


    @FXML
    void initialize(){
        Platform.runLater(this::setupComboBoxes);
    }

    private void setupComboBoxes() {
        this.cbAlgoritmoGerarHash.setItems(FXCollections.observableArrayList(
                "MD5", "SHA-256", "SHA-512", "SHA3-256", "SHA3-512"
        ));

        this.cbAlgoritmoCriptografar.setItems(FXCollections.observableArrayList(
                "MD5", "SHA-256", "SHA-512", "SHA3-256", "SHA3-512"
        ));
    }

    @FXML
    void compararHash(ActionEvent event) {
        boolean isHashEquals = inputCompararHash1.getText().equals(inputCompararHash2.getText());
        if (isHashEquals)
            showAlertInfo("Hashes iguais", "Os hashes são iguais.");
        else
            showAlertError("Hashes diferentes", "Os hashes são diferentes.");
    }

    @FXML
    void criptografar(ActionEvent event) {

    }

    @FXML
    void descriptografar(ActionEvent event) {

    }

    @FXML
    void gerarHash(ActionEvent event) {
        if (this.inputGerarHash.getText() == null || this.inputGerarHash.getText().isBlank()){
            showAlertError("Entrada inválida", "Insira o texto para gerar o hash.");
            return;
        }

        if (this.cbAlgoritmoGerarHash.getValue() == null || this.cbAlgoritmoGerarHash.getValue().isBlank()){
            showAlertError("Entrada inválida", "Selecione o algoritmo para gerar o hash.");
            return;
        }


        String text = this.inputGerarHash.getText();
        String algorithm = this.cbAlgoritmoGerarHash.getValue();

        String hash = this.cryptographyService.toHash(text, algorithm);
        this.outputGerarHash.setText(hash);
    }

    private void showAlertInfo(String header, String message){
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertError(String header, String message){
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
