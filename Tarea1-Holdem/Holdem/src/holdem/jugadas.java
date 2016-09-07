package holdem;

import java.util.Objects;

public class jugadas {
     
    public static Boolean escaleraReal(carta[] mano){
        int[] escalera = {13,12,11,10,1};
        String palo = mano[1].getPalo();
                
        for(int i = 1; i < 6; i++){
            
            if(!mano[i].getNumero().equals(escalera[i-1]) || !palo.equals(mano[i].getPalo())){
                return false;
            }      
            
        }
        return true;
    }
    
    public static Boolean escaleraColor(carta[] mano){
        int indice = mano[1].getNumero();
        String palo = mano[1].getPalo();
                       
        for(int i = 1; i < 6; i++){

            if(!mano[i].getNumero().equals(indice) || !palo.equals(mano[i].getPalo())){
                return false;
            }
            indice--;
            
        }
        return true;
    }
    
    public static Boolean poquer(carta[] mano){
           if(mano[1].getNumero().equals(mano[2].getNumero()) && mano[1].getNumero().equals(mano[3].getNumero())
                && mano[1].getNumero().equals(mano[4].getNumero())){
            return true;
        }
        else if(mano[2].getNumero().equals(mano[3].getNumero()) && mano[2].getNumero().equals(mano[4].getNumero())
                && mano[2].getNumero().equals(mano[5].getNumero())){
            return true;
        }
        return false;
    }
    
    public static Boolean fullHouse(carta[] mano){
        if(mano[1].getNumero().equals(mano[2].getNumero()) && mano[1].getNumero().equals(mano[3].getNumero())
                && mano[4].getNumero().equals(mano[5].getNumero())){
            return true;
        }
        else if(mano[1].getNumero().equals(mano[2].getNumero()) && mano[3].getNumero().equals(mano[4].getNumero())
                && mano[3].getNumero().equals(mano[5].getNumero())){
            return true;
        }
        return false;
    }
    
    public static Boolean color(carta[] mano){
        String palo = mano[1].getPalo();
                
        for(int i = 2; i < 6; i++){
            
            if(!palo.equals(mano[i].getPalo())){
                return false;
            }      
            
        }
        return true;
    }
    
    public static Boolean escalera(carta[] mano){
        int indice = mano[1].getNumero();
        if(mano[1].getNumero().equals(13) && mano[2].getNumero().equals(12) && mano[3].getNumero().equals(11) &&
                mano[4].getNumero().equals(10) && mano[5].getNumero().equals(1)){
            return true;
        }
                       
        for(int i = 1; i < 6; i++){
            if(!mano[i].getNumero().equals(indice)){
                return false;
            }
            indice--;
            
        }
        return true;
    }
    
    public static Boolean trio(carta[] mano){
        
        if(Objects.equals(mano[1].getNumero(), mano[2].getNumero()) && Objects.equals(mano[1].getNumero(), mano[3].getNumero())){
            return true;
        }
        
        else if(Objects.equals(mano[2].getNumero(), mano[3].getNumero()) && Objects.equals(mano[2].getNumero(), mano[4].getNumero())){
            return true;
        }
        
        else if(Objects.equals(mano[3].getNumero(), mano[4].getNumero()) && Objects.equals(mano[3].getNumero(), mano[5].getNumero())){
            return true;
        }
        else{
            return false;
        }
    }
    
    public static Boolean doblePar(carta[] mano){
        return cantidadPares(mano).equals(2);
    }
    
    public static Boolean par(carta[] mano){
        return cantidadPares(mano).equals(1);
    }
    
    public static Integer cartaAlta(carta[] mano){
        return mano[1].getNumero();
    }
    
    public static Integer cantidadPares(carta[] mano){
        int contador = 0;
        int valorActual = mano[1].getNumero();
        
        for(int i = 2; i < 6; i++){
            
            if(mano[i].getNumero().equals(valorActual)){
                contador++;                
            }
            valorActual = mano[i].getNumero();
            
        }
        
        return contador;
    }
    
    public static String buscarPoquer(carta[] manoJ1, carta[] manoJ2){
        int cartaJ1 = manoJ1[1].getNumero();
        int cartaJ2 = manoJ2[1].getNumero();
        
        if(!manoJ1[2].getNumero().equals(cartaJ1)){
            cartaJ1 = manoJ1[2].getNumero();
        }           
        
        if(!manoJ2[2].getNumero().equals(cartaJ2)){
            cartaJ2 = manoJ2[2].getNumero();
        }
        
        if(cartaJ1 > cartaJ2){
            return "gano";
        }
        
        else if(cartaJ2 < cartaJ1){
            return "perdio";
        }
        
        return buscarCartaAlta(manoJ1,manoJ2);
    }
    
    public static String buscarFullHouse(carta[] manoJ1, carta[] manoJ2){
        return buscarTrio(manoJ1,manoJ2,true);
    }
    
    public static String buscarTrio(carta[] manoJ1, carta[] manoJ2, boolean bandera){
        int trioJ1 = 0;
        int trioJ2 = 0;
        
        if(Objects.equals(manoJ1[1].getNumero(), manoJ1[2].getNumero()) && Objects.equals(manoJ1[1].getNumero(), manoJ1[3].getNumero())){
            trioJ1 = manoJ1[1].getNumero();
        }
        else if(Objects.equals(manoJ1[2].getNumero(), manoJ1[3].getNumero()) && Objects.equals(manoJ1[2].getNumero(), manoJ1[4].getNumero())){
            trioJ1 = manoJ1[2].getNumero();
        }
        else if(Objects.equals(manoJ1[3].getNumero(), manoJ1[4].getNumero()) && Objects.equals(manoJ1[3].getNumero(), manoJ1[5].getNumero())){
            trioJ1 = manoJ1[3].getNumero();
        }
          
        if(Objects.equals(manoJ2[1].getNumero(), manoJ2[2].getNumero()) && Objects.equals(manoJ2[1].getNumero(), manoJ2[3].getNumero())){
            trioJ2 = manoJ2[1].getNumero();
        }
        else if(Objects.equals(manoJ2[2].getNumero(), manoJ2[3].getNumero()) && Objects.equals(manoJ2[2].getNumero(), manoJ2[4].getNumero())){
            trioJ2 = manoJ2[2].getNumero();
        }
        else if(Objects.equals(manoJ2[3].getNumero(), manoJ2[4].getNumero()) && Objects.equals(manoJ2[3].getNumero(), manoJ2[5].getNumero())){
            trioJ2 = manoJ2[3].getNumero();
        }
        
        if(trioJ1 > trioJ2){
            return "gano";
        }
        else if(trioJ1 < trioJ2){
            return "perdio";
        }
        
        if(!bandera){
            return buscarCartaAlta(manoJ1,manoJ2);
        }
        else{
            return buscarPar(manoJ1,manoJ2);
        }
    }
    
    public static String buscar2Par(carta[] manoJ1, carta[] manoJ2){
        int parJ1 = 0, parJ2 = 0;
        int valorJ1 = manoJ1[1].getNumero();
        int valorJ2 = manoJ2[1].getNumero();
        boolean bandera = false;
        
        for(int i = 2; i < 6; i++){
            
            if(manoJ1[i].getNumero().equals(valorJ1)){
                parJ1 = manoJ1[i].getNumero();
                bandera = true;
            }
            
            if(manoJ2[i].getNumero().equals(valorJ2)){
                parJ2 = manoJ2[i].getNumero();   
                bandera = true;
            }
            
            valorJ1 = manoJ1[i].getNumero();
            valorJ2 = manoJ2[i].getNumero(); 
         
            if(valorJ1 > valorJ2 && bandera == true){
                return "gano";
            }
            else if(valorJ2 < valorJ2 && bandera == true){
                return "perdio"; 
            }
            bandera = false;
        }      
        return buscarCartaAlta(manoJ1, manoJ2);
    }
    
    public static String buscarPar(carta[] manoJ1, carta[] manoJ2){
        int parJ1 = 0, parJ2 = 0;
        int valorJ1 = manoJ1[1].getNumero();
        int valorJ2 = manoJ2[1].getNumero();
        
        for(int i = 2; i < 6; i++){
            
            if(manoJ1[i].getNumero().equals(valorJ1)){
                parJ1 = manoJ1[i].getNumero();                
            }
            
            if(manoJ2[i].getNumero().equals(valorJ2)){
                parJ2 = manoJ2[i].getNumero();                
            }
            
            valorJ1 = manoJ1[i].getNumero();
            valorJ2 = manoJ2[i].getNumero(); 
        }
        
        if(valorJ1 > valorJ2){
            return "gano";
        }
        else if(valorJ2 < valorJ2){
            return "perdio"; 
        }        
        return buscarCartaAlta(manoJ1, manoJ2);
    }
    
    public static String buscarCartaAlta(carta[] manoJ1, carta[] manoJ2){
        for(int i = 2; i < 6; i++){
            if(manoJ1[i].getNumero() > manoJ2[i].getNumero()){
                return "gano";
            }
            else if(manoJ1[i].getNumero() < manoJ2[i].getNumero()){
                return "perdio";
            }
        }
        return "empate";        
    }
    
}
