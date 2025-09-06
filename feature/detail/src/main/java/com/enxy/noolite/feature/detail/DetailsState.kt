package com.enxy.noolite.feature.detail

import androidx.compose.runtime.Immutable
import com.enxy.noolite.core.model.Group

@Immutable
data class DetailsState(
    val group: Group,
    val isFavorite: Boolean = false,
)
