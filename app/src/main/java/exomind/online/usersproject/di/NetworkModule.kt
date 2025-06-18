package exomind.online.usersproject.di

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo.cache.normalized.normalizedCache
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import exomind.online.usersproject.BuildConfig
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApolloClient(@ApplicationContext context: Context): ApolloClient {
        val cache = MemoryCacheFactory(10 * 1024 * 1024)
            .chain(SqlNormalizedCacheFactory(context))
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .dispatcher(Dispatchers.IO)
            .normalizedCache(cache)
            .addHttpHeader("Authorization", "Bearer 67f940014d4a07794c71972621fe85e6f25dd5b1de3d9ae4bc6e0253e38ca98c")
            .build()
    }
}