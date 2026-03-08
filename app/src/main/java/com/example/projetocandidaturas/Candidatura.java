package com.example.projetocandidaturas;

public class Candidatura {

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

    @Override
    public String toString() {
        return nomeCargo  + '\n' +
                empresa   + '\n' +
                indicacao + '\n' +
                regime    + '\n' +
                faixaSalarial;
    }
}
