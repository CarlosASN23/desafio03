package br.com.compassuol.Desafio._3.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRoles {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    public static String toEnum(String role){
        if(role == null){
            return null;
        }
        for(UserRoles userario : UserRoles.values()){
            if(role.equals(userario.role)){
                return role;
            }
        }
        throw new IllegalArgumentException("id inválido: " + role);
    }

}