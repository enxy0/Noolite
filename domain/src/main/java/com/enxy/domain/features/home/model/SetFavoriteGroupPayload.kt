package com.enxy.domain.features.home.model

import com.enxy.domain.features.common.Group

data class SetFavoriteGroupPayload(
    val group: Group,
    val isFavorite: Boolean
)
