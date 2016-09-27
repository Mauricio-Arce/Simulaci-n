package graficobastones;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenerarGrafica {
    private final String direccion;
    private final boolean tipoVariable;
    private int inicio;
    private int fin;
    private ArrayList<ArrayList<Float>> intervalosDiscretos = new ArrayList<ArrayList<Float>>();
    private ArrayList<ArrayList> variablesContinuas = new ArrayList<ArrayList>();
    private final ArrayList<Float> variablesAleatorias = new ArrayList<Float>();
    private ArrayList<Integer> resultadoVariables = new ArrayList<Integer>();

    public GenerarGrafica(String direccion, boolean tipoVariable){
        this.direccion = direccion;
        this.tipoVariable = tipoVariable;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFin() {
        return fin;
    }

    
    public ArrayList<Integer> getResultadoVariables() {
        return resultadoVariables;
    }

    public ArrayList<ArrayList> getVariablesContinuas() {
        return variablesContinuas;
    }

    public ArrayList<Float> getVariablesAleatorias() {
        return variablesAleatorias;
    }
   
    
    public void leerArchivo() throws FileNotFoundException, IOException{
        File archivo = new File(direccion);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        
        String linea;

        
        if(tipoVariable){
            linea = br.readLine();
            String replace;
            List<String>  intervalo;
            replace = linea.replace("(", "").replace(")","").replace(".", ",").replace(" ","");
            String rango[] = replace.split(",");
            intervalo = Arrays.asList(rango);
            inicio = Integer.parseInt(intervalo.get(0));
            fin = Integer.parseInt(intervalo.get(1));
        
            linea = br.readLine();
            replace = linea.replace("(", "").replace(")","").replace(" ", ",");
            String rangoArchivo[] = replace.split(",");
            intervalo = Arrays.asList(rangoArchivo);
            
            generarIntervaloDiscreto(intervalo);
        
        }

        while((linea = br.readLine())!=null){
            this.variablesAleatorias.add(Float.parseFloat(linea));
        }
        
        br.close();
        
        if(tipoVariable){
            mapeoVariablesDiscretas();
        }
        else{
            mapeoVariablesContinuas();
        }
        
    }
    
    public void mapeoVariablesDiscretas(){
       
        //Se crea los intervalos
        for(int i = 0; i < intervalosDiscretos.size(); i++){
            resultadoVariables.add(0);
        }
        
        //Mapea las variables con respecto a los rangos
        for(int j = 0; j < variablesAleatorias.size(); j++){
            float variableAleatoria = variablesAleatorias.get(j);
            for(int k = 0; k < intervalosDiscretos.size(); k++){
                if(variableAleatoria < intervalosDiscretos.get(k).get(1) && variableAleatoria >= intervalosDiscretos.get(k).get(0)){
                    int apariciones = this.resultadoVariables.get(k) + 1;
                    this.resultadoVariables.set(k, apariciones);
                    break;
                }
            }
        }
    }
    
    public void mapeoVariablesContinuas(){
        Collections.sort(variablesAleatorias);
        ArrayList<ArrayList> mapeoResultante = new ArrayList<>(); 
        DecimalFormat decimales = new DecimalFormat("#0.00");
        
        //Mapea las variables con respecto a los rangos
        for(int i = 0; i < variablesAleatorias.size(); i++){
            float variableAleatoria = variablesAleatorias.get(i);
            String aleatorioActual = decimales.format(variableAleatoria);
            int apariciones = 0;
            
            ArrayList mapeoActual = new ArrayList();
            for(int j = i; j < variablesAleatorias.size(); j++){
                float variable = variablesAleatorias.get(j);
                String aleatorio = decimales.format(variable); 
                
                if(aleatorioActual.equals(aleatorio)){
                     apariciones++;
                }  
                else{
                    break;
                }
            }
            mapeoActual.add(Float.parseFloat(aleatorioActual));
            mapeoActual.add(apariciones);
            mapeoResultante.add(mapeoActual);
        }
        variablesContinuas = (ArrayList<ArrayList>) mapeoResultante.clone();
    }
    
    public void generarIntervaloDiscreto(List<String> intervalo) {
        float limiteInferior, limiteSuperior;
        int inferior = 0;
        int superior = 1;
        float acumulado = 0;
        

        
        for(int i = 0; i < intervalo.size(); i++){
            ArrayList<Float> intervaloActual = new ArrayList<>();
            
            if(i == 0){
                limiteInferior = 0;
                limiteSuperior = Float.parseFloat(intervalo.get(inferior));
                acumulado = limiteSuperior;
            }
            else if(i == intervalo.size()-1){
                limiteInferior = acumulado;
                limiteSuperior = 1;
            }
            else{
                limiteInferior = acumulado;
                limiteSuperior = Float.parseFloat(intervalo.get(superior)) + limiteInferior;
                acumulado = limiteSuperior;
                superior++;
                inferior++;
                
            }
            intervaloActual.add(limiteInferior);
            intervaloActual.add(limiteSuperior);
            intervalosDiscretos.add(intervaloActual);
        }
    }

}
