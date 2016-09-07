package random;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.*;

public class random {
    public static void main(String[] args){
        FileWriter archivo = null;
        try {
            archivo = new FileWriter("/home/mauricio/Escritorio/Simulación/Tarea2/Pruebas/randomJava.txt");
            PrintWriter linea = new PrintWriter(archivo);
            Random aleatorio = new Random();
            Double numeroAleatorio;
            for(int indice = 0; indice < 1000000; indice++){
                numeroAleatorio = aleatorio.nextDouble();
                linea.println(BigDecimal.valueOf(numeroAleatorio).toPlainString());
            }   archivo.close();
        } 
        catch (IOException ex) {
            Logger.getLogger(random.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try {
                archivo.close();
            } catch (IOException ex) {
                Logger.getLogger(random.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
