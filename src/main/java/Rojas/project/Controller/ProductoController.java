package Rojas.project.Controller;

import Rojas.project.Domain.Entities.Productos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private FirebaseApp firebaseApp;

    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance(firebaseApp).getReference("Productos");
    }

    // Create
    @PostMapping("/add")
    public ResponseEntity<String> addProducto(@RequestBody Productos producto) {
        DatabaseReference ref = getDatabaseReference();
        String id = ref.push().getKey();
        producto.setId(id);
        ref.child(id).setValueAsync(producto);
        return ResponseEntity.ok("Producto añadido correctamente con ID: " + id);
    }

    // Read all
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<Productos>>> getAllProductos() {
        DatabaseReference ref = getDatabaseReference();
        CompletableFuture<ResponseEntity<List<Productos>>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Productos> productosList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Productos producto = snapshot.getValue(Productos.class);
                    producto.setId(snapshot.getKey());
                    productosList.add(producto);
                }
                future.complete(ResponseEntity.ok(productosList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    // Read by ID
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Productos>> getProductoById(@PathVariable String id) {
        DatabaseReference ref = getDatabaseReference().child(id);
        CompletableFuture<ResponseEntity<Productos>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Productos producto = dataSnapshot.getValue(Productos.class);
                if (producto != null) {
                    producto.setId(id); // Asegúrate de que el producto tenga el ID correcto
                    future.complete(ResponseEntity.ok(producto));
                } else {
                    future.complete(ResponseEntity.notFound().build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    // Update
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProducto(@PathVariable String id, @RequestBody Productos producto) {
        DatabaseReference ref = getDatabaseReference().child(id);
        producto.setId(id); // Asegúrate de que el producto tenga el ID correcto
        ref.setValueAsync(producto);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable String id) {
        DatabaseReference ref = getDatabaseReference().child(id);
        ref.removeValueAsync();
        return ResponseEntity.ok("Producto eliminado correctamente");
    }
}
