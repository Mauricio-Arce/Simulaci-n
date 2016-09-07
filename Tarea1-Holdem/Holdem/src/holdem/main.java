package holdem;

import java.sql.*;

public class main {
       
    public static void main(String[] args) throws SQLException {
        int contador = 0;
        int limite = 1000; 
           
        while(contador < limite){
            jugar.comenzar();
            contador++;
        }
        //conexionBD.mostrarTablaJugador();
        //conexionBD.mostrarCartas();
        conexionBD.mostrarTablaEstadicticas();
    }        
}
