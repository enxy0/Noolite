package com.enxy.noolite.domain.features.script

import com.enxy.noolite.domain.UseCase
import com.enxy.noolite.domain.features.common.Group

interface GetGroupsUseCase : UseCase<Unit, List<Group>>
