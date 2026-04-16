-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 16/04/2026 às 19:10
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `casa_nanquim`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `configuracoes`
--

CREATE TABLE `configuracoes` (
  `id` int(11) NOT NULL,
  `chave` varchar(50) NOT NULL,
  `valor` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `configuracoes`
--

INSERT INTO `configuracoes` (`id`, `chave`, `valor`) VALUES
(1, 'HORARIO', '09:00'),
(2, 'HORARIO', '10:00'),
(3, 'HORARIO', '11:00'),
(4, 'HORARIO', '12:00'),
(5, 'HORARIO', '13:00'),
(6, 'HORARIO', '14:00'),
(7, 'HORARIO', '15:00'),
(8, 'HORARIO', '16:00'),
(9, 'HORARIO', '17:00'),
(10, 'HORARIO', '18:00'),
(11, 'HORARIO', '19:00'),
(12, 'DIA_FECHADO', 'domingo');

-- --------------------------------------------------------

--
-- Estrutura para tabela `precos_espaco`
--

CREATE TABLE `precos_espaco` (
  `id` int(11) NOT NULL,
  `periodo` varchar(50) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `descricao` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `precos_espaco`
--

INSERT INTO `precos_espaco` (`id`, `periodo`, `valor`, `descricao`) VALUES
(1, '2_HORAS', 80.00, '2 horas'),
(2, 'MEIO_PERIODO', 150.00, '4 horas - Meio período'),
(3, 'DIA_INTEIRO', 250.00, '8 horas - Dia inteiro');

-- --------------------------------------------------------

--
-- Estrutura para tabela `solicitacoes`
--

CREATE TABLE `solicitacoes` (
  `id` int(11) NOT NULL,
  `tatuador_id` int(11) NOT NULL,
  `data_solicitacao` date NOT NULL,
  `horario_inicio` time NOT NULL,
  `horario_fim` time NOT NULL,
  `periodo_escolhido` varchar(50) DEFAULT NULL,
  `valor` decimal(10,2) NOT NULL,
  `status` varchar(20) DEFAULT 'PENDENTE',
  `observacao` text DEFAULT NULL,
  `criado_em` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `solicitacoes`
--

INSERT INTO `solicitacoes` (`id`, `tatuador_id`, `data_solicitacao`, `horario_inicio`, `horario_fim`, `periodo_escolhido`, `valor`, `status`, `observacao`, `criado_em`) VALUES
(1, 3, '2026-04-24', '09:00:00', '17:00:00', 'DIA_INTEIRO', 250.00, 'CONFIRMADO', '23\n2654584210320', '2026-04-16 14:17:13');

-- --------------------------------------------------------

--
-- Estrutura para tabela `tatuadores`
--

CREATE TABLE `tatuadores` (
  `id` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `senha` varchar(100) NOT NULL,
  `telefone` varchar(20) NOT NULL,
  `instagram` varchar(100) DEFAULT NULL,
  `especialidade` varchar(100) DEFAULT NULL,
  `contador_solicitacoes` int(11) DEFAULT 0,
  `data_cadastro` timestamp NOT NULL DEFAULT current_timestamp(),
  `ultimo_login` timestamp NULL DEFAULT NULL,
  `ativo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `tatuadores`
--

INSERT INTO `tatuadores` (`id`, `nome`, `email`, `senha`, `telefone`, `instagram`, `especialidade`, `contador_solicitacoes`, `data_cadastro`, `ultimo_login`, `ativo`) VALUES
(1, 'Marcos', 'marcos.rochaigsm@gmail.com', '0610', '11940028922', '@marcosk8_', 'Comer sushi', 0, '2026-04-16 13:42:57', NULL, 1),
(2, 'Tatuador Teste', 'tatuador@teste.com', '123456', '(11) 99999-8888', '@tatuadorteste', 'Realismo, Aquarela', 0, '2026-04-16 13:42:59', '2026-04-16 16:51:11', 1),
(3, 'The Paul', 'thepaul@kid.com', '1234', '11940028922', '@kidbengala_', 'Sexo', 1, '2026-04-16 13:43:55', '2026-04-16 14:16:51', 1);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `configuracoes`
--
ALTER TABLE `configuracoes`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `precos_espaco`
--
ALTER TABLE `precos_espaco`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `solicitacoes`
--
ALTER TABLE `solicitacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `tatuador_id` (`tatuador_id`);

--
-- Índices de tabela `tatuadores`
--
ALTER TABLE `tatuadores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `configuracoes`
--
ALTER TABLE `configuracoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de tabela `precos_espaco`
--
ALTER TABLE `precos_espaco`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de tabela `solicitacoes`
--
ALTER TABLE `solicitacoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `tatuadores`
--
ALTER TABLE `tatuadores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `solicitacoes`
--
ALTER TABLE `solicitacoes`
  ADD CONSTRAINT `solicitacoes_ibfk_1` FOREIGN KEY (`tatuador_id`) REFERENCES `tatuadores` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
