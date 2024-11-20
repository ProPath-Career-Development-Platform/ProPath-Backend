package Propath.controller;

import Propath.model.*;
import Propath.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.PaymentMethodType;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class CheckoutController {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentSessionReository paymentSessionReository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final BillHistoryRepository billHistoryRepository;



    public CheckoutController(UserRepository userRepository,
                              SubscriptionPlanRepository subscriptionPlanRepository,
                              PaymentSessionReository paymentSessionReository,
                              UserSubscriptionRepository userSubscriptionRepository,
                              BillHistoryRepository billHistoryRepository
    ) {
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.paymentSessionReository = paymentSessionReository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.billHistoryRepository = billHistoryRepository;
    }

    static {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        // Set your Stripe Secret Key
        Stripe.apiKey = dotenv.get("STRIPE_SECRET_KEY"); // Make sure STRIPE_SECRET_KEY is in the .env file
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> body) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email

            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            Optional<SubscriptionPlan> plan = subscriptionPlanRepository.findById(((Number) body.get("planId")).longValue());

            //delete prev sessions if availble

            Optional<PaymentSession> pSession = paymentSessionReository.findByUserAndUpgradeTrue(userOptional.get());


            if(pSession.isPresent()){
                paymentSessionReository.delete(pSession.get());
            }
            Optional<PaymentSession> pSession2 = paymentSessionReository.findByUserAndUpgradeFalse(userOptional.get());

            if(pSession2.isPresent()){
                paymentSessionReository.delete(pSession2.get());
            }




            // Get price from the request body
            long priceInLKR = ((Number) body.get("price")).longValue();
            String currency = "LKR"; // Currency is LKR
            String ProductName = "Change Your Subscription Plan to " + plan.get().getPlanName();
            String CustomerMail = userOptional.get().getEmail();
            Boolean upgrade = ((Boolean) body.get("upgrade"));
           // String productDescription = "You're about to switch to a new subscription plan. Review your new plan details below before confirming your change. The new plan will take effect immediately, and any applicable charges will be processed accordingly";


            // Create a Stripe session with the provided price, currency, and timeout (30 minutes)
            long expirationTime = System.currentTimeMillis() / 1000 + (30 * 60); // 30 minutes in seconds

            // Create session params with correct payment method type
            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .addPaymentMethodType(PaymentMethodType.CARD)  // Use PaymentMethodType enum for CARD
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)  // Set the currency as LKR
                                                    .setUnitAmount(priceInLKR * 100) // Amount in cents (multiply by 100)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(ProductName)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/jobprovider/plans-and-billing/payment/success?sessionId={CHECKOUT_SESSION_ID}&type=upgrade")
                    .setCancelUrl("http://localhost:5173/jobprovider/plans-and-billing/payment/failed")
                    .setExpiresAt(expirationTime) // Set session expiration time (30 minutes)
                    .setCustomerEmail(CustomerMail)
                    .build();

            // Create the session
            Session session = Session.create(sessionParams);

            // Return the session ID to frontend
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());

            //save session to payement session
           PaymentSession ps = new PaymentSession();

            ps.setSessionId(session.getId());
            ps.setCreatedAt(LocalDateTime.now());
            ps.setUpgrade(upgrade);
            ps.setSubscriptionPlan(plan.get());
            ps.setUser(userOptional.get());
            ps.setPrice(priceInLKR);

            paymentSessionReository.save(ps);

            return response;
        } catch (StripeException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creating session: " + e.getMessage());
            return errorResponse;
        }
    }

    @GetMapping("/create-checkout-session-monthly")
    public Map<String, String> createCheckoutSession() {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email

            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if(userOptional.isEmpty()){
                throw new RuntimeException("user not found");
            }

            Optional<UserSubscription> userSub = userSubscriptionRepository.findByUser_IdAndStatusAndPaidStatus(userOptional.get().getId(),"ACTIVE","none");

            //delete prev sessions if availble
            Optional<PaymentSession> pSession = paymentSessionReository.findByUserAndMonthlyTrue(userOptional.get());


            if(pSession.isPresent()){
                paymentSessionReository.delete(pSession.get());
            }

            // Get price from the request body
            double price = userSub.get().getSubscriptionPlan().getPrice();
            long priceInLKR = Math.round(price);
            String currency = "LKR"; // Currency is LKR
            String ProductName = "Pay Monthly bill";
            String CustomerMail = userOptional.get().getEmail();
            // String productDescription = "You're about to switch to a new subscription plan. Review your new plan details below before confirming your change. The new plan will take effect immediately, and any applicable charges will be processed accordingly";


            // Create a Stripe session with the provided price, currency, and timeout (30 minutes)
            long expirationTime = System.currentTimeMillis() / 1000 + (30 * 60); // 30 minutes in seconds

            // Create session params with correct payment method type
            SessionCreateParams sessionParams = SessionCreateParams.builder()
                    .addPaymentMethodType(PaymentMethodType.CARD)  // Use PaymentMethodType enum for CARD
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency)  // Set the currency as LKR
                                                    .setUnitAmount(priceInLKR * 100) // Amount in cents (multiply by 100)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(ProductName)
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:5173/jobprovider/plans-and-billing/payment/success?sessionId={CHECKOUT_SESSION_ID}&type=month")
                    .setCancelUrl("http://localhost:5173/jobprovider/plans-and-billing/payment/failed")
                    .setExpiresAt(expirationTime) // Set session expiration time (30 minutes)
                    .setCustomerEmail(CustomerMail)
                    .build();

            // Create the session
            Session session = Session.create(sessionParams);

            // Return the session ID to frontend
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());

            //save session to payement session
            PaymentSession ps = new PaymentSession();

            ps.setSessionId(session.getId());
            ps.setCreatedAt(LocalDateTime.now());
            ps.setMonthly(true);
            ps.setUser(userOptional.get());
            ps.setPrice(priceInLKR);

            paymentSessionReository.save(ps);

            return response;
        } catch (StripeException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creating session: " + e.getMessage());
            return errorResponse;
        }
    }

    @PostMapping("/verify-session-upgrade/{sessionId}")
    public boolean verifySessionUpgrade(@PathVariable("sessionId") String id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }
        Optional<PaymentSession> ps = paymentSessionReository.findByUserAndSessionId(userOptional.get(),id);

        if(ps.isPresent()){

            //upgrade pcg
            Optional<UserSubscription> userPlan = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(),"ACTIVE");
            if(userPlan.isEmpty()){
                throw new RuntimeException("user doesnt have plan");
            }



            UserSubscription savePlan = userPlan.get();

            //savePlan.setSubscriptionPlan(ps.get().getSubscriptionPlan());
            savePlan.setPaidStatus("success");
            savePlan.setStatus("EXPIRE");
            //savePlan.setStartDate(LocalDate.now());
            //savePlan.setEndDate(LocalDate.now().plusDays(30));

            LocalDate endDateOfThePrevPcg = savePlan.getEndDate();

            userSubscriptionRepository.save(savePlan);

            //create new plan
            UserSubscription newSub = new UserSubscription();

            newSub.setSubscriptionPlan(ps.get().getSubscriptionPlan());
            newSub.setPaidStatus("success");
            newSub.setStatus("ACTIVE");
            newSub.setStartDate(LocalDate.now());
            //newSub.setEndDate(LocalDate.now().plusDays(30));
            newSub.setUser(userOptional.get());

            //check upgrade before expire
            if(ps.get().getUpgrade()){ //true mean user upgrade before expire prev pcg
                newSub.setEndDate(endDateOfThePrevPcg);
            }else{ //new pcg upgrade get after expire
                newSub.setEndDate(LocalDate.now().plusDays(30));
            }

            userSubscriptionRepository.save(newSub);


            //bill
            Optional<SubscriptionPlan> newPlan = subscriptionPlanRepository.findById(ps.get().getSubscriptionPlan().getId());

            if(newPlan.isEmpty()){
                throw new RuntimeException("plan not exsist in db");
            }

            Optional<UserSubscription> newUserSub = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(),"ACTIVE");
            if(newUserSub.isEmpty()){
                throw new RuntimeException("user doesnt have plan");
            }

            BillingHistory bill = new BillingHistory();

            bill.setUser(userOptional.get());
            bill.setUserSubscription(newUserSub.get());
            bill.setAmountPaid(ps.get().getPrice());
            bill.setPaymentStatus("success");
            bill.setPaymentDate(LocalDateTime.now());

            billHistoryRepository.save(bill);

            paymentSessionReository.delete(ps.get());

            return true;

        }else{
            return false;
        }

    }

    @PostMapping("/verify-session-monthly/{sessionId}")
    public boolean verifySessionMonthly(@PathVariable("sessionId") String id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if(userOptional.isEmpty()){
            throw new RuntimeException("user not found");
        }
        Optional<PaymentSession> ps = paymentSessionReository.findByUserAndSessionId(userOptional.get(),id);

        if(ps.isPresent()){

            //upgrade pcg
            Optional<UserSubscription> userPlan = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(),"ACTIVE");
            if(userPlan.isEmpty()){
                throw new RuntimeException("user doesnt have plan");
            }



            UserSubscription savePlan = userPlan.get();

            //savePlan.setSubscriptionPlan(ps.get().getSubscriptionPlan());
            savePlan.setPaidStatus("success");
            //savePlan.setStatus("EXPIRE");
            savePlan.setStartDate(LocalDate.now());
            savePlan.setEndDate(LocalDate.now().plusDays(30));

            //LocalDate endDateOfThePrevPcg = savePlan.getEndDate();

            userSubscriptionRepository.save(savePlan);

            //bill

            BillingHistory bill = new BillingHistory();

            bill.setUser(userOptional.get());
            bill.setUserSubscription(userPlan.get());
            bill.setAmountPaid(ps.get().getPrice());
            bill.setPaymentStatus("success");
            bill.setPaymentDate(LocalDateTime.now());

            billHistoryRepository.save(bill);

            paymentSessionReository.delete(ps.get());

            return true;

        }else{
            return false;
        }

    }

    @GetMapping("/change-plan-basic")
    public boolean changePlanToBasic() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("user not found");
        }

        Optional<SubscriptionPlan> basic = subscriptionPlanRepository.findById(1L);

        if(basic.isEmpty()){
            throw new RuntimeException("basic plan not exsist in db");
        }


        //upgrade pcg
        Optional<UserSubscription> userPlan = userSubscriptionRepository.findByUser_IdAndStatus(userOptional.get().getId(), "ACTIVE");
        if (userPlan.isEmpty()) {
            return false;
           // throw new RuntimeException("user doesnt have plan");

        } else {

            UserSubscription savePlan = userPlan.get();

            //savePlan.setSubscriptionPlan(ps.get().getSubscriptionPlan());
            //savePlan.setPaidStatus("success");
            savePlan.setStatus("EXPIRE");
            //savePlan.setStartDate(LocalDate.now());
            //savePlan.setEndDate(LocalDate.now().plusDays(30));


            userSubscriptionRepository.save(savePlan);

            //create new plan
            UserSubscription newSub = new UserSubscription();

            newSub.setSubscriptionPlan(basic.get());
            newSub.setPaidStatus(null);
            newSub.setStatus("ACTIVE");
            newSub.setStartDate(LocalDate.now());
            newSub.setEndDate(null);
            newSub.setUser(userOptional.get());

            userSubscriptionRepository.save(newSub);


            return true;
        }


    }

    @GetMapping("/bill-info")
    public ResponseEntity<List<Map<String, Object>>> getUserBillDetail() {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Fetch billing history for the user
        List<BillingHistory> bills = billHistoryRepository.findByUser(userOptional.get());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a");
        List<Map<String, Object>> userBills = new ArrayList<>();


        for (BillingHistory bill : bills) {
            Map<String, Object> aBill = new HashMap<>();
            aBill.put("userId", bill.getUser().getId());
            aBill.put("planName", bill.getUserSubscription().getSubscriptionPlan().getPlanName());
            aBill.put("planPrice", bill.getAmountPaid());
            aBill.put("paymentDate", bill.getPaymentDate().format(formatter));
            aBill.put("billId", bill.getId());
            aBill.put("paymentStatus", bill.getPaymentStatus());

            userBills.add(aBill);
        }


        userBills.sort(Comparator.comparing(
                bill -> LocalDateTime.parse((String) bill.get("paymentDate"), formatter), Comparator.reverseOrder())
        );

        return new ResponseEntity<>(userBills, HttpStatus.OK);
    }


}
