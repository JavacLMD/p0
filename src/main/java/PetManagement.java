import com.github.JavacLMD.projectZero.controller.DOA;
import com.github.JavacLMD.projectZero.view.Config;
import com.github.JavacLMD.projectZero.view.Interface;

import java.util.Scanner;

public class PetManagement {

    public static void main(String[] args) {
        Config config = new Config(args);
        config.getAppInterface().run();
    }
}
