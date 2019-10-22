package com.phonecheck.braintree;

import com.braintreegateway.*;
//import com.phonecheck.service.BraintreeGateway;
//import com.phonecheck.service.Customer;
//import com.phonecheck.service.CustomerRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@Configuration
@SpringBootApplication
@ComponentScan({ "com.phonecheck.controller"})
public class BraintreeApplication {
	public static BraintreeGateway gateway;

	public static void main(String[] args) {
		SpringApplication.run(BraintreeApplication.class, args);

// Below code is  for Creating customers from excel sheet.
       /*gateway = new BraintreeGateway(Environment.SANDBOX,
                "jdkyhp9sfrgd8jcj",
                "4sqwtjzpywrtftwt",
                "468dca89d929a8912075cf596c9a2328");
		String csvFile = "/Users/PC/Downloads/BrainTreeTestSheet5.csv";
		String line = "";
		String cvsSplitBy = ",";
		String[] customerData={};
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {

				// use comma as separator
				customerData = line.split(cvsSplitBy);
				createCustomer(customerData);
				//System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}*/


	}



	public static Boolean createCustomer(String[] customerData ){
        BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX,
                "jdkyhp9sfrgd8jcj",
                "4sqwtjzpywrtftwt",
                "468dca89d929a8912075cf596c9a2328");
		Result<Address> aResult = new Result<>();
		CustomerRequest request = new CustomerRequest()
				.firstName(customerData[0])
				.lastName(customerData[1])
				.company(customerData[2])
				.email(customerData[3])
				.phone(customerData[4]);

		Result<Customer> result = gateway.customer().create(request);

		System.out.println(result.isSuccess());
		System.out.println(result);
		if(result.isSuccess()) {
			AddressRequest addressRequest = new AddressRequest()
					.firstName(customerData[0])
					.lastName(customerData[1])
					.company(customerData[2])
					.streetAddress(customerData[8])
					.extendedAddress(customerData[9])
					.locality(customerData[10])
					.region(customerData[11])
					.countryCodeAlpha2(customerData[12])
					.postalCode(customerData[13]);
			aResult = gateway.address().create(result.getTarget().getId(), addressRequest);
			System.out.println(aResult);
		}
		if (result.isSuccess()) {
			CreditCardRequest creditRequest = new CreditCardRequest()
					.customerId(result.getTarget().getId())
					.cardholderName(customerData[0] + " " + customerData[1])
					.number(customerData[5])
					.expirationDate(customerData[6])
					.cvv(customerData[7])
					.billingAddressId(aResult.getTarget().getId());

			Result<CreditCard> cResult=	gateway.creditCard().create(creditRequest);
			System.out.println(cResult.isSuccess());
			System.out.println(cResult);

		}
		return result.isSuccess();

	}


}
