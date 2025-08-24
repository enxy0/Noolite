package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.common.Group

interface GetFavoriteGroupUseCase : UseCase<Unit, Group?>
