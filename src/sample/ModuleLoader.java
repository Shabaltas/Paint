package sample;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ModuleLoader extends ClassLoader {

    //Path to modules
    private String pathtobin;
    private String packageName;

    public ModuleLoader(String pathtobin, String packageName, ClassLoader parent) {
        super(parent);
        this.pathtobin = pathtobin;
        this.packageName = packageName;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] b;
        try {
            // Get byte-code and upload runtime
            b = Files.readAllBytes(Paths.get(pathtobin + className + ".class"));
        }catch (IOException e) {
            Logger.getLogger(Controller.class).error(e);
            return super.findClass(className);
        }
        return defineClass(packageName + '.' + className, b, 0, b.length);
    }
}

