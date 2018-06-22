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
                boolean haycoma=line.startsWith("\"");
                String[] vector = line.split(",");
                if (vector.length < 3) {
                    continue;
                }
                if (vector[2].length() < 10) {
                    continue;
                }

                String frase = vector[2];
                String flag = vector[3];
                                
             
                if(haycoma){
                    frase+=","+vector[3];
                    contar_activas_fila(vector[4]);
                }else{
                        contar_activas_fila(vector[3]);    
                }
                
                frase=frase.replace("\"", "").replace("`", "");
                //System.out.println("--"+frase+"\n");
                
                sacar_etiquetas(frase);
                vector = new String[2];
                vector[0] = frase;
                vector[1] = flag;
                listado.add(vector);
            }
        } catch (Exception e) {
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
    
    
        public static void contar_activas_fila(String celda){
        try{
            if(Integer.parseInt(celda)==1){
                csvactivas+=1;
            }
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static int csvactivas=0;

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
    
    
        public static void inicializar_variables(){
        if(tokenizer==null){
            try {
                InputStream tokenModelIn=null;
                tokenModelIn = new FileInputStream("src/OPENNLP/en-token.bin");
                TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
                tokenizer = new TokenizerME(tokenModel);
                
                InputStream posModelIn = new FileInputStream("src/OPENNLP/en-pos-maxent.bin"); 
                posModel = new POSModel(posModelIn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static TokenizerME tokenizer;
        public static POSModel posModel;
    
    public static void sacar_etiquetas(String sentence){
      
        sentence=sentence.toLowerCase();

        try {
            if (tokenizer == null) {
                inicializar_variables();
            }
            String[] tokens = tokenizer.tokenize(sentence);

            POSTaggerME posTagger = new POSTaggerME(posModel);
            String tags[] = posTagger.tag(tokens);
            
            System.out.print("\n\n"+sentence+"\n");
            for (int i = 0; i < tokens.length; i++) {
                System.out.print(tags[i]+", ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    


    
    
    

    public static int ActivoPasivo(String sentence) {
        sujeto = false;
        verbo = false;
        auxiliar = false;
        by_word = false;
        
        sentence=sentence.toLowerCase();

        try {
            
            if (tokenizer == null) {
               inicializar_variables();

            }
            String[] tokens = tokenizer.tokenize(sentence);


            POSTaggerME posTagger = new POSTaggerME(posModel);

            String tags[] = posTagger.tag(tokens);

            
            // SI HAY SUJETO
            for (int i = 0; i < tokens.length; i++) {
                if (tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("NN") || tags[i].equals("PRP") || tags[i].equals("PRP$")) {
                    sujeto = true;
                }
            }
                
            // SI HAY VERBO
            for (int i = 0; i < tokens.length; i++) {
                if (tags[i].equals("VBN") || tags[i].equals("VBG") || tags[i].equals("VBD")) {
                    verbo = true;
                }
            }
            
            
            if(sentence.contains("was") || sentence.contains("were")  || sentence.contains("are") || sentence.contains("is") || sentence.contains("be")
                    || sentence.contains("being") || sentence.contains("been")
                    
                    ){   
                auxiliar = true;
            }

            if(sentence.contains("by")){
                by_word=true;
            }


            
            if(by_word ){
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
