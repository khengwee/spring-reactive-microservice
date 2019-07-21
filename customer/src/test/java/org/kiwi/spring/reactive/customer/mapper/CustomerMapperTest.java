package org.kiwi.spring.reactive.customer.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kiwi.spring.reactive.customer.client.CustomerAttributeDO;
import org.kiwi.spring.reactive.customer.client.CustomerDO;
import org.kiwi.spring.reactive.customer.web.CustomerDTO;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CustomerMapperTest {

    private static final CustomerMapper MAPPER = Mappers.getMapper(CustomerMapper.class);

    @Before
    public void setUp() {
    }

    @Test
    public void testToCustomerDTO() {
        CustomerAttributeDO customerAttributeDO = new CustomerAttributeDO();
        customerAttributeDO.setName("Tester 1");
        customerAttributeDO.setSegment("Personal");

        CustomerDO customerDO = new CustomerDO();
        customerDO.setId("1");
        customerDO.setType("customer");
        customerDO.setAttributes(customerAttributeDO);

        CustomerDTO customerDTO = MAPPER.toCustomerDTO(customerDO);
        assertEquals("1", customerDTO.getId());
        assertEquals("Tester 1", customerDTO.getName());
        assertEquals("Personal", customerDTO.getSegment());
    }

    @Test
    public void testFromCustomerDTO() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId("2");
        customerDTO.setName("Tester 2");
        customerDTO.setSegment("Premium");

        CustomerDO customerDO = MAPPER.fromCustomerDTO(customerDTO);
        assertEquals("2", customerDO.getId());
        assertEquals("Tester 2", customerDO.getAttributes().getName());
        assertEquals("Premium", customerDO.getAttributes().getSegment());
    }
}
