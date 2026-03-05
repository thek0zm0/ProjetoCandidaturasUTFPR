package com.example.projetocandidaturas;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CandidaturaActivity extends AppCompatActivity {

    private EditText editTextNome, editTextEmpresa;
    private CheckBox checkBoxIndicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatura);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmpresa = findViewById(R.id.editTextEmpresa);
        checkBoxIndicacao = findViewById(R.id.checkBoxIndicacao);
    }

    public void limparCampos(View view) {
        editTextNome.setText(null);
        editTextEmpresa.setText(null);
        checkBoxIndicacao.setChecked(false);

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

        Toast.makeText(this,
                getString(R.string.nome_valor) + nome + "\n"
                        + getString(R.string.empresa_valor) + empresa + "\n"
                        + getString(R.string.indicacao_valor) + isIndicacao, Toast.LENGTH_LONG).show();
    }
}