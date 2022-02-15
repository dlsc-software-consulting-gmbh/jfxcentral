///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS jitpack,mavencentral
//REPOS sandec=https://sandec.jfrog.io/artifactory/repo
// gluon does not seem necessary
////REPOS https://nexus.gluonhq.com/nexus/content/repositories/releases

// javafx added explicilty to get os specific variants.
//DEPS org.openjfx:javafx-base:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-controls:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-graphics:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-web:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-media:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-swing:18-ea+9:${os.detected.jfxname}
//DEPS org.openjfx:javafx-fxml:18-ea+9:${os.detected.jfxname}

// actual jar we want to run
//DEPS com.github.maxandersen:jfxcentral:jbang-SNAPSHOT

import com.dlsc.jfxcentral.JFXCentralApp;

import static java.lang.System.*;

public class main {

    public static void main(String... args) {
        JFXCentralApp.main(args);
    }
}
