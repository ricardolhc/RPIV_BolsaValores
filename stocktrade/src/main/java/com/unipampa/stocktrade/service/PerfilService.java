package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.entity.usuario.Admin;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.model.repository.usuario.AdminRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;
import com.unipampa.stocktrade.model.repository.usuario.EmpresaRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class PerfilService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RegistroRepository registroRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public void deleteConta(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        if (!usuario.isSenhaCorreta(dados.senha())) {
            throw new RuntimeException("Senha incorreta");
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            if (!cliente.getAcoes().isEmpty()) {
                throw new RuntimeException("Não é possível deletar a conta pois existem ações vinculadas a ela");
            }
            clienteRepository.delete(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            if (!empresa.getAcoes().isEmpty()) {
                throw new RuntimeException("Não é possível deletar a conta pois existem ações vinculadas a ela");
            }
            empresaRepository.delete(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.delete(admin);
        }
        
        Registro registro = new Registro("O usuário " + usuario.getEmail() + " foi deletado com sucesso.");
        registroRepository.save(registro);
        
        session.invalidate();
    }

    public void trocarEmail(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
        

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        String emailAntigo = usuario.getEmail();

        if (dados.email() != null && !dados.email().isEmpty() && !dados.email().equals(usuario.getEmail())) {
            usuario.trocarEmail(dados.email());
        }

        if (dados.newEmail() != null && !dados.newEmail().isEmpty() && !dados.newEmail().equals(usuario.getEmail())) {
            usuario.trocarEmail(dados.newEmail());
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            clienteRepository.save(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            empresaRepository.save(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.save(admin);
        }

        Registro registro = new Registro("O email de usuário " + emailAntigo + " foi trocado para " + usuario.getEmail() + " com sucesso.");
        registroRepository.save(registro);
    }

    public void trocarSenha(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (dados.senha() != null && dados.newPassword() != null && !dados.senha().isEmpty()
                && !dados.newPassword().isEmpty()) {
            if (!usuario.isSenhaCorreta(dados.senha())) {
                throw new RuntimeException("Senha atual incorreta");
            }
            usuario.trocarSenha(dados.newPassword());
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            clienteRepository.save(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            empresaRepository.save(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.save(admin);
        }

        Registro registro = new Registro("A senha do usuário " + usuario.getEmail() + " foi trocada com sucesso.");
        registroRepository.save(registro);
    }

    
}