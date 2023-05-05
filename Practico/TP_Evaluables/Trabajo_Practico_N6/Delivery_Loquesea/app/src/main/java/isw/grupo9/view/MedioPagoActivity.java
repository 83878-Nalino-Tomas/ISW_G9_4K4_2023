package isw.grupo9.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import isw.grupo9.R;
import isw.grupo9.controller.ControladorPedidoLoQueSea;

public class MedioPagoActivity extends AppCompatActivity {

    private Spinner spnMedioPago;
    private TextView tvMontoTotal;
    private EditText etMontoAbona, etTitular, etNroTarjeta, etCvc, etFechaV;
    private TextInputLayout tilMontoAbona, tilTitular, tilNroTarjeta, tilCvc, tilFechaV;
    private Button btnSiguiente;
    private LinearLayout llPagoEfectivo, llPagoTarjeta;
    private ControladorPedidoLoQueSea controlador;
    private ActionBar actionBar;
    private Button btnAtras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medio_pago);
        Bundle bundle = getIntent().getExtras();
        controlador = (ControladorPedidoLoQueSea) bundle.getSerializable("controlador");
        cargarVistas();
        agregarFiltros();
        cargarMedioPago();
        setListeners();
        tvMontoTotal.setText(String.valueOf(controlador.getMontoTotalPedido()));
        llPagoTarjeta.setVisibility(View.GONE);
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        cargarActionBar();
    }

    private void setListeners(){
        etMontoAbona.setOnFocusChangeListener((view, tieneFoco)-> cambioFocoMontoAbona(tieneFoco));
        etTitular.setOnFocusChangeListener((view, tieneFoco)-> cambioFocoTitular(tieneFoco));
        etNroTarjeta.setOnFocusChangeListener((view, tieneFoco)-> cambioFocoNroTarjeta(tieneFoco));
        etCvc.setOnFocusChangeListener((view, tieneFoco)-> cambioFocoCVC(tieneFoco));
        etFechaV.setOnFocusChangeListener((view, tieneFoco)-> cambioFocoFechaVencimiento(tieneFoco));
        spnMedioPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cambioSeleccionMedioPago(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btnSiguiente.setOnClickListener(view -> mostrarResumenPedido());
        etFechaV.addTextChangedListener(new ControladorPedidoLoQueSea.FechaVencimientoWatcher());
        etNroTarjeta.addTextChangedListener(new ControladorPedidoLoQueSea.NumeroTarjetaWatcher());
    }
    private void cargarActionBar(){
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view =actionBar.getCustomView();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(bView -> botonAtras());
    }
    private void botonAtras(){
        this.finish();
    }

    private void mostrarResumenPedido() {

        String medioPago = spnMedioPago.getSelectedItem().toString();
        String montoAbonar = etMontoAbona.getText().toString();
        String titular = etTitular.getText().toString();
        String nroTarjeta = etNroTarjeta.getText().toString();
        String cvc = etCvc.getText().toString();
        String fechaV = etFechaV.getText().toString();

        Map<Integer, String> errores = controlador.registrarMedioPago(medioPago, montoAbonar, titular,
                                                    nroTarjeta, cvc, fechaV, tilMontoAbona.getId(),
                                                    tilTitular.getId(), tilNroTarjeta.getId(),
                                                    tilCvc.getId(), tilFechaV.getId());
        if(errores != null){
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
            for(Map.Entry<Integer, String> entry : errores.entrySet()){
                try{
                    TextInputLayout til = findViewById(entry.getKey());
                    til.setError(entry.getValue());
                    til.setErrorEnabled(true);
                    til.startAnimation(animation);
                }catch(Exception e){
                    continue;

                }
            }
            return;
        }
        try{
            Intent intent = new Intent(this, ResumenActivity.class);
            intent.putExtra("controlador", controlador);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void cambioSeleccionMedioPago(int indiceItem) {
        String medioSelect = spnMedioPago.getItemAtPosition(indiceItem).toString();
        if(controlador.esEfectivo(medioSelect)){
            llPagoEfectivo.setVisibility(View.VISIBLE);
            llPagoTarjeta.setVisibility(View.GONE);
            etTitular.setText("");
            etNroTarjeta.setText("");
            etCvc.setText("");
            etFechaV.setText("");
        }else{
            llPagoEfectivo.setVisibility(View.GONE);
            llPagoTarjeta.setVisibility(View.VISIBLE);
            etMontoAbona.setText("");
        }
    }



    private void cargarVistas(){
        spnMedioPago = findViewById(R.id.spnMedioPago);
        tvMontoTotal = findViewById(R.id.tvMontoTotal);
        etMontoAbona = findViewById(R.id.etMontoAbona);
        etTitular = findViewById(R.id.etTitular);
        etNroTarjeta = findViewById(R.id.etNroTarjeta);
        etCvc = findViewById(R.id.etCVC);
        etFechaV = findViewById(R.id.etFechaVenc);
        tilMontoAbona = findViewById(R.id.tilMontoAbona);
        tilTitular = findViewById(R.id.tilTitular);
        tilNroTarjeta = findViewById(R.id.tilNroTarjeta);
        tilCvc = findViewById(R.id.tilCVC);
        tilFechaV = findViewById(R.id.tilFechaVenc);
        btnSiguiente = findViewById(R.id.btnPagar);
        llPagoEfectivo = findViewById(R.id.llPagoEfectivo);
        llPagoTarjeta = findViewById(R.id.llPagoTarjeta);
    }

    private void cargarMedioPago(){
        List<String> mediosPago = new ArrayList<>();
        mediosPago.add(ControladorPedidoLoQueSea.PAGO_EFECTIVO);
        mediosPago.add(ControladorPedidoLoQueSea.PAGO_TARJETA);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, mediosPago);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMedioPago.setAdapter(adapter);
    }

    private void agregarFiltros(){
        ArrayList<InputFilter> curInputFilters = new ArrayList<>(Arrays.asList(etTitular.getFilters()));
        curInputFilters.add(0, new ControladorPedidoLoQueSea.AlphabeticInputFilter());
        curInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newInputFilters = curInputFilters.toArray(new InputFilter[curInputFilters.size()]);
        etTitular.setFilters(newInputFilters);

        ArrayList<InputFilter> nroTarjInputFilters = new ArrayList<>(Arrays.asList(etNroTarjeta.getFilters()));
        nroTarjInputFilters.add(0, new ControladorPedidoLoQueSea.AlphaNumericInputFilter());
        nroTarjInputFilters.add(1, new InputFilter.AllCaps());
        InputFilter[] newRefInputFilters = nroTarjInputFilters.toArray(new InputFilter[nroTarjInputFilters.size()]);
        etNroTarjeta.setFilters(newRefInputFilters);

        //ArrayList<InputFilter> fechaVencFilters = new ArrayList<>(Arrays.asList(etFechaV.getFilters()));
        //fechaVencFilters.add(0, new ControladorPedidoLoQueSea.FechaVencimientoFilter());
        //InputFilter[] newFechaVencFilters = fechaVencFilters.toArray(new InputFilter[fechaVencFilters.size()]);
        //etFechaV.setFilters(newFechaVencFilters);
    }


    private void cambioFocoMontoAbona(boolean tieneFoco){
        if(!tieneFoco){
            String medioPagoSelec = spnMedioPago.getSelectedItem().toString();
            if(!controlador.validarMontoAbona(etMontoAbona.getText().toString().trim(),
                                               medioPagoSelec, tilMontoAbona.getId())){
                tilMontoAbona.setError(controlador.getError(tilMontoAbona.getId()));
                tilMontoAbona.setErrorEnabled(true);
            }else{
                tilMontoAbona.setError(null);
                tilMontoAbona.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoTitular(boolean tieneFoco){
        if(!tieneFoco){
            String medioPagoSelec = spnMedioPago.getSelectedItem().toString();
            if(!controlador.validarTitular(etTitular.getText().toString().trim(),
                                            medioPagoSelec, tilTitular.getId())){
                tilTitular.setError(controlador.getError(tilTitular.getId()));
                tilTitular.setErrorEnabled(true);
            }else{
                tilTitular.setError(null);
                tilTitular.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoNroTarjeta(boolean tieneFoco){
        if(!tieneFoco){
            String medioPagoSelec = spnMedioPago.getSelectedItem().toString();
            if(!controlador.validarNroTarjeta(etNroTarjeta.getText().toString().trim(),
                                                medioPagoSelec, tilNroTarjeta.getId())){
                tilNroTarjeta.setError(controlador.getError(tilNroTarjeta.getId()));
                tilNroTarjeta.setErrorEnabled(true);
            }else{
                tilNroTarjeta.setError(null);
                tilNroTarjeta.setErrorEnabled(false);
            }
        }
    }

    private void cambioFocoCVC(boolean tieneFoco){

        if(!tieneFoco){
            String medioPagoSelec = spnMedioPago.getSelectedItem().toString();
            if(!controlador.validarCVC(etCvc.getText().toString().trim(),
                    medioPagoSelec, tilCvc.getId())){
                tilCvc.setError(controlador.getError(tilCvc.getId()));
                tilCvc.setErrorEnabled(true);
            }else{
                tilCvc.setError(null);
                tilCvc.setErrorEnabled(false);
            }
        }

    }
    private void cambioFocoFechaVencimiento(boolean tieneFoco){
        if(!tieneFoco){
            String medioPagoSelec = spnMedioPago.getSelectedItem().toString();
            if(!controlador.validarFechaVenc(etFechaV.getText().toString().trim(),
                    medioPagoSelec, tilFechaV.getId())){
                tilFechaV.setError(controlador.getError(tilFechaV.getId()));
                tilFechaV.setErrorEnabled(true);
            }else{
                tilFechaV.setError(null);
                tilFechaV.setErrorEnabled(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}