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
    private final ArrayList<Integer> variablesAleatorias = new ArrayList<Integer>();
    private final ArrayList<Float> variablesAleatoriasC= new ArrayList<Float>();
    private ArrayList<ArrayList> resultadoVariables;

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

    
    public ArrayList<ArrayList> getResultadoVariables() {
        return resultadoVariables;
    }

    public ArrayList<ArrayList> getVariablesContinuas() {
        return variablesContinuas;
    }

    public ArrayList<Integer> getVariablesAleatorias() {
        return variablesAleatorias;
    }

    public void setResultadoVariables(ArrayList<ArrayList> resultadoVariables) {
        this.resultadoVariables = resultadoVariables;
    }
   
    
    public void leerArchivo() throws FileNotFoundException, IOException{
        File archivo = new File(direccion);
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        
        String linea;

        
        if(tipoVariable){
            while((linea = br.readLine())!=null){
                this.variablesAleatorias.add(Integer.parseInt(linea));
            }
        
            br.close();
        }
        else{
            while((linea = br.readLine())!=null){
                this.variablesAleatoriasC.add(Float.parseFloat(linea));
            }
            br.close();
        }

/*            linea = br.readLine();
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
*/
        
        if(tipoVariable){
            mapeoVariablesDiscretas();
        }
        else{
            mapeoVariablesContinuas();
        }
        
    }
    
    public void mapeoVariablesDiscretas(){
        ArrayList<ArrayList> pares = new ArrayList();
        Collections.sort(variablesAleatorias);
        
        ArrayList parActual;
        //Mapea las variables con respecto a los rangos
        for(int j = 0; j < variablesAleatorias.size(); j++){
            parActual = new ArrayList();
            int variableAleatoria = variablesAleatorias.get(j);
            int ocurrencias = Collections.frequency(variablesAleatorias, variableAleatoria);
            parActual.add(variableAleatoria);
            parActual.add(ocurrencias);
            j += ocurrencias;
            pares.add(parActual);
        }
        this.inicio = (Integer) (pares.get(0).get(0));
        this.fin = (Integer)(pares.get(pares.size()-1).get(0));
        this.resultadoVariables = pares;
        System.out.println(pares);
    }
    
    public void mapeoVariablesContinuas(){
        Collections.sort(variablesAleatoriasC);
        ArrayList<ArrayList> mapeoResultante = new ArrayList<>(); 
        DecimalFormat decimales = new DecimalFormat("#0.0");
        int total = 0;
        
        //Mapea las variables con respecto a los rangos
        for(int i = 0; i < variablesAleatoriasC.size(); i++){
            float variableAleatoria = variablesAleatoriasC.get(i);
            String aleatorioActual = decimales.format(variableAleatoria);
            int apariciones = 0;
            
            ArrayList mapeoActual = new ArrayList();
            int valorJ = 0;
            boolean bandera = true;
            while(bandera && i != 1001){
                float variable = variablesAleatoriasC.get(i);
                String aleatorio = decimales.format(variable); 
                
                if(aleatorioActual.equals(aleatorio)){
                     apariciones++; 
                    i++;
                }  
                else if(i == variablesAleatoriasC.size()-1){
                    bandera = false;
                }
                else{
                    bandera = false;
                    i--;
                }
            }
            if(total == 1000){
                break;
            }
            total +=apariciones;
            //i+=valorJ;
            mapeoActual.add(Float.parseFloat(aleatorioActual));
            mapeoActual.add(apariciones);
            mapeoResultante.add(mapeoActual);
            
        }
        System.out.println(total);
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
