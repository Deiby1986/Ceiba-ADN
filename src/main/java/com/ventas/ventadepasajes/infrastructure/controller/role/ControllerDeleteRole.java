package com.ventas.ventadepasajes.infrastructure.controller.role;

import com.ventas.ventadepasajes.aplication.command.handler.role.HandlerDeleteRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class ControllerDeleteRole {

    private HandlerDeleteRole handlerDeleteRole;

    public ControllerDeleteRole(HandlerDeleteRole handlerDeleteRole){
        this.handlerDeleteRole = handlerDeleteRole;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRole(@PathVariable long id){
        if(handlerDeleteRole.run(id)){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
