package com.jose.texaslogistics.driver;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}

//conversa com o banco de dados.
//Ele é responsável por:
//
//✅ salvar
//✅ buscar
//✅ deletar
//✅ atualizar