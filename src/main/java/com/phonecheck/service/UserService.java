package com.phonecheck.service;

import com.braintreegateway.*;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Customer;
import com.braintreegateway.CustomerRequest;
import com.phonecheck.braintree.BraintreeApplication;
import com.phonecheck.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;

public class UserService {

    @Autowired
    private BraintreeGateway gateway ;

    public void creatUser(@Valid User user) {
        gateway = new BraintreeGateway(Environment.SANDBOX,
                "jdkyhp9sfrgd8jcj",
                "4sqwtjzpywrtftwt",
                "468dca89d929a8912075cf596c9a2328");


        Result<Address> aResult = new Result<>();
        CustomerRequest customerRequest = new CustomerRequest()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .company(user.getCompany())
                .email(user.getEmail())
                .phone(user.getPhone());

        Result<Customer> result = gateway.customer().create(customerRequest);

        System.out.println(result.isSuccess());
        System.out.println(result);
        if (result.isSuccess()) {
            AddressRequest addressRequest = new AddressRequest()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .company(user.getCompany())
                    .streetAddress(user.getAddress1())
                    .extendedAddress(user.getAddress2())
                    .locality(user.getCity())
                    .region(user.getState())
                    .countryCodeAlpha2(user.getCountry())
                    .postalCode(user.getZip());
            aResult = gateway.address().create(result.getTarget().getId(), addressRequest);
            System.out.println(aResult);
        }
        if (result.isSuccess()) {
            CreditCardRequest creditRequest = new CreditCardRequest()
                    .customerId(result.getTarget().getId())
                    .cardholderName(user.getCardHolderName())
                    .number(user.getCardNo())
                    .expirationDate(user.getExpDate())
                    .cvv(user.getCvv())
                    .billingAddressId(aResult.getTarget().getId());

            Result<CreditCard> cResult = gateway.creditCard().create(creditRequest);
            System.out.println(cResult.isSuccess());
            System.out.println(cResult);

        }
    }
}
