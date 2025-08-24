package com.enxy.noolite.domain.features.settings

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload

interface GetNooliteGroupsUseCase : UseCase<NooliteSettingsPayload, List<Group>>
