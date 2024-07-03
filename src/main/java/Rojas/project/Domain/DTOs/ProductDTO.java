package Rojas.project.Domain.DTOs;

import Rojas.project.Domain.Entities.Product;

public record ProductDTO (String name, String id, double price, int stock, String uri) {
    public ProductDTO(Product p){
        this(p.getName(),p.getId(),p.getPrice(),p.getStock(),p.getUriImg());
    }
}
