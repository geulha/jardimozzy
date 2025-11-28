package com.giuliadev.jardimozzy.controller;

import com.giuliadev.jardimozzy.model.Usuario;
import com.giuliadev.jardimozzy.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // HOME
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    // A HISTÓRIA DO OZZY
    @GetMapping("/historia")
    public String historia() {
        return "historia";
    }

    // CADASTRO
    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam String confirmarSenha,
            @RequestParam String pergunta,
            @RequestParam String resposta,
            Model model) {

        if (!senha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não conferem!");
            return "cadastro";
        }

        if (usuarioRepository.existsByEmail(email)) {
            model.addAttribute("erro", "Este email já está cadastrado!");
            return "cadastro";
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerguntaSeguranca(pergunta);
        usuario.setResposta(passwordEncoder.encode(resposta));

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }

    // LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String logar(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            model.addAttribute("erro", "Email ou senha inválidos!");
            return "login";
        }

        session.setAttribute("usuarioLogado", usuario);
        return "redirect:/perfil";
    }

    // RECUPERAR SENHA - Página inicial
    @GetMapping("/senha")
    public String recuperarSenhaForm() {
        return "senha";
    }

    // RECUPERAR SENHA - Validação dos dados
    @PostMapping("/recuperar-senha")
    public String validarRecuperacao(
            @RequestParam String email,
            @RequestParam String pergunta,
            @RequestParam String resposta,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            model.addAttribute("erro", "Dados incorretos!");
            return "senha";
        }

        if (!usuario.getPerguntaSeguranca().equals(pergunta)) {
            model.addAttribute("erro", "Dados incorretos!");
            return "senha";
        }

        if (!passwordEncoder.matches(resposta, usuario.getResposta())) {
            model.addAttribute("erro", "Dados incorretos!");
            return "senha";
        }

        session.setAttribute("emailRecuperacao", email);
        return "redirect:/novasenha";
    }

    // NOVA SENHA - Página de redefinição
    @GetMapping("/novasenha")
    public String novaSenhaForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("emailRecuperacao");

        if (email == null) {
            return "redirect:/senha";
        }

        return "novasenha";
    }

    // REDEFINIR SENHA - Atualizar no banco
    @PostMapping("/redefinir-senha")
    public String redefinirSenha(
            @RequestParam String novaSenha,
            @RequestParam String confirmarSenha,
            HttpSession session,
            Model model) {

        String email = (String) session.getAttribute("emailRecuperacao");

        if (email == null) {
            return "redirect:/senha";
        }

        if (!novaSenha.equals(confirmarSenha)) {
            model.addAttribute("erro", "As senhas não conferem!");
            return "novasenha";
        }

        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return "redirect:/senha";
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        session.removeAttribute("emailRecuperacao");
        model.addAttribute("sucesso", "Senha redefinida com sucesso!");

        return "redirect:/login";
    }

    // PERFIL
    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        // Recarrega o usuário do banco para ter a lista atualizada de pets
        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        model.addAttribute("pets", usuario.getPets());
        return "perfil";
    }

    // EDITAR PERFIL - Carregar dados
    @GetMapping("/editarperfil")
    public String editarPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);
        return "editarperfil";
    }

    // ATUALIZAR PERFIL - Salvar alterações
    @PostMapping("/editarperfil")
    public String atualizarPerfil(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam(required = false) String senha,
            @RequestParam(required = false) String confirmarSenha,
            @RequestParam String pergunta,
            @RequestParam String resposta,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) return "redirect:/login";

        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuario == null) return "redirect:/login";

        Usuario usuarioComEmail = usuarioRepository.findByEmail(email);
        if (usuarioComEmail != null && !usuarioComEmail.getId().equals(usuario.getId())) {
            model.addAttribute("erro", "Este email já está cadastrado!");
            model.addAttribute("usuario", usuario);
            return "editarperfil";
        }

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setPerguntaSeguranca(pergunta);

        usuario.setResposta(passwordEncoder.encode(resposta));

        if (senha != null && !senha.trim().isEmpty()) {
            if (!senha.equals(confirmarSenha)) {
                model.addAttribute("erro", "As senhas não conferem!");
                model.addAttribute("usuario", usuario);
                return "editarperfil";
            }
            usuario.setSenha(passwordEncoder.encode(senha));
        }

        usuarioRepository.save(usuario);

        session.setAttribute("usuarioLogado", usuario);

        model.addAttribute("sucesso", "Perfil atualizado com sucesso!");
        return "redirect:/perfil";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }
}