package Rojas.project.Application.Services;

import Rojas.project.Domain.DTOs.ProductDTO;
import Rojas.project.Domain.Entities.Product;
import Rojas.project.Configure.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository pr;

    public Product Create(Product product){
        var data =  pr.Create(product);
        if(data != null){
            return data;
        }
        return null;
    }

    public Product Get(String id){
        return pr.Get(id);
    }

    public List<ProductDTO> GetAll(){
        List<Product> allp =  pr.GetAll();
        List<ProductDTO> dtop = allp.stream().map(ProductDTO::new).collect(Collectors.toList());
        return dtop;
    }

    public void delete(String id){
        pr.Delete(id);
    }

    public Product update(Product product){
        return pr.Update(product);
    }

}
