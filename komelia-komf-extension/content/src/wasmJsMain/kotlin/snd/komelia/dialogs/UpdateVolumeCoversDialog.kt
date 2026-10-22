package snd.komelia.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collect
import snd.komelia.AppNotification
import snd.komelia.LocalKomfViewModelFactory
import snd.komelia.ui.dialogs.AppDialog
import snd.komelia.ui.dialogs.DialogSimpleHeader
import snd.komelia.ui.strings.KomfExtensionStrings
import snd.komf.api.KomfServerLibraryId
import snd.komf.api.KomfServerSeriesId
import snd.komf.api.MediaServer
import snd.komf.api.job.KomfMetadataJobEvent
import snd.komf.api.job.KomfMetadataJobStatus

@Composable
fun UpdateVolumeCoversDialog(
    mediaServer: MediaServer,
    seriesId: KomfServerSeriesId,
    libraryId: KomfServerLibraryId,
    onDismissRequest: () -> Unit,
) {
    val factory = LocalKomfViewModelFactory.current
    val startingText = KomfExtensionStrings.updateVolumeCoversStarting()
    val downloadingText = KomfExtensionStrings.updateVolumeCoversDownloading()
    val applyingText = KomfExtensionStrings.updateVolumeCoversApplying()
    val failedText = KomfExtensionStrings.updateVolumeCoversFailed()
    val followFailedText = KomfExtensionStrings.updateVolumeCoversFollowFailed()
    val successText = KomfExtensionStrings.updateVolumeCoversSuccess()
    val title = KomfExtensionStrings.updateVolumeCoversTitle()
    var statusText by remember { mutableStateOf(startingText) }

    LaunchedEffect(mediaServer, seriesId, libraryId) {
        factory.appNotifications.runCatchingToNotifications {
            val response = factory.komfClientFactory.metadataClient(mediaServer)
                .updateVolumeCovers(libraryId, seriesId)
            var failureMessage: String? = null
            var eventStreamMissing = false

            factory.komfClientFactory.jobClient().getJobEvents(response.jobId).collect { event ->
                when (event) {
                    is KomfMetadataJobEvent.ProviderSeriesEvent -> statusText = downloadingText
                    is KomfMetadataJobEvent.ProviderCompletedEvent -> statusText = applyingText
                    is KomfMetadataJobEvent.ProviderErrorEvent -> failureMessage = event.message
                    is KomfMetadataJobEvent.ProcessingErrorEvent -> failureMessage = event.message
                    KomfMetadataJobEvent.PostProcessingStartEvent -> statusText = applyingText
                    KomfMetadataJobEvent.NotFound -> eventStreamMissing = true
                    else -> Unit
                }
            }

            if (eventStreamMissing) {
                val job = factory.komfClientFactory.jobClient().getJob(response.jobId)
                when (job.status) {
                    KomfMetadataJobStatus.COMPLETED -> Unit
                    KomfMetadataJobStatus.FAILED -> failureMessage = job.message ?: failedText
                    KomfMetadataJobStatus.RUNNING -> error(followFailedText)
                }
            }
            failureMessage?.let { error(it) }

            factory.appNotifications.add(AppNotification.Success(successText))
        }
        onDismissRequest()
    }

    AppDialog(
        header = { DialogSimpleHeader(title) },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CircularProgressIndicator()
                Text(statusText)
            }
        },
        controlButtons = {},
        onDismissRequest = {},
        contentPadding = PaddingValues(20.dp),
    )
}
