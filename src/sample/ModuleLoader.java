package sample;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ModuleLoader extends ClassLoader {

    //Path to modules
    private String pathtobin;
    private String packageName;
    //if we used Digital Signature
    private int fsize;
    private static final Logger LOGGER = Logger.getLogger(ModuleLoader.class);

    public ModuleLoader(String pathtobin, String packageName, ClassLoader parent) {
        super(parent);
        this.pathtobin = pathtobin;
        this.packageName = packageName;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] b;
        try {
            // Get byte-code and upload runtime
            b = Files.readAllBytes(Paths.get(pathtobin + className + ".class"));
        }catch (IOException e) {
            LOGGER.error(e);
            return super.findClass(className);
        }
        return defineClass(packageName + '.' + className, b, 0, this.fsize == 0 ? b.length : fsize);
    }
}

