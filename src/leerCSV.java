
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mauricio
 */
public class leerCSV  {
    public static void main(String [] args) {
        String csvFile = "C:\\Users\\Mauricio\\Desktop\\active_pasiveVoice\\src\\csvReader\\Phrases - Hoja 1.csv";
BufferedReader br = null;
String line = "";
//Se define separador ","
String cvsSplitBy = ",";
try {
    br = new BufferedReader(new FileReader(csvFile));
    while ((line = br.readLine()) != null) {                
        String[] datos = line.split(cvsSplitBy);
        //Imprime datos.
       System.out.println(datos[0] + ", " + datos[1] + ", " + datos[2]);
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
    }
}
