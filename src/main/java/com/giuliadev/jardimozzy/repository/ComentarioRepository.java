package com.giuliadev.jardimozzy.repository;

import com.giuliadev.jardimozzy.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByPetIdOrderByDataComentarioDesc(Long petId);
}

