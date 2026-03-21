package com.example.projetocandidaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class CandidaturaActivity extends AppCompatActivity {

    public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_EMPRESA = "KEY_EMPRESA";
    public static final String KEY_INDICACAO = "KEY_INDICACAO";
    public static final String KEY_REGIME = "KEY_REGIME";
    public static final String KEY_FAIXA = "KEY_FAIXA";
    public static final String KEY_MODO = "KEY_MODO";
    public static final int MODO_NOVO = 0;
    public static final int MODO_EDITAR = 1;
    private EditText editTextNome, editTextEmpresa;
    private CheckBox checkBoxIndicacao;
    private RadioGroup radioGroupReg;
    private Spinner spinner;
    private RadioButton radioButtonPJ, radioButtonCLT;
    private int modo;
    private Candidatura candidaturaOriginal;

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


        Intent intentAbertura = getIntent();

        Bundle bundle = intentAbertura.getExtras();

        if (bundle != null) {

            modo = bundle.getInt(KEY_MODO);

            if (modo == MODO_NOVO) {
                setTitle(getString(R.string.nova_candidatura));
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

        Toast.makeText(this, R.string.as_entrada_foram_apagadas, Toast.LENGTH_LONG).show();
    }

    public void salvarValores() {

        var nome    = editTextNome.getText().toString();
        var empresa = editTextEmpresa.getText().toString();

        if (nome == null || nome.isBlank()) {
            Toast.makeText(this, R.string.faltou_nome, Toast.LENGTH_LONG).show();

            editTextNome.requestFocus();
            return;
        }
        if (empresa == null || empresa.isBlank()) {
            Toast.makeText(this, R.string.faltou_empresa, Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, R.string.favor_selecionar_regime, Toast.LENGTH_LONG).show();
            return;
        }

        if (spinner.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, R.string.spinner_sem_valores, Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemSalvar) {
            salvarValores();
            return true;
        } else if (idMenuItem == R.id.menuItemLimpar){
            limparCampos();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}