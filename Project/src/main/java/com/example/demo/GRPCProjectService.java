package com.example.demo;

import com.example.grpc.server.grpcserver.ActionRequest;
import com.example.grpc.server.grpcserver.SystemEventsResponse;
import com.example.grpc.server.grpcserver.SystemEventsServiceGrpc;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GRPCProjectService {
    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;

    public String action(String microservice, String type, String resource, String responseType, Timestamp timestamp) {
        final InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("system-events", false);
        int port = Integer.valueOf(instanceInfo.getMetadata().get("gRPC.port"));
        ManagedChannel channel = ManagedChannelBuilder.forAddress(instanceInfo.getIPAddr(), port)
                .usePlaintext()
                .build();
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



