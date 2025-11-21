package com.bubble.probubblebopling.ror.domain.model

import com.google.gson.annotations.SerializedName


data class ProBubbleBoPlingEntity (
    @SerializedName("ok")
    val chickenOk: String,
    @SerializedName("url")
    val chickenUrl: String,
    @SerializedName("expires")
    val chickenExpires: Long,
)