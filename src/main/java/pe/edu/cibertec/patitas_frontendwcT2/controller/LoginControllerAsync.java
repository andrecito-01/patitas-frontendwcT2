package pe.edu.cibertec.patitas_frontendwcT2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontendwcT2.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontendwcT2.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontendwcT2.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontendwcT2.dto.LogoutResponseDTO;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;





    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO){






        if ( loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().length()==0 ||
                loginRequestDTO.numeroDocumento()==null || loginRequestDTO.numeroDocumento().trim().length()==0 ||
                loginRequestDTO.password()==null || loginRequestDTO.password().trim().length()==0)  {



            return Mono.just(new LoginResponseDTO("01","Error Debe completar sus credenciales de forma Correcta","",""));
        }


      try {




          return webClientAutenticacion.post().uri("/login")
                  .body(Mono.just(loginRequestDTO),LoginRequestDTO.class)
                  .retrieve()
                  .bodyToMono(LoginResponseDTO.class)
                  .flatMap(response -> {

                      if (response.codigo().equals("00")){
                          return Mono.just(new LoginResponseDTO("00","", response.nombreUsuario(), ""));

                      }else {
                          return Mono.just(new LoginResponseDTO("01","Error:Autenticacion Fallida", "", ""));


                      }

                  });





      }catch (Exception e){

          System.out.println(e.getMessage());

          return Mono.just(new LoginResponseDTO("99","Error:Autenticacion Fallida", "", ""));


      }






    }







    @PostMapping("/logout-async")
    public Mono<LogoutResponseDTO> cerarSesion(@RequestBody LogoutRequestDTO logoutRequestDTO) {

        if (logoutRequestDTO.tipoDocumento() == null || logoutRequestDTO.tipoDocumento().trim().isEmpty() ||
                logoutRequestDTO.numeroDocumento() == null || logoutRequestDTO.numeroDocumento().trim().isEmpty()) {


            return Mono.just(new LogoutResponseDTO("01", "Error:los datos de sesion son invalidos"));


        }


        try {
            return webClientAutenticacion.post().uri("/logout").body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                    .retrieve().bodyToMono(LogoutResponseDTO.class).flatMap(response -> {

                        if (response.codigo().equals("00")) {
                            return Mono.just(new LogoutResponseDTO("00", "Sesión cerrada correctamente"));
                        } else {
                            return Mono.just(new LogoutResponseDTO("01", "Error al momento del cierre de sesion"));
                        }

                    });
        } catch (Exception e) {

            System.out.println(e.getMessage());

            return Mono.just(new LogoutResponseDTO("99","Fallo el Server"));

        }


    }












}
