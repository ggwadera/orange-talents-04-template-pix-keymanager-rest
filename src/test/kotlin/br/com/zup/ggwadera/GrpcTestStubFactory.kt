package br.com.zup.ggwadera

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import org.mockito.Mockito
import javax.inject.Singleton

@Factory
@Replaces(factory = GrpcClientFactory::class)
@Suppress("unused")
class GrpcTestStubFactory {

    @Singleton
    fun stubRegisterService(): RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub =
        Mockito.mock(RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub::class.java)

    @Singleton
    fun stubDeleteService(): DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub =
        Mockito.mock(DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub::class.java)

    @Singleton
    fun stubFindService(): FindKeyServiceGrpc.FindKeyServiceBlockingStub =
        Mockito.mock(FindKeyServiceGrpc.FindKeyServiceBlockingStub::class.java)

}