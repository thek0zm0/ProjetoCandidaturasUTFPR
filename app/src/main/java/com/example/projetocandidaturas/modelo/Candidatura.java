package com.example.projetocandidaturas.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Candidatura {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(index = true)
    @NonNull
    private String nomeCargo;
    private String empresa;
    private boolean indicacao;
    private ERegime regime;
    private Integer faixaSalarial;

    public Candidatura(String nomeCargo, String empresa, boolean indicacao, ERegime regime, Integer faixaSalarial) {
        this.nomeCargo     = nomeCargo;
        this.empresa       = empresa;
        this.indicacao     = indicacao;
        this.regime        = regime;
        this.faixaSalarial = faixaSalarial;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public boolean isIndicacao() {
        return indicacao;
    }

    public void setIndicacao(boolean indicacao) {
        this.indicacao = indicacao;
    }

    public ERegime getRegime() {
        return regime;
    }

    public void setRegime(ERegime regime) {
        this.regime = regime;
    }

    public Integer getFaixaSalarial() {
        return faixaSalarial;
    }

    public void setFaixaSalarial(Integer faixaSalarial) {
        this.faixaSalarial = faixaSalarial;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return nomeCargo  + '\n' +
                empresa   + '\n' +
                indicacao + '\n' +
                regime    + '\n' +
                faixaSalarial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Candidatura that = (Candidatura) o;
        return indicacao == that.indicacao && Objects.equals(nomeCargo, that.nomeCargo) && Objects.equals(empresa, that.empresa) && regime == that.regime && Objects.equals(faixaSalarial, that.faixaSalarial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeCargo, empresa, indicacao, regime, faixaSalarial);
    }
}
