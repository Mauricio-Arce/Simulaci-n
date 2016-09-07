package holdem;

public class carta {
    private String palo;
    private Integer numero;

    public carta(String palo, Integer numero) {
        this.palo = palo;
        this.numero = numero;
    }

    public carta() {
    }
 
    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    
}
