package isw.grupo9.model;

import java.io.Serializable;

public class Domicilio implements Serializable {

    private String localidad;
    private String calle;
    private int numero;
    private String dpto;
    private String piso;
    private String referencia;

    public Domicilio(){

    }

    public Domicilio(String calle, int numero) {
        this.calle = calle;
        this.numero = numero;
    }

    public Domicilio(String calle, int numero, String dpto, String piso, String referencia) {
        this.calle = calle;
        this.numero = numero;
        this.dpto = dpto;
        this.piso = piso;
        this.referencia = referencia;
    }

    public Domicilio(String localidad, String calle, int numero, String dpto, String piso, String referencia) {
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.dpto = dpto;
        this.piso = piso;
        this.referencia = referencia;
    }

    public Domicilio(String calle, int numero, String dpto, String piso) {
        this.calle = calle;
        this.numero = numero;
        this.dpto = dpto;
        this.piso = piso;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
