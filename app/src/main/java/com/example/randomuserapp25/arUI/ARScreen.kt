package com.example.randomuserapp25.arUI

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
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
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1) Kamera-Permission
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

    // 2) PreviewView + Größe tracken
    var previewSize by remember { mutableStateOf(IntSize.Zero) }
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FIT_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // 3) Erkanntes User-Overlay
    val detected by viewModel.detected.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                previewSize = coords.size
            }
    ) {
        // 4) Kamera-Preview
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // 5) Scan-Quadrat und Overlay
        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(detected?.user?.id ?: "") {
                    detectTapGestures { pt ->
                        // Tap nur innerhalb des Quadrats auslösen
                        val vw = previewSize.width.toFloat()
                        val vh = previewSize.height.toFloat()
                        if (vw == 0f || vh == 0f) return@detectTapGestures

                        val side = min(vw, vh) * 0.6f
                        val left = (vw - side) / 2f
                        val top = (vh - side) / 2f

                        if (pt.x in left..(left + side) && pt.y in top..(top + side)) {
                            detected?.let { onUserClick(it.user) }
                        }
                    }
                }
        ) {
            val vw = previewSize.width.toFloat()
            val vh = previewSize.height.toFloat()
            if (vw == 0f || vh == 0f) return@Canvas

            // Größe und Position des Scan-Quadrats
            val side = min(vw, vh) * 0.6f
            val left = (vw - side) / 2f
            val top = (vh - side) / 2f

            // 5.1 Scan-Rahmen
            drawRect(
                color = Color.White,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(side, side),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
            )

            // 5.2 User-Overlay **immer** im Quadrat zentriert
            detected?.let { du ->
                // halbtransparenter Hintergrund
                drawRect(
                    color = Color(0x44000000),
                    topLeft = Offset(left, top),
                    size = androidx.compose.ui.geometry.Size(side, side),
                    style = Fill
                )
                // Name in die Mitte des Quadrats
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        du.user.name,
                        left + side / 2f,
                        top + side / 2f + 20f,
                        Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = 60f
                            textAlign = Paint.Align.CENTER
                            isAntiAlias = true
                        }
                    )
                }
            }
        }
    }

    // 6) CameraX + ML Kit für QR-Scan
    LaunchedEffect(previewView) {
        val cameraProviderF = ProcessCameraProvider.getInstance(context)
        cameraProviderF.addListener({
            val cameraProvider = cameraProviderF.get()

            // Preview-UseCase
            val previewUseCase = Preview.Builder()
                .setTargetRotation(previewView.display.rotation)
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // QR-only Scanner
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            // Analysis-UseCase
            val analysisUseCase = ImageAnalysis.Builder()
                .setTargetRotation(previewView.display.rotation)
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { useCase ->
                    useCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { proxy ->
                        proxy.image?.let { mediaImage ->
                            val input = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)
                            Log.d("ArScreen", "Analyzing frame")
                            scanner.process(input)
                                .addOnSuccessListener { barcodes: List<Barcode> ->
                                    viewModel.onBarcodes(barcodes)
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
