import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import { Container, MainMenu } from "./styles";

import BotaoMenu from "./BotaoMenu";
import BotaoExpande from "./BotaoExpande";

export default function MenuLateral(props) {
  const { pathname, state } = useLocation();
  const [nivel, setNivel] = useState(0);
  const gerenteConfig = [
    { texto: "Perfil", url: "/perfil" },
    { texto: "Alocar", url: "/alocacao" },
  ];

  const outrosConfig = [
    { texto: "Perfil", url: "/perfil" }
  ];

  let nivelStorage = localStorage.getItem("nivel");

  useEffect(() => {
    if (state) {
      setNivel(state.nivel);
    } else if (nivelStorage) {
      nivelStorage = Number(nivelStorage);
      setNivel(nivelStorage);
    } else if (pathname === "/login") {
      setNivel(0);
    }
  }, [pathname, nivelStorage]);

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("nivel");
    localStorage.removeItem("nome");

    window.location = "/login";
  }

  return (
    <>
      {nivel != 0 && (
        <Container>
          <MainMenu>
            <BotaoMenu
              descricao="Início"
              iconeNome="dashboard"
              alt="Logo do Software Zenite"
              url={"/"}
            />

            {nivel < 3 && (
              <BotaoMenu
                descricao="Fiscal"
                iconeNome="fiscal"
                url={"/fiscal"}
              />
            )}

            {nivel != 4 && (
              <BotaoExpande
                principal="Linha"
                btnEscondidos={[
                  {texto: "Linha", url: "/linha"},
                  {texto: "Parada", url: "/parada"}
                ]}
                iconeNome="linha"
              />
            )}

            {nivel != 4 && (
              <BotaoMenu
                descricao="Motorista"
                iconeNome="motorista"
                url={"/motorista"}
              />
            )}

            {nivel != 4 && (
              <BotaoMenu
                descricao="Ônibus"
                iconeNome="onibus"
                url={"/onibus"}
              />
            )}

            {nivel === 1 && (
              <BotaoMenu
                descricao="Admin"
                url={"/administrador"}
                iconeNome="admin"
              />
            )}

            {nivel != 1 && (
              <BotaoExpande
                principal="Configuração"
                btnEscondidos={nivel == 2 ? gerenteConfig : outrosConfig}
                iconeNome="config"
              />
            )}

            {nivel <= 2 && (
              <BotaoMenu
                descricao="Gerente"
                url={"/gerente"}
                iconeNome="gerente"
              />
            )}
          </MainMenu>

          <BotaoMenu descricao="Sair" iconeNome="logout" onclick={logout} />
        </Container>
      )}
    </>
  );
}
