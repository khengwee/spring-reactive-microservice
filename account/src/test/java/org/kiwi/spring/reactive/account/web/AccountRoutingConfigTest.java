package org.kiwi.spring.reactive.account.web;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.account.repository.AccountEntity;
import org.kiwi.spring.reactive.account.repository.AccountRepository;
import org.kiwi.spring.reactive.core.utility.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountRoutingConfigTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebTestClient webTestClient;

    private SimpleDateFormat simpleDateFormat;

    @Before
    public void setUp() throws ParseException {
        accountRepository.deleteAll().block();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date createdDate = simpleDateFormat.parse("2018-06-08T17:00:00");
        Date updatedDate = simpleDateFormat.parse("2019-06-08T22:00:00");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNo("12345");
        accountEntity.setAccountType("Savings Account");
        accountEntity.setAccountStatus("Active");
        accountEntity.setCreatedBy("System");
        accountEntity.setCreatedDate(createdDate);
        accountEntity.setUpdatedBy("System");
        accountEntity.setUpdatedDate(updatedDate);

        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNo("1234567");
        accountEntity2.setAccountType("Current Account");
        accountEntity2.setAccountStatus("Dormant");
        accountEntity2.setCreatedBy("System");
        accountEntity2.setCreatedDate(createdDate);
        accountEntity2.setUpdatedBy("System");
        accountEntity2.setUpdatedDate(updatedDate);

        accountRepository.save(accountEntity).block();
        accountRepository.save(accountEntity2).block();
    }

    @Test
    public void testFindAll_Success() throws Exception {
        webTestClient.get().uri("/api/accounts")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(FileUtil.loadFile("json/route_response_GET_account_success_many.json"));
    }

    @Test
    public void testFindOne_Success() throws Exception {
        webTestClient.get().uri("/api/accounts/1234567")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(FileUtil.loadFile("json/route_response_GET_account_success_one.json"));
    }

    @Test
    public void testSave_Success() throws Exception {
        webTestClient.post().uri("/api/accounts")
                .header("Authorization", "Basic " + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8)))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(FileUtil.loadFile("json/route_request_POST_account.json"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(FileUtil.loadFile("json/route_request_POST_account.json"));
    }

}
