package Rojas.project.Domain.Entities;


import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Productos {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria;
}