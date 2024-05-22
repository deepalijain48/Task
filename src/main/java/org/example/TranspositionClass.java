package org.example;

import org.json.simple.JSONArray;

import java.io.*;
import java.util.*;

public class TranspositionClass {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("All arguments are not provided");
            return;
        }

        String inputFile = args[0];
        int semitone = Integer.parseInt(args[1]);
        String outputFile = args[2];

        try
        {   List<int[]> notes = readNotes(inputFile);
            List<int[]> transposedNotes = transposeNotes(notes, semitone);
            writeNotes(transposedNotes, outputFile);
        } catch (IOException e) {
            System.err.println("Exception in reading/writing files: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception occurred: " +e);
        }
    }

    private static List<int[]> transposeNotes(List<int[]> list, int semitone) {
        List<int[]> transposedNotes = new ArrayList<>();
        int octave = 0;
        int noteNum = 0;
        for(int[] arr :  list) {

            checkInput(arr);
            int noteSum = arr[1] + semitone ;

            if (noteSum > 0 && noteSum < 12) {
                octave = arr[0];
                noteNum = noteSum;
            } else if (noteSum > 12) {
                noteNum = noteSum - 12;
                octave = arr[0]+1;
            } else if (noteSum < 0){
                octave = arr[0]-1;
                noteNum = 12+noteSum;
            }

            if(octave < -3 || octave > 5) {
                throw new RuntimeException("Transpose notes are not in the scope for : "+ Arrays.toString(arr));
            } else if((octave == -3 & noteNum < 10) || ((octave == 5 && noteNum > 1))) {
                throw new RuntimeException("Transpose notes are not in the scope for : "+ Arrays.toString(arr));
            }  else
                transposedNotes.add(new int[]{octave,noteNum});
        }
        return transposedNotes;
    }

    private static void checkInput(int[] arr) {
        if(arr[0] > 5 || arr[0] < -3 ) {
            throw new RuntimeException("Transpose notes are not in the scope for : "+ Arrays.toString(arr));
        } else if ( arr[0] == 5 && arr[1] > 1 ) {
            throw new RuntimeException("Transpose notes are not in the scope for : " + Arrays.toString(arr));
        }
        else if( arr[0] == -3 && arr[1] <10 ) {
            throw new RuntimeException("Transpose notes are not in the scope for : " + Arrays.toString(arr));
        }
    }

    private static List<int[]> readNotes(String inputFile){
        List<int[]> arrayList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[\\[\\]]", "").trim();
                String[] pairs = line.split("\\s*,\\s*");

                for (int i = 0; i < pairs.length; i += 2) {
                    int[] pair = {Integer.parseInt(pairs[i]), Integer.parseInt(pairs[i + 1])};
                    arrayList.add(pair);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static void writeNotes(List<int[]> notes, String outputFile) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            bw.write("[");
            for (int i = 0; i < notes.size(); i++) {
                int[] note = notes.get(i);
                bw.write("[" + note[0] + "," + note[1] + "]");
                if (i < notes.size() - 1) {
                    bw.write(",");
                }
            }
            bw.write("]");
        }
    }
}