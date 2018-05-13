package cdbm.ucab.ingsw.controller;

import cdbm.ucab.ingsw.command.UserChangingAttributesCommand;
import cdbm.ucab.ingsw.command.UserSignUpCommand;
import cdbm.ucab.ingsw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import cdbm.ucab.ingsw.command.UserLoginCommand;

@Slf4j

@CrossOrigin
@RestController
@RequestMapping(value = "/user", produces = "application/json")
public class UserController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/register", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity register(@Valid @RequestBody UserSignUpCommand command) {
        return userService.registerUser(command);
    }

    @RequestMapping(value = "/login", consumes = "application/json", method = RequestMethod.POST)
    public ResponseEntity login(@Valid @RequestBody UserLoginCommand command) {
        return userService.loginAuthenticator(command);
    }

    @RequestMapping(value = "/update/{id}", consumes = "application/json", method = RequestMethod.PUT)
    public ResponseEntity update(@Valid @RequestBody UserChangingAttributesCommand command, @PathVariable("id") String id) {
        return userService.updateUser(command, id);
    }

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    public ResponseEntity getUsersByName(@PathVariable("name") String name) {
        return userService.getUsersByName(name);
    }


    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable("id") String id) {

        return userService.getUserById(id);
    }

}


