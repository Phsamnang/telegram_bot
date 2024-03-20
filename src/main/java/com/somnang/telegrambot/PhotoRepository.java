package com.somnang.telegrambot;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PhotoRepository extends JpaRepository<PhotoAndCmd,Integer> {

    @Query("select p from photo p where p.cmd = ?1")
    PhotoAndCmd findByCmd(String cmd);
}
