package com.enxy.domain.features.home

import com.enxy.domain.UseCase
import com.enxy.domain.features.common.Group

interface GetFavoriteGroupUseCase : UseCase<Unit, Group?>
