package com.example.projetocandidaturas;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.example.projetocandidaturas.modelo.Candidatura;
import com.example.projetocandidaturas.persistencia.CandidaturaDatabase;
import com.example.projetocandidaturas.utils.UtilsAlert;

import java.util.List;

public class CandidaturasActivity extends AppCompatActivity {

    private ListView listViewCandidaturas;
    private List<Candidatura> listaCandidaturas;
    private CandidaturaAdapter candidaturaAdapter;
    private int posicaoSelecionada = -1;
    private ActionMode actionMode;
    private View viewSelecionada;
    private Drawable background;
    public static final String ARQUIVO_PREFERENCIAS = "projetocandidaturas.PREFERENCIAS";

    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.candidaturas_item_selecionado, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int idMenuItem = item.getItemId();

            if (idMenuItem == R.id.menuItemEditar) {
                editarCandidatura();
            } else if (idMenuItem == R.id.menuItemExcluir) {
                excluirCandidatura();
                mode.finish();
                return true;
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            posicaoSelecionada = -1;

            if (viewSelecionada != null) {
                viewSelecionada.setBackground(background);
            }

            actionMode = null;
            viewSelecionada = null;
            background = null;

            listViewCandidaturas.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas);
        setTitle(getString(R.string.controle_de_candidaturas));

        listViewCandidaturas = findViewById(R.id.listViewCandidaturas);

        listViewCandidaturas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (actionMode != null) {
                    return false;
                }

                posicaoSelecionada = position;

                viewSelecionada = view;
                background = view.getBackground();

                view.setBackgroundColor(Color.LTGRAY);

                listViewCandidaturas.setEnabled(false);

                actionMode = startSupportActionMode(callback);

                return true;
            }});

        popularListaCandidaturas();

        registerForContextMenu(listViewCandidaturas);
    }

    private void popularListaCandidaturas() {

        CandidaturaDatabase database = CandidaturaDatabase.getInstance(this);

        listaCandidaturas = database.getCandidaturaDao().queryAllAscending();

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

                    long id = bundle.getLong("KEY_ID");

                    CandidaturaDatabase database = CandidaturaDatabase.getInstance(CandidaturasActivity.this);

                    Candidatura candidatura = database.getCandidaturaDao().queryForId(id);

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

        int idMenuItem = item.getItemId();

        if (idMenuItem == R.id.menuItemEditar) {
            editarCandidatura();
        } else if (idMenuItem == R.id.menuItemExcluir) {
            excluirCandidatura();
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

                    long id = bundle.getLong("KEY_ID");

                    CandidaturaDatabase database = CandidaturaDatabase.getInstance(CandidaturasActivity.this);

                    Candidatura candidatura = database.getCandidaturaDao().queryForId(id);

                    listaCandidaturas.set(posicaoSelecionada, candidatura);

                    posicaoSelecionada = -1;

                    candidaturaAdapter.notifyDataSetChanged();
                }
            }

            posicaoSelecionada = -1;

            if (actionMode != null) {
                actionMode.finish();
            }
        }
    });

    private void editarCandidatura() {

        var candidatura = listaCandidaturas.get(posicaoSelecionada);

        Intent intentAbertura = new Intent(this, CandidaturaActivity.class);

        intentAbertura.putExtra(CandidaturaActivity.KEY_MODO, CandidaturaActivity.MODO_EDITAR);
        intentAbertura.putExtra(CandidaturaActivity.KEY_ID, candidatura.getId());

        launcherEditarCandidatura.launch(intentAbertura);
    }

    private void excluirCandidatura() {

        final Candidatura candidatura = listaCandidaturas.get(posicaoSelecionada);

        final int posicaoParaRemover = posicaoSelecionada;

        if (posicaoParaRemover < 0 || posicaoParaRemover >= listaCandidaturas.size()) {
            return;
        }

        String mensagem = getString(R.string.deletar_confirmar);

        DialogInterface.OnClickListener listenerSim =  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CandidaturaDatabase database = CandidaturaDatabase.getInstance(CandidaturasActivity.this);

                int quantidadeDelete = database.getCandidaturaDao().delete(candidatura);

                if (quantidadeDelete != 1) {
                    UtilsAlert.mostrarAviso(CandidaturasActivity.this, getString(R.string.erro_ao_excluir), null);
                    return;
                }

                listaCandidaturas.remove(posicaoParaRemover);
                candidaturaAdapter.notifyDataSetChanged();
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        };

        UtilsAlert.confirmarAcao(this, mensagem, listenerSim, null);
    }
}