package Rojas.project.Domain.Entities;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
    private String email;
    private String password;

}
