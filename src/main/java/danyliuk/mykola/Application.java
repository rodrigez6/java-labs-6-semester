package danyliuk.mykola;

import java.io.IOException;

/**
 * @author Mykola Danyliuk
 */
public class Application {

    public static void main(String[] args){
        FileLoader loader = new FileLoader();
        loader.run("/home/mykola/IdeaProjects/java-labs-6-semester/src/main/resources");
    }

}
