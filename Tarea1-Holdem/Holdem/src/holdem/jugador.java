package holdem;

public class jugador {
    private String jugada;
    private String resultado;
    private carta[] mano;
    private carta[] cartasIniciales;
    private String[] cartasInicialesBD;

    public jugador(String jugada, String resultado, carta[] cartas, carta[] cartasIniciales) {
        this.jugada = jugada;
        this.resultado = resultado;
        this.mano = cartas;
        this.cartasIniciales = cartasIniciales;
    }

    public jugador() {
    }

    public String getJugada() {
        return jugada;
    }

    public void setJugada(String jugada) {
        this.jugada = jugada;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public carta[] getMano() {
        return mano;
    }

    public void setMano(carta[] cartas) {
        this.mano = cartas;
    }  
    
    public carta[] getCartasIniciales() {
        return cartasIniciales;
    }

    public void setCartasIniciales(carta[] cartasIniciales) {
        this.cartasIniciales = cartasIniciales;
    }
    
    public String[] getCartasInicialesBD() {
        return cartasInicialesBD;
    }

    public void setCartasInicialesBD(String[] cartasInicialesBD) {
        this.cartasInicialesBD = cartasInicialesBD;
    }

}
