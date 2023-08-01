package com.example.citybikes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Network> networkListView;

    private static final String API_URL = "http://api.citybik.es/v2/networks";

    @FXML
    public void initialize() {
        try {
            List<Network> networkList = fetchDataFromAPI();
            ObservableList<Network> observableNetworkList = FXCollections.observableArrayList(networkList);
            FilteredList<Network> filteredNetworkList = new FilteredList<>(observableNetworkList, p -> true);
            networkListView.setItems(filteredNetworkList);

            // Set the search functionality
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredNetworkList.setPredicate(network -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true; // Show all items if the search field is empty
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    // Check if the name or city contains the search query
                    return network.getName().toLowerCase().contains(lowerCaseFilter)
                            || network.getCity().toLowerCase().contains(lowerCaseFilter);
                });
            });

        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error fetching data from API");
        }
    }

    private List<Network> fetchDataFromAPI() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        JsonParser parser = new JsonParser();
        JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();
        JsonArray networksArray = jsonResponse.getAsJsonArray("networks");

        List<Network> networkList = new ArrayList<>();
        for (JsonElement networkElement : networksArray) {
            JsonObject networkJson = networkElement.getAsJsonObject();
            String id = networkJson.get("id").getAsString();
            String name = networkJson.has("name") ? networkJson.get("name").getAsString() : "N/A";
            JsonObject locationJson = networkJson.getAsJsonObject("location");
            String city = locationJson.has("city") ? locationJson.get("city").getAsString() : "N/A";

            Network network = new Network(id, name, city);
            networkList.add(network);
        }

        return networkList;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
