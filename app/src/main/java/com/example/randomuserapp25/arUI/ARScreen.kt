package com.example.randomuserapp25.arUI

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.randomuserapp25.domain.User
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlin.math.min

@OptIn(ExperimentalGetImage::class)
@Composable
fun ArScreen(
    onUserClick: (User) -> Unit,
    viewModel: ArScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Camera permission
    var granted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted = it }
    LaunchedEffect(Unit) {
        if (!granted) launcher.launch(Manifest.permission.CAMERA)
    }
    if (!granted) return

    // PreviewView size
    var previewSize by remember { mutableStateOf(IntSize.Zero) }
    val previewView = remember { PreviewView(context) }

    // Detected user overlay
    val detected by viewModel.detected.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                previewSize = coords.size
            }
    ) {
        // CameraX preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay & tap handling
        detected?.let { du ->
            Canvas(
                Modifier
                    .fillMaxSize()
                    .pointerInput(du.user.id) {
                        detectTapGestures { pt ->
                            du.corners?.let { corners ->
                                if (isInsidePolygon(pt, corners)) {
                                    onUserClick(du.user)
                                }
                            }
                        }
                    }
            ) {
                val vw = previewSize.width.toFloat()
                val vh = previewSize.height.toFloat()
                if (vw == 0f || vh == 0f) return@Canvas

                // Letterboxing scale and offset
                val scale = min(vw / 1280f, vh / 720f)
                val dx = (vw - 1280f * scale) / 2f
                val dy = (vh - 720f * scale) / 2f

                // Map corners
                val mapped = du.corners!!.map { Offset(it.x * scale + dx, it.y * scale + dy) }

                // Draw overlay polygon
                drawPath(Path().apply {
                    moveTo(mapped[0].x, mapped[0].y)
                    lineTo(mapped[1].x, mapped[1].y)
                    lineTo(mapped[2].x, mapped[2].y)
                    lineTo(mapped[3].x, mapped[3].y)
                    close()
                }, color = Color(0x660000FF), style = Fill)

                // Draw user name
                drawContext.canvas.nativeCanvas.drawText(
                    du.user.name,
                    mapped[0].x,
                    mapped[0].y - 20f,
                    Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 48f
                        isAntiAlias = true
                    }
                )
            }
        }
    }

    // CameraX + ML Kit setup
    LaunchedEffect(previewView) {
        val providerFuture = ProcessCameraProvider.getInstance(context)
        providerFuture.addListener({
            val cameraProvider = providerFuture.get()

            // Preview use case
            val previewUseCase = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            // QR-only scanner
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            // Analysis use case
            val analysisUseCase = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { useCase ->
                    useCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { proxy ->
                        proxy.image?.let { mediaImage ->
                            val input = InputImage.fromMediaImage(
                                mediaImage,
                                proxy.imageInfo.rotationDegrees
                            )
                            Log.d("ArScreen", "Analyzing frame")
                            scanner.process(input)
                                .addOnSuccessListener { barcodes: List<Barcode> ->
                                    Log.d("ArScreen", "Found ${barcodes.size} barcodes")
                                    viewModel.onBarcodes(barcodes)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("ArScreen", "Scan failed", e)
                                }
                                .addOnCompleteListener { proxy.close() }
                        } ?: proxy.close()
                    }
                }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                analysisUseCase
            )
        }, ContextCompat.getMainExecutor(context))
    }
}

fun isInsidePolygon(pt: Offset, polygon: List<Offset>): Boolean {
    var inside = false
    polygon.forEachIndexed { i, p1 ->
        val p2 = polygon[(i + 1) % polygon.size]
        if (((p1.y > pt.y) != (p2.y > pt.y)) &&
            (pt.x < (p2.x - p1.x) * (pt.y - p1.y) / (p2.y - p1.y) + p1.x)
        ) {
            inside = !inside
        }
    }
    return inside
}
