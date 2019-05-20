package by.bsuir.oop.paint.sample;

import by.bsuir.oop.paint.action.Signer;
import by.bsuir.oop.paint.configuration.language.Words;
import by.bsuir.oop.paint.entity.*;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    private static FileChooser fileChooser = new FileChooser();
    private static String extension = Main.extension;
    private static HashMap<Words, String> languageMap = Main.language.getWordsMap();
    private static Alert alert = new Alert(Alert.AlertType.ERROR);
    static {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        alert.setTitle("Error dialog");
        alert.setHeaderText("Check your file");
    }
    @FXML
    private Menu menuFile, menuEdit, menuHelp;

    @FXML
    private MenuButton menuShapes;

    @FXML
    private Button bClear, bBack;

    @FXML
    private MenuItem iopen, iadd, isave, iclose, icreate, iupload, idelete, iabout;

    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;

    private ArrayList<Shape> list = new ArrayList<>();
    private static ShapeFactory shapeFactory;
    private static Shape shape;
    private boolean rainbow = false;
    private ArrayList<String> types = new ArrayList<>();
    private HashMap<String, ShapeFactory> factoryHashMap = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(Controller.class.getSimpleName());

    public void colorPickerSelect(){
        canvas.getGraphicsContext2D().setStroke(colorPicker.getValue());
        LOGGER.info("chosen color: " + colorPicker.getValue());
    }

    private void chooseShape(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        menuShapes.setText(item.getText());
        LOGGER.info("button clicked: btn" + item.getText());
        shapeFactory = factoryHashMap.get(item.getText());
    }

    public void mousePressed(MouseEvent mouseEvent){
        if (shapeFactory != null) {
            shape = shapeFactory.newShape(new MyPoint(mouseEvent.getX(), mouseEvent.getY()), new MyPoint(0, 0));
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (shapeFactory != null){
            if (rainbow) {
                Color color = (Color) canvas.getGraphicsContext2D().getStroke();
                canvas.getGraphicsContext2D().setStroke(Color.color((color.getRed() + 0.01) % 1, (color.getGreen() + 0.01) % 1, (color.getBlue() + 0.01) % 1, color.getOpacity()));
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
            shape.setSecondPoint(new MyPoint(mouseEvent.getX(), mouseEvent.getY()));
            shape.setColor((Color)canvas.getGraphicsContext2D().getStroke());
            LOGGER.info("add new shape: " + shape.getType());
            shape.draw(canvas);
            list.add(shape);
            LOGGER.info("draw new shape: " + shape.getType());
        }
    }

    public void save() {
        LOGGER.info("file -> save...");
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Enter XML-file name");
        inputDialog.setContentText("File name:");
        inputDialog.setTitle("saveDialog");
        Optional<String> result = inputDialog.showAndWait();
        String fileName;
        if (result.isPresent()){
            fileName = result.get() + ".xml";
            try (FileOutputStream encoder = new FileOutputStream("data\\user\\pictures\\" + fileName)){
                LOGGER.info("file " + fileName + " created");
                ShapeListWrapper drawnShapes = new ShapeListWrapper();
                drawnShapes.setShapeList(list);
                JAXBContext context = JAXBContext.newInstance(ShapeListWrapper.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.marshal(drawnShapes, encoder);
            } catch (IOException | JAXBException e) {
                LOGGER.error(e);
            }
            LOGGER.info("file " + fileName + " saved");
        }
    }

    public void add() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            LOGGER.info("file " + file.getName() + " opened");
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
                LOGGER.error(e);
            }
            LOGGER.info("file " + file.getName() + " closed");
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
            Color color = Color.valueOf(list.get(list.size()-1).getColor());
            list.remove(list.size() - 1);
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawAll();
            canvas.getGraphicsContext2D().setStroke(color);
        }
    }

    private void drawAll(){
        for (Shape _shape : list) {
            canvas.getGraphicsContext2D().setStroke(Color.valueOf(_shape.getColor()));
            _shape.draw(canvas);
        }
    }

    private void loadModules(String pathToDir, String packageName) {
        ModuleLoader loader = new ModuleLoader(pathToDir, packageName, ClassLoader.getSystemClassLoader());
        File dir = new File(pathToDir);
        String[] modules = dir.list();
        try {
            if (modules != null) {
                for (String module : modules) {
                    File curr = new File(pathToDir + "\\" + module);
                    if (curr.isFile() && module.contains(extension)) {
                        String moduleName = module.split(extension)[0];
                        Signer signer = Signer.getInstance(true);
                        if (signer.isOriginal(pathToDir + module)) {
                            loader.setFsize(signer.signedFileContentSize);
                            Class loadedClass = loader.loadClass(moduleName);
                            LOGGER.info(moduleName + extension + " loaded");
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
                    }
                    if (curr.isDirectory()) {
                        loadModules(curr.getAbsolutePath() + "\\", packageName + "." + curr.getName());
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("Error while uploading shapes:\n" + e);
            alert.setHeaderText("Error while uploading shapes");
            alert.showAndWait();
        }
    }

    private void loadJar(String jarPath) throws IOException{
        File pluginDir = new File(jarPath);
        File[] jars = pluginDir.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
        for (int i = 0; i < jars.length; i++) {
            try {
                JarFile jarFile = new JarFile(jars[i]);
                if (jarVerify(jarFile)){
                    URL jarURL = jars[i].toURI().toURL();
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()){
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class")){
                            String moduleName = entry.getName();
                            moduleName = moduleName.replaceAll("/", ".").split("\\.class")[0];
                            Class loadedClass = classLoader.loadClass("modules.shapes." + moduleName);
                            if (loadedClass != null) {
                                if (ShapeFactory.class.isAssignableFrom(loadedClass)) {
                                    factoryHashMap.put(loadedClass.getSimpleName().substring(0, loadedClass.getSimpleName().indexOf("Factory")), (ShapeFactory) loadedClass.newInstance());
                                }
                                if (Shape.class.isAssignableFrom(loadedClass)) {
                                    types.add(loadedClass.getSimpleName());
                                    MenuItem item = new MenuItem(loadedClass.getSimpleName());
                                    item.setOnAction(this::chooseShape);
                                    menuShapes.getItems().add(item);
                                }
                            }
                        }
                    }
                }
            } catch (MalformedURLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                Logger.getLogger(Controller.class).error("Error while uploading shapes:\n" + e);
            }
        }
    }

    private static boolean jarVerify(JarFile jar) throws IOException{
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            try {
                byte[] buffer = new byte[16384];
                InputStream is = jar.getInputStream(entry);
                while ((is.read(buffer,0,buffer.length)) != -1){
                    // Только чтение, которое может вызвать
                    // SecurityException, если цифровая подпись
                    // нарушена.
                }
            } catch (SecurityException se) {
                LOGGER.error("SecurityException : " +
                        se.getMessage());
                return false;
            }
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuShapes.setText(languageMap.get(Words.SHAPES));
            menuFile.setText(languageMap.get(Words.FILE));
            menuEdit.setText(languageMap.get(Words.EDIT));
            menuHelp.setText(languageMap.get(Words.HELP));
            iabout.setText(languageMap.get(Words.ABOUT));
            iadd.setText(languageMap.get(Words.ADD));
            iclose.setText(languageMap.get(Words.CLOSE));
            icreate.setText(languageMap.get(Words.CREATE));
            iopen.setText(languageMap.get(Words.OPEN));
            isave.setText(languageMap.get(Words.SAVE));
            idelete.setText(languageMap.get(Words.DELETE));
            iupload.setText(languageMap.get(Words.UPLOAD));
            bBack.setText(languageMap.get(Words.BACK));
            bClear.setText(languageMap.get(Words.CLEAR));
            String dir = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")).getPath();
            loadModules("F:\\modules\\shapes\\", "by.bsuir.oop.paint.modules.shapes");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e);
        }
    }

    public void upload(ActionEvent actionEvent) {
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if (files != null) {
            for (File file : files) {
                LOGGER.info("file " + file.getName() + " opened");
                try (FileInputStream decoder = new FileInputStream(file)) {
                    JAXBContext context = JAXBContext.newInstance(UserShape.class);
                    Unmarshaller um = context.createUnmarshaller();
                    UserShape uShape = (UserShape) um.unmarshal(decoder);
                    String name = file.getName().replace(".xml", "");
                    factoryHashMap.put(name, new UserShapeFactory(uShape, factoryHashMap));
                    MenuItem item = new MenuItem(name);
                    item.setOnAction(this::chooseShape);
                    menuShapes.getItems().add(item);
                } catch (JAXBException | IOException e) {
                    alert.showAndWait();
                    LOGGER.error(e);
                }
                LOGGER.info("Shape " + file.getName() + " uploaded");
            }
        }
    }

    public void saveUserShape(ActionEvent actionEvent) {
        LOGGER.info("file -> creating user shape...");
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setHeaderText("Enter name of your shape");
        inputDialog.setContentText("Your shape:");
        inputDialog.setTitle("Create");
        Optional<String> uShapeName = inputDialog.showAndWait();
        String fileName;
        if (uShapeName.isPresent()){
            fileName = uShapeName.get() + ".xml";
            try (FileOutputStream encoder = new FileOutputStream("data\\user\\shapes\\" + fileName)){
                LOGGER.info("Shape " + uShapeName + " created");
                UserShape uShape = new UserShape();
                uShape.setShapes(list);
                JAXBContext context = JAXBContext.newInstance(UserShape.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.marshal(uShape, encoder);
            } catch (IOException | JAXBException e) {
                LOGGER.error(e);
            }
            LOGGER.info("User shape \"" + fileName + "\" saved");
        }
    }
}
