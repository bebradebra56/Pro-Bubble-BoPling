package com.bubble.probubblebopling.ror.domain.model

import com.google.gson.annotations.SerializedName


private const val CHICKEN_A = "com.bubble.probubblebopling"
data class ChickenParam (
    @SerializedName("af_id")
    val chickenAfId: String,
    @SerializedName("bundle_id")
    val chickenBundleId: String = CHICKEN_A,
    @SerializedName("os")
    val chickenOs: String = "Android",
    @SerializedName("store_id")
    val chickenStoreId: String = CHICKEN_A,
    @SerializedName("locale")
    val chickenLocale: String,
    @SerializedName("push_token")
    val chickenPushToken: String,
    @SerializedName("firebase_project_id")
    val chickenFirebaseProjectId: String = "probubblebopling",

    )