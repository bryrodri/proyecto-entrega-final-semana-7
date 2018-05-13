package cdbm.ucab.ingsw.response;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class UserProfileRelatedResponse {
    private Long id;
    private String firstName;
    private String lastName;     //OBJETO QUE SE ENVÍA COMO RESPUESTA DE LOS PROCESOS RELACIONADOS CON
    private String email;        //EL PERFIL DEL USUARIO
    private String password;
    private String dateOfBirth;
}
