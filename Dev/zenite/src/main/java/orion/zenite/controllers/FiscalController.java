package orion.zenite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import orion.zenite.dao.ContaDao;
import orion.zenite.dao.EnderecoDao;
import orion.zenite.models.*;
import orion.zenite.payload.ApiResponse;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/fiscal")
public class FiscalController {

    // Classes que realiza consulta no banco de dados
    @Autowired
    private EnderecoDao enderecoBD = new EnderecoDao();

    @Autowired
    private ContaDao contaBD = new ContaDao();

    @PostMapping("cadastro")
    public ResponseEntity<?> cadastro(ServletRequest req, @RequestBody Fiscal fiscal) {

        // Na requisição possui o email que foi decodificado do token
        HttpServletRequest request = (HttpServletRequest) req;

        String email = request.getAttribute("email").toString();
        boolean existe = contaBD.verificarSeExistePorEmail(email);

        if (existe) {
            Fiscal novoFiscal = fiscal;
            Conta novaConta = new Conta();
            novaConta.setSenha(novoFiscal.getSenha());
            novaConta.setEmail(novoFiscal.getEmail());
            novaConta.setNivel(novoFiscal.getNivel());

            // inserir conta
            boolean resultado = contaBD.inserir(novaConta);

            // inserir endereco

            // inserir dispositivo

            //  inserir funcionario

            if (resultado) {
                return new ResponseEntity<>(
                        new ApiResponse(true, "Fiscal cadastrado"),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Erro no cadastro do funcionario", novoFiscal),
                        HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Não autorizado, verifique sua credenciais/nível."),
                    HttpStatus.UNAUTHORIZED);
        }
    }

}
