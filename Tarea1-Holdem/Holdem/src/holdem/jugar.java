package holdem;

import java.sql.*;
import java.util.*;

public class jugar {
    static jugador jugador1 = new jugador();
    static jugador jugador2 = new jugador();
    private static boolean mejorManoMatriz = false;
    
    public static void comenzar() throws SQLException{
        carta[] manoJ1;
        carta[] manoJ2;
        carta[] pozo;
        
        manoJ1 = barajar("jugador1", 2);
        definirCartas(manoJ1);
        jugador1.setCartasIniciales(manoJ1);
        
        manoJ2 = barajar("jugador2", 2);
        jugador2.setCartasIniciales(manoJ2);   
        
        pozo = barajar("pozo", 5);
        
        generarCombinaciones(jugador1,pozo);
        generarCombinaciones(jugador2,pozo);
                
        elegirGanador();
        conexionBD.insertarJugadores(jugador1,jugador2);
    }
    
    //Asigna una conjunto de cartas a cada jugador
    public static carta[] barajar(String tipo, int limite){
        boolean bandera = false;
        String[] tipoCartas = {"corazones","diamantes","treboles","picas"};
        carta mano[] = new carta[limite];
        
        for(int i = 0; i < limite; i++){
            carta nuevaCarta = new carta();
            int numero = 0, indicePalo = 0;
            String palo = null;
                
            while(!bandera){
                numero = (int) (Math.random()*13+1);
                indicePalo = (int) (Math.random()*4);
                palo = tipoCartas[indicePalo];
            
                if(manoCorrecta(mano, numero, palo, i) && tipo.equals("jugador1")){
                    bandera = true;
                }
                else if(manoCorrecta(mano, numero, palo, i) && tipo.equals("jugador2") && manoCorrecta(jugador1.getCartasIniciales(),numero,palo,2)){
                    bandera = true;
                }
                else if(manoCorrecta(mano, numero, palo, i) && manoCorrecta(jugador1.getCartasIniciales(),numero,palo,2) && manoCorrecta(jugador2.getCartasIniciales(),numero,palo,2)){
                    bandera = true;
                }
            }
            bandera = false;
            nuevaCarta.setNumero(numero);
            nuevaCarta.setPalo(palo);
            mano[i] = nuevaCarta;
        }
        return mano;
    }
    
    //Verifca que la carta escogida no se haya seleccionado anteriormente en la mano 
    public static Boolean manoCorrecta(carta mano[], int numero, String palo, int largo){
        for(int i = 0; i < largo; i++){
            if(mano[i].getNumero().equals(numero) && mano[i].getPalo().equals(palo)){
                return false;
            }
        }
        return true;
    }
    
    public static void definirCartas(carta[] mano){
        Map<Integer, String> mapaCartas = new HashMap<>();
        mapaCartas.put(1, "A");
        mapaCartas.put(11, "J");
        mapaCartas.put(12, "Q");
        mapaCartas.put(13, "k");
        String[] cartas = new String[2];
        
        for(int i = 0; i < 2; i++){
            if(mapaCartas.containsKey(mano[i].getNumero())){
                cartas[i] = mapaCartas.get(mano[i].getNumero());
            }else{
                cartas[i] = mano[i].getNumero().toString();
            }
        }
        jugador1.setCartasInicialesBD(cartas);
    }
    
    public static void generarCombinaciones(jugador j1, carta[] pozoCartas){
        Integer[] combinaciones3 = {1,2,3,1,2,4,1,2,5,1,3,4,1,3,5,1,4,5,2,3,4,2,3,5,2,4,5,3,4,5};
        Integer[] combinaciones4 = {1,2,3,4,1,2,4,5,1,2,3,5,1,3,4,5,2,3,4,5};
        
        carta[][] matrizCombinaciones = new carta[21][6];
        carta[] manoTemporal = new carta[21];
        carta[] cartasIniciales = j1.getCartasIniciales();
        carta cartaActualC4 = cartasIniciales[0];

        int indiceCartasIni = 0;
        int indice3 = 0;
        int indice4 = 0;
        
        
        for(int i = 0; i < 21; i++){
            manoTemporal[i] = new carta();
            if(i==0){
                for(int j = 0; j < 6; j++){
                    if(j == 0){
                        manoTemporal[j].setNumero(14);
                        manoTemporal[j].setPalo("respuesta");
                    }
                    else{
                        manoTemporal[j] = pozoCartas[j-1];
                    }
                }
            }

            else if(i < 11){
                for(int j = 0; j < 6; j++){
                     
                   if(j == 0){
                        manoTemporal[j].setNumero(14);
                        manoTemporal[j].setPalo("respuesta");
                    }
                    else if(combinaciones3[indice3].equals(j)){
                        manoTemporal[j] = pozoCartas[combinaciones3[indice3] - 1];
                        indice3++;
                    }
                    else{
                        manoTemporal[j] = cartasIniciales[indiceCartasIni];
                        indiceCartasIni++;
                    }
                }
                indiceCartasIni = 0;
            }
            else{
                 for(int j = 0; j < 6; j++){
                    if(j == 0){
                        manoTemporal[j].setNumero(14);
                        manoTemporal[j].setPalo("respuesta");
                    }
                    else if(combinaciones4[indice4].equals(j)){
                        manoTemporal[j] = pozoCartas[combinaciones4[indice4] - 1];
                        indice4++;
                    }else{
                        manoTemporal[j] = cartaActualC4;
                    }
                }
                if(i==15){
                    cartaActualC4 = cartasIniciales[1];
                    indice4 = 0;
                }
            
            }
            matrizCombinaciones[i] = ordenamientoCartas(manoTemporal);
            manoTemporal = definirJugada(manoTemporal);
            matrizCombinaciones[i] = manoTemporal;
        }
        mejorMano(j1,matrizCombinaciones);
    }
    
    public static void mejorMano(jugador j1, carta[][] matrizCombinaciones){
        carta[] mejorMano = matrizCombinaciones[0];
        
        for(int i = 1; i < 10; i++){
            if(matrizCombinaciones[i][0].getNumero() > mejorMano[0].getNumero()){
                mejorMano = matrizCombinaciones[i];
            }
            else if(matrizCombinaciones[i][0].getNumero().equals(mejorMano[0].getNumero())){
                elegirDesempate(matrizCombinaciones[i], mejorMano,false);
                if(mejorManoMatriz){
                    mejorMano = matrizCombinaciones[i];
                    mejorManoMatriz = false;
                }
            }
        }
        
        j1.setMano(mejorMano);
    }
    
    //Ordena las cartas de la mano de cada jugador 
    public static carta[] ordenamientoCartas(carta[] mano){
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5 - i; j++){
                if(mano[j].getNumero() < mano[j+1].getNumero()){
                    int tempNumero = mano[j].getNumero();
                    String tempPalo = mano[j].getPalo();
                    mano[j].setNumero(mano[j+1].getNumero());
                    mano[j].setPalo(mano[j+1].getPalo());
                    mano[j+1].setNumero(tempNumero);
                    mano[j+1].setPalo(tempPalo);    
                }
            }
        }
        return mano;
    }
     
    //Evalua que jugada hay en la mano
    public static carta[] definirJugada(carta[] mano){
        if(jugadas.escaleraReal(mano)){
            mano[0].setNumero(10);
            mano[0].setPalo("Escalera Real");
        }
        else if(jugadas.escaleraColor(mano)){
            mano[0].setNumero(9);
            mano[0].setPalo("Escalera Color");
        }
        else if(jugadas.poquer(mano)){
            mano[0].setNumero(8);
            mano[0].setPalo("Poquer");
        }
        else if(jugadas.fullHouse(mano)){
            mano[0].setNumero(7);
            mano[0].setPalo("Full House");
        }
        else if(jugadas.color(mano)){
            mano[0].setNumero(6);
            mano[0].setPalo("Color");
        }
        else if(jugadas.escalera(mano)){
            mano[0].setNumero(5);
            mano[0].setPalo("Escalera");
        }
        else if(jugadas.trio(mano)){
            mano[0].setNumero(4);
            mano[0].setPalo("Trio");
        }
        else if(jugadas.doblePar(mano)){
            mano[0].setNumero(3);
            mano[0].setPalo("Doble Par");
        }    
        else if(jugadas.par(mano)){
            mano[0].setNumero(2);
            mano[0].setPalo("Par");
        }
        else{
            mano[0].setNumero(1);
            mano[0].setPalo("Carta Alta");
        }
        return mano;
    }
  
    public static void elegirGanador(){
        carta[] manoJ1 = jugador1.getMano();
        carta[] manoJ2 = jugador2.getMano();
        
        int jugadaJ1 = manoJ1[0].getNumero();
        int jugadaJ2 = manoJ2[0].getNumero();
        
        if(jugadaJ1 > jugadaJ2){
            jugador1.setResultado("gano");
            jugador2.setResultado("perdio");
        }
        else if(jugadaJ1 < jugadaJ2){
            jugador2.setResultado("gano");
            jugador1.setResultado("perdio");            
        }
        else{
            elegirDesempate(manoJ1,manoJ2, true);
        }
    }
    
    public static void elegirDesempate(carta[] manoJ1, carta[] manoJ2, boolean bandera){
        String resultado = null;
        
        switch (manoJ1[0].getNumero()) {
            case 1:
                resultado = jugadas.buscarCartaAlta(manoJ1,manoJ2);
                break;
            case 2:
                resultado = jugadas.buscarPar(manoJ1,manoJ2);
                break;
            case 3:
                resultado = jugadas.buscar2Par(manoJ1,manoJ2);
                break;
            case 4:
                resultado = jugadas.buscarTrio(manoJ1,manoJ2, false);
                break;
            case 5:
                resultado = jugadas.buscarCartaAlta(manoJ1,manoJ2);
                break;
            case 6:
                resultado = jugadas.buscarCartaAlta(manoJ1,manoJ2);
                break;
            case 7:
                resultado = jugadas.buscarFullHouse(manoJ1,manoJ2);
                break;
            case 8:
                resultado = jugadas.buscarPoquer(manoJ1,manoJ2);
                break;
            case 9:
                resultado = jugadas.buscarCartaAlta(manoJ1,manoJ2);
                break;
            default:
                break;
        }
        
        //Elegir el ganador del juego, en caso de empate
        if(bandera){
            jugador1.setResultado(resultado);
        }
        //Elegir la mejor mano de la matriz de combinaciones
        else if("gano".equals(resultado) && !bandera){
            mejorManoMatriz = true;
        }      
    }
}