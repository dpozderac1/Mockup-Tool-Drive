// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/SystemEvents.proto

package com.example.grpc.server.grpcserver;

public final class SystemEvents {
  private SystemEvents() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_grpc_server_grpcserver_ActionRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_grpc_server_grpcserver_ActionRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_example_grpc_server_grpcserver_SystemEventsResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_example_grpc_server_grpcserver_SystemEventsResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\030proto/SystemEvents.proto\022\"com.example." +
      "grpc.server.grpcserver\"n\n\rActionRequest\022" +
      "\024\n\014microservice\030\001 \001(\t\022\014\n\004type\030\002 \001(\t\022\020\n\010r" +
      "esource\030\003 \001(\t\022\024\n\014responseType\030\004 \001(\t\022\021\n\tt" +
      "imestamp\030\005 \001(\t\"(\n\024SystemEventsResponse\022\020" +
      "\n\010response\030\001 \001(\t2\214\001\n\023SystemEventsService" +
      "\022u\n\006action\0221.com.example.grpc.server.grp" +
      "cserver.ActionRequest\0328.com.example.grpc" +
      ".server.grpcserver.SystemEventsResponseB" +
      "\002P\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_com_example_grpc_server_grpcserver_ActionRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_example_grpc_server_grpcserver_ActionRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_grpc_server_grpcserver_ActionRequest_descriptor,
        new java.lang.String[] { "Microservice", "Type", "Resource", "ResponseType", "Timestamp", });
    internal_static_com_example_grpc_server_grpcserver_SystemEventsResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_example_grpc_server_grpcserver_SystemEventsResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_example_grpc_server_grpcserver_SystemEventsResponse_descriptor,
        new java.lang.String[] { "Response", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
