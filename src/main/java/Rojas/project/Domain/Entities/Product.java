package Rojas.project.Domain.Entities;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends Auditory{
    private String id;
    private String name;
    private double price;
    private int stock;
    private String uriImg;
}
