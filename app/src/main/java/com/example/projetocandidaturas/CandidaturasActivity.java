package com.example.projetocandidaturas;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CandidaturasActivity extends AppCompatActivity {

    private ListView listViewCandidaturas;
    private List<Candidatura> listaCandidaturas;
    private CandidaturaAdapter candidaturaAdapter;
    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas);
        setTitle(getString(R.string.controle_de_candidaturas));

        listViewCandidaturas = findViewById(R.id.listViewCandidaturas);

        /*
        listViewCandidaturas.setOnItemClickListener((parent, view, position, id) -> {

            Candidatura candidatura = (Candidatura) listViewCandidaturas.getItemAtPosition(position);

            Toast.makeText(getApplicationContext(), "Cargo \"" + candidatura.getNomeCargo() + "\" foi clicado.", Toast.LENGTH_LONG).show();
        });*/

        popularListaCandidaturas();

        registerForContextMenu(listViewCandidaturas);
    }

    private void popularListaCandidaturas() {

        listaCandidaturas = new ArrayList<>();

        candidaturaAdapter = new CandidaturaAdapter(this, listaCandidaturas);

        listViewCandidaturas.setAdapter(candidaturaAdapter);
    }

    public void abrirSobre() {

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

    public void abrirNovaCandidatura() {

        Intent intent = new Intent(this, CandidaturaActivity.class);

        intent.putExtra(CandidaturaActivity.KEY_MODO, CandidaturaActivity.MODO_NOVO);

        launcherNovaCandidatura.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.candidaturas_opcoes, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemAdicionar) {
            abrirNovaCandidatura();
            return true;
        } else if (idMenuItem == R.id.menuItemSobre){
            abrirSobre();
            return true;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getMenuInflater().inflate(R.menu.candidaturas_item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemEditar) {
            editarCandidatura(info.position);
        } else if (idMenuItem == R.id.menuItemExcluir) {
            excluirCandidatura(info.position);
            return true;
        }

        return true;
    }

    ActivityResultLauncher<Intent> launcherEditarCandidatura = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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

                    var candidatura = listaCandidaturas.get(posicaoSelecionada);

                    candidatura.setNomeCargo(nome);
                    candidatura.setEmpresa(empresa);
                    candidatura.setIndicacao(indicacao);
                    candidatura.setRegime(ERegime.valueOf(regime));
                    candidatura.setFaixaSalarial(faixa);

                    posicaoSelecionada = -1;

                    candidaturaAdapter.notifyDataSetChanged();
                }
            }
        }
    });

    private void editarCandidatura(int position) {

        posicaoSelecionada = position;

        var candidatura = listaCandidaturas.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, CandidaturaActivity.class);

        intentAbertura.putExtra(CandidaturaActivity.KEY_MODO, CandidaturaActivity.MODO_EDITAR);
        intentAbertura.putExtra(CandidaturaActivity.KEY_NOME, candidatura.getNomeCargo());
        intentAbertura.putExtra(CandidaturaActivity.KEY_EMPRESA, candidatura.getEmpresa());
        intentAbertura.putExtra(CandidaturaActivity.KEY_INDICACAO, candidatura.isIndicacao());
        intentAbertura.putExtra(CandidaturaActivity.KEY_REGIME, candidatura.getRegime().toString());
        intentAbertura.putExtra(CandidaturaActivity.KEY_FAIXA, candidatura.getFaixaSalarial());

        launcherEditarCandidatura.launch(intentAbertura);
    }

    private void excluirCandidatura(int position) {
        listaCandidaturas.remove(position);

        candidaturaAdapter.notifyDataSetChanged();
    }
}