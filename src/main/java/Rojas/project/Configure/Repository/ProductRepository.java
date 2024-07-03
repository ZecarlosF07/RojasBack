package Rojas.project.Configure.Repository;

import Rojas.project.Application.Repository.IRepository;
import Rojas.project.Domain.Entities.Product;
import Rojas.project.Utils.Functions;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
public class ProductRepository implements IRepository<Product> {
    @Override
    public Product Create(Product entity) {
        String guid = UUID.randomUUID().toString();
        entity.setId(guid);
        Firestore firestore = FirestoreClient.getFirestore();

        CollectionReference productsRef = firestore.collection("Products");
        entity.setName(Functions.toTitleCase(entity.getName()));
        ApiFuture<QuerySnapshot> query = productsRef.whereEqualTo("name", entity.getName()).get();

        try {
            QuerySnapshot querySnapshot = query.get();
            if (!querySnapshot.isEmpty()) {
                // Si ya existe un producto con el mismo nombre, devolver null o lanzar una excepción
                throw new Exception("Product with the same name already exists");
            }

            // Si no existe un producto con el mismo nombre, proceder a guardar el nuevo producto
            ApiFuture<WriteResult> collectionApi = firestore.collection("Products").document(guid).set(entity);
            if (collectionApi.get().getUpdateTime() != null) {
                return entity;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Manejar la excepción de acuerdo a tus necesidades
            System.err.println("Error creating product: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void Delete(String id) {
        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference productDocRef = firestore.collection("Products").document(id);

        CollectionReference cartProductsRef = firestore.collection("Cart_Products");

        ApiFuture<WriteResult> productDeleteFuture = productDocRef.delete();

        try {
            WriteResult productDeleteResult = productDeleteFuture.get();
            ApiFuture<QuerySnapshot> cartProductsQuery = cartProductsRef.whereEqualTo("productId", id).get();
            QuerySnapshot cartProductsSnapshot = cartProductsQuery.get();

            List<QueryDocumentSnapshot> cartProductDocuments = cartProductsSnapshot.getDocuments();
            for (QueryDocumentSnapshot document : cartProductDocuments) {
                ApiFuture<WriteResult> cartProductDeleteFuture = document.getReference().delete();
                WriteResult cartProductDeleteResult = cartProductDeleteFuture.get();
            }

        } catch (Exception e) {
            System.err.println("Error eliminando documentos: " + e.getMessage());
            throw new RuntimeException("Error Eliminando");
        }
    }

    @Override
    public Product Update(Product entity) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference productDocRef = firestore.collection("Products").document(entity.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("name",entity.getName());
        updates.put("price",entity.getPrice());
        updates.put("stock",entity.getStock());
        ApiFuture<WriteResult> writeResult = productDocRef.update(updates);
        try {
            WriteResult result = writeResult.get();
            return entity;

        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Product Get(String id) {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference ref =  firestore.collection("Products").document(id);
        ApiFuture<DocumentSnapshot> future = ref.get();
        DocumentSnapshot doc = null;
        try {
            doc = future.get();
        } catch (Exception e) {
            return null;
        }
        var e = new Product();
        if(doc.exists()){
            e = doc.toObject(Product.class);
            System.out.println(doc);
            return  e;
        }

        return null;
    }

    @Override
    public List<Product> GetAll() {
        Firestore firestore = FirestoreClient.getFirestore();
        CollectionReference productsRef = firestore.collection("Products");

        ApiFuture<QuerySnapshot> query = productsRef.get();

        try {
            QuerySnapshot querySnapshot = query.get();

            List<Product> allProducts = querySnapshot.toObjects(Product.class);

            return allProducts;
        } catch (Exception e) {
            System.err.println("Error Getting Products: " + e.getMessage());
            return null; // Devolver null en caso de error
        }
    }
}
