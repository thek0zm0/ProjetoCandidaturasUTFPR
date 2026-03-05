package com.example.projetocandidaturas;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CandidaturaActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmpresa;
    private CheckBox checkBoxIndicacao;
    private RadioGroup radioGroupReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatura);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmpresa = findViewById(R.id.editTextEmpresa);
        checkBoxIndicacao = findViewById(R.id.checkBoxIndicacao);
        radioGroupReg = findViewById(R.id.radioGroupRegime);
    }

    public void limparCampos(View view) {
        editTextNome.setText(null);
        editTextEmpresa.setText(null);
        checkBoxIndicacao.setChecked(false);
        radioGroupReg.clearCheck();

        editTextNome.requestFocus();

        Toast.makeText(this, R.string.as_entrada_foram_apagadas, Toast.LENGTH_LONG).show();
    }

    public void salvarValores(View view) {

        var nome = editTextNome.getText().toString();
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
        }

        Toast.makeText(this,
                getString(R.string.nome_valor) + nome + "\n"
                        + getString(R.string.empresa_valor) + empresa + "\n"
                        + getString(R.string.indicacao_valor) + isIndicacao + "\n"
                        + getString(R.string.regime_contratacao_valor) + regime, Toast.LENGTH_LONG).show();
    }
}