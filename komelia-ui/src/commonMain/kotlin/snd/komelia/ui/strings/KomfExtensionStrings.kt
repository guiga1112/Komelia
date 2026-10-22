package snd.komelia.ui.strings

import androidx.compose.runtime.Composable
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.Res
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.series_update_volume_covers
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_applying
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_downloading
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_failed
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_follow_failed
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_starting
import io.github.snd_r.komelia.ui.komelia_ui.generated.resources.volume_cover_update_success
import org.jetbrains.compose.resources.stringResource

object KomfExtensionStrings {
    @Composable
    fun updateVolumeCoversTitle() = stringResource(Res.string.series_update_volume_covers)

    @Composable
    fun updateVolumeCoversStarting() = stringResource(Res.string.volume_cover_update_starting)

    @Composable
    fun updateVolumeCoversDownloading() = stringResource(Res.string.volume_cover_update_downloading)

    @Composable
    fun updateVolumeCoversApplying() = stringResource(Res.string.volume_cover_update_applying)

    @Composable
    fun updateVolumeCoversFailed() = stringResource(Res.string.volume_cover_update_failed)

    @Composable
    fun updateVolumeCoversFollowFailed() = stringResource(Res.string.volume_cover_update_follow_failed)

    @Composable
    fun updateVolumeCoversSuccess() = stringResource(Res.string.volume_cover_update_success)
}
