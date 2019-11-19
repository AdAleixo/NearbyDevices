package com.github.promoapp.dominio.anuncio;

import java.util.Date;
import java.util.Objects;

public class Anuncio {

    private Long id;
    private String nome;
    private String descricao;
    private Double preco = 0.0;
    private String url;
    private Date validade;

    public Anuncio() {
    }

    public Anuncio(Long id, String nome, String descricao, Double preco, String url, Date validade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.url = url;
        this.validade = validade;
    }

    public String getItemLista() {
        return "Nome: " + nome + "\nDescrição: " + descricao + "\nPreço: " + preco
                + "\n Link: " + url;
    }

    @Override
    public String toString() {
        return "Anuncio{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", url='" + url + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(id, anuncio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
