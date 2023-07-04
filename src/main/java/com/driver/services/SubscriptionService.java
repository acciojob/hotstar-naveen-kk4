package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        int totalAmount = 0;
        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
            totalAmount= 500+(200*subscription.getNoOfScreensSubscribed());
        }
        else if(subscription.getSubscriptionType()==SubscriptionType.PRO){
            totalAmount= 800+(250*subscription.getNoOfScreensSubscribed());
        }
        else{
            totalAmount= 1000+(350*subscription.getNoOfScreensSubscribed());
        }
        subscription.setTotalAmountPaid(totalAmount);

        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);


        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();
        int totalScreens = subscription.getNoOfScreensSubscribed();
        int tempAmount = 0;

        if(subscription.getSubscriptionType()==SubscriptionType.ELITE)throw new Exception("Already the best Subscription");
        else if(subscription.getSubscriptionType()==SubscriptionType.PRO){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            tempAmount = (1000+(350*totalScreens))-subscription.getTotalAmountPaid();

        }
        else {
            subscription.setSubscriptionType(SubscriptionType.PRO);
            tempAmount =(800+(250*totalScreens))-subscription.getTotalAmountPaid();

        }
        subscriptionRepository.save(subscription);
        return tempAmount;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        Integer totalRevenue = 0;
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        for(Subscription subscription : subscriptionList){
            totalRevenue+=subscription.getTotalAmountPaid();
        }


        return totalRevenue;
    }

}
