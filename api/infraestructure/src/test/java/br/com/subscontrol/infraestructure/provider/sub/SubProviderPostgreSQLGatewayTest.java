package br.com.subscontrol.infraestructure.provider.sub;

import br.com.subscontrol.infraestructure.PostgreSQLGatewayTest;
import br.com.subscontrol.infraestructure.provider.sub.persistence.SubProviderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@PostgreSQLGatewayTest
public class SubProviderPostgreSQLGatewayTest {

    @Autowired
    private SubProviderPostgreSQLGateway gateway;

    @Autowired
    private SubProviderRepository repository;

    @Test
    void contextLoad() {
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }
}
