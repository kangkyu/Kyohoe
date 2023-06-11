package com.example.kyohoe

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val kind: String,
    val etag: String,
    val id: VideoId,
    val snippet: Snippet
)

@Serializable
data class VideoId(
    val kind: String,
    val videoId: String
)

@Serializable
data class Snippet(
    val publishedAt: String,
    val channelId: String,
    val title: String,
    val description: String,
    val thumbnails: Thumbnails,
    val channelTitle: String,
    val liveBroadcastContent: String,
    val publishTime: String
)

@Serializable
data class Thumbnails(
    val default: Thumbnail,
    val medium: Thumbnail,
    val high: Thumbnail
)

@Serializable
data class Thumbnail(
    val url: String,
    val width: Int,
    val height: Int
)

@Serializable
data class SearchListResponse(
    val kind: String,
    val etag: String,
    val nextPageToken: String,
    val regionCode: String,
    val pageInfo: PageInfo,
    val items: List<SearchResult>
)

@Serializable
data class PageInfo(
    val totalResults: Int,
    val resultsPerPage: Int
)
