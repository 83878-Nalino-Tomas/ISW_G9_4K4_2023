package isw.grupo1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;

import isw.grupo1.R;
import isw.grupo1.controller.ControladorPedidoLoQueSea;
import isw.grupo1.model.Domicilio;
import isw.grupo1.model.PedidoLoQueSea;
import isw.grupo1.model.TarjetaCredito;

public class ResumenActivity extends AppCompatActivity {

    private ControladorPedidoLoQueSea controlador;
    private TextView tvCalleRetiro, tvNroCalleRetiro, tvReferenciaRetiro;
    private TextView tvCalleEnvio, tvNroCalleEnvio, tvPisoEnvio, tvDptpEnvio, tvReferenciaEnvio;
    private TextView tvPisoRetiro, tvDptoRetiro;
    private TextView tvDescripcionProducto, tvMontoTotal, tvMedioPago, tvMontoAbona;
    private TextView tvNroTarjeta, tvMomentoEnvio;
    private ImageView ivImagenProducto;
    private ConstraintLayout clPagoEfectivo, clPagoTarjeta;
    private PedidoLoQueSea pedido;
    private ActionBar actionBar;
    private Button btnConfirmar, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        Bundle bundle = getIntent().getExtras();
        controlador = (ControladorPedidoLoQueSea) bundle.getSerializable("controlador");
        pedido = controlador.getPedido();
        cargarVistas();
        ivImagenProducto.setImageURI(null);
        cargarDomicilioRetiro();
        cargarDomicilioEnvio();
        cargarDescripcionProducto();
        cargarPago();
        cargarMomentoEnvio();
        actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        cargarActionBar();
        btnConfirmar.setOnClickListener(view -> confirmarPedido());
    }

    private void confirmarPedido() {
        Intent confirmarIntent = new Intent(this, ConfirmacionActivity.class);
        startActivity(confirmarIntent);
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

    private void cargarPago() {

        tvMedioPago.setText(pedido.getMedioPago());
        tvMontoTotal.setText(String.valueOf(pedido.getTotal()));
        if(pedido.esPagoTarjeta()){
            clPagoTarjeta.setVisibility(View.VISIBLE);
            clPagoEfectivo.setVisibility(View.GONE);
            tvNroTarjeta.setText(pedido.getResumenNroTarjeta());



        }else{
            clPagoEfectivo.setVisibility(View.VISIBLE);
            clPagoTarjeta.setVisibility(View.GONE);
            tvMontoAbona.setText(String.valueOf(pedido.getMontoAbonar()));
        }

    }

    private void cargarDescripcionProducto() {
        tvDescripcionProducto.setText(pedido.getDescripcionPedido());
        if(pedido.getUriImagen() != null && !pedido.getUriImagen().isEmpty()){
            Uri uriImagen = Uri.parse(pedido.getUriImagen());
            ivImagenProducto.setImageURI(uriImagen);
        }

    }

    private void cargarDomicilioEnvio() {
        tvCalleEnvio.setText(pedido.getDomicilioEnvio().getCalle());
        tvNroCalleEnvio.setText(String.valueOf(pedido.getDomicilioEnvio().getNumero()));
        String referenciaEnvio = pedido.getDomicilioEnvio().getReferencia();
        tvReferenciaEnvio.setText(referenciaEnvio);
        String piso = pedido.getDomicilioEnvio().getPiso();
        tvPisoEnvio.setText((piso != null) ? String.valueOf(piso) : "");
        tvDptpEnvio.setText(pedido.getDomicilioEnvio().getDpto());


    }
    private void cargarMomentoEnvio(){
        String momentoEnvio = (pedido.esLoAntesPosible()) ? "Lo antes posible" :
                pedido.getMomentoEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
        tvMomentoEnvio.setText(momentoEnvio);
    }

    private void cargarDomicilioRetiro() {

        tvCalleRetiro.setText(pedido.getDomicilioRetiro().getCalle());
        tvNroCalleRetiro.setText(String.valueOf(pedido.getDomicilioRetiro().getNumero()));
        //tvReferenciaRetiro.setText("Hola Mundo!!");
        String referencia = pedido.getDomicilioRetiro().getReferencia();
        tvReferenciaRetiro.setText(referencia);
        String piso = pedido.getDomicilioRetiro().getPiso();
        tvPisoRetiro.setText((piso != null) ? String.valueOf(piso) : "");
        tvDptoRetiro.setText(pedido.getDomicilioRetiro().getDpto());
    }

    private void cargarVistas(){

        tvCalleRetiro = findViewById(R.id.tvCalleRetiroResumen);
        tvNroCalleRetiro = findViewById(R.id.tvNroCalleRetiroResumen);
        tvReferenciaRetiro = findViewById(R.id.tvReferenciaRetiroResumen);
        tvPisoRetiro = findViewById(R.id.tvPisoRetiroResumen);
        tvDptoRetiro = findViewById(R.id.tvDptoRetiroResumen);
        tvCalleEnvio = findViewById(R.id.tvCalleEnvioResumen);
        tvNroCalleEnvio = findViewById(R.id.tvNroCalleEnvioResumen);
        tvPisoEnvio = findViewById(R.id.tvPisoEnvioResumen);
        tvDptpEnvio = findViewById(R.id.tvDptoEnvioResumen);
        tvReferenciaEnvio = findViewById(R.id.tvReferenciaEnvio);
        tvDescripcionProducto = findViewById(R.id.tvDescripcionProductoResumen);
        tvMedioPago = findViewById(R.id.tvMedioPagoResumen);
        tvMontoTotal = findViewById(R.id.tvMontoResumen);
        tvMontoAbona = findViewById(R.id.tvMontoAbonaResumen);
        tvNroTarjeta = findViewById(R.id.tvNroTarjetaResumen);
        ivImagenProducto = findViewById(R.id.ivImagenPedidoResumen);
        clPagoEfectivo = findViewById(R.id.clPagoEfectivoResumen);
        clPagoTarjeta = findViewById(R.id.clPagoTarjetaResumen);
        tvMomentoEnvio = findViewById(R.id.tvMomentoEnvioResumen);
        btnConfirmar = findViewById(R.id.btnConfirmarPedido);
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