package pe.edu.cibertec.patitas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.client.AutenticacionClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    @Autowired
    private AutenticacionClient autenticacionClient;

    @PostMapping("/autenticar-async")
    public LoginResponseDTO autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().isEmpty() ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().isEmpty() ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().isEmpty()) {
            return new LoginResponseDTO("01", "Error: Debe completar correctamente sus credenciales", "", "");
        }

        try {
            ResponseEntity<LoginResponseDTO> responseEntity = autenticacionClient.login(loginRequestDTO);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                LoginResponseDTO response = responseEntity.getBody();

                if (response != null && "00".equals(response.codigo())) {
                    return new LoginResponseDTO("00", "", response.nombreUsuario(), "");
                } else {
                    return new LoginResponseDTO("02", "Error: Autenticación fallida", "", "");
                }

            } else {
                return new LoginResponseDTO("99", "Error: Ocurrió un problema al invocar el servicio", "", "");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponseDTO("99", "Error: Ocurrió un problema en la autenticación", "", "");
        }
    }

    @PostMapping("/logout-async")
    public LogoutResponseDTO logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        if (logoutRequestDTO.tipoDocumento() == null || logoutRequestDTO.tipoDocumento().trim().isEmpty() ||
                logoutRequestDTO.numeroDocumento() == null || logoutRequestDTO.numeroDocumento().trim().isEmpty()) {
            return new LogoutResponseDTO("01", "Error: Debe completar correctamente los datos para cerrar sesión");
        }

        try {
            ResponseEntity<LogoutResponseDTO> responseEntity = autenticacionClient.logout(logoutRequestDTO);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                LogoutResponseDTO response = responseEntity.getBody();

                if (response != null && "00".equals(response.codigo())) {
                    return new LogoutResponseDTO("00", "Logout exitoso");
                } else {
                    return new LogoutResponseDTO("02", "Error: Logout fallido");
                }

            } else {
                return new LogoutResponseDTO("99", "Error: Ocurrió un problema al invocar el servicio");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LogoutResponseDTO("99", "Error: Ocurrió un problema en el logout");
        }
    }

}
