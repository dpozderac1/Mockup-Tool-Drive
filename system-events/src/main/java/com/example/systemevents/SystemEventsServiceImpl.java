package com.example.systemevents;

//import com.example.grpc.server.grpcserver.*;
import com.example.grpc.server.grpcserver.ActionRequest;
import com.example.grpc.server.grpcserver.SystemEventsResponse;
import com.example.grpc.server.grpcserver.SystemEventsServiceGrpc;
import com.example.systemevents.Repositories.ActionRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

@GrpcService
public class SystemEventsServiceImpl extends SystemEventsServiceGrpc.SystemEventsServiceImplBase {
    @Autowired
    private ActionRepository actionRepository;

    @Override
    public void action(
            ActionRequest request, StreamObserver<SystemEventsResponse> responseObserver) {
        System.out.println("Zahtjev je: ");
        System.out.println(request.getMicroservice());

        Action akcija=new Action(request.getMicroservice(),request.getType(),request.getResource(),request.getResponseType(), Timestamp.valueOf(request.getTimestamp()));
        actionRepository.save(akcija);
        String odgovor = new StringBuilder()
                .append("SUCCESS")
                .toString();
        SystemEventsResponse response = SystemEventsResponse.newBuilder()
                .setResponse(odgovor)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
