@file:Suppress("UNCHECKED_CAST")
package com.example.exoplayerpoc.utils

import com.example.exoplayerpoc.model.CatalogueListResponse
import com.example.exoplayerpoc.model.MPXStationThumbnail
import com.example.exoplayerpoc.model.MPXThumbnail
import com.example.exoplayerpoc.model.MPXThumbnails
import com.example.exoplayerpoc.model.MpxChannel
import com.example.exoplayerpoc.model.MpxEvent
import com.example.exoplayerpoc.model.MpxListingProgram
import com.example.exoplayerpoc.model.MpxStation
import com.example.exoplayerpoc.model.MpxStationThumbnails
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun <T> decodeFromString(string: String, key: String): T {

    val content = Gson().fromJson(string, JsonObject::class.java)

    return when (key) {

        /*"MpxWatchLive" -> {
            content.toMpxWatchLive() as T
        }*/

        "CatalogueListResponse<MpxChannel>" -> {
            content.toCatalogueListResponseMpxChannel() as T
        }

        "CatalogueListResponse<MpxStation>" -> {
            content.toCatalogueListResponseMpxStation() as T
        }

        "CatalogueListResponse<MpxEvent>" -> {
            content.toCatalogueListResponseMpxEvent() as T
        }

        else -> {
            throw NotImplementedError("Key not found")
        }
    }
}

private fun JsonElement.toLabels(): Map<String, String> {
    return if (this.isJsonNull) {
        mapOf()
    } else {
        val content = this.asJsonObject
        content.entrySet().map { it.key to it.value.asString }.toList().associate { it.first to it.second }
    }
}

private fun JsonElement.toScreenSetParams(): Map<String, String> {
    return if (this.isJsonNull) {
        mapOf()
    } else {
        val content = this.asJsonObject
        content.entrySet().map { it.key to it.value.asString }.toList()
            .associate { it.first to it.second }
    }
}

private fun JsonElement.toLoggedFeeds(): List<String> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map { it.asStringOrNull() ?: "" }
}

private fun JsonElement.toValidCountries(): List<String> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map { it.asStringOrNull() ?: "" }
}

private fun JsonElement.toListOfStrings(): List<String> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map { it.asStringOrNull() ?: "" }
}

private fun JsonElement.toCatalogueListResponseMpxChannel(): CatalogueListResponse<MpxChannel> {
    return if (this.isJsonNull) {
        CatalogueListResponse()
    } else {
        val content = this.asJsonObject
        CatalogueListResponse(
            startIndex = (content["startIndex"]?.asStringOrNull() ?: "0").toInt(),
            totalResults = (content["totalResults"]?.asStringOrNull() ?: "0").toInt(),
            itemsPerPage = (content["itemsPerPage"]?.asStringOrNull() ?: "0").toInt(),
            entryCount = (content["entryCount"]?.asStringOrNull() ?: "0").toInt(),
            title = content["title"]?.asStringOrNull() ?: "",
            longTitle = content["longTitle"]?.asStringOrNull() ?: "",
            description = content["description"]?.asStringOrNull() ?: "",
            items = content["entries"]?.toMpxChannels() ?: listOf(),
            trackId = content["trackId"]?.asStringOrNull() ?: ""
        )
    }
}

private fun JsonElement.toMpxChannels(): List<MpxChannel> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map { it.toMpxChannel() }
}

private fun JsonElement.toCatalogueListResponseMpxStation(): CatalogueListResponse<MpxStation> {
    return if (this.isJsonNull) {
        CatalogueListResponse()
    } else {
        val content = this.asJsonObject
        CatalogueListResponse(
            startIndex = (content["startIndex"]?.asStringOrNull() ?: "0").toInt(),
            totalResults = (content["totalResults"]?.asStringOrNull() ?: "0").toInt(),
            itemsPerPage = (content["itemsPerPage"]?.asStringOrNull() ?: "0").toInt(),
            entryCount = (content["entryCount"]?.asStringOrNull() ?: "0").toInt(),
            title = content["title"]?.asStringOrNull() ?: "",
            longTitle = content["longTitle"]?.asStringOrNull() ?: "",
            description = content["description"]?.asStringOrNull() ?: "",
            items = content["entries"]?.toMpxStationList() ?: listOf(),
            trackId = content["trackId"]?.asStringOrNull() ?: ""
        )
    }
}

private fun JsonElement.toCatalogueListResponseMpxEvent(): CatalogueListResponse<MpxEvent> {
    return if (this.isJsonNull) {
        CatalogueListResponse()
    } else {
        val content = this.asJsonObject
        CatalogueListResponse(
            startIndex = (content["startIndex"]?.asStringOrNull() ?: "0").toInt(),
            totalResults = (content["totalResults"]?.asStringOrNull() ?: "0").toInt(),
            itemsPerPage = (content["itemsPerPage"]?.asStringOrNull() ?: "0").toInt(),
            entryCount = (content["entryCount"]?.asStringOrNull() ?: "0").toInt(),
            title = content["title"]?.asStringOrNull() ?: "",
            longTitle = content["longTitle"]?.asStringOrNull() ?: "",
            description = content["description"]?.asStringOrNull() ?: "",
            items = content["entries"]?.toMpxEvents() ?: listOf(),
            trackId = content["trackId"]?.asStringOrNull() ?: ""
        )
    }
}

private fun JsonElement.toMpxStationList(): List<MpxStation> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map {
        it.toMpxStation()
    }
}

private fun JsonElement.toMpxStation(): MpxStation {
    return if (this.isJsonNull) {
        MpxStation()
    } else {
        val content = this.asJsonObject
        MpxStation(
            id = content["id"]?.asStringOrNull() ?: "",
            guid = content["guid"]?.asStringOrNull() ?: "",
            title = content["title"]?.asStringOrNull() ?: "",
            description = content["description"]?.asStringOrNull() ?: "",
            callSign = content["plstation\$callSign"]?.asStringOrNull() ?: "",
            isVirtual = content["plstation\$isVirtual"]?.asStringOrNull()?.lowercase() == "true",
            isSigned = content["plstation\$isSigned"]?.asStringOrNull()?.lowercase() == "true",
            stationLogo = content["plstation\$thumbnails"]?.toMpxStationThumbnails() ?: MpxStationThumbnails(),
            colorCode = content["rtestation\$backColourCode"]?.asStringOrNull() ?: "",
            displayOrder = (content["rtestation\$displayOrder"]?.asStringOrNull() ?: "1").toInt(),
            active = content["rtestation\$active"]?.asStringOrNull()?.lowercase() == "true",
            googleSsaiKey = content["rte\$google-ssai-dash"]?.asStringOrNull() ?: "",
        )
    }
}

private fun JsonElement.toMpxStationThumbnails(): MpxStationThumbnails {
    return if (this.isJsonNull) {
        MpxStationThumbnails()
    } else {
        val content = this.asJsonObject
        MpxStationThumbnails(
            default = content["default"]?.toMpxStationThumbnail() ?: MPXStationThumbnail(),
            browse = content["browse"]?.toMpxStationThumbnail() ?: MPXStationThumbnail(),
        )
    }
}

private fun JsonElement.toMpxStationThumbnail(): MPXStationThumbnail {
    return if (this.isJsonNull) {
        MPXStationThumbnail()
    } else {
        val content = this.asJsonObject
        MPXStationThumbnail(
            url = content["plstation\$url"]?.asStringOrNull() ?: "",
        )
    }
}

private fun JsonElement.toMpxChannel(): MpxChannel {
    return if (this.isJsonNull) {
        MpxChannel()
    } else {
        val content = this.asJsonObject
        MpxChannel(
            callSign = content["plchannelschedule\$callSign"]?.asStringOrNull() ?: "",
            events = content["plchannelschedule\$listings"]?.toMpxEvents() ?: listOf(),
            title = content["title"]?.asStringOrNull() ?: ""
        )
    }
}

private fun JsonElement.toMpxEvents(): List<MpxEvent> {
    if (!this.isJsonArray) {
        return listOf()
    }
    val content = this.asJsonArray
    return content.map { it.toMpxEvent() }
}

private fun JsonElement.toMpxEvent(): MpxEvent {
    return if (this.isJsonNull) {
        MpxEvent()
    } else {
        val content = this.asJsonObject
        MpxEvent(
            id = content["id"]?.asStringOrNull() ?: "",
            listingId = content["pl\$id"]?.asStringOrNull() ?: "",
            startTime = (content["pllisting\$startTime"]?.asStringOrNull() ?: "0").toLong(),
            endTime = (content["pllisting\$endTime"]?.asStringOrNull() ?: "0").toLong(),
            stationId = content["pllisting\$stationId"]?.asStringOrNull() ?: "",
            availabilityGeos = content["rtelisting\$availabilityGeos"]?.asStringOrNull() ?: "",
            isRestart = content["rtelisting\$isRestart"]?.asStringOrNull()?.lowercase() == "true",
            availabilityPlatform = content["rtelisting\$availabilityPlatform"]?.toListOfStrings() ?: listOf(),
            mediaPid = content["rtelisting\$mediaPid"]?.asStringOrNull() ?: "",
            program = content["pllisting\$program"]?.toMpxListingProgram() ?: MpxListingProgram()
        )
    }
}

private fun JsonElement.toMpxListingProgram(): MpxListingProgram {
    return if (this.isJsonNull) {
        MpxListingProgram()
    } else {
        val content = this.asJsonObject
        MpxListingProgram(
            id = content["pl\$id"]?.asStringOrNull() ?: "",
            guid = content["pl\$guid"]?.asStringOrNull() ?: "",
            title = content["pl\$title"]?.asStringOrNull() ?: "",
            description = content["pl\$description"]?.asStringOrNull() ?: "",
            longDescription = content["plprogram\$longDescription"]?.asStringOrNull() ?: "",
            pubDdate = content["plprogram\$pubDate"]?.asStringOrNull() ?: "",
            longTitle = content["plprogram\$longTitle"]?.asStringOrNull() ?: "",
            thumbnails = content["plprogram\$thumbnails"]?.toMpxThumbnails() ?: MPXThumbnails(),
            tvSeasonId = content["plprogram\$tvSeasonId"]?.asStringOrNull() ?: "",
            tvSeasonNumber = (content["plprogram\$tvSeasonNumber"]?.asStringOrNull() ?: "0").toInt(),
            tvSeasonEpisodeNumber = (content["plprogram\$tvSeasonEpisodeNumber"]?.asStringOrNull() ?: "0").toInt(),
            secondaryTitle = content["plprogram\$secondaryTitle"]?.asStringOrNull() ?: "",
        )
    }
}

private fun JsonElement.asStringOrNull(): String? {
    return if (this.isJsonNull) {
        null
    } else {
        this.asString
    }
}

/*private fun JsonElement.toMpxWatchLive(): MpxWatchLive {
    return if (this.isJsonNull) {
        MpxWatchLive()
    } else {
        val content = this.asJsonObject
        MpxWatchLive(
            stationId = content["stationId"]?.asStringOrNull() ?: "",
            currentListing = content["currentListing"]?.toWatchLiveItem() ?: WatchLiveItem(),
            nextListing = content["nextListing"]?.toWatchLiveItem() ?: WatchLiveItem(),
            locationId = content["locationId"]?.asStringOrNull() ?: "",
            channelId = content["channelId"]?.asStringOrNull() ?: "",
            channelInfo = content["channelInfo"]?.toChannelInfo() ?: ChannelInfo()
        )
    }
}

private fun JsonElement.toChannelInfo(): ChannelInfo {
    return if (this.isJsonNull) {
        ChannelInfo()
    } else {
        val content = this.asJsonObject
        ChannelInfo(
            guid = content["guid"]?.asStringOrNull() ?: "",
            ownerId = content["ownerId"]?.asStringOrNull() ?: "",
            title = content["title"]?.asStringOrNull() ?: "",
            channelNumber = content["channelNumber"]?.asStringOrNull() ?: "",
            googleDaiKey = content["pl1\$google-ssai-dash"]?.asStringOrNull() ?: ""
        )
    }
}

private fun JsonElement.toWatchLiveItem(): WatchLiveItem {
    return if (this.isJsonNull) {
        WatchLiveItem()
    } else {
        val content = this.asJsonObject
        WatchLiveItem(
            id = content["id"]?.asStringOrNull() ?: "",
            programId = content["programId"]?.asStringOrNull() ?: "",
            title = content["programTitle"]?.asStringOrNull() ?: "",
            thumbnails = content["programThumbnails"]?.toMpxThumbnails(),
            secondaryTitle = content["programSecondaryTitle"]?.asStringOrNull() ?: "",
            ratings = content["programRatings"]?.toMpxRatings() ?: listOf(),
            startTime = (content["startTime"]?.asStringOrNull() ?: "0").toLong(),
            endTime = (content["endTime"]?.asStringOrNull() ?: "0").toLong()
        )
    }
}*/

private fun JsonElement.toMpxThumbnails(): MPXThumbnails {
    return if (this.isJsonNull) {
        MPXThumbnails()
    } else {
        val content = this.asJsonObject
        MPXThumbnails(
            default = content["default"]?.toMpxThumbnail() ?: MPXThumbnail(),
            logo = content["logo"]?.toMpxThumbnail() ?: MPXThumbnail(),
            poster = content["poster"]?.toMpxThumbnail() ?: MPXThumbnail(),
            titleCard = content["title card"]?.toMpxThumbnail() ?: MPXThumbnail(),
            masterBoxart = content["master_boxart"]?.toMpxThumbnail() ?: MPXThumbnail(),
            showLogo = content["show_logo"]?.toMpxThumbnail() ?: MPXThumbnail()
        )
    }
}

private fun JsonElement.toMpxThumbnail(): MPXThumbnail {
    return if (this.isJsonNull) {
        MPXThumbnail()
    } else {
        val content = this.asJsonObject
        MPXThumbnail(
            url = content["plprogram\$url"]?.asStringOrNull() ?: ""
        )
    }
}