package com.github.promoapp.dominio.anuncio;

import java.util.List;

public interface AnuncioRepository {

    List<Anuncio> recuperarTodos();

    void salvar(Anuncio anuncio);
}
