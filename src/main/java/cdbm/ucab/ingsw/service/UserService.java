package cdbm.ucab.ingsw.service;

import cdbm.ucab.ingsw.command.UserSignUpCommand;
import cdbm.ucab.ingsw.command.UserLoginCommand;
import cdbm.ucab.ingsw.command.UserChangingAttributesCommand;
import cdbm.ucab.ingsw.model.User;
import cdbm.ucab.ingsw.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import cdbm.ucab.ingsw.response.NotifyResponse;
import cdbm.ucab.ingsw.repository.UserRepository;
import cdbm.ucab.ingsw.response.UserProfileRelatedResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j

@Service("UserService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

 public ResponseEntity<Object> loginAuthenticator(UserLoginCommand command) {
        log.debug("About to process [{}]", command);
        User user = userRepository.findByEmail(command.getEmail());
        if(user == null){
            log.info("Cannot find user with email={}", command.getEmail());

            return  ResponseEntity.badRequest().body(buildNotifyResponse("Dirección de correo no válida."));
        }
        else{
            if(user.getPassword().equals(command.getPassword())) {
                log.info("Successful login for user={}", user.getId());

                UserResponse userResponse = new UserResponse();
                userResponse.setFirstName(user.getFirstName());
                userResponse.setLastName(user.getLastName());
                userResponse.setEmail(user.getEmail());
                userResponse.setId(user.getId());
                userResponse.setDateOfBirth(user.getDateOfBirth());
                return ResponseEntity.ok(userResponse);
            }
            else{
                log.info("{} is not valid password for user {}", command.getPassword(), user.getId());

                return  ResponseEntity.badRequest().body(buildNotifyResponse("Proceso no válido. "));
            }
        }

    }

    private User buildNewUser(UserSignUpCommand command) { //CONSTRUYE UN NUEVO USUARIO
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setEmail(command.getEmail());
        user.setPassword(command.getPassword());
        user.setDateOfBirth(command.getDateOfBirth());

        return user;
    }

    private User buildExistingUser(UserChangingAttributesCommand command, String id) { //CREA UN USUARIO PARA MODIFICAR
        User user = new User();                                                        //EL PERFIL
        user.setId(Long.parseLong(id));
        user.setFirstName(command.getFirstName());
        user.setLastName(command.getLastName());
        user.setEmail(command.getEmail());
        user.setPassword(command.getPassword());
        user.setDateOfBirth(command.getDateOfBirth());

        return user;
    }

    private NotifyResponse buildNotifyResponse(String message){ //MUESTRA UN MENSAJE DE NOTIFICACIÓN
        NotifyResponse response = new NotifyResponse();
        response.setMessage(message);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    private User searchUserById(String id){ //BUSCA UN USUARIO CON ID COMO DATO DE BÚSQUEDA
        if(userRepository.findById(Long.parseLong(id)).isPresent()){
            User user = userRepository.findById(Long.parseLong(id)).get();
            return user;
        }
        else
            return null;
    }

    public ResponseEntity<Object> registerUser(UserSignUpCommand command) { //SE ENCARGA DE REGISTRAR TODOS LOS USUARIOS
        log.debug("About to be processed [{}]", command);

        if(userRepository.existsByEmail(command.getEmail())){
            log.info("La dirección de correo {} ya se encuentra en la base de datos.", command.getEmail());

            return ResponseEntity.badRequest().body(buildNotifyResponse("El usuario ya se encuentra registrado en el sistema."));
        }
        else {
            if(!command.getPassword().equals(command.getConfirmationPassword())) {
                log.info("The passwords are not equal");
                return ResponseEntity.badRequest().body(buildNotifyResponse("Las contrasenas no coinciden"));
            }

            else {
                User user = buildNewUser(command);
                user = userRepository.save(user);

                log.info("Registered user with ID={}", user.getId());

                return ResponseEntity.ok().body(buildNotifyResponse("Usuario registrado."));
            }
        }
    }

    public ResponseEntity<Object> updateUser(UserChangingAttributesCommand command, String id) {
        log.debug("About to process [{}]", command);
        if (!userRepository.existsById(Long.parseLong(id))) {
            log.info("Cannot find user with ID={}", id);
            return ResponseEntity.badRequest().body(buildNotifyResponse("invalid_Id"));
        } else {
            User user = buildExistingUser(command, id);
            user = userRepository.save(user);

            log.info("Updated user with ID={}", user.getId());

            return ResponseEntity.ok().body(buildNotifyResponse("La operación ha sido exitosa."));
        }
    }



    public ResponseEntity<Object> getUserById(String id){ //BÚSQUEDA DE USUARIO CON ID COMO PARÁMETRO DE BÚSQUEDA
        log.debug("About to process [{}]", id);

        User user = searchUserById(id);
        if (user == null) {
            log.info("Cannot find user with ID={}", id);

            return ResponseEntity.badRequest().body(buildNotifyResponse("El ID del usuario no es válido."));
        }
        else {
            UserProfileRelatedResponse userProfileResponse = new UserProfileRelatedResponse();
            userProfileResponse.setId(user.getId());
            userProfileResponse.setFirstName(user.getFirstName());
            userProfileResponse.setLastName(user.getLastName());
            userProfileResponse.setEmail(user.getEmail());
            userProfileResponse.setPassword(user.getPassword());
            userProfileResponse.setDateOfBirth(user.getDateOfBirth());
            log.info("Returning info for user with ID={}", id);
            return ResponseEntity.ok(userProfileResponse);
        }

    }

    public ArrayList<UserResponse> searchUsersByName(String search){ //BÚSQUEDA DE USUARIO CON NOMBRE COMO PARÁMETRO
        log.debug("About to process search for name [{}]", search);
        ArrayList<UserResponse> response = new ArrayList<>();
        userRepository.findAll().forEach(it->{
            String name = it.getFirstName();

            String lastName = it.getLastName();                   //UNE EL "FIRSTNAME" Y "LASTNAME" PARA PODER HACER UNA
            String fullName = name.concat(lastName);              //BÚSQUEDA EXITOSA
            if(fullName.toLowerCase().contains(search.toLowerCase())) {
                UserResponse userResponse = new UserResponse();
                userResponse.setFirstName(it.getFirstName());
                userResponse.setLastName(it.getLastName());
                userResponse.setEmail(it.getEmail());
                userResponse.setId(it.getId());
                userResponse.setDateOfBirth(it.getDateOfBirth());

                response.add(userResponse);
            }
        });
        return response;
    }

    public ResponseEntity getUsersByName(String search){
        ArrayList<UserResponse> response = searchUsersByName(search);
        if(response.isEmpty()){
            log.info("Cannot find user with name={}", search);
            return ResponseEntity.badRequest().body(buildNotifyResponse("NO hubo coincidencia alguna."));
        }
        else {
            log.info("Returning info for user with name={}", search);
            return ResponseEntity.ok(response);
        }
    }
}

