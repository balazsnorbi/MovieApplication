package com.norbert.balazs.movies.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.norbert.balazs.movies.data.local.MovieDatabase
import com.norbert.balazs.movies.data.remote.MovieDbApi
import com.norbert.balazs.movies.data.repository.LocalMovieRepositoryImpl
import com.norbert.balazs.movies.data.repository.MovieRepositoryImpl
import com.norbert.balazs.movies.domain.repository.LocalMovieRepository
import com.norbert.balazs.movies.domain.repository.MovieRepository
import com.norbert.balazs.movies.common.*
import com.norbert.balazs.movies.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    private const val READ_TIMEOUT = 30
    private const val WRITE_TIMEOUT = 30
    private const val CONNECTION_TIMEOUT = 10
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

    @Provides
    @Singleton
    @Named("HeaderInterceptor")
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            it.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    @Named("HttpCache")
    internal fun provideCache(
        @ApplicationContext context: Context
    ): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    @Named("OkHttpClient")
    fun provideOkHttpClient(
        @Named("HeaderInterceptor") headerInterceptor: Interceptor,
        @Named("HttpCache") cache: Cache
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .apply {
                connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
                readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                cache(cache)
                addInterceptor(headerInterceptor)
            }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    @Named("MovieDbRetrofit")
    fun provideMovieDbRetrofit(
        @Named("OkHttpClient") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_MOVIE_DB_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("ImageRetrofit")
    fun provideImageRetrofit(
        @Named("OkHttpClient") client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_IMAGE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("MovieDbApi")
    fun provideMovieDbApi(
        @Named("MovieDbRetrofit") retrofit: Retrofit
    ): MovieDbApi {
        return retrofit.create(MovieDbApi::class.java)
    }

    @Provides
    @Singleton
    @Named("MovieRepository")
    fun provideMovieRepository(
        @Named("MovieDbApi") movieDbApi: MovieDbApi
    ): MovieRepository {
        return MovieRepositoryImpl(movieDbApi)
    }

    @Provides
    @Singleton
    @Named("LocalMovieRepository")
    fun provideLocalMovieRepository(
        @Named("MovieDatabase") movieDatabase: MovieDatabase
    ): LocalMovieRepository {
        return LocalMovieRepositoryImpl(movieDatabase)
    }

    @Provides
    @Singleton
    @Named("GetRecommendationUseCase")
    fun provideGetRecommendationUseCase(
        @Named("MovieRepository") movieRepository: MovieRepository,
        @Named("LocalMovieRepository") localMovieRepository: LocalMovieRepository
    ): GetRecommendationsUseCase {
        return GetRecommendationsUseCase(movieRepository, localMovieRepository)
    }

    @Provides
    @Singleton
    @Named("SearchMovieUseCase")
    fun provideSearchMovieUseCase(
        @Named("MovieRepository") movieRepository: MovieRepository,
        @Named("LocalMovieRepository") localMovieRepository: LocalMovieRepository
    ): SearchMovieUseCase {
        return SearchMovieUseCase(movieRepository, localMovieRepository)
    }

    @Provides
    @Singleton
    @Named("GetMovieDetailsUseCase")
    fun provideGetMovieDetailsUseCase(
        @Named("MovieRepository") movieRepository: MovieRepository,
        @Named("LocalMovieRepository") localMovieRepository: LocalMovieRepository
    ): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(movieRepository, localMovieRepository)
    }

    @Provides
    @Singleton
    @Named("PreferencesDataStore")
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile(PREFERENCES_STORE_NAME)
            }
        )
    }

    @Provides
    @Singleton
    @Named("PreferencesManager")
    fun providePreferencesManager(
        @Named("PreferencesDataStore") datastore: DataStore<Preferences>
    ): PreferencesManager {
        return PreferencesManager(datastore)
    }

    @Provides
    @Singleton
    @Named("MovieDatabase")
    fun provideMovieDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        MovieDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    @Named("UpdateFavoriteStateUseCase")
    fun provideUpdateFavoriteStateUseCase(
        @Named("LocalMovieRepository") localMovieRepository: LocalMovieRepository
    ): UpdateFavoriteStateUseCase {
        return UpdateFavoriteStateUseCase(localMovieRepository)
    }

    @Provides
    @Singleton
    @Named("GetFavoritesMoviesUseCase")
    fun provideGetFavoritesMoviesUseCase(
        @Named("LocalMovieRepository") localMovieRepository: LocalMovieRepository
    ): GetFavoritesMoviesUseCase {
        return GetFavoritesMoviesUseCase(localMovieRepository)
    }
}