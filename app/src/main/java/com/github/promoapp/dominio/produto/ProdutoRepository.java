package com.github.promoapp.dominio.produto;

import java.util.List;

public interface ProdutoRepository {

    Produto recuperarPorId(Long id);

    List<Produto> pesquisar(String termoPesquisa);

    void salvar(Produto produto);

    void excluir(Long id);
}
