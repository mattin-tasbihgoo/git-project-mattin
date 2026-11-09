For fixing the createblob I had to make sure the file reader in create blob read every line and it initially didnt because he used readLine but I switched to the same way as in fileHasher with readAllBytes.
Also, updateIndex() would delete entries when a file was restaged and unchaged, so if you created a blob with the same hash the method removed the line from the index instead of keeping just one.
Lastly createTree() skipped all entries in the root because for a path without a / the parent directory was never established so the root tree came out empty. Fixed.

Added Commit method in git.java, GitWrapper class to bring all functionality into one method, and  GitTeter class to confirm.
