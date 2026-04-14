package com.casananquim.servlets;

import com.casananquim.dao.AgendamentoDAO;
import com.casananquim.dao.ArtistaDAO;
import com.casananquim.dao.PrecoEspacoDAO;
import com.casananquim.dao.ReservaEspacoDAO;
import com.casananquim.dao.TatuadorExternoDAO;
import com.casananquim.models.Artista;
import com.casananquim.models.Agendamento;
import com.casananquim.models.PrecoEspaco;
import com.casananquim.models.ReservaEspaco;
import com.casananquim.models.TatuadorExterno;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private ArtistaDAO artistaDAO = new ArtistaDAO();
    private PrecoEspacoDAO precoEspacoDAO = new PrecoEspacoDAO();
    private ReservaEspacoDAO reservaEspacoDAO = new ReservaEspacoDAO();
    private TatuadorExternoDAO tatuadorExternoDAO = new TatuadorExternoDAO();
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        String path = req.getPathInfo();
        
        if (path == null || path.equals("/")) {
            resp.getWriter().write("{\"message\":\"API Casa Nanquim funcionando!\"}");
            return;
        }
        
        try {
            switch (path) {
                case "/artistas":
                    List<Artista> artistas = artistaDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(artistas));
                    break;
                    
                case "/agendamentos":
                    List<Agendamento> agendamentos = agendamentoDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(agendamentos));
                    break;
                    
                case "/configuracoes":
                    HashMap<String, Object> configs = new HashMap<>();
                    configs.put("horarios", agendamentoDAO.getHorariosConfig());
                    configs.put("diasFechados", agendamentoDAO.getDiasFechados());
                    resp.getWriter().write(gson.toJson(configs));
                    break;
                    
                case "/horarios-disponiveis":
                    String data = req.getParameter("data");
                    String artistaId = req.getParameter("artista");
                    if (data != null && artistaId != null) {
                        List<String> ocupados = agendamentoDAO.getHorariosOcupados(data, Integer.parseInt(artistaId));
                        List<String> todosHorarios = agendamentoDAO.getHorariosConfig();
                        List<String> disponiveis = new ArrayList<>();
                        for (String horario : todosHorarios) {
                            if (!ocupados.contains(horario)) {
                                disponiveis.add(horario);
                            }
                        }
                        HashMap<String, Object> response = new HashMap<>();
                        response.put("horarios", disponiveis);
                        resp.getWriter().write(gson.toJson(response));
                    }
                    break;
                    
                case "/precos-espaco":
                    List<PrecoEspaco> precos = precoEspacoDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(precos));
                    break;
                    
                case "/reservas-espaco":
                    List<ReservaEspaco> reservas = reservaEspacoDAO.listarTodos();
                    resp.getWriter().write(gson.toJson(reservas));
                    break;
                    
                case "/horarios-disponiveis-espaco":
                    String dataEspaco = req.getParameter("data");
                    if (dataEspaco != null) {
                        List<String> ocupados = reservaEspacoDAO.getHorariosOcupados(dataEspaco);
                        resp.getWriter().write(gson.toJson(ocupados));
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
            Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            HashMap<String, Object> dados = gson.fromJson(reader, type);
            HashMap<String, Object> response = new HashMap<>();
            
            if ("/agendar".equals(path)) {
                Agendamento a = new Agendamento();
                a.setClienteNome((String) dados.get("nome"));
                a.setClienteWhatsapp((String) dados.get("whatsapp"));
                
                Object artistaIdObj = dados.get("artista_id");
                int artistaId = 0;
                if (artistaIdObj instanceof Double) {
                    artistaId = ((Double) artistaIdObj).intValue();
                } else if (artistaIdObj instanceof Integer) {
                    artistaId = (Integer) artistaIdObj;
                }
                a.setArtistaId(artistaId);
                
                a.setDataAgendamento((String) dados.get("data"));
                a.setHorario((String) dados.get("horario"));
                a.setDescricao((String) dados.get("descricao"));
                a.setLocalCorpo((String) dados.get("local"));
                
                if (agendamentoDAO.salvar(a)) {
                    response.put("success", true);
                    response.put("message", "Agendamento realizado com sucesso!");
                } else {
                    response.put("success", false);
                    response.put("error", "Erro ao salvar no banco");
                }
            } 
            else if ("/reservar-espaco".equals(path)) {
                String nome = (String) dados.get("nome");
                String whatsapp = (String) dados.get("whatsapp");
                String email = (String) dados.get("email");
                String instagram = (String) dados.get("instagram");
                String especialidade = (String) dados.get("especialidade");
                String dataReserva = (String) dados.get("dataReserva");
                String horarioInicio = (String) dados.get("horarioInicio");
                String horarioFim = (String) dados.get("horarioFim");
                double valor = ((Number) dados.get("valor")).doubleValue();
                String observacao = (String) dados.get("observacao");
                
                // Verificar se tatuador já existe
                TatuadorExterno existente = tatuadorExternoDAO.buscarPorWhatsapp(whatsapp);
                int tatuadorId;
                if (existente == null) {
                    TatuadorExterno novo = new TatuadorExterno();
                    novo.setNome(nome);
                    novo.setWhatsapp(whatsapp);
                    novo.setEmail(email);
                    novo.setInstagram(instagram);
                    novo.setEspecialidade(especialidade);
                    tatuadorExternoDAO.salvar(novo);
                    existente = tatuadorExternoDAO.buscarPorWhatsapp(whatsapp);
                }
                tatuadorId = existente.getId();
                
                ReservaEspaco reserva = new ReservaEspaco();
                reserva.setTatuadorId(tatuadorId);
                reserva.setTatuadorNome(nome);
                reserva.setDataReserva(dataReserva);
                reserva.setHorarioInicio(horarioInicio);
                reserva.setHorarioFim(horarioFim);
                reserva.setValor(valor);
                reserva.setObservacao(observacao);
                
                if (reservaEspacoDAO.salvar(reserva)) {
                    response.put("success", true);
                    response.put("message", "Reserva solicitada com sucesso!");
                } else {
                    response.put("success", false);
                    response.put("error", "Erro ao salvar reserva");
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
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> dados = gson.fromJson(reader, type);
            
            if (path != null && path.contains("/status/")) {
                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[partes.length - 1]);
                String status = dados.get("status");
                boolean atualizou = agendamentoDAO.atualizarStatus(id, status);
                response.put("success", atualizou);
            }
            else if (path != null && path.contains("/reserva-status/")) {
                String[] partes = path.split("/");
                int id = Integer.parseInt(partes[partes.length - 1]);
                String status = dados.get("status");
                boolean atualizou = reservaEspacoDAO.atualizarStatus(id, status);
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
            
            if (idStr.startsWith("agendamento/")) {
                idStr = idStr.substring(12);
                int id = Integer.parseInt(idStr);
                boolean deletou = agendamentoDAO.deletar(id);
                response.put("success", deletou);
            }
            else if (idStr.startsWith("reserva-espaco/")) {
                idStr = idStr.substring(15);
                int id = Integer.parseInt(idStr);
                boolean deletou = reservaEspacoDAO.deletar(id);
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