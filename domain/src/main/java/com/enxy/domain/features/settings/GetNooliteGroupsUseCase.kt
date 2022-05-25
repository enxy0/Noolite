package com.enxy.domain.features.settings

import com.enxy.domain.UseCase
import com.enxy.domain.features.common.Group
import com.enxy.domain.features.settings.model.NooliteSettingsPayload

interface GetNooliteGroupsUseCase : UseCase<NooliteSettingsPayload, List<Group>>
