import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

        if (!Files.exists(path)) {
            throw new IOException("Path doesn't exist");
        }
        if (Files.isDirectory(path)) {
            throw new IOException("Cannot stage a directory");
        }

        String entry = git.hashFile(filePath) + " " + filePath;
        for (String indexLine : Files.readAllLines(Path.of("git/index"))) {
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
     * tree: <tree_sha>
     * parent: <parent_sha> (omit line for initial commit)
     * author: <author>
     * date: <date>
     * message: <message>
     *
     * @param author  The author name.
     * @param message The commit message.
     * @return The SHA-1 hash of the new commit.
     */
    public static String commit(String author, String message) {
        return git.commit(author, message);
    }

    public static void checkout(String target) {
        if (new File("git/refs/heads/" + target).isFile()) {
            checkoutBranch(target);
            return;
        }

        try {
            restoreCommit(target);

            FileWriter headWriter = new FileWriter("git/HEAD");
            headWriter.write(target);
            headWriter.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

        public static void checkoutBranch(String branchName) {
        try {
            BufferedReader branchReader = new BufferedReader(new FileReader("git/refs/heads/" + branchName));
            String commitHash = branchReader.readLine();
            branchReader.close();

            restoreCommit(commitHash);

            FileWriter headWriter = new FileWriter("git/HEAD");
            headWriter.write("ref: refs/heads/" + branchName);
            headWriter.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // Recursively rebuilds one tree. Each tree line is "blob|tree <sha> <name>".
    private static void restoreTree(String treeHash, String folder) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("git/objects/" + treeHash));
        for (String e : lines) {
            String type = e.substring(0, 4);
            String hash = e.substring(5, 45);
            String path = folder + e.substring(46);

            if (type.equals("tree")) {
                Files.createDirectories(Path.of(path));
                restoreTree(hash, path + "/");
            } else {
                // blobs have the file's original bytes, so copy them back out
                Files.write(Path.of(path), Files.readAllBytes(Path.of("git/objects/" + hash)));
            }
        }
    }

    public static void createBranch(String branchName) {
        try {
            File branchFile = new File("git/refs/heads/" + branchName);
            if (branchFile.exists())
                throw new IllegalArgumentException("Branch already exists: " + branchName);

            String currentHash = git.currentCommit();
            if (currentHash.equals(""))
                throw new IllegalStateException("No commits yet");

            FileWriter branchWriter = new FileWriter(branchFile);
            branchWriter.write(currentHash);
            branchWriter.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void restoreCommit(String commitHash) throws IOException {
        BufferedReader commitReader = new BufferedReader(new FileReader("git/objects/" + commitHash));
        String firstLine = commitReader.readLine();
        commitReader.close();

        restoreTree(firstLine.substring(6), "");
    }
}
