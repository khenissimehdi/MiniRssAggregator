package com.example.rss_aggregator;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
    @KafkaListener(topics = "rss")
        // TODO - si on a des instances de la même application, ils peuvent lire sur la même partition ou
        //  le même topic, on peut ajouter un groupId en arg ?
    void listener(String data){
        System.out.println("Listener received "+data+ "it has been consumed!!!");
        // TODO - dernière partie de la vidéo est l'API rest pour publier des messages
        //  dans une queue et envoyer des requests (POST,GET, etc... au topic)
    }

    // TODO - problème pas une seule mention de BDD dans la vidéo, comment intégrer l'API pour via kafka
    //  interroger la BDD Cassandra et que le topic avec les scrapper envoien des données en continu à Cassandra?
}
