package com.example.projetocandidaturas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetocandidaturas.utils.UtilsAlert;

import java.util.Objects;

public class CandidaturaActivity extends AppCompatActivity {

    public static final String KEY_NOME         = "KEY_NOME";
    public static final String KEY_EMPRESA      = "KEY_EMPRESA";
    public static final String KEY_INDICACAO    = "KEY_INDICACAO";
    public static final String KEY_REGIME       = "KEY_REGIME";
    public static final String KEY_FAIXA        = "KEY_FAIXA";
    public static final String KEY_MODO         = "KEY_MODO";
    public static final String KEY_SUGERIR_TIPO = "SUGERIR_TIPO";
    public static final String KEY_ULTIMO_TIPO  = "ULTIMO_TIPO";
    public static final int    MODO_NOVO        = 0;
    public static final int    MODO_EDITAR      = 1;
    private EditText     editTextNome, editTextEmpresa;
    private CheckBox     checkBoxIndicacao;
    private RadioGroup   radioGroupReg;
    private Spinner      spinner;
    private RadioButton  radioButtonPJ, radioButtonCLT;
    private int          modo;
    private Candidatura  candidaturaOriginal;
    private boolean      sugerirTipo;
    private int          ultimoTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatura);
        setTitle(getString(R.string.nova_candidatura));

        editTextNome      = findViewById(R.id.editTextNome);
        editTextEmpresa   = findViewById(R.id.editTextEmpresa);
        checkBoxIndicacao = findViewById(R.id.checkBoxIndicacao);
        radioGroupReg     = findViewById(R.id.radioGroupRegime);
        spinner           = findViewById(R.id.spinnerFaixaSalarial);
        radioButtonPJ     = findViewById(R.id.radioButtonPj);
        radioButtonCLT    = findViewById(R.id.radioButtonClt);

        lerPreferencias();

        Intent intentAbertura = getIntent();

        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null) {

            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.nova_candidatura));

                if (sugerirTipo) {
                    spinner.setSelection(ultimoTipo);
                }
            } else {
                setTitle(getString(R.string.editar_candidatura));

                var nome      = bundle.getString(CandidaturaActivity.KEY_NOME);
                var empresa   = bundle.getString(CandidaturaActivity.KEY_EMPRESA);
                var indicacao = bundle.getBoolean(CandidaturaActivity.KEY_INDICACAO);
                var regime    = bundle.getString(CandidaturaActivity.KEY_REGIME);
                var faixa     = bundle.getInt(CandidaturaActivity.KEY_FAIXA);

                candidaturaOriginal = new Candidatura(nome, empresa, indicacao, ERegime.valueOf(regime), faixa);

                editTextNome.setText(nome);
                editTextEmpresa.setText(empresa);
                checkBoxIndicacao.setChecked(indicacao);
                spinner.setSelection(faixa);

                if (Objects.equals(regime, ERegime.CLT.toString())) {
                    radioButtonCLT.setChecked(true);
                } else if (Objects.equals(regime, ERegime.PJ.toString())){
                    radioButtonPJ.setChecked(true);
                }
            }
        }
    }

    public void limparCampos() {
        editTextNome.setText(null);
        editTextEmpresa.setText(null);
        checkBoxIndicacao.setChecked(false);
        radioGroupReg.clearCheck();
        spinner.setSelection(0);

        editTextNome.requestFocus();

        UtilsAlert.mostrarAviso(this, R.string.as_entrada_foram_apagadas);
    }

    public void salvarValores() {

        var nome    = editTextNome.getText().toString();
        var empresa = editTextEmpresa.getText().toString();

        if (nome == null || nome.isBlank()) {
            UtilsAlert.mostrarAviso(this, R.string.faltou_nome);

            editTextNome.requestFocus();
            return;
        }
        if (empresa == null || empresa.isBlank()) {
            UtilsAlert.mostrarAviso(this, R.string.faltou_empresa);

            editTextEmpresa.requestFocus();
            return;
        }

        var isIndicacao = checkBoxIndicacao.isChecked();

        var radioButtonId = radioGroupReg.getCheckedRadioButtonId();
        ERegime regime;

        if (R.id.radioButtonPj == radioButtonId) {
            regime = ERegime.PJ;
        } else if (R.id.radioButtonClt == radioButtonId) {
            regime = ERegime.CLT;
        } else {
            UtilsAlert.mostrarAviso(this, R.string.favor_selecionar_regime);

            return;
        }

        if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            UtilsAlert.mostrarAviso(this, R.string.spinner_sem_valores);

            return;
        }

        var faixa = spinner.getSelectedItemPosition();

        if (modo == MODO_EDITAR
            && nome.equals(candidaturaOriginal.getNomeCargo())
            && empresa.equals(candidaturaOriginal.getEmpresa())
            && isIndicacao == candidaturaOriginal.isIndicacao()
            && regime == candidaturaOriginal.getRegime()
            && faixa == candidaturaOriginal.getFaixaSalarial()) {

            setResult(CandidaturaActivity.RESULT_CANCELED);
            finish();
            return;
        }

        salvarUltimoTipo(faixa);

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_NOME, nome);
        intentResposta.putExtra(KEY_EMPRESA, empresa);
        intentResposta.putExtra(KEY_INDICACAO, isIndicacao);
        intentResposta.putExtra(KEY_REGIME, regime.toString());
        intentResposta.putExtra(KEY_FAIXA, faixa);

        setResult(CandidaturaActivity.RESULT_OK, intentResposta);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.candidatura_opcoes, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.menuItemSugerirTipo);

        item.setChecked(sugerirTipo);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {
            salvarValores();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar){
            limparCampos();
            return true;
        } else {
            if (idMenuItem == R.id.menuItemSugerirTipo) {
                boolean valor = !item.isChecked();

                salvarSugerirTipo(valor);
                item.setChecked(valor);

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void lerPreferencias() {
        SharedPreferences preferences = getSharedPreferences(CandidaturasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        sugerirTipo = preferences.getBoolean(KEY_SUGERIR_TIPO, sugerirTipo);
        ultimoTipo  = preferences.getInt(KEY_ULTIMO_TIPO, ultimoTipo);
    }

    private void salvarSugerirTipo(boolean novoValor) {
        SharedPreferences preferences = getSharedPreferences(CandidaturasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(KEY_SUGERIR_TIPO, novoValor);

        editor.apply();

        sugerirTipo = novoValor;
    }

    private void salvarUltimoTipo(int novoValor) {
        SharedPreferences preferences = getSharedPreferences(CandidaturasActivity.ARQUIVO_PREFERENCIAS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(KEY_ULTIMO_TIPO, novoValor);

        editor.apply();

        ultimoTipo = novoValor;
    }
}