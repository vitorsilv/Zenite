package orion.zenite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orion.zenite.models.*;
import orion.zenite.payload.ApiResponse;
import orion.zenite.repository.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/*
 * Todas as rotas que começam com /api/alguma-coisa
 * estão protegidas pelo JWToken.
 * Todas as URI então recebem o token decodificado
 * como um atributo email da requisição
 *
 * a decodificação ocorre na classe /security/JwtFilter
 */
@RestController
@RequestMapping("/api/fiscal")
public class FiscalController {

    // Classes que realiza consulta no banco de dados
    @Autowired
    private EnderecoDao enderecoBD;

    @Autowired
    private ContaDao contaBD;

    @PostMapping("cadastro")
    public ResponseEntity<?> cadastro(ServletRequest req, @RequestBody Fiscal fiscal) {

        try {
            // Na requisição possui o email que foi decodificado do token
            HttpServletRequest request = (HttpServletRequest) req;

            String email = request.getAttribute("email").toString();
            Conta conta = contaBD.findByEmail(email);

            if (conta == null) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Não autorizado, verifique sua credenciais/nível."),
                        HttpStatus.UNAUTHORIZED);
            } else {
                // inserir conta
                contaBD.save(fiscal.getConta());
                fiscal.getConta().setIdConta(contaBD.lastId());

                // inserir endereco
                Endereco endereco = fiscal.getEndereco();
                enderecoBD.save(endereco);
                endereco.setId(enderecoBD.lastId());
                fiscal.setEndereco(endereco);

                //Dispositivo

                // Fiscal

                return new ResponseEntity<>(
                        new ApiResponse(true, "Fiscal cadastrado", fiscal),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Erro no cadastro do funcionario: " + e.getMessage()),
                    HttpStatus.OK);
        }

    }
}
