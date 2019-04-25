package sample;

import entity.*;
import entity.Shape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    private static FileChooser fileChooser = new FileChooser();
    private static Alert alert = new Alert(Alert.AlertType.ERROR);
    static {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        alert.setTitle("Error dialog");
        alert.setHeaderText("Check your file");
    }

    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuButton menuShapes;

    private ArrayList<Shape> list = new ArrayList<>();
    private static ShapeFactory shapeFactory;
    private static Shape shape;
    private boolean rainbow = false;
    private ArrayList<String> types = new ArrayList<>();
    private HashMap<String, ShapeFactory> factoryHashMap = new HashMap<>();

    public void colorPickerSelect(){
        canvas.getGraphicsContext2D().setStroke(colorPicker.getValue());
        Logger.getLogger(Controller.class).info("chosen color: " + colorPicker.getValue());
    }

    public void mousePressed(MouseEvent mouseEvent){
        if (shapeFactory != null) {
            shape = shapeFactory.newShape(new MyPoint(0, 0), new MyPoint(0, 0));
            shape.setFirstPoint(new MyPoint(mouseEvent.getX(), mouseEvent.getY()));
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (shapeFactory != null){
            if (rainbow){
                Color color = (Color)canvas.getGraphicsContext2D().getStroke();
                canvas.getGraphicsContext2D().setStroke(Color.color((color.getRed() + 0.01)%1, (color.getGreen() + 0.01)%1, (color.getBlue() + 0.01)%1, color.getOpacity()));
            } else {
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Paint tmpPaint = canvas.getGraphicsContext2D().getStroke();
                drawAll();
                canvas.getGraphicsContext2D().setStroke(tmpPaint);
            }
            shape.setSecondPoint(new MyPoint(mouseEvent.getX(), mouseEvent.getY()));
            shape.draw(canvas);
        }
    }

    public void mouseReleased(MouseEvent mouseEvent){
        if (shapeFactory != null) {
            Shape _shape = shapeFactory.newShape(shape.getFirstPoint(), new MyPoint(mouseEvent.getX(), mouseEvent.getY()));
            _shape.setColor((Color)canvas.getGraphicsContext2D().getStroke());
            list.add(_shape);
            Logger.getLogger(Controller.class).info("add new shape: " + _shape.getType());
            _shape.draw(canvas);
            Logger.getLogger(Controller.class).info("draw new shape: " + _shape.getType());
        }
    }

    public void save() {
        Logger.getLogger(Controller.class).info("file -> save...");
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Enter XML-file name");
        inputDialog.setContentText("File name:");
        inputDialog.setTitle("saveDialog");
        Optional<String> result = inputDialog.showAndWait();
        String fileName;
        if (result.isPresent()){
            fileName = result.get() + ".xml";
            try (FileOutputStream encoder = new FileOutputStream(fileName)){
                Logger.getLogger(Main.class).info("file " + fileName + " created");
                ShapeListWrapper drawnShapes = new ShapeListWrapper();
                drawnShapes.setShapeList(list);
                JAXBContext context = JAXBContext.newInstance(ShapeListWrapper.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.marshal(drawnShapes, encoder);
            } catch (IOException | JAXBException e) {
                Logger.getLogger(Controller.class).error(e);
            }
            Logger.getLogger(Main.class).info("file " + fileName + " saved");
        }
    }

    public void add() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Logger.getLogger(Main.class).info("file " + file.getName() + " opened");
            try (FileInputStream decoder = new FileInputStream(file)) {
                JAXBContext context = JAXBContext.newInstance(ShapeListWrapper.class);
                Unmarshaller um = context.createUnmarshaller();
                ShapeListWrapper drawnShapes = (ShapeListWrapper) um.unmarshal(decoder);
                for (Shape shape : drawnShapes.getShapeList()){
                    Pattern pattern = Pattern.compile("^0x[0-9a-fA-F]{8}$");
                    Matcher matcher = pattern.matcher(shape.getColor());
                    if (types.isEmpty()){
                        alert.setHeaderText("Classes not found");
                        throw new IOException();
                    }
                    if (!types.contains(shape.getType()) || !matcher.find()){
                        throw new IOException();
                    }
                    Shape sh = factoryHashMap.get(shape.getType()).newShape(shape.getFirstPoint(), shape.getSecondPoint());
                    sh.setColor(shape.getColor());
                    canvas.getGraphicsContext2D().setStroke(Color.valueOf(sh.getColor()));
                    sh.draw(canvas);
                    list.add(sh);
                }
            } catch (JAXBException | IOException e){
                alert.showAndWait();
                Logger.getLogger(Controller.class).error(e);
            }
            Logger.getLogger(Main.class).info("file " + file.getName() + " closed");
            canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        }
    }

    public void open() {
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        list.clear();
        add();
    }

    public void rainbowClicked() {
        rainbow = !rainbow;
    }

    public void clearAll() {
        list.clear();
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void rollBack() {
        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawAll();
        }
    }

    private void drawAll(){
        for (Shape _shape : list) {
            canvas.getGraphicsContext2D().setStroke(Color.valueOf(_shape.getColor()));
            _shape.draw(canvas);
        }
    }

    private void chooseShape(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        menuShapes.setText(item.getText());
        Logger.getLogger(Controller.class).info("button clicked: btn" + item.getText());
        shapeFactory = factoryHashMap.get(item.getText());
    }

    private void loadModules(String pathToDir, String packageName) {
        ModuleLoader loader = new ModuleLoader(pathToDir, packageName, ClassLoader.getSystemClassLoader());
        File dir = new File(pathToDir);
        String[] modules = dir.list();
        try {
            if (modules != null) {
                for (String module : modules) {
                    File curr = new File(pathToDir + "\\" + module);
                    if (curr.isFile() && module.contains(".class")) {
                        String moduleName = module.split("\\.class")[0];
                        Class loadedClass = loader.loadClass(moduleName);
                        //Logger.getLogger(Controller.class).info(moduleName + ".class loaded");
                        if (loadedClass != null) {
                            if (ShapeFactory.class.isAssignableFrom(loadedClass)) {
                                factoryHashMap.put(moduleName.substring(0, moduleName.indexOf("Factory")), (ShapeFactory) loadedClass.newInstance());
                            }
                            if (Shape.class.isAssignableFrom(loadedClass)) {
                                types.add(moduleName);
                                MenuItem item = new MenuItem(loadedClass.getSimpleName());
                                item.setOnAction(this::chooseShape);
                                menuShapes.getItems().add(item);
                            }
                        }
                    }
                    if (curr.isDirectory()) {
                        loadModules(curr.getAbsolutePath() + "\\", packageName + "." + curr.getName());
                    }
                }
            }
        }catch (Exception e){
            Logger.getLogger(Controller.class).error("Error while uploading shapes:\n" + e);
            alert.setHeaderText("Error while uploading shapes");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String dir = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")).getPath();
            loadModules(dir + "\\modules\\shapes\\", "modules.shapes");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void upload(ActionEvent actionEvent) {
/*        try {
            String dir = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")).getPath();
            loadModules(dir + "\\modules\\shapes\\", "modules.shapes");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    public void saveUserShape(ActionEvent actionEvent) {
    }
}
