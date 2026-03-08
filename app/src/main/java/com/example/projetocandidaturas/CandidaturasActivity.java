package com.example.projetocandidaturas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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

        listViewCandidaturas = findViewById(R.id.listViewCandidaturas);

        listViewCandidaturas.setOnItemClickListener((parent, view, position, id) -> {

            Candidatura candidatura = (Candidatura) listViewCandidaturas.getItemAtPosition(position);

            Toast.makeText(getApplicationContext(), "Cargo \"" + candidatura.getNomeCargo() + "\" foi clicado.", Toast.LENGTH_LONG).show();
        });

        popularListaCandidaturas();


    }

    private void popularListaCandidaturas() {

        String[] cargosNomes    = getResources().getStringArray(R.array.nome_cargo);
        String[] empresas       = getResources().getStringArray(R.array.empresa);
        int[] indicacoes        = getResources().getIntArray(R.array.indicacao);
        int[] regimes           = getResources().getIntArray(R.array.regime);
        int[] faixasSalarios    = getResources().getIntArray(R.array.faixas_salarios);

        listaCandidaturas = new ArrayList<>();

        var enumRegimesValues = ERegime.values();

        for (int i=0; i<cargosNomes.length; i++) {
            listaCandidaturas.add(new Candidatura(
                    cargosNomes[i],
                    empresas[i],
                    indicacoes[i] == 1,
                    enumRegimesValues[regimes[i]],
                    faixasSalarios[i]));
        }

        candidaturaAdapter = new CandidaturaAdapter(this, listaCandidaturas);

        listViewCandidaturas.setAdapter(candidaturaAdapter);
    }
}