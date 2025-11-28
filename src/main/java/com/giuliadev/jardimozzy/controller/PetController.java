package com.giuliadev.jardimozzy.controller;

import com.giuliadev.jardimozzy.model.Comentario;
import com.giuliadev.jardimozzy.model.Pet;
import com.giuliadev.jardimozzy.model.Usuario;
import com.giuliadev.jardimozzy.repository.ComentarioRepository;
import com.giuliadev.jardimozzy.repository.PetRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private static final String UPLOAD_DIR = "uploads/pets/";

    // ADICIONAR PET
    @GetMapping("/criarpet")
    public String criarPet(HttpSession session) {
        if (session.getAttribute("usuarioLogado") == null)
            return "redirect:/login";
        return "criarpet";
    }

    @PostMapping("/criarpet")
    public String cadastrarPet(
            @RequestParam String nome,
            @RequestParam String dataNascimento,
            @RequestParam String dataFalecimento,
            @RequestParam String sobre,
            @RequestParam(required = false) MultipartFile foto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        try {
            Pet pet = new Pet();
            pet.setNome(nome);
            pet.setDataNascimento(LocalDate.parse(dataNascimento));
            pet.setDataFalecimento(LocalDate.parse(dataFalecimento));
            pet.setSobre(sobre);
            pet.setUsuario(usuario);

            if (foto != null && !foto.isEmpty()) {
                pet.setFoto(salvarFoto(foto));
            }

            petRepository.save(pet);
            return "redirect:/perfil";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao cadastrar o pet.");
            return "criarpet";
        }
    }

    // EDITAR PET
    @GetMapping("/editarpet/{id}")
    public String editarPet(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        Pet pet = petRepository.findByIdAndUsuarioId(id, usuario.getId());
        if (pet == null) return "redirect:/perfil";

        model.addAttribute("pet", pet);
        return "editarpet";
    }

    @PostMapping("/editarpet/{id}")
    public String atualizarPet(
            @PathVariable Long id,
            @RequestParam String nome,
            @RequestParam String dataNascimento,
            @RequestParam String dataFalecimento,
            @RequestParam String sobre,
            @RequestParam(required = false) MultipartFile foto,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        Pet pet = petRepository.findByIdAndUsuarioId(id, usuario.getId());
        if (pet == null) return "redirect:/perfil";

        try {
            pet.setNome(nome);
            pet.setDataNascimento(LocalDate.parse(dataNascimento));
            pet.setDataFalecimento(LocalDate.parse(dataFalecimento));
            pet.setSobre(sobre);

            if (foto != null && !foto.isEmpty()) {
                pet.setFoto(salvarFoto(foto));
            }

            petRepository.save(pet);
            return "redirect:/perfil";

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar o pet.");
            return "editarpet";
        }
    }

    // EXCLUIR PET
    @GetMapping("/excluirpet/{id}")
    public String excluirPet(@PathVariable Long id, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Pet pet = petRepository.findByIdAndUsuarioId(id, usuario.getId());

        if (pet == null) {
            return "redirect:/perfil";
        }

        if (pet.getFoto() != null && !pet.getFoto().isEmpty()) {
            try {
                Path fotoPath = Paths.get(UPLOAD_DIR + pet.getFoto());
                Files.deleteIfExists(fotoPath);
            } catch (IOException e) {
                System.err.println("Erro ao excluir foto: " + e.getMessage());
            }
        }

        petRepository.delete(pet);

        return "redirect:/perfil";
    }

    // PERFIL DO PET
    @GetMapping("/pet/{id}")
    public String perfilPet(@PathVariable Long id, Model model, HttpSession session) {

        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) return "redirect:/memorial";

        List<Comentario> comentarios = comentarioRepository.findByPetIdOrderByDataComentarioDesc(id);

        model.addAttribute("pet", pet);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioLogado"));

        return "pet";
    }

    // MEMORIAL
    @GetMapping("/memorial")
    public String memorial(Model model) {
        List<Pet> pets = petRepository.findAll();
        model.addAttribute("pets", pets);
        return "memorial";
    }

    // SALVAR FOTO
    private String salvarFoto(MultipartFile foto) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String original = foto.getOriginalFilename();
        String ext = original.substring(original.lastIndexOf("."));
        String nome = UUID.randomUUID() + ext;

        Files.copy(foto.getInputStream(), uploadPath.resolve(nome));
        return nome;
    }
}