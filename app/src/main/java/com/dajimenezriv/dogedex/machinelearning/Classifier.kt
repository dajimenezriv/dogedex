package com.dajimenezriv.dogedex.machinelearning

import android.graphics.Bitmap
import com.dajimenezriv.dogedex.MAX_RECOGNITION_DOG_RESULTS
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.DequantizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.MappedByteBuffer
import java.util.*
import javax.inject.Inject

// the inputs are out trained model and the labels
class Classifier @Inject constructor(tfLiteModel: MappedByteBuffer, private val labels: List<String>) {
    /**
     * Image size along the x axis.
     */
    private val imageSizeX: Int

    /**
     * Image size along the y axis.
     */
    private val imageSizeY: Int

    /** Optional GPU delegate for acceleration.  */
    /**
     * An instance of the driver class to run model inference with Tensorflow Lite.
     */
    private var tfLite: Interpreter

    /**
     * Input image TensorBuffer.
     */
    private var inputImageBuffer: TensorImage

    /**
     * Output probability TensorBuffer.
     */
    private val outputProbabilityBuffer: TensorBuffer

    /**
     * Processor to apply post processing of the output probability.
     */
    private val tensorProcessor: TensorProcessor

    init {
        val tfLiteOptions = Interpreter.Options()
        // tfLiteOptions.setNumThreads(5)
        // if we remove this, android will as many threads as it needs
        tfLite = Interpreter(tfLiteModel, tfLiteOptions)

        // reads type and shape of input and output tensors, respectively
        val imageTensorIndex = 0
        val imageShape = tfLite.getInputTensor(imageTensorIndex).shape() // [1 224 224 3]
        imageSizeY = imageShape[1]
        imageSizeX = imageShape[2]
        val imageDataType = tfLite.getInputTensor(imageTensorIndex).dataType() // uINT8
        val probabilityTensorIndex = 0
        // we have 117 classes (one for each dog)
        val probabilityShape =
            tfLite.getOutputTensor(probabilityTensorIndex).shape() // {1, NUM_CLASSES}
        val probabilityDataType = tfLite.getOutputTensor(probabilityTensorIndex).dataType()

        // creates the input tensor
        inputImageBuffer = TensorImage(imageDataType)

        // creates the output tensor and its processor
        outputProbabilityBuffer = TensorBuffer.createFixedSize(
            probabilityShape,
            probabilityDataType
        )

        // creates the post processor for the output probability
        tensorProcessor = TensorProcessor.Builder().add(DequantizeOp(0f, 1 / 255.0f)).build()
    }

    /**
     * Runs inference and returns the classification results.
     */
    fun recognizeImage(bitmap: Bitmap): List<DogRecognition> {
        inputImageBuffer = loadImage(bitmap)
        // with rewind we make sure that the buffer doesn't get overload
        val rewoundOutputBuffer = outputProbabilityBuffer.buffer.rewind()
        tfLite.run(inputImageBuffer.buffer, rewoundOutputBuffer)
        // gets the map of label and probability
        val labeledProbability =
            TensorLabel(labels, tensorProcessor.process(outputProbabilityBuffer)).mapWithFloatValue

        // gets top-k results
        return getTopKProbability(labeledProbability)
    }

    /**
     * Loads input image, and applies pre processing.
     */
    private fun loadImage(bitmap: Bitmap): TensorImage {
        // loads bitmap into a TensorImage
        inputImageBuffer.load(bitmap)

        // creates processor for the TensorImage
        // everytime we load an image we change its size to match with the model input image
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .build()
        return imageProcessor.process(inputImageBuffer)
    }

    /**
     * Gets the top-k results.
     */
    private fun getTopKProbability(labelProb: Map<String, Float>): List<DogRecognition> {
        // find the best classifications
        // sort the values according to the comparison of confidences
        val priorityQueue =
            PriorityQueue(MAX_RECOGNITION_DOG_RESULTS) { lhs: DogRecognition, rhs: DogRecognition ->
                (rhs.confidence).compareTo(lhs.confidence)
            }

        for ((key, value) in labelProb) {
            priorityQueue.add(DogRecognition(key, value * 100.0f))
        }

        val recognitions = mutableListOf<DogRecognition>()
        val recognitionsSize = minOf(priorityQueue.size, MAX_RECOGNITION_DOG_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(priorityQueue.poll()!!)
        }

        return recognitions
    }
}