package com.example.movieapplication.modules

import com.example.movieapplication.BuildConfig
import com.example.movieapplication.core.MovieApiService
import com.example.movieapplication.core.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieApi(): MovieApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URLS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    fun provideMovieRepository(api: MovieApiService): MovieRepository = MovieRepository(api)
}