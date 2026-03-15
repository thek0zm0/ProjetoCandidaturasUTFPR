package com.example.projetocandidaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CandidaturaActivity extends AppCompatActivity {

    public static final String KEY_NOME = "KEY_NOME";
    public static final String KEY_EMPRESA = "KEY_EMPRESA";
    public static final String KEY_INDICACAO = "KEY_INDICACAO";
    public static final String KEY_REGIME = "KEY_REGIME";
    public static final String KEY_FAIXA = "KEY_FAIXA";
    private EditText editTextNome, editTextEmpresa;
    private CheckBox checkBoxIndicacao;
    private RadioGroup radioGroupReg;
    private Spinner spinner;

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
    }

    public void limparCampos(View view) {
        editTextNome.setText(null);
        editTextEmpresa.setText(null);
        checkBoxIndicacao.setChecked(false);
        radioGroupReg.clearCheck();
        spinner.setSelection(0);

        editTextNome.requestFocus();

        Toast.makeText(this, R.string.as_entrada_foram_apagadas, Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {

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

        Intent intentResposta = new Intent();
        intentResposta.putExtra(KEY_NOME, nome);
        intentResposta.putExtra(KEY_EMPRESA, empresa);
        intentResposta.putExtra(KEY_INDICACAO, isIndicacao);
        intentResposta.putExtra(KEY_REGIME, regime.toString());
        intentResposta.putExtra(KEY_FAIXA, faixa);

        setResult(CandidaturaActivity.RESULT_OK, intentResposta);

        finish();
    }
}