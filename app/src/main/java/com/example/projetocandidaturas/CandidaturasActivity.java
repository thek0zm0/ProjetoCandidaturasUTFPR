package com.example.projetocandidaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CandidaturasActivity extends AppCompatActivity {

    private ListView listViewCandidaturas;
    private List<Candidatura> listaCandidaturas;
    private CandidaturaAdapter candidaturaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas);
        setTitle(getString(R.string.controle_de_candidaturas));

        listViewCandidaturas = findViewById(R.id.listViewCandidaturas);

        listViewCandidaturas.setOnItemClickListener((parent, view, position, id) -> {

            Candidatura candidatura = (Candidatura) listViewCandidaturas.getItemAtPosition(position);

            Toast.makeText(getApplicationContext(), "Cargo \"" + candidatura.getNomeCargo() + "\" foi clicado.", Toast.LENGTH_LONG).show();
        });

        popularListaCandidaturas();


    }

    private void popularListaCandidaturas() {

        listaCandidaturas = new ArrayList<>();

        candidaturaAdapter = new CandidaturaAdapter(this, listaCandidaturas);

        listViewCandidaturas.setAdapter(candidaturaAdapter);
    }

    public void abrirSobre(View view) {

        Intent intentAbertura = new Intent(this, SobreActivity.class);

        startActivity(intentAbertura);
    }

    ActivityResultLauncher<Intent> launcherNovaCandidatura = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == CandidaturasActivity.RESULT_OK) {
                Intent intent = result.getData();

                Bundle bundle = intent.getExtras();

                if (bundle != null) {

                    var nome      = bundle.getString(CandidaturaActivity.KEY_NOME);
                    var empresa   = bundle.getString(CandidaturaActivity.KEY_EMPRESA);
                    var indicacao = bundle.getBoolean(CandidaturaActivity.KEY_INDICACAO);
                    var regime    = bundle.getString(CandidaturaActivity.KEY_REGIME);
                    var faixa     = bundle.getInt(CandidaturaActivity.KEY_FAIXA);

                    var candidatura = new Candidatura(nome, empresa, indicacao, ERegime.valueOf(regime), faixa);

                    listaCandidaturas.add(candidatura);

                    candidaturaAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    public void abrirNovaCandidatura(View view) {

        Intent intent = new Intent(this, CandidaturaActivity.class);

        launcherNovaCandidatura.launch(intent);
    }
}