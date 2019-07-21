package org.kiwi.spring.reactive.customer.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.customer.client.CustomerResourceClient;
import org.kiwi.spring.reactive.core.utility.FileUtil;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerRoutingConfigTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.password}")
	private String password;

    @Autowired
    private CustomerResourceClient customerResourceClient;

    @Autowired
    private ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void setUp() {
    }

	@Test
	public void testFindOne_Success() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_success_one.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

		webTestClient.get().uri("/api/customers/1")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody().json(FileUtil.loadFile("json/route_response_GET_customer_success_one.json"));
	}

	@Test
	public void testFindAll_Success() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_success_many.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

		webTestClient.get().uri("/api/customers")
				.header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody().json(FileUtil.loadFile("json/route_response_GET_customer_success_many.json"));
	}

    @Test
    public void testFindAll_Error_Business() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/vnd.api+json")
                        .withBody(FileUtil.loadFile("json/client_response_customer_error_500.json"))));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        webTestClient.get().uri("/api/customers")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().json(FileUtil.loadFile("json/route_response_GET_customer_error_business.json"));
    }

    @Test
    public void testFindAll_Error_Technical() throws Exception {
        // Mock Customer Resource Client
        givenThat(get(urlEqualTo("/api/mock/customers"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/vnd.api+json")));
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), "http://localhost:8089/api/mock/customers");

        webTestClient.get().uri("/api/customers")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().json(FileUtil.loadFile("json/route_response_GET_customer_error_technical.json"));
    }

    @Test
    public void testFindAll_Error_Other() throws Exception {
        FieldSetter.setField(customerResourceClient, customerResourceClient.getClass()
                .getDeclaredField("customerResourceUrl"), null);

        webTestClient.get().uri("/api/customers")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody().json(FileUtil.loadFile("json/route_response_GET_customer_error_other.json"));
    }
}
