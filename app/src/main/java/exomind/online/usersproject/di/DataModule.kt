package exomind.online.usersproject.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exomind.online.usersproject.data.UsersRepositoryImpl
import exomind.online.usersproject.domain.UsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun provideCharactersRepository(
        impl: UsersRepositoryImpl,
    ): UsersRepository

}