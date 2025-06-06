package exomind.online.usersproject.di

import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exomind.online.usersproject.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .addHttpHeader("Authorization", "Bearer 67f940014d4a07794c71972621fe85e6f25dd5b1de3d9ae4bc6e0253e38ca98c")
            .build()
    }
}