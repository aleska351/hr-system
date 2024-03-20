package com.codingdrama.hrsystem.service.dto;


import com.codingdrama.hrsystem.model.Position;
import com.codingdrama.hrsystem.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private Position position;

    public CreateUserResponse(User user) {
        CreateUserResponse userResponse = new CreateUserResponse();
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPosition(Position.ACCOUNTANT);
    }
}
