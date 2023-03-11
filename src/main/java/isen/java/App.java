package isen.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import isen.java.db.DataSourceFactory;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void init() throws Exception {
        super.init();
        
        Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();

		statement.executeUpdate("CREATE TABLE IF NOT EXISTS person (\r\n"
				+ "idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n"
				+ "lastname VARCHAR(45) NOT NULL,\r\n" + "firstname VARCHAR(45) NOT NULL,\r\n"
				+ "nickname VARCHAR(45) NOT NULL,\r\n" + "phone_number VARCHAR(15) NULL,\r\n"
				+ "address VARCHAR(200) NULL,\r\n" + "email_address VARCHAR(150) NULL,\r\n"
                + "birth_date DATE NULL);");
                
        statement.close();
        connection.close();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/isen/java/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}