import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GitTester {
    public static void main(String[] args) throws IOException {
        GitWrapper.init();
        System.out.println(read("git/HEAD"));

        write("a.txt", "one");
        GitWrapper.add("a.txt");
        System.out.println(GitWrapper.commit("me", "one").equals(read("git/refs/heads/main")));

        GitWrapper.createBranch("feature");
        System.out.println(read("git/refs/heads/feature").equals(read("git/refs/heads/main")));

        GitWrapper.checkout("feature");
        write("a.txt", "two");
        GitWrapper.add("a.txt");
        GitWrapper.commit("me", "two");
        System.out.println(read("git/refs/heads/feature").equals(read("git/refs/heads/main")) == false);

        GitWrapper.checkout("main");
        System.out.println(read("a.txt") + " " + read("git/HEAD"));

        GitWrapper.checkout("feature");
        System.out.println(read("a.txt"));

        try {
            GitWrapper.createBranch("feature");
        } catch (RuntimeException e) {
            System.out.println("error");
        }

        try {
            GitWrapper.checkout("nope");
        } catch (RuntimeException e) {
            System.out.println("error");
        }
    }

    public static String read(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        reader.close();
        return line;
    }

    public static void write(String fileName, String text) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(text);
        writer.close();
    }
}