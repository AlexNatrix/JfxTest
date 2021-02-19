package sample;

import com.hummeling.if97.IF97;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.analysis.*;
import javafx.event.Event.*;
import sample.Phi;
import org.apache.commons.math3.exception.NoBracketingException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.function.Function;


class graph2D{
    private String name;
    private AreaChart<Number, Number> numberLineChart;
    private NumberAxis x;
    private NumberAxis y;
    graph2D(String t){
        this.name = t;
        this.x = new NumberAxis();
        this.y = new NumberAxis();
        this.numberLineChart = new AreaChart<Number, Number>(x,y);
        //numberLineChart.setTitle(t);
    }

    public AreaChart<Number, Number> getNumberLineChart() {
        return numberLineChart;
    }
}

class dataSeries{
    private String name;
    private XYChart.Series<Number, Number> series;
    ObservableList<XYChart.Data<Number, Number>> data;
    dataSeries(String t){
        this.name = t;
        this.series = new XYChart.Series<Number, Number>();
        series.setName(t);
        this.data= FXCollections.observableArrayList();
    }

    public void add(XYChart.Data<Number, Number> x){
        data.add(x);
    }
    public void setData() {
        if (data.isEmpty()){
            throw new NoSuchElementException("Data is empty!");
        }else{
            series.setData(this.data);
        }
    }
    public XYChart.Series<Number, Number> getSeries(){
        return series;
    }
}

class titleBar{
    MenuBar menu;
    HBox hbox;
    Menu mFile;
    Menu mEdit;
    Menu mShare;
    Menu mShow;
    MenuItem m1;
    MenuItem m2;
    MenuItem m3;
    titleBar() {
        this.hbox = new HBox();
        this.menu = new MenuBar();
        this.mFile = new Menu("File");
        this.mEdit = new Menu("Edit");
        this.mShare = new Menu("Share");
        this.mShow = new Menu("Show");
        this.m1 = new MenuItem("menu item 1");
        this.m2 = new MenuItem("menu item 2");
        this.m3 = new MenuItem("menu item 3");
        this.m1.setOnAction(e -> {
            System.out.println("Menu Item 1 Selected");
        });
        this.mFile.getItems().addAll(this.m1,this.m2,this.m3);
        this.menu.getMenus().addAll(this.mFile,this.mEdit,this.mShare,this.mShow);
        this.hbox.getChildren().addAll(this.menu);
        this.hbox.setAlignment(Pos.BASELINE_LEFT);
    }
    public HBox get(){
        return this.hbox;
    }
}

class getRoots{
    double convergencePoint = -1;
    double upperIntervalBound = 100;
    int retriesLeft = 8;
    public getRoots(UnivariateSolver solver,dataSeries series,double i) {
        double k = i;
        UnivariateFunction function = v -> Math.pow(v, k) - k;
        while (retriesLeft > 0 && convergencePoint == -1) {
            try {
                convergencePoint = solver.solve(100, function, k - upperIntervalBound, k + upperIntervalBound, 0);
                series.add(new XYChart.Data<>(k, convergencePoint));
            } catch (NoBracketingException e) {
                retriesLeft--;
                upperIntervalBound /= 2;
                System.out.println(String.format("No solution could be found in interval %f for %f, " +
                        "number of retries %d", upperIntervalBound, k, retriesLeft));
            }
        }
        if (convergencePoint == -1) {
            throw new IllegalStateException(String.format("No solution could be found in interval %f", upperIntervalBound));
        }
    }

}


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("JavaFX Chart (Series)");
        graph2D graph = new graph2D("test title");
        dataSeries series = new dataSeries("test series");
        double absolutAccuracy = 1.0e-6;
        for(double i=1000.0; i<3000; i+=50.0){
            double k = i;
            series.add(new XYChart.Data<>(k, Contour.test(k)));
        }
        series.setData();
        titleBar tbar = new titleBar();
        Button button = new Button("Click Me!");
        button.setOnMouseClicked((event)->{

            System.out.println("You just clicked me");
        });
        StackPane spButton = new StackPane();
        spButton.getChildren().addAll(graph.getNumberLineChart(),tbar.get(),button);
        VBox vbox = new VBox();
        VBox.setVgrow(spButton, Priority.ALWAYS);//Make line chart always grow vertically
        vbox.getChildren().addAll(spButton,button);
        Scene scene = new Scene(vbox, 600,600, Color.BLUE);
        graph.getNumberLineChart().getData().add(series.getSeries());
        graph.getNumberLineChart().setPadding(new Insets(20,0,0,20));
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
}

/**
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX App");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefSize(1000,100);
        VBox vBox = new VBox(progressBar);
        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();


        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                double progress = 0;
                for(int i=0; i<10; i++){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress += 0.1;

                    double reportedProgress = progress;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(reportedProgress);
                        }
                    });
                }

            }
        });

        taskThread.start();
    }

}
 **/