import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Verify {
        public static void main(String[] args) throws IOException {
        git.init();
        git.createBlob("tree-test/docs/hello.txt");
        git.commit("Mr. Hubbard", "First Commit");
        git.createBlob("tree-test/docs/world.txt");
        git.commit("Mr. Hubbard", "Second Commit");
   
        // git.createBlob("tree-test/notes.txt");
        // System.out.println(Files.readString(Path.of("git/index")));

        // git.createBlob("tree-test/docs/world.txt");
        // System.out.println(Files.readString(Path.of("git/index")));

        // System.out.println("Root tree: " + git.buildRootTree() + " Objects: " + new File("git/objects").list().length);
        // System.out.println("Root tree: " + git.buildRootTree() + " Objects: " + new File("git/objects").list().length);

        // Files.writeString(Path.of("tree-test/docs/hello.txt"), "hello again");
        // git.createBlob("tree-test/docs/hello.txt");
        // System.out.println("index after editing " + Files.readString(Path.of("git/index")));

    }

}
