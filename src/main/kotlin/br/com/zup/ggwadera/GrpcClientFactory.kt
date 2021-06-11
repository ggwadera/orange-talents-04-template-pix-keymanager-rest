package br.com.zup.ggwadera

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
@Suppress("unused")
class GrpcClientFactory(@GrpcChannel("keymanager") private val channel: ManagedChannel) {

    @Singleton
    fun register(): RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub =
        RegisterKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun delete(): DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub = DeleteKeyServiceGrpc.newBlockingStub(channel)

}