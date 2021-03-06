package be.kdg.integratieproject2.data.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(value = "be.kdg.integratieproject2.data.implementations")
public class MongoConfig {

    private static final String URI = "mongodb://teamcrypto:integratieproject@kandoe-shard-00-00-wr8ld.mongodb.net:27017,kandoe-shard-00-01-wr8ld.mongodb.net:27017,kandoe-shard-00-02-wr8ld.mongodb.net:27017/Kandoe?ssl=true&replicaSet=Kandoe-shard-0&authSource=admin";

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoClientURI mongoClientURI = new MongoClientURI(URI);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        return new SimpleMongoDbFactory(mongoClient, "Kandoe");
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}
