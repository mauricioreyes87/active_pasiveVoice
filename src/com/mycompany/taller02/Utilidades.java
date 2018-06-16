package com.mycompany.taller02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Utilidades {

    public static boolean sujeto;
    public static boolean verbo;
    public static boolean auxiliar;
    public static boolean by_word;

    public static ArrayList<String[]> cargar_csv(String ruta) {
        String csvFile = ruta;
        BufferedReader br = null;
        String line = "";

        String[] datos = null;
        ArrayList<String[]> listado = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                String[] vector = line.split(",");
                if (vector.length < 3) {
                    continue;
                }
                if (vector[2].length() < 10) {
                    continue;
                }

                String frase = vector[2];
                String flag = vector[3];
                System.out.println(frase);

                vector = new String[2];
                vector[0] = frase.replace("\"", "");
                vector[1] = flag;
                listado.add(vector);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return listado;
    }

    public static String retornar_frases(ArrayList<String[]> dats) {
        String frases = "";
        for (String[] linea : dats) {
            frases += linea[0] + "\n";
        }
        return frases;
    }

    public static String[] retornar_vector_frases(ArrayList<String[]> dats) {
        String frases = "";
        String[] vfrases = new String[dats.size()];
        for (int i = 0; i < dats.size(); i++) {
            vfrases[i] = dats.get(i)[0];
        }
        return vfrases;
    }

    public static TokenizerME tokenizer;

    public static int ActivoPasivo(final String sentence) {
        sujeto = false;
        verbo = false;
        auxiliar = false;
        by_word = false;

        try {
            InputStream tokenModelIn = null;
            if (tokenizer == null) {
                tokenModelIn = new FileInputStream("src\\OPENNLP\\en-token.bin");
                TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
                tokenizer = new TokenizerME(tokenModel);

            }
            String[] tokens = tokenizer.tokenize(sentence);

            InputStream posModelIn = new FileInputStream("src\\OPENNLP\\en-pos-maxent.bin");
            POSModel posModel = new POSModel(posModelIn);
            POSTaggerME posTagger = new POSTaggerME(posModel);

            String tags[] = posTagger.tag(tokens);

            for (int i = 0; i < tokens.length; i++) {
                if (tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("NN") || tags[i].equals("PRP") || tags[i].equals("PRP$")) {
                    sujeto = true;
                }
            }

            for (int i = 0; i < tokens.length; i++) {
                if (tags[i].equals("VBN") || tags[i].equals("VBG") || tags[i].equals("VBD")) {
                    verbo = true;
                }
            }

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("was") || tokens[i].equals("were") || tokens[i].equals("are") || tokens[i].equals("is") || tokens[i].equals("be")) {
                    auxiliar = true;
                }
            }

            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("by")) {
                    by_word = true;
                }
            }

            if (tokenModelIn != null) {
                tokenModelIn.close();
            }

            if (posModelIn != null) {
                posModelIn.close();
            }
            
            if(by_word){
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
