package com.phonecheck.service;

import com.braintreegateway.Configuration;
import com.braintreegateway.Environment;
import com.braintreegateway.util.GraphQLClient;
import com.braintreegateway.util.Http;

//import com.braintreegateway.CustomerGateway;

public class BraintreeGateway {

    private Configuration configuration;
    private Http http;
    public GraphQLClient graphQLClient;

    public BraintreeGateway(Environment environment, String merchantId, String publicKey, String privateKey) {
        this.configuration = new Configuration(environment, merchantId, publicKey, privateKey);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String environment, String merchantId, String publicKey, String privateKey) {
        this.configuration = new Configuration(environment, merchantId, publicKey, privateKey);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String clientId, String clientSecret) {
        this.configuration = new Configuration(clientId, clientSecret);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String accessToken) {
        this.configuration = new Configuration(accessToken);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public CustomerGateway customer() {
        return new CustomerGateway(http, configuration);
    }

}
