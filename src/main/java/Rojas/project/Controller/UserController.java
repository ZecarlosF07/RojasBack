package Rojas.project.Controller;

import Rojas.project.Domain.Entities.User;
import Rojas.project.Domain.Entities.UserLogin;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private FirebaseApp firebaseApp;

    private DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance(firebaseApp).getReference("Usuario");
    }

    // Create
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody User user) {
        DatabaseReference ref = getDatabaseReference();
        String id = ref.push().getKey();
        user.setId(id);
        ref.child(id).setValueAsync(user);
        return ResponseEntity.ok("User añadido correctamente con ID: " + id);
    }

    // Read all
    @GetMapping("/all")
    public CompletableFuture<ResponseEntity<List<User>>> getAllUsers() {
        DatabaseReference ref = getDatabaseReference();
        CompletableFuture<ResponseEntity<List<User>>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    user.setId(snapshot.getKey());
                    userList.add(user);
                }
                future.complete(ResponseEntity.ok(userList));
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
    public CompletableFuture<ResponseEntity<User>> getUserById(@PathVariable String id) {
        DatabaseReference ref = getDatabaseReference().child(id);
        CompletableFuture<ResponseEntity<User>> future = new CompletableFuture<>();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setId(id);
                    future.complete(ResponseEntity.ok(user));
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
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody User user) {
        DatabaseReference ref = getDatabaseReference().child(id);
        user.setId(id);
        ref.setValueAsync(user);
        return ResponseEntity.ok("User actualizado correctamente");
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        DatabaseReference ref = getDatabaseReference().child(id);
        ref.removeValueAsync();
        return ResponseEntity.ok("User eliminado correctamente");
    }

    // Authenticate
    @PostMapping("/authenticate")
    public CompletableFuture<ResponseEntity<?>> authenticateUser(@RequestBody UserLogin userLogin) {
        DatabaseReference ref = getDatabaseReference();
        CompletableFuture<ResponseEntity<?>> future = new CompletableFuture<>();
        final String correo = userLogin.getEmail();
        final String contraseña = userLogin.getPassword();

        ref.orderByChild("email").equalTo(userLogin.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String correoDB = userSnapshot.child("email").getValue(String.class);
                        String contraseñaDB = userSnapshot.child("password").getValue(String.class);

                        if (correo.equals(correoDB) && contraseña.equals(contraseñaDB)) {
                            JSONObject response = new JSONObject();
                            try {
                                response.put("message", "Autenticación exitosa");
                            } catch (JSONException e) {
                                future.completeExceptionally(e);
                                return;
                            }
                            future.complete(ResponseEntity.ok(response.toString()));
                            return;
                        }
                    }
                }
                future.complete(ResponseEntity.status(401).body("Credenciales incorrectas"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

}
