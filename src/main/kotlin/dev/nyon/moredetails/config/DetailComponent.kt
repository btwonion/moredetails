package dev.nyon.moredetails.config

import kotlinx.serialization.Serializable

@Serializable
sealed class DetailComponent<T : DetailComponentValue>(
    val enabled: Boolean,
    val x: Int,
    val y: Int,
    val color: Int,
    val background: DetailBackground,
    val fontSize: Int,
    val height: Int,
    val width: Int
) {
    abstract suspend fun update(value: T)
}

@Serializable
sealed class DetailComponentValue