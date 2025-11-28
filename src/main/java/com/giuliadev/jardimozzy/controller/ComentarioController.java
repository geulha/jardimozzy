package com.giuliadev.jardimozzy.controller;

import com.giuliadev.jardimozzy.model.Comentario;
import com.giuliadev.jardimozzy.model.Pet;
import com.giuliadev.jardimozzy.model.Usuario;
import com.giuliadev.jardimozzy.repository.ComentarioRepository;
import com.giuliadev.jardimozzy.repository.PetRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PetRepository petRepository;

    // ADICIONAR COMENTÁRIO
    @PostMapping("/adicionar")
    public String adicionarComentario(
            @RequestParam Long petId,
            @RequestParam String conteudo,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Usuário não logado → volta para login
        if (usuario == null) {
            return "redirect:/login";
        }

        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return "redirect:/home";
        }

        if (conteudo == null || conteudo.trim().isEmpty()) {
            return "redirect:/pet/" + petId;
        }

        Comentario comentario = new Comentario(conteudo.trim(), usuario, pet);
        comentarioRepository.save(comentario);

        return "redirect:/pet/" + petId;
    }

    // DEIXAR UM CARINHO
    @PostMapping("/carinho")
    public String deixarCarinho(
            @RequestParam Long petId,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return "redirect:/home";
        }

        String mensagemCarinho = "❤️ CARINHO";

        Comentario carinho = new Comentario(mensagemCarinho, usuario, pet);
        comentarioRepository.save(carinho);

        return "redirect:/pet/" + petId;
    }
}