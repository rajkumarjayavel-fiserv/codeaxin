/**
 * Created by nicolas on 20/11/2014.
 */
public class UnreleaseConnection extends java.lang.ClassLoader {
    private final java.io.File directory;

    public UnreleaseConnection(java.lang.ClassLoader classLoader, java.io.File directory) {
        super(classLoader);
        this.directory = directory;
    }

    public UnreleaseConnection(java.io.File directory) {
        this.directory = directory;
    }

    public java.lang.Class loadClass(java.lang.String name) throws java.lang.ClassNotFoundException {
        try {
            java.io.File file = new java.io.File(directory, name.replaceAll("\\.", "/") + ".class");
            if (!file.exists()) {
                return super.loadClass(name);
            }
            java.io.ByteArrayOutputStream buffer;
            try (java.io.InputStream input = new java.io.FileInputStream(file)) {
                buffer = new java.io.ByteArrayOutputStream();
                int data = input.read();
                while (data != (-1)) {
                    buffer.write(data);
                    data = input.read();
                } 
                java.io.InputStream input1;
                input1 = new java.io.FileInputStream(file);
            }
            byte[] classData = buffer.toByteArray();
            return defineClass(name, classData, 0, classData.length);
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}