import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GitWrapper {

    /**
     * Initializes a new Git repository.
     * Creates git/, git/objects/, git/index, and git/HEAD.
     */
    public static void init() {
        git.init();
    }

    /**
     * Stages a file for the next commit.
     * Throws IOException if the path does not exist or is a directory.
     * Does nothing if the file's content is already staged for that path.
     *
     * @param filePath The path to the file to stage.
     */
    public static void add(String filePath) throws IOException {
        Path path = Path.of(filePath);

        if(!Files.exists(path)) { 
            throw new IOException("Path doesn't exist");
        }
        if (Files.isDirectory(path)) { 
            throw new IOException("Cannot stage a directory");
        }

        String entry = git.hashFile(filePath) + " " + filePath;
        for(String indexLine : Files.readAllLines(Path.of("git/index"))) { 
            if (indexLine.equals(entry)) { 
                return;
            }
        }

        git.createBlob(filePath);
    }

    /**
     * Creates a commit capturing the current state of the index.
     * Builds trees, writes the commit file, updates HEAD.
     *
     * Commit format:
     *   tree: <tree_sha>
     *   parent: <parent_sha>   (omit line for initial commit)
     *   author: <author>
     *   date: <date>
     *   message: <message>
     *
     * @param author  The author name.
     * @param message The commit message.
     * @return The SHA-1 hash of the new commit.
     */
    public static String commit(String author, String message) {
        return git.commit(author, message);
    }
}
