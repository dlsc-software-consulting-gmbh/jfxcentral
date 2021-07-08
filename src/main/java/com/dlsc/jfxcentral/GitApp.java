package com.dlsc.jfxcentral;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.ContentMergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitApp {

    public static void main(String[] args) throws GitAPIException, IOException {
        File repoDirectory = new File(System.getProperty("user.home") + "/.jfxcentralrepo");
        if (!repoDirectory.exists()) {
            Git.cloneRepository()
                    .setURI("https://github.com/dlemmermann/jfxcentral-data.git")
                    .setBranch("live")
                    .setDirectory(repoDirectory)
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();
        } else {
            repoDirectory = new File(System.getProperty("user.home") + "/.jfxcentralrepo/.git");
            Git git = new Git(new FileRepositoryBuilder().create(repoDirectory));
            git.pull().setContentMergeStrategy(ContentMergeStrategy.THEIRS).call();
        }

        System.out.println("done");
    }
}
