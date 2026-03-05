package com.example.projetocandidaturas;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CandidaturaActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmpresa;
    private CheckBox checkBoxIndicacao;
    private RadioGroup radioGroupReg;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatura);

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

        var isIndicacao = checkBoxIndicacao.isActivated();

        var radioButtonId = radioGroupReg.getCheckedRadioButtonId();
        String regime = "";

        if (R.id.radioButtonPj == radioButtonId) {
            regime = getString(R.string.pessoaj);
        } else if (R.id.radioButtonClt == radioButtonId) {
            regime = getString(R.string.pessoaF);
        } else {
            Toast.makeText(this, R.string.favor_selecionar_regime, Toast.LENGTH_LONG).show();
            return;
        }

        if (spinner.getSelectedItem() == null) {
            Toast.makeText(this, "Spinner sem valores.", Toast.LENGTH_LONG).show();
            return;
        }

        var faixa = spinner.getSelectedItem().toString();

        Toast.makeText(this,
                getString(R.string.nome_valor) + nome + "\n"
                        + getString(R.string.empresa_valor) + empresa + "\n"
                        + getString(R.string.indicacao_valor) + isIndicacao + "\n"
                        + getString(R.string.regime_contratacao_valor) + regime + "\n"
                        + "Faixa Selecionada: " + faixa, Toast.LENGTH_LONG).show();
    }
}