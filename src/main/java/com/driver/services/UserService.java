package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        userRepository.save(user);
        return 0;
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription() ;
        Integer count = 0;
        for(WebSeries webSeries : webSeriesList){
            if(user.getAge()>webSeries.getAgeLimit()){
                if(subscription.getSubscriptionType()==SubscriptionType.ELITE)count++;
                else if(subscription.getSubscriptionType()==SubscriptionType.PRO &&
                        (webSeries.getSubscriptionType()==SubscriptionType.PRO || webSeries.getSubscriptionType()==SubscriptionType.BASIC))count++;
                else if(subscription.getSubscriptionType()==SubscriptionType.BASIC && webSeries.getSubscriptionType()==SubscriptionType.BASIC)count++;

            }


        }
        return count;
    }


}
