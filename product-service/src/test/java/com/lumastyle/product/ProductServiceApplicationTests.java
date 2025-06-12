package com.lumastyle.product;

import com.lumastyle.product.event.ProductEvent;
import com.lumastyle.product.infrastructure.search.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@SpringBootTest
@ActiveProfiles("test")
class ProductServiceApplicationTests {


    @MockitoBean
    private ProductSearchRepository searchRepository;

    @MockitoBean
    private KafkaTemplate<String, ProductEvent> kafkaTemplate;


    @Test
    void contextLoads() {
        //Tests will be added later
    }

}
