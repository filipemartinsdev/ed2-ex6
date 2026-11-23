package com.ed2ex6.controller;

import com.ed2ex6.model.CryptographyAlgorithm;
import com.ed2ex6.model.HashAlgorithm;
import com.ed2ex6.service.CryptographyService;
import com.ed2ex6.service.HashService;
import com.ed2ex6.service.KeyUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class MainController {
    private HashService hashService = new HashService();
    private CryptographyService cryptographyService = new CryptographyService();

    @FXML
    private Button btnCompararHash;

    @FXML
    private Button btnGerarChaveAleatoria;

    @FXML
    private Button btnCriptografar;

    @FXML
    private Button btnDescriptografar;

    @FXML
    private Button btnGerarHash;

    @FXML
    private ComboBox<CryptographyAlgorithm> cbAlgoritmoCriptografar;

    @FXML
    private ComboBox<HashAlgorithm> cbAlgoritmoGerarHash;

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
        setupAlgoritmoGerarHash();
        setupALgoritmoCriptografar();
    }

    private void setupALgoritmoCriptografar() {
        this.cbAlgoritmoCriptografar.setItems(FXCollections.observableArrayList(
                CryptographyAlgorithm.values()
        ));

        this.cbAlgoritmoCriptografar.setConverter(new StringConverter<CryptographyAlgorithm>() {
            @Override
            public String toString(CryptographyAlgorithm object) {
                return object != null ? object.name() : "";
            }

            @Override
            public CryptographyAlgorithm fromString(String string) {
                return null;
            }
        });
    }

    private void setupAlgoritmoGerarHash() {
        this.cbAlgoritmoGerarHash.setItems(FXCollections.observableArrayList(
                HashAlgorithm.values()
        ));

        this.cbAlgoritmoGerarHash.setConverter(new StringConverter<HashAlgorithm>() {
            @Override
            public String toString(HashAlgorithm object) {
                return object != null ? object.name() : "";
            }

            @Override
            public HashAlgorithm fromString(String string) {
                return null;
            }
        });
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
    void gerarChaveCriptografiaAleatoria(ActionEvent event){
        if (cbAlgoritmoCriptografar.getValue() == null){
            showAlertError("Algoritmo necessário", "Selecione um algoritmo de criptografia");
            return;
        }

        SecretKey secretKey = KeyUtils.random(cbAlgoritmoCriptografar.getValue());

        this.inputChaveCriptografar.setText(KeyUtils.toHex(secretKey));
    }

    @FXML
    void criptografar(ActionEvent event) {
        if (cbAlgoritmoCriptografar.getValue() == null){
            showAlertError("Algoritmo necessário", "Selecione um algoritmo de criptografia");
            return;
        }

        if (inputTextoCriptografar.getText() == null || inputTextoCriptografar.getText().isBlank()){
            showAlertError("Plaintext necessário", "Insira um plaintext para criptografar");
            return;
        }

        if (inputChaveCriptografar.getText() == null || inputChaveCriptografar.getText().isBlank()){
            showAlertError("Chave necessária", "Insira uma chave de criptografia");
            return;
        }

        SecretKey secretKey = KeyUtils.fromHex(inputChaveCriptografar.getText(), cbAlgoritmoCriptografar.getValue());

        String plaintext = inputTextoCriptografar.getText();

        CryptographyAlgorithm algorithm = cbAlgoritmoCriptografar.getValue();


        getCriptografarThread(plaintext, algorithm, secretKey)
                .start();
    }

    private Thread getCriptografarThread(String plaintext, CryptographyAlgorithm algorithm, SecretKey secretKey) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return cryptographyService.encrypt(plaintext, algorithm, secretKey);
            }
        };

        task.setOnFailed(e -> {
            showAlertError("Erro na criptografia", "Impossível criptografar o texto");
        });

        task.setOnSucceeded(e -> {
            this.outputCriptografia.setText(task.getValue());
        });

        return new Thread(task);
    }

    @FXML
    void descriptografar(ActionEvent event) {
        if (cbAlgoritmoCriptografar.getValue() == null){
            showAlertError("Algoritmo necessário", "Selecione um algoritmo de criptografia");
            return;
        }

        if (inputTextoCriptografar.getText() == null || inputTextoCriptografar.getText().isBlank()){
            showAlertError("Ciphertext necessário", "Insira um ciphertext para descriptografar");
            return;
        }

        if (inputChaveCriptografar.getText() == null || inputChaveCriptografar.getText().isBlank()){
            showAlertError("Chave necessária", "Insira uma chave de criptografia");
            return;
        }

        SecretKey secretKey = KeyUtils.fromHex(inputChaveCriptografar.getText(), cbAlgoritmoCriptografar.getValue());

        String ciphertext = inputTextoCriptografar.getText();

        CryptographyAlgorithm algorithm = cbAlgoritmoCriptografar.getValue();

        String plaintext = cryptographyService.decrypt(ciphertext, algorithm, secretKey);

        getDescriptografarThread(ciphertext, algorithm, secretKey)
                .start();

        this.outputCriptografia.setText(plaintext);
    }

    private Thread getDescriptografarThread(String ciphertext, CryptographyAlgorithm algorithm, SecretKey secretKey) {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return cryptographyService.decrypt(ciphertext, algorithm, secretKey);
            }
        };

        task.setOnFailed(e -> {
            showAlertError("Erro na criptografia", "Impossível descriptografar o texto");
        });

        task.setOnSucceeded(e -> {
            this.outputCriptografia.setText(task.getValue());
        });

        return new Thread(task);
    }

    @FXML
    void gerarHash(ActionEvent event) {
        if (this.inputGerarHash.getText() == null || this.inputGerarHash.getText().isBlank()){
            showAlertError("Entrada inválida", "Insira o texto para gerar o hash.");
            return;
        }

        if (this.cbAlgoritmoGerarHash.getValue() == null){
            showAlertError("Entrada inválida", "Selecione o algoritmo para gerar o hash.");
            return;
        }


        String text = this.inputGerarHash.getText();
        String algorithm = this.cbAlgoritmoGerarHash.getValue().label;

        String hash = this.hashService.toHash(text, algorithm);
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
