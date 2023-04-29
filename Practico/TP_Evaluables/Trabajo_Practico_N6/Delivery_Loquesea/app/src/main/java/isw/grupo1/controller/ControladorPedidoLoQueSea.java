package isw.grupo1.controller;

import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import isw.grupo1.model.Domicilio;
import isw.grupo1.model.PedidoLoQueSea;

public class ControladorPedidoLoQueSea implements Serializable {

    private static final int CALLE_MAX = 100;
    private final Map<Integer, String> errores;
    private PedidoLoQueSea pedidoLoQueSea;
    public static final String PAGO_EFECTIVO = "Efectivo";
    public static final String PAGO_TARJETA = "Tarjeta de Crédito";
    private static final String MASTERCARD_REGEX = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$";
    private static final String VISA_REGEX = "^4[0-9]{12}(?:[0-9]{3})?$";

    private final int[] COSTOS = {250, 300, 180, 200, 160, 320, 280, 350, 210, 150, 370, 310, 190};
    private final Domicilio[] DOMICILIOS_RANDOM;


    public ControladorPedidoLoQueSea(){
        errores = new Hashtable<>();
        pedidoLoQueSea = new PedidoLoQueSea();
        //Limpiar errores cuando se pasa a una nueva activity
        DOMICILIOS_RANDOM = new Domicilio[10];
        generarDomiciliosRandom();
    }

    private void generarDomiciliosRandom() {

        DOMICILIOS_RANDOM[0] = new Domicilio("Gral. Paz", 1456, "B", "3");
        DOMICILIOS_RANDOM[1] = new Domicilio("Velez Sarsfield", 3212);
        DOMICILIOS_RANDOM[2] = new Domicilio("San Martín", 25);
        DOMICILIOS_RANDOM[3] = new Domicilio("Uriburu", 567);
        DOMICILIOS_RANDOM[4] = new Domicilio("Cnel. Pringles", 895, "C", "2");
        DOMICILIOS_RANDOM[5] = new Domicilio("27 de Abril", 1623);
        DOMICILIOS_RANDOM[6] = new Domicilio("Hipolito Yrigoyen", 123, "F", "6");
        DOMICILIOS_RANDOM[7] = new Domicilio("Gral. Paz", 21);
        DOMICILIOS_RANDOM[8] = new Domicilio("San Martín", 432);
        DOMICILIOS_RANDOM[9] = new Domicilio("Gral. Paz", 1958);
    }

    public Domicilio obtenerDomicilioRandom(){
        Random random = new Random();
        return DOMICILIOS_RANDOM[random.nextInt(DOMICILIOS_RANDOM.length)];
    }

    public boolean validarNumeroCalle(String numero, int idVista){
        if(numero != null && numero.trim().length() > 0 && numero.trim().length() < CALLE_MAX) {
            try{
                Integer.parseInt(numero.trim());
                return true;
            }catch(Exception e){
                errores.put(idVista, "Campo Obligatorio. Numérico");
                return false;
            }
        } else{
            errores.put(idVista, "Campo Obligatorio");
            return false;
        }
    }

    public boolean validarReferenciaDomicilio(String referencia, int idVista){
        if(referencia == null || referencia.trim().isEmpty()) return true;
        if( referencia.trim().length() <= 400) {
            return true;
        } else{
            errores.put(idVista, "Máximo 400 caracteres");
            return false;
        }

    }

    public boolean validarCalle(String calle, int idVista){

        if(calle != null && calle.trim().length() > 0 && calle.trim().length() < CALLE_MAX) {
            return true;
        } else{
            errores.put(idVista, "Campo Obligatorio");
            return false;
        }
    }

    public boolean sinErrores(){
        return errores.isEmpty();
    }

    public Map<Integer, String> registrarDomicilioRetiro(String localidad, String calle,
                                                  String numero, String piso, String dpto, String referencia,
                                                  boolean loAntesPosible, LocalDateTime fechaHoraEnvio,
                                                  int idVistaLocalidad, int idVistaCalle,
                                                  int idVistaNumero, int idVistaReferencia,
                                                  int idVistaFechaEnvio){
        errores.clear();
        validarCalle(calle, idVistaCalle);
        validarNumeroCalle(numero, idVistaNumero);
        validarReferenciaDomicilio(referencia, idVistaReferencia);


        if(!loAntesPosible){
            validarFechaEnvio(fechaHoraEnvio, idVistaFechaEnvio);
        }
        if(!errores.isEmpty()){
            return errores;
        }
        //Integer iPiso = (piso != null && !piso.isEmpty()) ? Integer.parseInt(piso.trim()) : null;

        pedidoLoQueSea.setDomicilioRetiro(localidad, calle, Integer.parseInt(numero), referencia,
                piso, dpto);
        pedidoLoQueSea.setEntregaAntesPosible(loAntesPosible);
        pedidoLoQueSea.setMomentoEntrega(fechaHoraEnvio);
        return null;

    }

    public boolean validarFechaEnvio(LocalDateTime fechaEnvio, int idVistaFechaEnvio) {

        if(fechaEnvio == null){
            errores.put(idVistaFechaEnvio, "Debe seleccionar Una fecha");
            return false;
        }
        LocalDateTime fechaActual = LocalDateTime.now();
        if(fechaEnvio.isBefore(fechaActual)){
            errores.put(idVistaFechaEnvio, "Fecha anterior a la actual");
            return false;
        }
        LocalDateTime fechaMaxima = fechaActual.plusDays(7);
        if(fechaEnvio.isAfter(fechaMaxima)){
            errores.put(idVistaFechaEnvio, "No puede superar una semana");
            return false;
        }
        if(fechaEnvio.getHour() < 9 || (fechaEnvio.getHour() >= 23 && fechaEnvio.getMinute() > 0)){
            errores.put(idVistaFechaEnvio, "Los envíos se realizan de 9hs a 23hs");
            return false;
        }

        return true;

    }

    public String getError(int idVista){
        return errores.getOrDefault(idVista, null);
    }

    public boolean validarDescripcionPedido(String descripcion, int idvista){

        if(descripcion == null || descripcion.trim().isEmpty()){
            errores.put(idvista,"Campo Obligatorio");
            return false;
        }
        if(descripcion.trim().length() > 400){
            errores.put(idvista, "Máximo 400 caracteres");
            return false;
        }
        return true;

    }

    public Map<Integer, String> registrarDescripcionPedido(String descripcionPedido, Uri uriImagen, int idVista) {

        errores.clear();
        if (!validarDescripcionPedido(descripcionPedido, idVista)) {
            return errores;
        }
        pedidoLoQueSea.setDescripcionPedido(descripcionPedido, uriImagen);
        return null;
    }

    private int calcularCosto(){

        return 100 + (int)(Math.random()*500);
        //Random random = new Random();
        //return COSTOS[random.nextInt(COSTOS.length)];
    }


    public Map<Integer, String> registrarDomicilioEnvio(String localidad, String calle, String numero,
                                                        String referencia, String piso, String dpto,
                                                        int idVistaCalle, int idVistaNro, int idVistaRef) {
        errores.clear();
        if(localidad == null || localidad.trim().isEmpty()){
            errores.put(0,"Debe seleccionar la localidad de envío");
        }
        validarCalle(calle, idVistaCalle);
        validarNumeroCalle(numero, idVistaNro);
        validarReferenciaDomicilio(referencia, idVistaRef);

        if(!errores.isEmpty()){
            return errores;
        }
        //Integer iPiso = (piso != null && !piso.isEmpty()) ? Integer.parseInt(piso.trim()) : null;

        pedidoLoQueSea.setDomicilioEntrega(localidad, calle, Integer.parseInt(numero), referencia,
                piso, dpto);
        pedidoLoQueSea.setTotal(calcularCosto());
        return null;

    }

    public float getMontoTotalPedido() {
        return pedidoLoQueSea.getTotal();
    }

    public boolean validarMontoAbona(String montoAbona, String medioPagoSelec, int idVista) {

        if(medioPagoSelec == null) return false;
        if(medioPagoSelec.equals(PAGO_EFECTIVO)){
            if(montoAbona == null || montoAbona.trim().isEmpty()){
                errores.put(idVista, "Campo Obligatorio");
                return false;
            }
            try{
                if(Float.parseFloat(montoAbona.trim()) < pedidoLoQueSea.getTotal()){
                    errores.put(idVista, "El monto debe ser superior a $" + pedidoLoQueSea.getTotal());
                    return false;
                }
            }catch(Exception e){
                errores.put(idVista, "El monto debe ser numérico");
                return false;
            }
        }
        return true;

    }

    public boolean validarTitular(String titular, String medioPagoSelec, int id) {

        if(medioPagoSelec == null || medioPagoSelec.isEmpty()) return false;
        if(medioPagoSelec.equals(PAGO_TARJETA)){

            if(titular == null || titular.isEmpty()){
                errores.put(id, "Campo Obligatorio");
                return false;
            }
            for(int i = 0; i < titular.length(); i++){
                char c = titular.charAt(i);
                int ic = c;
                if(!Character.isLetterOrDigit(ic) && !Character.isSpaceChar(ic)){
                    errores.put(id, "No se permiten caracteres especiales");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarNroTarjeta(String nroTarjeta, String medioPagoSelec, int idVista){
        if(medioPagoSelec == null || medioPagoSelec.isEmpty()) return false;
        if(medioPagoSelec.equals(PAGO_TARJETA)){
            if(nroTarjeta == null || nroTarjeta.isEmpty()){
                errores.put(idVista, "Campo Obligatorio");
                return false;
            }
            nroTarjeta = nroTarjeta.replace(" ", "");
            if(!nroTarjeta.matches(MASTERCARD_REGEX) && !nroTarjeta.matches(VISA_REGEX)){
                errores.put(idVista, "Ingrese una tarjeta Visa o Mastercard");
                return false;
            }
        }
        return true;
    }

    public boolean validarCVC(String cvc, String medioPagoSelec, int idVista){

        if(medioPagoSelec == null || medioPagoSelec.isEmpty()) return false;
        if(medioPagoSelec.equals(PAGO_TARJETA)){
            if(cvc == null || cvc.isEmpty()){
                errores.put(idVista, "Campo Obligatorio");
                return false;
            }
            if(cvc.trim().length() != 3){
                errores.put(idVista, "Formato incorrecto");
                return false;
            }
            cvc = cvc.replace(" ", "");
            for(int i = 0; i < cvc.length(); i++){
                char c = cvc.charAt(i);
                if(!Character.isDigit(c)){
                    errores.put(idVista, "Ingrese solo números");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validarFechaVenc(String fechaVenc, String medioPagoSelec, int idVista){

        if(medioPagoSelec == null || medioPagoSelec.isEmpty()) return false;
        if(medioPagoSelec.equals(PAGO_TARJETA)){
            fechaVenc = fechaVenc.replace(" ", "");
            if(fechaVenc.length() != 5){
                errores.put(idVista, "Formato incorrecto (MM/AA)");
                return false;
            }
            String []partes = fechaVenc.split("/");
            try{
                int mes = Integer.parseInt(partes[0]);
                int anio = Integer.parseInt(partes[1]) + 2000;
                int dia = 1;
                LocalDate fechaVencDate = LocalDate.of(anio, mes, dia);
                LocalDate fechaActual = LocalDate.now();
                if(fechaVencDate.isBefore(fechaActual) || fechaVencDate.isEqual(fechaActual)){
                    errores.put(idVista, "Está Vencida");
                    return false;
                }
            }catch (Exception e){
                errores.put(idVista, "Formato incorrecto (MM/AA)");
                return false;
            }
        }
        return true;
    }

    public boolean esEfectivo(String medioSelect) {
        return medioSelect.equals(PAGO_EFECTIVO);
    }

    public Map<Integer, String> registrarMedioPago(String medioPago, String montoAbonar, String titular,
                                                   String nroTarjeta, String cvc, String fechaV,
                                                   int idVistaMonto, int idVistaTitular,
                                                   int idVistaNroTjta, int idVistaCvc, int idVistaFechaV){
        errores.clear();
        if(medioPago == null || medioPago.isEmpty()){
            errores.put(0, "Debe seleccionar un método de pago");
            return errores; //No debería darse nunca este caso
        }
        if(medioPago.equals(PAGO_EFECTIVO)){
            if(!validarMontoAbona(montoAbonar, medioPago, idVistaMonto)){
                return errores;
            }
            pedidoLoQueSea.setPagoEfectivo(medioPago, Float.parseFloat(montoAbonar));
            return null;
        }
        if(!validarTarjeta(medioPago, titular, nroTarjeta, cvc, fechaV, idVistaTitular, idVistaNroTjta,
                idVistaCvc, idVistaFechaV)){
            return errores;
        }
        pedidoLoQueSea.setPagoTarjeta(medioPago, titular, nroTarjeta, cvc, fechaV);
        return null;

    }

    private boolean validarTarjeta(String medioPago, String titular, String nroTarjeta, String cvc,
                                   String fechaV, int idVistaTitular, int idVistaNroTjta, int idVistaCvc,
                                   int idVistaFechaV) {
        return validarTitular(titular, medioPago, idVistaTitular)  &
                validarNroTarjeta(nroTarjeta, medioPago, idVistaNroTjta) &
                validarCVC(cvc, medioPago, idVistaCvc) &
                validarFechaVenc(fechaV, medioPago, idVistaFechaV);
    }


    public static class AlphaNumericInputFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            // Only keep characters that are alphanumeric
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c) || Character.valueOf(c).equals('\u00D1') || Character.valueOf(c).equals('\u00F1')) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        }
    }
    public static class AlphabeticInputFilter implements InputFilter {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            // Only keep characters that are alphanumeric
            StringBuilder builder = new StringBuilder();
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (Character.isLetter(c) || Character.isSpaceChar(c) || Character.valueOf(c).equals('\u00D1') || Character.valueOf(c).equals('\u00F1')) {
                    builder.append(c);
                }
            }

            // If all characters are valid, return null, otherwise only return the filtered characters
            boolean allCharactersValid = (builder.length() == end - start);
            return allCharactersValid ? null : builder.toString();
        }
    }
    public PedidoLoQueSea getPedido(){
        return pedidoLoQueSea;
    }



    public static class FechaVencimientoWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0 && (editable.length() % 3) == 0) {
                final char c = editable.charAt(editable.length() - 1);
                if ('/' == c) {
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
            if (editable.length() > 0 && (editable.length() % 3) == 0) {
                char c = editable.charAt(editable.length() - 1);
                if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf("/")).length <= 2) {
                    editable.insert(editable.length() - 1, String.valueOf("/"));
                }
            }

        }
    }

    public static class NumeroTarjetaWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0 && (editable.length() % 5) == 0) {
                final char c = editable.charAt(editable.length() - 1);
                if (' ' == c) {
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
            if (editable.length() > 0 && (editable.length() % 5) == 0) {
                char c = editable.charAt(editable.length() - 1);
                if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf(" ")).length <= 3) {
                    editable.insert(editable.length() - 1, String.valueOf(" "));
                }
            }

        }
    }

}
