package com.jose.texaslogistics.assignment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    boolean existsByDriverId(Long driverId);
}
