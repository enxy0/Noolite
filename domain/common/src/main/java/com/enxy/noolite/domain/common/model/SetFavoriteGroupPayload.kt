package com.enxy.noolite.domain.common.model

import com.enxy.noolite.core.model.Group

data class SetFavoriteGroupPayload(
    val group: Group,
    val isFavorite: Boolean
)