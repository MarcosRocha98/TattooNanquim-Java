package com.casananquim.servlets;

import com.casananquim.dao.PrecoEspacoDAO;
import com.casananquim.dao.SolicitacaoDAO;
import com.casananquim.dao.TatuadorDAO;
import com.casananquim.models.PrecoEspaco;
import com.casananquim.models.Solicitacao;
import com.casananquim.models.Tatuador;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TatuadorDAO tatuadorDAO = new TatuadorDAO();
    private SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO();
    private PrecoEspacoDAO precoEspacoDAO = new PrecoEspacoDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String path = req.getPathInfo();
        HttpSession session = req.getSession(false);
        
        if (path == null || path.equals("/")) {
            resp.getWriter().write("{\"message\":\"API Casa Nanquim funcionando!\"}");
            return;
        }
        
        try {
            switch (path) {
                case "/precos-espaco":
                    List<PrecoEspaco> precos = precoEspacoDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(precos));
                    break;
                    
                case "/horarios-disponiveis-espaco":
                    String dataEspaco = req.getParameter("data");
                    if (dataEspaco != null) {
                        List<String> ocupados = solicitacaoDAO.getHorariosOcupados(dataEspaco);
                        resp.getWriter().write(gson.toJson(ocupados));
                    }
                    break;
                    
                case "/tatuador/solicitacoes":
                    if (session != null && session.getAttribute("tatuadorId") != null) {
                        int tatuadorId = (int) session.getAttribute("tatuadorId");
                        List<Solicitacao> solicitacoes = solicitacaoDAO.listarPorTatuador(tatuadorId);
                        resp.getWriter().write(gson.toJson(solicitacoes));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        resp.getWriter().write("{\"error\":\"Não autorizado\"}");
                    }
                    break;
                    
                case "/admin/tatuadores":
                    List<Tatuador> tatuadores = tatuadorDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(tatuadores));
                    break;
                    
                case "/admin/solicitacoes":
                    List<Solicitacao> todasSolicitacoes = solicitacaoDAO.listarTodas();
                    resp.getWriter().write(gson.toJson(todasSolicitacoes));
                    break;
                    
                case "/tatuador/perfil":
                    if (session != null && session.getAttribute("tatuadorId") != null) {
                        int tatuadorId = (int) session.getAttribute("tatuadorId");
                        Tatuador t = tatuadorDAO.buscarPorId(tatuadorId);
                        if (t != null) {
                            t.setSenha(null);
                            resp.getWriter().write(gson.toJson(t));
                        } else {
                            resp.getWriter().write("{\"error\":\"Tatuador não encontrado\"}");
                        }
                    } else {
                        resp.getWriter().write("{\"error\":\"Não autorizado\"}");
                    }
                    break;
                    
                default:
                    resp.getWriter().write("{\"error\":\"Endpoint nao encontrado: " + path + "\"}");
            }
        } catch (Exception e) {
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String path = req.getPathInfo();
        
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            
            @SuppressWarnings("unchecked")
            HashMap<String, Object> dados = gson.fromJson(json, HashMap.class);
            HashMap<String, Object> response = new HashMap<>();
            
            // ========== CADASTRO DE TATUADOR ==========
            if ("/cadastrar".equals(path)) {
                String nome = (String) dados.get("nome");
                String email = (String) dados.get("email");
                String senha = (String) dados.get("senha");
                String telefone = (String) dados.get("telefone");
                String instagram = (String) dados.get("instagram");
                String especialidade = (String) dados.get("especialidade");
                
                System.out.println("Cadastrando tatuador: " + nome + " - " + email);
                
                // Verificar se email já existe
                Tatuador existente = tatuadorDAO.buscarPorEmail(email);
                if (existente != null) {
                    response.put("success", false);
                    response.put("error", "Email já cadastrado!");
                } else {
                    Tatuador t = new Tatuador();
                    t.setNome(nome);
                    t.setEmail(email);
                    t.setSenha(senha);
                    t.setTelefone(telefone);
                    t.setInstagram(instagram);
                    t.setEspecialidade(especialidade);
                    
                    if (tatuadorDAO.cadastrar(t)) {
                        response.put("success", true);
                        response.put("message", "Cadastro realizado com sucesso!");
                    } else {
                        response.put("success", false);
                        response.put("error", "Erro ao cadastrar no banco");
                    }
                }
            } 
            // ========== LOGIN ==========
            else if ("/login".equals(path)) {
                String email = (String) dados.get("email");
                String senha = (String) dados.get("senha");
                
                System.out.println("Tentativa de login: " + email);
                
                Tatuador t = tatuadorDAO.buscarPorEmail(email);
                if (t != null && t.getSenha().equals(senha)) {
                    HttpSession session = req.getSession();
                    session.setAttribute("tatuadorId", t.getId());
                    session.setAttribute("tatuadorNome", t.getNome());
                    session.setAttribute("tatuadorEmail", t.getEmail());
                    
                    tatuadorDAO.atualizarUltimoLogin(t.getId());
                    
                    response.put("success", true);
                    response.put("message", "Login realizado com sucesso!");
                    response.put("tatuadorId", t.getId());
                    response.put("tatuadorNome", t.getNome());
                } else {
                    response.put("success", false);
                    response.put("error", "Email ou senha inválidos!");
                }
            }
            // ========== LOGOUT ==========
            else if ("/logout".equals(path)) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                response.put("success", true);
                response.put("message", "Logout realizado com sucesso!");
            }
            // ========== SOLICITAR RESERVA ==========
            else if ("/solicitar-reserva".equals(path)) {
                HttpSession session = req.getSession(false);
                if (session == null || session.getAttribute("tatuadorId") == null) {
                    response.put("success", false);
                    response.put("error", "Faça login para solicitar reserva");
                } else {
                    int tatuadorId = (int) session.getAttribute("tatuadorId");
                    String dataSolicitacao = (String) dados.get("dataSolicitacao");
                    String horarioInicio = (String) dados.get("horarioInicio");
                    String horarioFim = (String) dados.get("horarioFim");
                    String periodoEscolhido = (String) dados.get("periodoEscolhido");
                    double valor = ((Number) dados.get("valor")).doubleValue();
                    String observacao = (String) dados.get("observacao");
                    
                    // Verificar se horário já está ocupado
                    List<String> ocupados = solicitacaoDAO.getHorariosOcupados(dataSolicitacao);
                    if (ocupados.contains(horarioInicio)) {
                        response.put("success", false);
                        response.put("error", "Este horário já está ocupado!");
                    } else {
                        Solicitacao s = new Solicitacao();
                        s.setTatuadorId(tatuadorId);
                        s.setDataSolicitacao(dataSolicitacao);
                        s.setHorarioInicio(horarioInicio);
                        s.setHorarioFim(horarioFim);
                        s.setPeriodoEscolhido(periodoEscolhido);
                        s.setValor(valor);
                        s.setObservacao(observacao);
                        
                        if (solicitacaoDAO.salvar(s)) {
                            tatuadorDAO.incrementarContadorSolicitacoes(tatuadorId);
                            response.put("success", true);
                            response.put("message", "Solicitação enviada com sucesso!");
                        } else {
                            response.put("success", false);
                            response.put("error", "Erro ao salvar solicitação");
                        }
                    }
                }
            }
            else {
                response.put("success", false);
                response.put("error", "Endpoint nao encontrado: " + path);
            }
            
            resp.getWriter().write(gson.toJson(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(errorResponse));
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String path = req.getPathInfo();
        HashMap<String, Object> response = new HashMap<>();
        
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            
            @SuppressWarnings("unchecked")
            HashMap<String, String> dados = gson.fromJson(json, HashMap.class);
            
            if (path != null && path.contains("/admin/solicitacao/status/")) {
                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[partes.length - 1]);
                String status = dados.get("status");
                boolean atualizou = solicitacaoDAO.atualizarStatus(id, status);
                response.put("success", atualizou);
            }
            else {
                response.put("success", false);
                response.put("error", "Endpoint nao encontrado: " + path);
            }
            
            resp.getWriter().write(gson.toJson(response));
            
        } catch (NumberFormatException e) {
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "ID inválido");
            resp.getWriter().write(gson.toJson(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(errorResponse));
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String path = req.getPathInfo();
        HashMap<String, Object> response = new HashMap<>();
        
        try {
            String idStr = path.substring(1);
            
            if (idStr.startsWith("admin/solicitacao/")) {
                idStr = idStr.substring(19);
                int id = Integer.parseInt(idStr);
                boolean deletou = solicitacaoDAO.deletar(id);
                response.put("success", deletou);
            }
            else {
                response.put("success", false);
                response.put("error", "Endpoint nao encontrado: " + path);
            }
            
            resp.getWriter().write(gson.toJson(response));
            
        } catch (NumberFormatException e) {
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "ID inválido");
            resp.getWriter().write(gson.toJson(errorResponse));
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(errorResponse));
        }
    }
}