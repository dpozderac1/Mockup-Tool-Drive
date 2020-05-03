package com.example.online_testing;

import com.example.grpc.server.grpcserver.ActionRequest;
import com.example.grpc.server.grpcserver.SystemEventsResponse;
import com.example.grpc.server.grpcserver.SystemEventsServiceGrpc;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GRPCOnlineTestingService {


    public String action(String microservice, String type, String resource, String responseType, Timestamp timestamp) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        /*final InstanceInfo instanceInfo = client.getNextServerFromEureka("system-events", false);
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), instanceInfo.getPort())
                .usePlaintext()
                .build();*/
        SystemEventsServiceGrpc.SystemEventsServiceBlockingStub stub
                = SystemEventsServiceGrpc.newBlockingStub(channel);
        SystemEventsResponse helloResponse = stub.action(ActionRequest.newBuilder()
                .setMicroservice(microservice)
                .setType(type)
                .setResource(resource)
                .setResponseType(responseType)
                .setTimestamp(timestamp.toString())
                .build());
        channel.shutdown();
        return helloResponse.getResponse();
    }
}


