/*
 * Copyright 2018 Priyank Vasa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.priyankvasa.android.cameraviewex

import android.content.Context
import android.media.Image
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This is a sample setup method to demonstrate appropriate and safe usage of [CameraView.setPreviewFrameListener]
 */
@ExperimentalCoroutinesApi
@Suppress("unused", "UNUSED_ANONYMOUS_PARAMETER", "UNUSED_VARIABLE")
internal fun setupCameraSample(context: Context) {

    CameraView(context).apply {

        val processing = AtomicBoolean(false)

        addCameraOpenedListener { Timber.i("Camera opened.") }

        setPreviewFrameListener { image: Image ->

            if (processing.compareAndSet(false, true)) {

                val result = GlobalScope.async { /* Some background image processing task */ }

                result.invokeOnCompletion { t ->
                    val output = result.getCompleted()
                    /* ...  use the output ... */
                    // Set processing flag to false
                    processing.set(false)
                }
            }
        }

        addPictureTakenListener { imageData: ByteArray -> Timber.i("Picture taken successfully.") }

        addCameraClosedListener { Timber.i("Camera closed.") }
    }
}