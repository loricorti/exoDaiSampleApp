package com.example.exoplayerpoc.model

internal data class CatalogueListResponse<T>(
    val startIndex: Int = 0,
    val totalResults: Int = 0,
    val itemsPerPage: Int = 0,
    val entryCount: Int = 0,
    val title: String = "",
    val longTitle: String = "",
    val description: String = "",
    val items: List<T> = listOf(),
    val trackId: String = ""
)

internal data class MpxChannel(
    val callSign: String? = null,
    val events: List<MpxEvent>? = null,
    val title: String? = ""
)

internal data class MpxEvent(
    val id: String? = "",
    val listingId : String? = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val stationId: String? = "",
    val availabilityGeos: String? = "",
    val isRestart: Boolean? = false,
    val availabilityPlatform: List<String> = listOf(),
    val mediaPid: String? = "",
    val program: MpxListingProgram = MpxListingProgram(),
    val callSign: String = ""
)

internal data class MpxListingProgram(
    val id: String? = "",
    val guid: String = "",
    val title: String = "",
    val description: String? = "",
    val longDescription: String? = "",
    val pubDdate: String? = "",
    val longTitle: String? = "",
    val thumbnails: MPXThumbnails = MPXThumbnails(),
    val tvSeasonId: String? = "",
    val tvSeasonNumber: Int? = 0,
    val tvSeasonEpisodeNumber: Int? = 0,
    val secondaryTitle: String? = ""
) {
    fun toBaseDescription(): String {
        return if (!longDescription.isNullOrEmpty()) {
            longDescription
        } else if (!description.isNullOrEmpty()) {
            description
        } else {
            ""
        }
    }

    fun toTitle(): String {
        if (longTitle.isNullOrEmpty()) {
            return title
        }
        return longTitle
    }
}

internal data class MPXThumbnails(
    val default: MPXThumbnail = MPXThumbnail(),
    val logo: MPXThumbnail = MPXThumbnail(),
    val poster: MPXThumbnail = MPXThumbnail(),
    val titleCard: MPXThumbnail = MPXThumbnail(),
    val masterBoxart: MPXThumbnail = MPXThumbnail(),
    val showLogo: MPXThumbnail = MPXThumbnail()
)


internal data class MPXThumbnail(
    val url: String = "",
)

internal data class MpxStation(
    val id: String? = "",
    //@Json(name = "guid")
    val guid: String? = "",
    val title: String? = "",
    val description: String? = "",
    val callSign: String? = "",
    val isVirtual: Boolean = false,
    val isSigned: Boolean = false,
    val stationLogo: MpxStationThumbnails? = MpxStationThumbnails(),
    val colorCode: String? = "",
    val displayOrder: Int? = 0,
    val active: Boolean? = true,
    val googleSsaiKey: String? = ""
)

internal data class MpxStationThumbnails(
    val default: MPXStationThumbnail = MPXStationThumbnail(),
    val browse: MPXStationThumbnail = MPXStationThumbnail()
)


internal data class MPXStationThumbnail(
    val url: String = "",
)



