

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by nicolas on 20/11/2014.
 */
public class UnreleaseConnection_tryresourceBlock extends ClassLoader {
    private final File directory;

    public UnreleaseConnection_tryresourceBlock(ClassLoader classLoader, File directory) {
        super(classLoader);
        this.directory = directory;
    }

    public UnreleaseConnection_tryresourceBlock(File directory) {
        this.directory = directory;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        try {
            File file = new File(directory, name.replaceAll("\\.", "/") + ".class");
            if (!file.exists()) {
                return super.loadClass(name);
            }
            ByteArrayOutputStream buffer;
            java.io.InputStream input1 = null;
            try(InputStream input = new FileInputStream(file)) {
                input1 = new java.io.FileInputStream(file);
                buffer = new ByteArrayOutputStream();
                int data = input.read();
                while (data != -1) {
                    buffer.write(data);
                    data = input.read();

                }
            } finally {
                if (input1 != null ) input1.close();
            }

           byte[] classData = buffer.toByteArray();
       return defineClass(name, classData, 0, classData.length);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
