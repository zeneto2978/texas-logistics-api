package com.jose.texaslogistics.driver;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    Page<Driver> findByStatus(DriverStatus status, Pageable pageable);
}

//conversa com o banco de dados.
//Ele é responsável por:
//
//✅ salvar
//✅ buscar
//✅ deletar
//✅ atualizar