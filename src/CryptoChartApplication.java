
import javafx.application.Application;
import javafx.stage.Stage;

public class CryptoChartApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        CryptoChartView view =new CryptoChartView(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
