package com.dajimenezriv.dogedex.di

import android.content.Context
import com.dajimenezriv.dogedex.LABELS_PATH
import com.dajimenezriv.dogedex.MODEL_PATH
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.tensorflow.lite.support.common.FileUtil
import java.nio.MappedByteBuffer

@Module
@InstallIn(SingletonComponent::class)
object ClassifierConstructorModule {

    @Provides
    // @ApplicationContext provides us a context
    fun provideClassifierModel(@ApplicationContext context: Context): MappedByteBuffer =
        FileUtil.loadMappedFile(context, MODEL_PATH)

    @Provides
    fun provideClassifierLabels(@ApplicationContext context: Context): List<String> =
        FileUtil.loadLabels(context, LABELS_PATH)
}