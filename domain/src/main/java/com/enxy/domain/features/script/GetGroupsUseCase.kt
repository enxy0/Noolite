package com.enxy.domain.features.script

import com.enxy.domain.UseCase
import com.enxy.domain.features.common.Group

interface GetGroupsUseCase : UseCase<Unit, List<Group>>
