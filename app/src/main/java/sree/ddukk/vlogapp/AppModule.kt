package sree.ddukk.vlogapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VlogDatabase =
        Room.databaseBuilder(
            context,
            VlogDatabase::class.java,
            "vlog_db"
        ).build()

    @Provides
    fun provideVlogDao(database: VlogDatabase): VlogDao = database.vlogDao()

    @Provides
    @Singleton
    fun provideRepository(dao: VlogDao): VlogRepository = VlogRepository(dao)
}
