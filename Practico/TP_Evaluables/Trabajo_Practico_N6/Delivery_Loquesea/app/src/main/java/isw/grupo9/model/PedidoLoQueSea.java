package isw.grupo9.model;

import android.net.Uri;

import java.io.Serializable;
import java.time.LocalDateTime;

import isw.grupo9.controller.ControladorPedidoLoQueSea;

public class PedidoLoQueSea implements Serializable {

    private boolean entregaAntesPosible;
    private LocalDateTime momentoPedido;
    private LocalDateTime momentoEntrega; //Debería ser LocalDateTime, pero es para mostrar
    private Domicilio domicilioRetiro;
    private Domicilio domicilioEnvio;
    private String descripcionPedido;
    private String uriImagen;
    private float total;
    private float montoAbonar;
    private String medioPago;
    private TarjetaCredito tarjetaCredito;

    public void setDomicilioRetiro(String localidad, String calle, int numero, String referencia,
                                   String piso, String dpto) {
        domicilioRetiro = new Domicilio();
        domicilioRetiro.setCalle(calle);
        domicilioRetiro.setLocalidad(localidad);
        domicilioRetiro.setNumero(numero);
        domicilioRetiro.setReferencia(referencia);
        domicilioRetiro.setPiso(piso);
        domicilioRetiro.setDpto(dpto);
    }

    public String getMedioPago() {
        return medioPago;
    }

    public boolean isEntregaAntesPosible() {
        return entregaAntesPosible;
    }

    public LocalDateTime getMomentoPedido() {
        return momentoPedido;
    }
    public void setEntregaAntesPosible(boolean tipoEntrega){
        entregaAntesPosible = tipoEntrega;
    }

    public LocalDateTime getMomentoEntrega() {
        return momentoEntrega;
    }
    public void setMomentoEntrega(LocalDateTime momentoEntrega){
        this.momentoEntrega = momentoEntrega;
    }

    public Domicilio getDomicilioRetiro() {
        return domicilioRetiro;
    }

    public Domicilio getDomicilioEnvio() {
        return domicilioEnvio;
    }

    public String getDescripcionPedido() {
        return descripcionPedido;
    }

    public String getUriImagen() {
        return uriImagen;
    }

    public float getTotal() {
        return total;
    }

    public float getMontoAbonar() {
        return montoAbonar;
    }

    public TarjetaCredito getTarjetaCredito() {
        return tarjetaCredito;
    }

    public void setDescripcionPedido(String descripcionPedido, Uri uriImagen){
        this.descripcionPedido = descripcionPedido;
        if(uriImagen != null) {
            this.uriImagen = uriImagen.toString();
        }
    }

    public boolean esLoAntesPosible(){
        return entregaAntesPosible;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setDomicilioEntrega(String localidad, String calle, int numero,
                                    String referencia, String piso, String dpto) {
        domicilioEnvio = new Domicilio();
        domicilioEnvio.setLocalidad(localidad);
        domicilioEnvio.setCalle(calle);
        domicilioEnvio.setNumero(numero);
        domicilioEnvio.setReferencia(referencia);
        domicilioEnvio.setPiso(piso);
        domicilioEnvio.setDpto(dpto);

    }

    public void setPagoEfectivo(String medioPago, float montoAbonar){
        this.medioPago = medioPago;
        this.montoAbonar = montoAbonar;

    }
    public boolean esPagoTarjeta(){
        return medioPago.equals(ControladorPedidoLoQueSea.PAGO_TARJETA);
    }

    public void setPagoTarjeta(String medioPago, String titular, String nroTarjeta,
                               String cvc, String fechaV) {
        this.medioPago = medioPago;
        tarjetaCredito = new TarjetaCredito();
        tarjetaCredito.setTitular(titular);
        tarjetaCredito.setNroTarjeta(nroTarjeta);
        tarjetaCredito.setCvc(cvc);
        tarjetaCredito.setVencimiento(fechaV);
    }

    public String getResumenNroTarjeta(){
        if(tarjetaCredito == null) return null;
        return tarjetaCredito.getResumenNroTarjeta();
    }

}
