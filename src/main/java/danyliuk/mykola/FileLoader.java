package danyliuk.mykola;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mykola Danyliuk
 */
public class FileLoader {

    private static Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.print("Enter filepath or /q to quit: ");
        String path = scanner.next();
        if(path.equals("/q")){
            return;
        }
        File mainDirectory = new File(path);
        try {
            openDirectory(mainDirectory);
        } catch (IOException e) {
            System.err.println(e.getMessage());;
        }
    }

    private void openDirectory(File directory) throws IOException {
        File[] elements = directory.listFiles();
        if(elements!=null){
            //System.out.print("Directory " + directory.getName() + " elements: ");
            for(File element:elements){
                //System.out.print(element.getName() + " ");
            }
            System.out.print("\n");
            int size = elements.length;
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            for(final File element:elements){
                if(element.isDirectory()){
                    Callable<Void> callable = new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            openDirectory(element);
                            return null;
                        }
                    };
                    executorService.submit(callable);
                } else if(element.isFile()){
                    openFile(element);
                }
            }
            executorService.shutdown();
        }
    }

    private void openFile(File file) throws IOException {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(fileExtension.equals(".txt")){
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
                System.out.println("Replaced in " + file.getAbsolutePath());
            }
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