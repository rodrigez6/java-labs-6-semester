package danyliuk.mykola;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mykola Danyliuk
 */
public class FileLoader {

    public void run(String path) {
        File mainDirectory = new File(path);
        openDirectory(mainDirectory);
    }

    private void openDirectory(File directory) {
        System.out.println("In Directory " + directory.getName());
        File[] elements = directory.listFiles();
        if(elements!=null){
            int size = elements.length;
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            for(final File element:elements){
                Callable<File> callable = new Callable<File>() {
                    @Override
                    public File call() throws Exception {
                        if(element.isFile()){
                            openFile(element);
                        } else if(element.isDirectory()){
                            openDirectory(element);
                        }
                        return null;
                    }
                };
                executorService.submit(callable);
            }
        }
    }

    private void openFile(File file) throws IOException {
        System.out.println("In File " + file.getName());
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line!=null){
                String updated = replaceWords(line);
                text.append(updated).append('\n');
                line = br.readLine();
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(String.valueOf(text));
        }
    }

    String replaceWords(String s){
        String[] words = s.split(" ");
        int size = words.length;
        if(size>=2){
            String firstWord = words[0];
            int firstIndex = s.indexOf(firstWord);
            String beforeFirstWord = s.substring(0,firstIndex);
            String lastWord = words[size-1];
            int lastIndex = s.lastIndexOf(lastWord);
            String afterLastWord = s.substring(lastIndex + lastWord.length());
            String tail = s.substring(firstIndex+firstWord.length(),lastIndex);
            StringBuilder sb = new StringBuilder().append(beforeFirstWord).append(lastWord)
                    .append(tail).append(firstWord).append(afterLastWord);
            return String.valueOf(sb);
        }
        return s;
    }
}
