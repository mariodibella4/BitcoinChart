
import javafx.scene.chart.XYChart;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CryptoChartModel {
    public static ArrayList<String> apiOutputMinute=new ArrayList<String>();
    public static ArrayList<String> apiOutputHour=new ArrayList<String>();
    public static ArrayList<String> apiOutputDay=new ArrayList<String>();

    public  static void getCryptoInfoInitialize() {
        String addr = "https://min-api.cryptocompare.com/data/histominute?aggregate=1&e=CCCAGG&extraParams=CryptoCompare&fsym=BTC&limit=10&tryConversion=false&tsym=USD";
        try {
            URL address = new URL(addr);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                String[] tokens = line.split("\\{");
                String[] cleanTokens=new String[tokens.length-2];
                for(int i=2;i<tokens.length;i++) {
                    for(int j=0;i<cleanTokens.length;i++){
                        cleanTokens[j]=tokens[i].toString().replaceAll("\"|\\}", "");
                        String[] cleanerTokens=cleanTokens[j].split(",volumefrom");
                    for(int k=0;k<cleanerTokens.length;k++) {
                        if (!cleanerTokens[k].contains("volumeto:"))
                            apiOutputMinute.add(cleanerTokens[k]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public  static void getCryptoInfoInitializeHistoHour() {
        String addr = "https://min-api.cryptocompare.com/data/histohour?aggregate=1&e=CCCAGG&extraParams=CryptoCompare&fsym=BTC&limit=10&tryConversion=false&tsym=USD";
        try {
            URL address = new URL(addr);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {

                if (line.isEmpty()) {
                    break;
                }
                String[] tokens = line.split("\\{");
                String[] cleanTokens=new String[tokens.length-2];
                for(int i=2;i<tokens.length;i++) {
                    for(int j=0;i<cleanTokens.length;i++){
                        cleanTokens[j]=tokens[i].toString().replaceAll("\"|\\}", "");
                        String[] cleanerTokens=cleanTokens[j].split(",volumefrom");
                        for(int k=0;k<cleanerTokens.length;k++) {
                            if (!cleanerTokens[k].contains("volumeto:"))
                                apiOutputHour.add(cleanerTokens[k]);
                        }

                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public  static void getCryptoInfoInitializeHistoDay() {
        String addr = "https://min-api.cryptocompare.com/data/histoday?aggregate=1&e=CCCAGG&extraParams=CryptoCompare&fsym=BTC&limit=10&tryConversion=false&tsym=USD";
        try {
            URL address = new URL(addr);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {

                if (line.isEmpty()) {
                    break;
                }
                String[] tokens = line.split("\\{");
                String[] cleanTokens=new String[tokens.length-2];
                for(int i=2;i<tokens.length;i++) {
                    for(int j=0;i<cleanTokens.length;i++){
                        cleanTokens[j]=tokens[i].toString().replaceAll("\"|\\}", "");
                        String[] cleanerTokens=cleanTokens[j].split(",volumefrom");
                        for(int k=0;k<cleanerTokens.length;k++) {
                            if (!cleanerTokens[k].contains("volumeto:"))
                                apiOutputDay.add(cleanerTokens[k]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static String getBitcoinCurrentPrice(){
        String addr ="https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD";
        try {
            URL address = new URL(addr);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);
            String line = "";
            while ((line = buffer.readLine()) != null) {

                if (line.isEmpty()) {
                    break;
                }
                String[] tokens=line.split(":");
                String bitcoinPrice=tokens[1].replaceAll("\"|\\}", "");
                return bitcoinPrice;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addSeriesData(XYChart.Series<String,Double> series){
        for(int i=0;i<apiOutputMinute.size();i++) {
            String[] tokens =apiOutputMinute.get(i).split(",");
            long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
            Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
            Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
            Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
            Double open=Double.parseDouble(tokens[4].substring(tokens[4].lastIndexOf(":")+1));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes(time*1000),open));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+20)*1000),high));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+40)*1000),low));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+60)*1000),close));
        }
    }
    public static void addSeriesDataHour(XYChart.Series<String,Double> series){
        for(int i=0;i<apiOutputHour.size();i++) {
            String[] tokens = apiOutputHour.get(i).split(",");
            long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
            Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
            Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
            Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
            Double open=Double.parseDouble(tokens[4].substring(tokens[4].lastIndexOf(":")+1));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes(time*1000),open));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+1200)*1000),high));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+2400)*1000),low));
            series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+3600)*1000),close));
        }
    }
    public static void addSeriesDataDay(XYChart.Series<String,Double> series){
        for(int i=0;i<apiOutputDay.size();i++) {
            String[] tokens = apiOutputDay.get(i).split(",");
            long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
            Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
            Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
            Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
            Double open=Double.parseDouble(tokens[4].substring(tokens[4].lastIndexOf(":")+1));
            series.getData().add(new XYChart.Data<String, Double>(formatDay(time*1000),open));
            series.getData().add(new XYChart.Data<String, Double>(formatDay((time+28800)*1000),high));
            series.getData().add(new XYChart.Data<String, Double>(formatDay((time+57600)*1000),low));
            series.getData().add(new XYChart.Data<String, Double>(formatDay((time+86400)*1000),close));
        }
    }
    public static XYChart.Series<String,Double> addSeriesDataUpdateMinsChart(XYChart.Series<String,Double> series){
        String[] tokens = apiOutputMinute.get(apiOutputMinute.size()-1).split(",");
        long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
        Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
        Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
        Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+20)*1000),high));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+40)*1000),low));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+60)*1000),close));
        return series;
    }
    public static XYChart.Series<String,Double> addSeriesDataUpdateHoursChart(XYChart.Series<String,Double> series){
        String[] tokens = apiOutputHour.get(apiOutputHour.size()-1).split(",");
        long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
        Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
        Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
        Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+1200)*1000),high));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+2400)*1000),low));
        series.getData().add(new XYChart.Data<String, Double>(formatMinutes((time+3600)*1000),close));
        return series;
    }
    public static  XYChart.Series<String,Double> addSeriesDataUpdateDaysChart(XYChart.Series<String,Double> series){
        String[] tokens = apiOutputDay.get(apiOutputDay.size()-1).split(",");
        long time=Long.parseLong(tokens[0].substring(tokens[0].lastIndexOf(":")+1));
        Double close=Double.parseDouble(tokens[1].substring(tokens[1].lastIndexOf(":")+1));
        Double high=Double.parseDouble(tokens[2].substring(tokens[2].lastIndexOf(":")+1));
        Double low=Double.parseDouble(tokens[3].substring(tokens[3].lastIndexOf(":")+1));
        series.getData().add(new XYChart.Data<String, Double>(formatDay((time+28800)*1000),high));
        series.getData().add(new XYChart.Data<String, Double>(formatDay((time+57600)*1000),low));
        series.getData().add(new XYChart.Data<String, Double>(formatDay((time+86400)*1000),close));
        return series;
    }
    public static String formatMinutes(long time){
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }
    public static String formatDay(long time){
        DateFormat formatter = new SimpleDateFormat("HH dd-MM-yy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return formatter.format(calendar.getTime());
    }
    public static long millisToNextHour(Calendar calendar){
        int min=calendar.get(Calendar.MINUTE);
        int sec=calendar.get(Calendar.SECOND);
        int millis=calendar.get(Calendar.MILLISECOND);
        int minutesToNextHour=60-min;
        int secToNextHour=60-sec;
        int millisToNextHour=1000-millis;
        return minutesToNextHour*60*1000+secToNextHour*1000+millisToNextHour;
    }
    public static long millisToNextDay(Calendar calendar){
        int hour=calendar.get(Calendar.HOUR);
        int min=calendar.get(Calendar.MINUTE);
        int sec=calendar.get(Calendar.SECOND);
        int millis=calendar.get(Calendar.MILLISECOND);
        int hoursToNextDay=24-hour;
        int minutesToNextHour=60-min;
        int secToNextHour=60-sec;
        int millisToNextHour=1000-millis;
        return hoursToNextDay*60*60*1000+minutesToNextHour*60*1000+secToNextHour*1000+millisToNextHour;
    }
   public  static long millisToNextMinute(Calendar calendar){
       int sec=calendar.get(Calendar.SECOND);
       int millis=calendar.get(Calendar.MILLISECOND);
       int secToNextMin=60-sec;
       int millisToNextMin=1000-millis;
       return secToNextMin*1000+millisToNextMin;
    }

}
