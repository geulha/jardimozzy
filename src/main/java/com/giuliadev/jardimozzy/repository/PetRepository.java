package com.giuliadev.jardimozzy.repository;

import com.giuliadev.jardimozzy.model.Pet;
import com.giuliadev.jardimozzy.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByUsuario(Usuario usuario);

    List<Pet> findByUsuarioId(Long usuarioId);

    Pet findByIdAndUsuarioId(Long petId, Long usuarioId);
}