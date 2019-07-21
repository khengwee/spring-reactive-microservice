package org.kiwi.spring.reactive.customer.mapper;

import org.kiwi.spring.reactive.customer.client.CustomerDO;
import org.kiwi.spring.reactive.customer.web.CustomerDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CustomerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributes.name", target = "name")
    @Mapping(source = "attributes.segment", target = "segment")
    CustomerDTO toCustomerDTO(CustomerDO customer);

    @InheritInverseConfiguration
    CustomerDO fromCustomerDTO(CustomerDTO customerDTO);
}