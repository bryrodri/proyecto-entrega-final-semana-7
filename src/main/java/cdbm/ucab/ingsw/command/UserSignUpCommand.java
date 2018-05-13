package cdbm.ucab.ingsw.command;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;

@ToString
@Data
public class UserSignUpCommand implements Serializable {

   //ÉSTOS SON LOS COMANDOS QUE RIGEN LAS VALIDACIONES A LA HORA DE REGISTRAR UN NUEVO USUARIO
    @NotNull(message = "Por favor, introduzca su nombre.")
    @NotEmpty(message = "Por favor, introduzca su nombre.")
    @Size(max = ValidationRules.FIRST_LAST_NAME_MAX_SIZE, message = "El nombre no puede contener más de 40 caracteres.")
    @Pattern(regexp = ValidationRules.FIRST_LAST_NAME_REGEX, message = "El nombre posee caracteres no válidos.")
    private String firstName;

    @NotNull(message = "Por favor, introduzca su apellido.")
    @NotEmpty(message = "Por favor, introduzca su apellido.")
    @Size(max = ValidationRules.FIRST_LAST_NAME_MAX_SIZE, message = "El apellido no puede contener más de 40 caracteres.")
    @Pattern(regexp = ValidationRules.FIRST_LAST_NAME_REGEX, message = "El apellido posee caracteres no válidos.")
    private String lastName;

    @NotNull(message = "Por favor, introduzca una dirección de correo.")
    @NotEmpty(message = "Por favor, introduzca una dirección de correo.")
    @Size(min = ValidationRules.EMAIL_MIN_SIZE, message = "La dirección de correo debe poseer al menos 12 caracteres.")
    @Email(message = "error.format.email")
    private String email;

    @NotNull(message = "Por favor, introduzca una contraseña.")
    @NotEmpty(message = "Por favor, introduzca una contraseña.")
    @Size(min = ValidationRules.PASSWORD_MIN_SIZE, message = "La contraseña debe poseer al menos 8 caracteres.")
    private String password;

    @NotNull(message = "Por favor, repita la contraseña.")
    @NotEmpty(message = "Por favor, repita la contraseña.")
    @Size(min = ValidationRules.PASSWORD_MIN_SIZE, message = "La contraseña debe poseer al menos 8 caracteres.")
    private String confirmationPassword;

    @NotNull(message = "Por favor, introduzca una fecha de nacimiento.")
    @NotEmpty(message = "Por favor, introduzca una fecha de nacimiento.")
    private String dateOfBirth;
}
