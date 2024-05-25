package org.group;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AppController {

    @FXML
    private VBox mainMenuContainer;
    @FXML
    private HBox mainMenuHbox;
    @FXML   
    private Button exitButton; 
    @FXML 
    private Button minButton;
    @FXML 
    private Button maxButton;
    @FXML
    private TextArea mainTextArea;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Pane mainPaneSpacer;
    @FXML
    private Pane resizePaneBottom;
    @FXML
    private Pane resizePaneRight;
    @FXML
    private Pane resizePaneLeft;
    @FXML
    private TabPane tabPane;
    @FXML
    private HBox tabPaneHolder;
    @FXML
    private Tab addTab;

    private Stage stage;

    private Map<TextArea, File> cachedTextFile = new HashMap<>();

    @SuppressWarnings("exports")
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleCreateTab() {
        Tab newTab = new Tab();
        Label tabLabel = new Label("New Tab");
        newTab.setGraphic(tabLabel);
        
        AnchorPane pane = new AnchorPane();
        TextArea txtArea = new TextArea();

        AnchorPane.setTopAnchor(txtArea, 10.0);
        AnchorPane.setLeftAnchor(txtArea, 10.0);
        AnchorPane.setRightAnchor(txtArea, 10.0);
        AnchorPane.setBottomAnchor(txtArea, 10.0);

        txtArea.setWrapText(true);

        pane.getChildren().add(txtArea);
        newTab.setContent(pane);
        tabPane.getTabs().add(newTab);

        tabLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleMenuRename(newTab, tabLabel);
            }
        });
    }

    private void handleMenuRename(Tab tab, Label tabLabel) {
        TextField textField = new TextField(tabLabel.getText());
        textField.setOnAction(event -> {
            tabLabel.setText(textField.getText());
            tab.setGraphic(tabLabel);
        });
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                tabLabel.setText(textField.getText());
                tab.setGraphic(tabLabel);
            }
        });
    
        tab.setGraphic(textField);
        textField.selectAll();
        textField.requestFocus();
    }

 
@FXML
public void handleOpenFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Text File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

    File selectedFile = fileChooser.showOpenDialog(stage);
    if (selectedFile != null) {
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            StringBuilder textContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                textContent.append(line).append("\n");
            }

            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                AnchorPane pane = (AnchorPane) selectedTab.getContent();
                TextArea txtArea = (TextArea) pane.getChildren().get(0); // Assuming the TextArea is the first child
                txtArea.setText(textContent.toString());
                
                // Store the file associated with this TextArea
                cachedTextFile.put(txtArea, selectedFile);
            }
        } catch (IOException e) {
            System.err.println("Error reading text file: " + e.getMessage());
        }
    } else {
        System.out.println("No file selected.");
    }
}

@FXML
public void handleSaveChanges() {
    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
    if (selectedTab != null) {
        AnchorPane pane = (AnchorPane) selectedTab.getContent();
        TextArea txtArea = (TextArea) pane.getChildren().get(0); // Assuming the TextArea is the first child
        File file = cachedTextFile.get(txtArea);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(txtArea.getText());
                System.out.println("Changes saved to file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving changes to file: " + e.getMessage());
            }
        } else {
            System.out.println("No file associated with this TextArea.");
        }
    } else {
        System.out.println("No tab selected.");
    }
}

@FXML
public void handleSaveAsFile() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            AnchorPane pane = (AnchorPane) selectedTab.getContent();
            TextArea txtArea = (TextArea) pane.getChildren().get(0);
            String textToSave = txtArea.getText();

            if (!textToSave.isEmpty()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write(textToSave);
                    } catch (IOException e) {
                        System.err.println("Error saving text to file: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("No text to save.");
            }
        } else {
            System.out.println("No tab selected.");
        }
    }
    
    @FXML 
    private void handleFontChange() { 
        // TODO method needs to be implemented
    }

    @FXML 
    private void handleStyleChange() { 
        // TODO method needs to be implemented
    }

    @FXML 
    private void handleFormatChange() { 
        // TODO method needs to be implemented
    }

    public void setupEventHandlers() {
        resizePaneBottom.setOnMousePressed(pressEvent -> { 
            resizePaneBottom.setOnMouseDragged(dragEvent -> { 
                stage.setHeight(dragEvent.getSceneY());
            });
        });

        resizePaneRight.setOnMousePressed(pressEvent -> { 
            resizePaneRight.setOnMouseDragged(dragEvent -> { 
                stage.setWidth(dragEvent.getSceneX());
            });
        });

        resizePaneLeft.setOnMousePressed(pressEvent -> {
            double initialX = pressEvent.getSceneX();
            double initialWidth = stage.getWidth();
            double initialXPos = stage.getX();

            resizePaneLeft.setOnMouseDragged(dragEvent -> {
                double dragX = dragEvent.getSceneX();
                double deltaX = initialX - dragX;

                double newWidth = initialWidth + deltaX;
                double newXPos = initialXPos - deltaX;

                stage.setWidth(newWidth);
                stage.setX(newXPos);
            });

            resizePaneLeft.setOnMouseReleased(releaseEvent -> {
                resizePaneLeft.setOnMouseDragged(null);
            });
        });

        mainPaneSpacer.setOnMousePressed(pressEvent -> {
            mainPaneSpacer.setOnMouseDragged(dragEvent -> {
                Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getBounds();
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
                stage.setMaximized(false);

                if(dragEvent.getScreenX() <= 1) { 
                    stage.setMaximized(false);
                    stage.setMaxHeight(screenBounds.getHeight());
                    stage.setMaxWidth(screenBounds.getWidth());
                    stage.setX(0);
                    stage.setY(0);
                    stage.setHeight(screenBounds.getHeight());
                    stage.setWidth(screenBounds.getWidth()/2);
                } 
                else if(dragEvent.getScreenX() > 1 && dragEvent.getScreenX() <= screenBounds.getWidth() - 1.5 && dragEvent.getScreenY() > 1.5){
                    stage.setMaximized(false);
                    stage.setMaxHeight(screenBounds.getHeight() - 50);
                    stage.setMaxWidth(screenBounds.getWidth() - 50);
                } 
                else if (dragEvent.getScreenX() >= screenBounds.getWidth() - 1){ 
                    stage.setMaximized(false);
                    stage.setMaxHeight(screenBounds.getHeight());
                    stage.setMaxWidth(screenBounds.getWidth());
                    stage.setX(screenBounds.getWidth()/2);
                    stage.setY(0);
                    stage.setHeight(screenBounds.getHeight());
                    stage.setWidth(screenBounds.getWidth()/2);
                } else if(dragEvent.getScreenY() <= 1) { 
                    stage.setMaxHeight(screenBounds.getHeight());
                    stage.setMaxWidth(screenBounds.getWidth());
                    stage.setX(0);
                    stage.setY(0);
                    stage.setMaximized(true);
                }
            });
        });

        minButton.setOnAction(e -> { 
            stage.setIconified(true);
        });

        maxButton.setOnAction(e -> { 
            if(stage.isMaximized()) {
                stage.setMaximized(false);
                stage.setHeight(768);
                stage.setWidth(960);
                stage.centerOnScreen();
            } else {
                stage.setMaximized(true);
            }
        });

        exitButton.setOnAction(e -> {  
            Platform.exit();
        });
    }
}
