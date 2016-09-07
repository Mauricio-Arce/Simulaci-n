package holdem;

import java.sql.*;

public class conexionBD {
    private static PreparedStatement insertar;
    private static Statement estado;
    private static Connection con;
    
    public static void insertarJugadores(jugador j1,jugador j2) throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        
        insertar = con.prepareStatement("INSERT INTO Juegos (jugador1,jugador2,resultado)" + "values(?,?,?)");
        
        carta[] jugadaJ1 = j1.getMano();
        carta[] jugadaJ2 = j2.getMano();
        
        insertar.setString(1, jugadaJ1[0].getPalo());
        insertar.setString(2, jugadaJ2[0].getPalo());
        insertar.setString(3,j1.getResultado());
        
        insertar.executeUpdate();
        
        actualizarEstadisticas(j1);
        actualizarCartas(j1);
        insertar.close();
        estado.close();
        con.close(); 
    }
           
    public static void actualizarEstadisticas(jugador j1) throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        
        ResultSet resultado = estado.executeQuery("select * from Estadisticas");
        
        carta[] jugadaJ1 = j1.getMano();
        int cantidad, victorias,derrotas, empates, jugada;
        
        while(resultado.next()){
            jugada = jugadaJ1[0].getNumero();
            cantidad = resultado.getInt("cantidad") + 1;
                
            if(j1.getResultado().equals("gano") && resultado.getInt("id") == jugada){
                victorias = resultado.getInt("victorias") + 1;
                insertar = con.prepareStatement("UPDATE Estadisticas SET cantidad ='" + cantidad + "',victorias ='"+ victorias +"'WHERE id ='" + jugada + "'");            
                break;
            }
            else if(j1.getResultado().equals("perdio") && resultado.getInt("id") == jugada){
                derrotas = resultado.getInt("derrotas") + 1;
                insertar = con.prepareStatement("UPDATE Estadisticas SET cantidad ='" + cantidad + "',derrotas ='"+ derrotas +"'WHERE id ='" + jugada + "'");
                break;
            }
            else if(j1.getResultado().equals("empate") && resultado.getInt("id") == jugada){
                empates = resultado.getInt("empates") + 1;
                insertar = con.prepareStatement("UPDATE Estadisticas SET cantidad ='" + cantidad + "',empates ='"+ empates +"'WHERE id ='" + jugada + "'");
                break;
            }
        }
        insertar.executeUpdate();
        insertar.close();
        estado.close();
        con.close(); 
    }
    
    public static void actualizarCartas(jugador j1) throws SQLException{
        String consultaUpdate = "UPDATE Cartas SET apariciones = ?,victorias = ?,derrotas = ?, empates = ? WHERE carta1 = ? AND carta2 = ? || carta1 = ? AND carta2 = ?"; 
        String[] cartasIniciales = j1.getCartasInicialesBD();
  
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        insertar = con.prepareStatement(consultaUpdate);
           
        ResultSet resultado = estado.executeQuery("select * from Cartas");
        
        //int tempVictoria, tempDerrota, tempEmpate;
        int apariciones;
        int victorias,derrotas, empates;
        
        boolean bandera = false;
        
        while(resultado.next()){
            apariciones = resultado.getInt("apariciones") + 1;
            //tempVictoria = ((apariciones-1)* resultado.getInt("victorias"))/100;                    
            //tempDerrota = ((apariciones-1)* resultado.getInt("derrotas"))/100;                    
            //tempEmpate = ((apariciones-1)* resultado.getInt("empates"))/100;                    
            
            if(j1.getResultado().equals("gano") && (resultado.getString("carta1").equals(cartasIniciales[0]) && resultado.getString("carta2").equals(cartasIniciales[1]) 
                    || resultado.getString("carta1").equals(cartasIniciales[1]) && resultado.getString("carta2").equals(cartasIniciales[0])) ){
                
                /*victorias = (100*(tempVictoria+1))/apariciones;
                derrotas = (100*tempDerrota)/apariciones;
                empates = (100*tempEmpate)/apariciones;*/
                victorias = resultado.getInt("victorias") + 1;
                derrotas = resultado.getInt("derrotas");
                empates = resultado.getInt("empates");
                
                insertar.setInt(1, apariciones);
                insertar.setFloat(2, victorias);
                insertar.setFloat(3, derrotas);
                insertar.setFloat(4, empates);
                insertar.setString(5, cartasIniciales[0]);
                insertar.setString(6, cartasIniciales[1]);
                insertar.setString(7, cartasIniciales[1]);
                insertar.setString(8, cartasIniciales[0]);
                insertar.executeUpdate();
                bandera = true;
                break;
            }
            else if(j1.getResultado().equals("perdio") && (resultado.getString("carta1").equals(cartasIniciales[0]) && resultado.getString("carta2").equals(cartasIniciales[1]) 
                    || resultado.getString("carta1").equals(cartasIniciales[1]) && resultado.getString("carta2").equals(cartasIniciales[0])) ){
                
                /*victorias = (100*tempVictoria)/apariciones;
                derrotas = (100*(tempDerrota+1))/apariciones;
                empates = (100*tempEmpate)/apariciones;*/
                victorias = resultado.getInt("victorias");
                derrotas = resultado.getInt("derrotas") + 1;
                empates = resultado.getInt("empates");
                
                insertar.setInt(1, apariciones);
                insertar.setFloat(2, victorias);
                insertar.setFloat(3, derrotas);
                insertar.setFloat(4, empates);
                insertar.setString(5, cartasIniciales[0]);
                insertar.setString(6, cartasIniciales[1]);
                insertar.setString(7, cartasIniciales[1]);
                insertar.setString(8, cartasIniciales[0]);
                insertar.executeUpdate();
                bandera = true;
                break;
            }            
            else if(j1.getResultado().equals("empate") && (resultado.getString("carta1").equals(cartasIniciales[0]) && resultado.getString("carta2").equals(cartasIniciales[1]) 
                    || resultado.getString("carta1").equals(cartasIniciales[1]) && resultado.getString("carta2").equals(cartasIniciales[0])) ){
                /*victorias = (100*tempVictoria)/apariciones;
                derrotas = (100*tempDerrota)/apariciones;
                empates = (100*(tempEmpate+1))/apariciones;*/
                victorias = resultado.getInt("victorias");
                derrotas = resultado.getInt("derrotas");
                empates = resultado.getInt("empates") + 1;
                
                insertar.setInt(1, apariciones);
                insertar.setFloat(2, victorias);
                insertar.setFloat(3, derrotas);
                insertar.setFloat(4, empates);
                insertar.setString(5, cartasIniciales[0]);
                insertar.setString(6, cartasIniciales[1]);
                insertar.setString(7, cartasIniciales[1]);
                insertar.setString(8, cartasIniciales[0]);
                insertar.executeUpdate();
                bandera = true;
                break;
            }
        }    
        insertar.close();
        if(!bandera){
            insertarCartas(j1);
        }
        insertar.close(); 
        con.close();
        estado.close();
    }
    
    public static void insertarCartas(jugador j1) throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        String[] cartas = j1.getCartasInicialesBD();
        
        insertar = con.prepareStatement("INSERT INTO Cartas (carta1,carta2,apariciones,victorias,derrotas,empates)" + "values(?,?,?,?,?,?)"); 
        insertar.setString(1, cartas[0]);
        insertar.setString(2, cartas[1]);
      
        
        switch (j1.getResultado()) {
                
            case "gano":
                insertar.setInt(3, 1);
                insertar.setInt(4, 1);
                insertar.setInt(5, 0);
                insertar.setInt(6, 0);
                break;
            case "perdio":
                insertar.setInt(3, 1);
                insertar.setInt(4, 0);
                insertar.setInt(5, 1);
                insertar.setInt(6, 0);
                break;
            case "empate":
                insertar.setInt(3, 1);
                insertar.setInt(4, 0);
                insertar.setInt(5, 0);
                insertar.setInt(6, 1);
                break;
            default:
                break;
        }
        insertar.executeUpdate();
        insertar.close();
        estado.close();
        con.close(); 
    }
    
    public static void mostrarTablaJugador() throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        
        ResultSet resultado = estado.executeQuery("select * from Juegos");
        
        System.out.println("Tabla de los Juegos");
        System.out.println("|ID|   |J1|   |J2|   |RES|");
        while(resultado.next()){
            System.out.println("|"+resultado.getInt("id") + "|---|" + resultado.getString("jugador1") + "|---|" + resultado.getString("jugador2") + "|---|" + resultado.getString("resultado")+"|");
        }
        estado.close();
        con.close();
       
    }
    
    public static void mostrarCartas() throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        ResultSet resultado = estado.executeQuery("select * from Cartas");
        
        System.out.println("Tabla de Cartas");
        System.out.println("ID\t\tC1\t\tC2\t\tCANTIDAD\tVICTORIAS\tDERROTAS\tEMPATES");
        while(resultado.next()){
            System.out.println(resultado.getInt("id") + "\t\t" + resultado.getString("carta1") + "\t\t" + resultado.getString("carta2") + "\t\t" + resultado.getInt("apariciones")
                    + "\t\t" + resultado.getInt("victorias") + "\t\t" + resultado.getInt("derrotas") + "\t\t" + resultado.getInt("empates"));
        }
        estado.close();
        con.close();
    }
    
    public static void mostrarTablaEstadicticas() throws SQLException{
        con =  DriverManager.getConnection("jdbc:mysql://localhost/Poker?useSSL=false","root","mau1995");
        estado = con.createStatement();
        ResultSet resultado = estado.executeQuery("select * from Estadisticas");
        
        System.out.println("Tabla de Estadisticas");
        System.out.println("ID\t\tCANTIDAD\tJUGADA\t\t\tVICTORIAS\t\tDERROTAS\tEMPATES");
        while(resultado.next()){
            System.out.println(resultado.getInt("id") + "\t\t" + resultado.getString("cantidad") + "\t\t" + resultado.getString("jugada") + "\t\t" + resultado.getInt("victorias") 
                    + "\t\t\t" + resultado.getInt("derrotas") + "\t\t" + resultado.getInt("empates"));
        }
        estado.close();
        con.close();
    }
}