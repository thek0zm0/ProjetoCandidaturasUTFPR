package com.example.projetocandidaturas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CandidaturaAdapter extends BaseAdapter {

    private Context context;
    private List<Candidatura> listaCandidatura;
    private String[] faixas;

    private static class CandidaturaHolder {
        public TextView textViewValorNome;
        public TextView textViewValorEmpresa;
        public TextView textViewValorIndicacao;
        public TextView textViewValorRegime;
        public TextView textViewValorFaixa;
    }

    public CandidaturaAdapter(Context context, List<Candidatura> listaCandidatura) {
        this.context = context;
        this.listaCandidatura = listaCandidatura;
        faixas = context.getResources().getStringArray(R.array.faixaSalarios);
    }

    @Override
    public int getCount() {
        return listaCandidatura.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCandidatura.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CandidaturaHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.linha_lista_candidaturas, parent, false);

            holder = new CandidaturaHolder();

            holder.textViewValorNome      = convertView.findViewById(R.id.textViewValorNome);
            holder.textViewValorEmpresa   = convertView.findViewById(R.id.textViewValorEmpresa);
            holder.textViewValorIndicacao = convertView.findViewById(R.id.textViewValorIndicacao);
            holder.textViewValorRegime    = convertView.findViewById(R.id.textViewValorRegime);
            holder.textViewValorFaixa     = convertView.findViewById(R.id.textViewValorFaixa);

            convertView.setTag(holder);
        } else {
            holder = (CandidaturaHolder) convertView.getTag();
        }

        var candidatura = listaCandidatura.get(position);

        holder.textViewValorNome.setText(candidatura.getNomeCargo());
        holder.textViewValorEmpresa.setText(candidatura.getEmpresa());
        holder.textViewValorIndicacao.setText(candidatura.isIndicacao() ? context.getString(R.string.com_indicacao) : context.getString(R.string.sem_indicacao));
        holder.textViewValorFaixa.setText(faixas[candidatura.getFaixaSalarial()]);

        switch (candidatura.getRegime()) {
            case CLT:
                holder.textViewValorRegime.setText(R.string.pessoaF);
                break;
            case PJ:
                holder.textViewValorRegime.setText(R.string.pessoaJ);
                break;
        }

        return convertView;
    }
}
