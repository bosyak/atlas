package panov;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtlasApplication {

    private static final Logger LOG = LoggerFactory.getLogger(AtlasApplication.class);

    private static Cluster cluster;
    private static Session session;

    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }
}
