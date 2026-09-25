import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.nio.file.Files;
import java.nio.file.Path;

public class git {

    public static void main(String[] args) {
        init();

    }

    public static void init() {
        try {
            File gitDir = new File("git");
            if (gitDir.exists())
                throw new IOException("Git directory exists");
            gitDir.mkdirs();

            File objectsDir = new File("git/Objects");
            objectsDir.mkdirs();

            File index = new File("git/index");
            if (!index.exists())
                index.createNewFile();

            File head = new File("git/HEAD");
            if (!head.exists())
                head.createNewFile();

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}