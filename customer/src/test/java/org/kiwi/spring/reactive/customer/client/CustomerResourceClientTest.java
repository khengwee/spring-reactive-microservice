package org.kiwi.spring.reactive.customer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.core.exception.BusinessException;
import org.kiwi.spring.reactive.core.exception.TechnicalException;
import org.kiwi.spring.reactive.core.utility.FileUtil;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerResourceClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private CustomerResourceClient customerResourceClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    @Before
    public void setUp() {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                this.reactiveClientRegistrationRepository,
                new UnAuthenticatedServerOAuth2AuthorizedClientRepository());
        customerResourceClient = new CustomerResourceClient(
                WebClient.builder().filter(oauth).build(), objectMapper);
    }

    @Test
    public void testGetCustomerById_Success() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_success_one.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        CustomerDO customerDO = customerResourceClient.getCustomerById("1").block();
        assertEquals("1", customerDO.getId());
        assertEquals("John Smith", customerDO.getAttributes().getName());
        assertEquals("Priority", customerDO.getAttributes().getSegment());
        assertEquals("CustomerDO{id='1', type='customers', attributes=CustomerAttributeDO{name='John Smith', segment='Priority'}}",
                customerDO.toString());
    }

    @Test
    public void testGetCustomerById_Error_HTTP_500() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers/1"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_error_500.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<CustomerDO> monoCustomerDO = customerResourceClient.getCustomerById("1");
        StepVerifier.create(monoCustomerDO)
                .expectError(BusinessException.class)
                .verify();
        StepVerifier.create(monoCustomerDO)
                .expectErrorMessage("No records found.")
                .verify();
    }

    @Test
    public void testGetCustomerById_Error_HTTP_503() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers/1"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/vnd.api+json")));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<CustomerDO> monoCustomerDO = customerResourceClient.getCustomerById("1");
        StepVerifier.create(monoCustomerDO)
                .expectError(TechnicalException.class)
                .verify();
        StepVerifier.create(monoCustomerDO)
                .expectErrorMessage("503 SERVICE_UNAVAILABLE")
                .verify();
    }

    @Test
    public void testGetCustomerById_Error_HTTP_404() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers/1"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/vnd.api+json")));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<CustomerDO> monoCustomerDO = customerResourceClient.getCustomerById("1");
        StepVerifier.create(monoCustomerDO)
                .expectError(TechnicalException.class)
                .verify();
        StepVerifier.create(monoCustomerDO)
                .expectErrorMessage("404 NOT_FOUND")
                .verify();
    }

    @Test
    public void testGetCustomers_Success() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_success_many.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        List<CustomerDO> customerDOs = customerResourceClient.getCustomers().block();
        assertEquals(2, customerDOs.size());
        assertEquals("2", customerDOs.get(1).getId());
        assertEquals("Tailor Swift", customerDOs.get(1).getAttributes().getName());
        assertEquals("Personal", customerDOs.get(1).getAttributes().getSegment());
    }

    @Test
    public void testGetCustomers_Error_HTTP_500() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_error_500.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<List<CustomerDO>> monoCustomerDOs = customerResourceClient.getCustomers();
        StepVerifier.create(monoCustomerDOs)
                .expectError(BusinessException.class)
                .verify();
        StepVerifier.create(monoCustomerDOs)
                .expectErrorMessage("No records found.")
                .verify();
    }

    @Test
    public void testGetCustomers_Error_HTTP_503() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/vnd.api+json")));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<List<CustomerDO>> monoCustomerDOs = customerResourceClient.getCustomers();
        StepVerifier.create(monoCustomerDOs)
                .expectError(TechnicalException.class)
                .verify();
        StepVerifier.create(monoCustomerDOs)
                .expectErrorMessage("503 SERVICE_UNAVAILABLE")
                .verify();
    }

    @Test
    public void testGetCustomers_Error_HTTP_404() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/vnd.api+json")));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        Mono<List<CustomerDO>> monoCustomerDOs = customerResourceClient.getCustomers();
        StepVerifier.create(monoCustomerDOs)
                .expectError(TechnicalException.class)
                .verify();
        StepVerifier.create(monoCustomerDOs)
                .expectErrorMessage("404 NOT_FOUND")
                .verify();
    }
}
