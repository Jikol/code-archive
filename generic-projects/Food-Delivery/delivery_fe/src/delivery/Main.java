package delivery;

import com.orm.dao.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.TextField;

import javax.swing.plaf.FontUIResource;

public class Main extends Application {

    public static boolean needDisconnected = false;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Database db = Database.create();
        db.Connect();

        MainWindow.show(primaryStage, "Objednávkový systém jídla", 600, 400);

        if (needDisconnected) {
            db.Close();
        }
    }
}
