package orion.zenite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import orion.zenite.dao.ContaDao;
import orion.zenite.dao.LinhaDao;
import orion.zenite.dao.MotoristaDao;
import orion.zenite.models.Motorista;

import java.util.List;

/*
 * Todas as rotas que começam com /api/alguma-coisa
 * estão protegidas pelo JWToken.
 * Todas as URI então recebem o token decodificado
 * como um atributo email da requisição
 *
 * a decodificação ocorre na classe /security/JwtFilter
 */
@RestController
@RequestMapping("/api/motorista")
public class MotoristaController {

    @Autowired
    private LinhaDao linhaBD;

    @Autowired
    private ContaDao contaBD;

    @Autowired
    private MotoristaDao motoristaBD;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Motorista> consulta() {

        List<Motorista> lista = motoristaBD.findAll();
        if(!lista.isEmpty()){
            return lista;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista vazia");

    }

}



