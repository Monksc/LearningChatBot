import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
public class Memory {
    
    private final String DIR;
    
    public Memory(String dir) {
        this.DIR = dir;
    }
    
    private byte[] readFile(String fileName) throws IOException {
        Path path = Paths.get("memory/" + DIR + "/" + fileName);
        
        return Files.readAllBytes(path);
    }
    
    private void writeFile(String fileName, byte[] aBytes) throws IOException {
        Path path = Paths.get("memory/" + DIR + "/" + fileName);
        Files.write(path, aBytes); //creates, overwrites
    }
    
    public boolean setInteger(String name, int i) {
        byte[] b = {(byte)(i)};
        
        try {
            writeFile("memory/" + DIR + "/" + name, b);
            return true;
        } catch(Exception e) {
            System.out.println("ERROR: In Memory.java setInteger(String , int)");
            return false;
        }
    }
    public int getInteger(String name) {
        try {
            byte[] bytes = readFile("memory/" + DIR + "/" + name);
            return bytes[0] & 0xFF;
        } catch (Exception e) {
            System.out.println("ERROR: In Memory.java getNumber(String)");
        }
        
        return -1;
    }
    
    public boolean setString(String name, String str) {
        byte[] b = str.getBytes();
        
        try {
            writeFile(name, b);
            return true;
        } catch(Exception e) {
            System.out.println("ERROR: In Memory.java setString(String , String)");
            return false;
        }
    }
    public String getString(String name) {
        try {
            return new String(readFile(name));
        } catch(Exception e) {
            System.out.println("ERROR: In Memory.java getString(" + name + ") in dir " + DIR);
            return null;
        }
    }
    public boolean appendString(String name, String newStr) {
        String oldStr = getString(name);
        if (oldStr == null) {
            return setString(name, newStr);
        }
        return setString(name, oldStr + newStr);
    }
    
    public String[] getFileNamesOfWords() {
        File folder = new File(DIR);
        File[] listOfFiles = folder.listFiles();

        String[] fileNames = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fileNames[i] = listOfFiles[i].getName();
            }
        }
        
        return fileNames;
    }
    
}