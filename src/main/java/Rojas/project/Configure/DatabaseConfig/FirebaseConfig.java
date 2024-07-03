package Rojas.project.Configure.DatabaseConfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream serviceAccount = classLoader.getResourceAsStream("serviceAccountKey.json");

        if (serviceAccount == null) {
            throw new FileNotFoundException("serviceAccountKey.json not found in resources");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://rojasapp-7d224-default-rtdb.firebaseio.com/")
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
