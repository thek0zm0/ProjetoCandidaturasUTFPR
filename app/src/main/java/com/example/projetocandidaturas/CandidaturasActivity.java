package com.example.projetocandidaturas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class CandidaturasActivity extends AppCompatActivity {

    private ListView listViewCandidaturas;
    private List<Candidatura> listaCandidaturas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas);

        listViewCandidaturas = findViewById(R.id.listViewCandidaturas);

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
                    Boolean.parseBoolean(String.valueOf(indicacoes[i])),
                    enumRegimesValues[regimes[i]],
                    faixasSalarios[i]));
        }

        ArrayAdapter<Candidatura> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaCandidaturas);

        listViewCandidaturas.setAdapter(adapter);
    }
}