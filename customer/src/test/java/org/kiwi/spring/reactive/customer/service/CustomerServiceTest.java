package org.kiwi.spring.reactive.customer.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.core.utility.FileUtil;
import org.kiwi.spring.reactive.customer.client.CustomerDO;
import org.kiwi.spring.reactive.customer.client.CustomerResourceClient;
import org.kiwi.spring.reactive.customer.web.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    private CustomerResourceClient customerResourceClient;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
    }

    @Test
    public void testGetCustomerById() throws IOException {
        // Mock Response
        JsonNode customerNode = objectMapper.readTree(
                FileUtil.loadFile("json/client_response_customer_success_one.json"));
        CustomerDO customerDO = objectMapper.readValue(
                customerNode.get("data").toString(), CustomerDO.class);
        Mono<CustomerDO> monoCustomerDO = Mono.just(customerDO);

        when(customerResourceClient.getCustomerById(anyString())).thenReturn(monoCustomerDO);
        Mono<CustomerDTO> response = customerService.getCustomerById("1");
        StepVerifier.create(response).expectNextMatches(customerDTO -> {
            assertEquals("1", customerDTO.getId());
            assertEquals("John Smith", customerDTO.getName());
            assertEquals("Priority", customerDTO.getSegment());
            return true;
        }).verifyComplete();
    }

    @Test
    public void testGetCustomers() throws IOException {
        // Mock Response
        JsonNode customerNode = objectMapper.readTree(
                FileUtil.loadFile("json/client_response_customer_success_many.json"));
        List<CustomerDO> customerDOs = objectMapper.readValue(
                customerNode.get("data").toString(), new TypeReference<List<CustomerDO>>() {});
        Mono<List<CustomerDO>> monoCustomerDOs = Mono.just(customerDOs);

        when(customerResourceClient.getCustomers()).thenReturn(monoCustomerDOs);
        Mono<List<CustomerDTO>> response = customerService.getCustomers();
        StepVerifier.create(response).expectNextMatches(customerDTOs -> {
            assertEquals(2, customerDTOs.size());
            assertEquals("1", customerDTOs.get(0).getId());
            assertEquals("John Smith", customerDTOs.get(0).getName());
            assertEquals("Priority", customerDTOs.get(0).getSegment());
            assertEquals("2", customerDTOs.get(1).getId());
            assertEquals("Tailor Swift", customerDTOs.get(1).getName());
            assertEquals("Personal", customerDTOs.get(1).getSegment());
            return true;
        }).verifyComplete();
    }
}
