package com.enxy.noolite.domain.features.home.model

import com.enxy.noolite.domain.features.common.Group

data class SetFavoriteGroupPayload(
    val group: Group,
    val isFavorite: Boolean
)
