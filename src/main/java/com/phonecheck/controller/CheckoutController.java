package com.phonecheck.controller;

import com.braintreegateway.*;
import com.phonecheck.braintree.BraintreeApplication;
import com.phonecheck.model.User;

import com.phonecheck.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Status;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@RestController
public class CheckoutController {

    /**
     * This is dependency injected from the BraintreeConfig class
     */
   private BraintreeGateway gateway = BraintreeApplication.gateway;


    /**
     * Successful status codes from credit card processing
     */
    private Transaction.Status[] TRANSACTION_SUCCESS_STATUSES = new Transaction.Status[]{
            Transaction.Status.AUTHORIZED,
            Transaction.Status.AUTHORIZING,
            Transaction.Status.SETTLED,
            Transaction.Status.SETTLEMENT_CONFIRMED,
            Transaction.Status.SETTLEMENT_PENDING,
            Transaction.Status.SETTLING,
            Transaction.Status.SUBMITTED_FOR_SETTLEMENT
    };

    /**
     * Redirect users to /checkouts if they hit the root
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model) {
        return "redirect:/checkouts";
    }

    /**
     * Main page - accepts a couple of different payment types
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.GET)
    public ModelAndView checkout(ModelAndView model) {
        //get the token
       // String clientToken = gateway.clientToken().generate();

        //add the token to the model - this will be used in JavaScript code
        //model.addAttribute("clientToken", "dsdsds");
        model.setViewName("newTransaction");
        //serve new.html
        return model;
    }

    /**
     * We get here when the user submits the transaction - note that it's a POST
     */
    @RequestMapping(value = "/checkouts", method = RequestMethod.POST)
    public String postForm(@RequestParam("amount") String amount,
                           @RequestParam("payment_method_nonce") String nonce, Model model,
                           final RedirectAttributes redirectAttributes) {

        //get rid of whitespace
        amount = amount.trim();

        BigDecimal decimalAmount;

        try {
            //get the decimal version of the text entered
            decimalAmount = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            //we get here if it's not a valid number
            String errorMessage = getErrorMessage(amount);
            redirectAttributes.addFlashAttribute("errorDetails", errorMessage);
            redirectAttributes.addFlashAttribute("amount", amount);
            return "redirect:checkouts";
        }

        //submit the request for processing
        TransactionRequest request = new TransactionRequest()
                .amount(decimalAmount)
                .paymentMethodNonce(nonce)
                .options()
                .submitForSettlement(true)
                .done();

        //get the response
        Result<Transaction> result = gateway.transaction().sale(request);

        //if it's a successful transaction, go to the transaction results page
        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            return "redirect:checkouts/" + transaction.getId();
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            return "redirect:checkouts/" + transaction.getId();
        } else {
            //if the transaction failed, return to the payment page and display all errors
            String errorString = "";
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
            }
            redirectAttributes.addFlashAttribute("errorDetails", errorString);
            redirectAttributes.addFlashAttribute("amount", amount);
            return "redirect:checkouts";
        }
    }


    /**
     * Creates the server-side error message
     */
    private String getErrorMessage(String amount) {
        String errorMessage = amount + " is not a valid price.";

        if (amount.equals("")) {
            errorMessage = "Please enter a valid price.";
        }

        return errorMessage;
    }


    /**
     * We get here when the user has submitted a transaction and received a
     * Transaction ID.
     */
    @RequestMapping(value = "/checkouts/{transactionId}")
    public String getTransaction(@PathVariable String transactionId, Model model) {
        Transaction transaction;
        CreditCard creditCard;
        Customer customer;

        try {
            //find the transaction by its ID
            transaction = gateway.transaction().find(transactionId);

            //grab credit card info
            creditCard = transaction.getCreditCard();

            //grab the customer info
            customer = transaction.getCustomer();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return "redirect:/checkouts";
        }

        //set a boolean that determines whether or not the transaction was successful
        model.addAttribute("isSuccess", Arrays.asList(TRANSACTION_SUCCESS_STATUSES).contains(transaction.getStatus()));

        //put the relevant objects in the model
        model.addAttribute("transaction", transaction);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("customer", customer);

        //server transactionResults.html
        return "transactionResults";
    }

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user){
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView processRegistrationForm(ModelAndView modelAndView, @Valid User user, BindingResult bindingResult, HttpServletRequest request) {

        // Lookup user in database by e-mail
       // User userExists = userService.findByEmail(user.getEmail());
        System.out.println(request.getParameter("firstName"));
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        }
        else{
            UserService userService = new UserService();
            userService.creatUser(user);
          /* gateway = new BraintreeGateway(Environment.SANDBOX,
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
            if(result.isSuccess()) {
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

                Result<CreditCard> cResult=	gateway.creditCard().create(creditRequest);
                System.out.println(cResult.isSuccess());
                System.out.println(cResult);

            }*/


           // modelAndView.addObject("confirmationMessage", "A confirmation e-mail has been sent to " );
            modelAndView.setViewName("register");
        }


        return modelAndView;
    }

}
