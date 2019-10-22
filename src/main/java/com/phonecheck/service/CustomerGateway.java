package com.phonecheck.service;

import com.braintreegateway.Configuration;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.Result;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;


public class CustomerGateway {
    private Configuration configuration;
    private Http http;

    public CustomerGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }





    /**
     * Creates a {@link Customer}.
     *
     * @param request
     *            the request.
     * @return a {@link Result}.
     */
    public Result<Customer> create(CustomerRequest request) {
        NodeWrapper node = http.post(configuration.getMerchantPath() + "/customers", request);
        return new Result<Customer>(node, Customer.class);
    }

    /**
     * Deletes a {@link Customer} by id.
     *
     * @param id
     *            the id of the {@link Customer}.
     * @return a {@link Result}.
     */
    public Result<Customer> delete(String id) {
        http.delete(configuration.getMerchantPath() + "/customers/" + id);
        return new Result<Customer>();
    }

    /**
     * Finds a {@link Customer} by id.
     *
     * @param id
     *            the id of the {@link Customer}.
     * @return the {@link Customer} or raises a
     *         {@link NotFoundException}.
     */
    public Customer find(String id) {
        if(id == null || id.trim().equals(""))
            throw new NotFoundException();

        return new Customer(http.get(configuration.getMerchantPath() + "/customers/" + id));
    }

    /**
     * Finds a {@link Customer} by id.
     *
     * @param id
     *            the id of the {@link Customer}.
     * @param associationFilterId
     *            the id of the association filter to use.
     * @return the {@link Customer} or raises a
     *         {@link NotFoundException}.
     */
    public Customer find(String id, String associationFilterId) {
        if(id == null || id.trim().equals(""))
            throw new NotFoundException();

        if(associationFilterId == null || associationFilterId.isEmpty())
            throw new NotFoundException();

        String queryParams = "?association_filter_id=" + associationFilterId;
        return new Customer(http.get(configuration.getMerchantPath() + "/customers/" + id + queryParams));
    }

    /**
     * Finds all Transactions that match the query and returns a {@link ResourceCollection}.
     * See: <a href="https://developers.braintreepayments.com/reference/request/transaction/search/java" target="_blank">https://developers.braintreepayments.com/reference/request/transaction/search/java</a>
     * @param query the request query to use for search
     * @return a {@link ResourceCollection}.
     */


    /**
     * Please use gateway.transparentRedirect().url() instead
     * @deprecated see TransparentRedirectGateway#url()
     * @return the redirect URL for create
     */
    @Deprecated
    public String transparentRedirectURLForCreate() {
        return configuration.getBaseURL() + configuration.getMerchantPath() + "/customers/all/create_via_transparent_redirect_request";
    }

    /**
     * Please use gateway.transparentRedirect().url() instead
     * @deprecated see TransparentRedirectGateway#url()
     * @return the redirect URL for update
     */

    /**
     * Updates a {@link Customer}.
     *
     * @param id
     *            the id of the {@link Customer}.
     * @param request
     *            the request.
     * @return a {@link Result}.
     */


}