package com.github.davidji80.maven.mahout;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateFile {

    public static void main(String[] args) {

        FileOutputStream out = null;
        try {
            int[][] arg={
                    {3,1,4,4,1,0,0},
                    {0,5,1,0,0,4,0},
                    {1,0,5,4,3,5,2},
                    {3,1,4,3,5,0,0},
                    {5,2,0,1,0,5,5}
            };
            out = new FileOutputStream(new File("D:\\movie.data"));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    if (arg[i][j]>0) {
                        String a = i + "," + j + "," + arg[i][j] + "\n";
                        out.write(a.getBytes());
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
