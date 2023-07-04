package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

            WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
            if(!Objects.isNull(webSeries))throw new Exception("Series is already present");
       WebSeries webSeries1 = new WebSeries();
       webSeries1.setSeriesName(webSeriesEntryDto.getSeriesName());
       webSeries1.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
       webSeries1.setAgeLimit(webSeriesEntryDto.getAgeLimit());
       webSeries1.setRating(webSeriesEntryDto.getRating());
       ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
       webSeries1.setProductionHouse(productionHouse);
       List<WebSeries> webSeriesList = productionHouse.getWebSeriesList();
      double rating = productionHouse.getRatings();
      double totalRatings = 1.0*rating*webSeriesList.size();
      totalRatings+=webSeries1.getRating();
      webSeriesList.add(webSeries1);
      productionHouse.setRatings(1.0*totalRatings/webSeriesList.size());
      productionHouseRepository.save(productionHouse);
      return 123;
    }

}
