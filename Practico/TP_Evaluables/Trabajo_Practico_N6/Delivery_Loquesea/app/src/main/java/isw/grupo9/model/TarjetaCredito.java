package isw.grupo9.model;

import java.io.Serializable;

public class TarjetaCredito implements Serializable {

    private String titular;
    private String nroTarjeta;
    private String cvc;
    private String vencimiento;

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getNroTarjeta() {
        return nroTarjeta;
    }

    public void setNroTarjeta(String nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getResumenNroTarjeta(){
        if(nroTarjeta == null) return null;
        return "**** " + getNroTarjeta().substring(nroTarjeta.length()-4);
    }

}
