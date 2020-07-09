import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CryptoChartController implements Initializable {

    public LineChart cryptoChart;
    public HBox hBoxLineChart;
    public SplitPane splitPane;
    public Button hourButton;
    public Button minButton;
    public Button dayButton;
    public Label bitcoinPriceLabel;
    private ArrayList<Float> bitcoinPrice=new ArrayList<Float>();
    private ObservableList<XYChart.Series<String,Double>> graphs= FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        XYChart.Series<String,Double> seriesMinute=new XYChart.Series<>();
        XYChart.Series<String,Double> seriesHour=new XYChart.Series<>();
        XYChart.Series<String,Double> seriesDay=new XYChart.Series<>();

        HBox.setHgrow(cryptoChart, Priority.ALWAYS);
        GridPane.setHgrow(splitPane,Priority.ALWAYS);
        bitcoinPrice.add(Float.valueOf(CryptoChartModel.getBitcoinCurrentPrice()));
    // 1582 1611 1569ms when below is done sync significantly slower vs 466 482 480ms with no sync
        bitcoinPriceLabel.setText(CryptoChartModel.getBitcoinCurrentPrice());
        CryptoChartModel.getCryptoInfoInitialize();
        CryptoChartModel.getCryptoInfoInitializeHistoHour();
        CryptoChartModel.getCryptoInfoInitializeHistoDay();
    //The Code Below When synced improved performance by ~12ms
        CompletableFuture<Void> addSeriesData=CompletableFuture.runAsync(() ->CryptoChartModel.addSeriesData(seriesMinute)).
                                                                    thenRun(()->{CryptoChartModel.addSeriesDataHour(seriesHour);}).
                                                                    thenRun(()->{CryptoChartModel.addSeriesDataDay(seriesDay);});
        cryptoChart.setTitle("Bitcoin by Min (HH:mm:ss)");
        cryptoChart.getData().add(seriesMinute);
        graphs.addAll(seriesMinute,seriesHour,seriesDay);
//Buttons
        dayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cryptoChart.getData().clear();
                cryptoChart.getData().add(graphs.get(2));
                cryptoChart.setTitle("Bitcoin by Day (HH DD/MM/YY)");
            }
        });
        hourButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cryptoChart.getData().clear();
                cryptoChart.getData().add(graphs.get(1));
                cryptoChart.setTitle("Bitcoin by Hour (HH:mm:ss)");
            }
        });
        minButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cryptoChart.getData().clear();
                cryptoChart.getData().add(graphs.get(0));
                cryptoChart.setTitle("Bitcoin by Min (HH:mm:ss)");
            }
        });

//Timers
        Calendar calendar=Calendar.getInstance();
        Timer bitcoinCurrentPriceTimer=new Timer();
        BGTask t2=new BGTask(){
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Float newBitcoinPrice= Float.valueOf(CryptoChartModel.getBitcoinCurrentPrice());
                    if(bitcoinPrice.get(bitcoinPrice.size()-1)>newBitcoinPrice){
                        bitcoinPriceLabel.setText(CryptoChartModel.getBitcoinCurrentPrice());
                        bitcoinPriceLabel.setStyle("-fx-text-fill: red;");
                    }
                    if(bitcoinPrice.get(bitcoinPrice.size()-1)<newBitcoinPrice){
                        bitcoinPriceLabel.setText(CryptoChartModel.getBitcoinCurrentPrice());
                        bitcoinPriceLabel.setStyle("-fx-text-fill: green;");
                    }
                });
            }
        };
        bitcoinCurrentPriceTimer.scheduleAtFixedRate(t2,10000,10000);

        Timer minChartUpdate = new Timer();
        BGTask t = new BGTask();
        t.series = seriesMinute;
        minChartUpdate.scheduleAtFixedRate(t, CryptoChartModel.millisToNextMinute(calendar), 60000);

        Timer hourChartUpdate= new Timer();
        BGTask t3=new BGTask(){
             @Override
             public void run() {
                 CryptoChartModel.getCryptoInfoInitializeHistoHour();
                 Platform.runLater(() -> {
                     CryptoChartModel.addSeriesDataUpdateHoursChart(seriesHour);
                 });
             }
        };
        hourChartUpdate.scheduleAtFixedRate(t3,CryptoChartModel.millisToNextHour(calendar),3600000);

        Timer dayChartUpdate=new Timer();
        BGTask t4=new BGTask(){
            @Override
            public void run() {
                CryptoChartModel.getCryptoInfoInitializeHistoDay();
                Platform.runLater(() -> {
                    CryptoChartModel.addSeriesDataUpdateDaysChart(seriesDay);
                });
            }
        };
        dayChartUpdate.scheduleAtFixedRate(t4,CryptoChartModel.millisToNextDay(calendar),86400000);
    }

    private class BGTask extends TimerTask {
        XYChart.Series<String, Double> series;
        @Override
        public void run() {
            CryptoChartModel.getCryptoInfoInitialize();
            Platform.runLater(() -> {
                CryptoChartModel.addSeriesDataUpdateMinsChart(series);
            });

        }
    }
}
